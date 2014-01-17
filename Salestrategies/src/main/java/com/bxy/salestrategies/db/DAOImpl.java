package com.bxy.salestrategies.db;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.log4j.Logger;

import com.bxy.salestrategies.model.Account;
import com.bxy.salestrategies.model.Choice;
import com.bxy.salestrategies.model.Contact;
import com.bxy.salestrategies.model.User;
import com.google.common.base.Joiner;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
/**
 * 数据查询中
 * @author rexen
 *
 */
public class DAOImpl {
	 private static final Logger logger = Logger.getLogger(DAOImpl.class);
	 private static Cache<String, String> pickListCache = CacheBuilder.newBuilder()
	            .maximumSize(1000).expireAfterWrite(10, TimeUnit.MINUTES)
	            .build();
	    
	 private static Cache<String, String> relationDataCache = CacheBuilder.newBuilder()
            .maximumSize(1000).expireAfterWrite(10, TimeUnit.MINUTES)
            .build();
	    /**
	     * 登录
	     * @param loginName
	     * @param password
	     * @return
	     */
	    public static User login(String loginName, String password) {
	    	System.out.println("登录");
	        Connection conn = null;
	        User user = new User();
	        try {
	            conn = DBConnector.getConnection();
	            QueryRunner run = new QueryRunner();
	            ResultSetHandler<User> h = new BeanHandler<User>(User.class);
	            password = DigestUtils.md5Hex(password);
	            logger.debug("MD5 password is:" + password);
	            user = run.query(conn, "SELECT * FROM user where login_name=? AND password=?", h, loginName, password);
	        } catch (SQLException e) {
	            logger.error("signIn fail", e);
	        } finally {
	            try {
	                DbUtils.close(conn);
	            } catch (SQLException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }

	        return user;
	    }
	    /**
	     * 根据loginName查询用户
	     * @param loginName
	     * @return
	     */
	    public  static User getUserByLoginName(String loginName){
	        Connection conn = null;
	        User user = new User();
	        try {
	            conn = DBConnector.getConnection();
	            QueryRunner run = new QueryRunner();
	            ResultSetHandler<User> h = new BeanHandler<User>(User.class);
	            user = run.query(conn, "SELECT * FROM user where login_name=?", h, loginName);
	        } catch (SQLException e) {
	            logger.error("failed to get all accounts", e);
	        } finally {
	            DBHelper.closeConnection(conn);
	        }
	        return user;
	    }
	 /**
	  * 查询所有的客户
	  * @return
	  */
	 public static List<Account> getAllAccounts() {
	        List<Account> accounts = Lists.newArrayList();
	        Connection conn = null;
	        try {
	            conn = DBConnector.getConnection();
	            QueryRunner run = new QueryRunner();
	            ResultSetHandler<List<Account>> h = new BeanListHandler<Account>(Account.class);

	            accounts = run.query(conn, "SELECT * FROM account", h);

	        } catch (SQLException e) {
	            logger.error("failed to get all accounts", e);
	        } finally {
	            try {
	                DbUtils.close(conn);
	            } catch (SQLException e) {
	                // TODO Auto-generated catch block
	                logger.error("failed to close connection", e);
	            }
	        }

	        return accounts;
	    }
	 	/**
	 	 * 查询所有的客户用户
	 	 * @return
	 	 */
	    public static List<Contact> getAllContacts() {
	        List<Contact> contacts = Lists.newArrayList();
	        Connection conn = null;
	        try {
	            conn = DBConnector.getConnection();
	            QueryRunner run = new QueryRunner();
	            ResultSetHandler<List<Contact>> h = new BeanListHandler<Contact>(Contact.class);

	            contacts = run.query(conn, "SELECT * FROM contact", h);

	        } catch (SQLException e) {
	            logger.error("failed to get all accounts", e);
	        } finally {
	            try {
	                DbUtils.close(conn);
	            } catch (SQLException e) {
	                // TODO Auto-generated catch block
	                logger.error("failed to close connection", e);
	            }
	        }

	        return contacts;
	    }
	    public static List queryEntityRelationList(String sql, String... params) {
	        Connection conn = null;
	        List lMap = Lists.newArrayList();
	        try {
	          
	            conn = DBConnector.getConnection();
	            QueryRunner run = new QueryRunner();
	            lMap = (List) run.query(conn, sql, new MapListHandler(), params);
	        } catch (SQLException e) {
	            logger.error("failed to queryEntityRelationList", e);
	        } finally {
	            DBHelper.closeConnection(conn);
	        }
	        return lMap;
	    }

	    public static List queryEntityWithFilter(String sql, String filterField, List<String> filters, String... params) {

	        List<String> joinedFilter = Lists.newArrayList();
	        if (filters.size() > 0) {
	            for (String f : filters) {
	                if(f.equalsIgnoreCase("-1")){
	                    joinedFilter.add(filterField + " is NULL ");
	                }else{
	                  joinedFilter.add(filterField + " = " + f);
	                }
	                
	            }
	            sql = sql + " where (" + Joiner.on(" OR ").join(joinedFilter) + ")";
	        } else {
	            sql = sql + " where " + filterField + " = -1";
	        }
	        Connection conn = null;
	        List lMap = Lists.newArrayList();
	        try {
	            conn = DBConnector.getConnection();
	            QueryRunner run = new QueryRunner();
	            lMap = (List) run.query(conn, sql, new MapListHandler(), params);

	        } catch (SQLException e) {
	            logger.error("failed to get entity", e);
	        } finally {
	            DBHelper.closeConnection(conn);
	        }

	        return lMap;

	    }
	    public static Map queryEntityById(String sql, String id) {
	        String query = sql.replace("?", id);

	        Connection conn = null;
	        Map map = null;
	        try {
	            conn = DBConnector.getConnection();
	            QueryRunner run = new QueryRunner();
	            map = (Map) run.query(conn, query, new MapHandler());

	        } catch (SQLException e) {
	            logger.error("failed to get user", e);
	        } finally {
	            DBHelper.closeConnection(conn);
	        }

	        return map;
	    }
	    public static User getUserInfoById(int id) {
	        Connection conn = null;
	        User user = null;
	        try {
	            conn = DBConnector.getConnection();
	            QueryRunner run = new QueryRunner();
	            ResultSetHandler<User> h = new BeanHandler<User>(User.class);
	            user = run.query(conn, "SELECT * FROM user where id=?", h, id);
	        } catch (SQLException e) {
	            logger.error("failed to get user", e);
	        } finally {
	            DBHelper.closeConnection(conn);
	        }
	        return user;
	    }
	    public static String queryPickListByIdCached(final String picklist, final String id){
	        String value = "";
	        try{
	            value =  pickListCache.get(picklist + "_" + id, new Callable<String>() {
	                @Override
	                public String call() throws Exception {                   
	                    return queryPickListById(picklist,id);
	                }
	          });
	        }catch(Exception e){
	            logger.error("Failed to get data from cache",e);
	        }
	        
	        //logger.debug("hitRate:"+pickListCache.stats().hitRate() + " size:"+ pickListCache.size());
	        return value;
	    }
	    
	    public static String queryPickListById(String picklist, String id) {
	        String query = "select id, val from " + picklist + " where id=? ";
	        // logger.debug(query);
	        String result = "";
	        Connection conn = null;
	        Map map = null;
	        try {
	            conn = DBConnector.getConnection();
	            QueryRunner run = new QueryRunner();
	            map = (Map) run.query(conn, query, new MapHandler(), id);
	            if (map != null) {
	                Object value = map.get("val");
	                if (value != null) {
	                    result = (String) value;
	                }
	            }

	        } catch (SQLException e) {
	            logger.error("failed to get queryPickListById", e);
	        } finally {
	            DBHelper.closeConnection(conn);
	        }

	        return result;

	    }
	    public static String queryCachedRelationDataById(final String tableName, final String id){
	        String value = "";
	        try{
	            value =  relationDataCache.get(tableName + "_" + id, new Callable<String>() {
	                @Override
	                public String call() throws Exception {                   
	                    return queryRelationDataById(tableName,id);
	                }
	          });
	        }catch(Exception e){
	            logger.error("Failed to get data from cache",e);
	        }
	        return value;
	    }
	    public static String queryRelationDataById(final String tableName, final String id){
	        String query="";
	        if(tableName.equals("province")){
	             query = "select id, val from " + tableName + " where id=? ";
	        }else{
	             query = "select id, name from " + tableName + " where id=? ";
	        }
	        String result = "";
	        Connection conn = null;
	        Map map = null;
	        try {
	            conn = DBConnector.getConnection();
	            QueryRunner run = new QueryRunner();
	            map = (Map) run.query(conn, query, new MapHandler(), id);
	            if (map != null) {
	                 Object value=null;
	                if(tableName.equals("province")){
	                     value = map.get("val");
	                }else{
	                     value = map.get("name");
	                }
	                if (value != null) {
	                    result = (String) value;
	                }
	            }

	        } catch (SQLException e) {
	            logger.error("failed to get queryPickListById", e);
	        } finally {
	            DBHelper.closeConnection(conn);
	        }
	        return result;
	    }
	    public static List<Choice> queryPickList(String picklist) {
	        String query = null;
	        if(picklist.equalsIgnoreCase("product")){
	          query =  "select id, name from " + picklist;
	        }else{
	          query =  "select id, val from " + picklist;
	        }
	        List<Choice> choices = Lists.newArrayList();
	        Connection conn = null;
	        try {
	            conn = DBConnector.getConnection();
	            QueryRunner run = new QueryRunner();
	            ResultSetHandler<List<Choice>> h = new BeanListHandler<Choice>(Choice.class);
	            choices = run.query(conn, "SELECT * FROM " + picklist, h);

	        } catch (SQLException e) {
	            logger.error("failed to get queryPickListById", e);
	        } finally {
	            DBHelper.closeConnection(conn);
	        }

	        return choices;
	    }
}
