package com.bxy.salestrategies.db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
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

import com.bxy.salestrategies.common.Entity;
import com.bxy.salestrategies.model.Account;
import com.bxy.salestrategies.model.AccountUserTeam;
import com.bxy.salestrategies.model.Activity;
import com.bxy.salestrategies.model.Choice;
import com.bxy.salestrategies.model.Contact;
import com.bxy.salestrategies.model.ContactUserTeam;
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
//	    
	    
	    public static Activity getActivityById(int entityId){
	    	System.out.println("根据活动ID获取用户"+entityId);
	        Connection conn = null;
	        Activity activity = new Activity();
	        try {
	            conn = DBConnector.getConnection();
	            QueryRunner run = new QueryRunner();
	            ResultSetHandler<Activity> h = new BeanHandler<Activity>(Activity.class);
	            activity = run.query(conn, "SELECT * FROM activity where id=?", h, entityId);
	        } catch (SQLException e) {
	            logger.error("failed to get all accounts", e);
	        } finally {
	            DBHelper.closeConnection(conn);
	        }
	        return activity; 
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
	    
	    public static int deleteRecord(String entityId,String entityName) {
	        String sql = "";
	        if(entityName.equals("coaching")||entityName.equalsIgnoreCase("willcoaching")){
	          sql = "DELETE from activity where id = " + entityId;
	        }else{
	          sql = "DELETE from " + entityName +" where id = " + entityId;
	        }
	        logger.debug("delte record sql:"+ sql);
	        Connection conn = null;
	        int inserts = 0;
	        try {
	            conn = DBConnector.getConnection();
	            QueryRunner run = new QueryRunner();
	          
	            inserts += run.update(conn, sql);
	            
	        } catch (Exception e) {
	            logger.error("failed to delete  calendar event", e);
	        } finally {
	            DBHelper.closeConnection(conn);
	        }

	        return inserts;
	    }
	    
	    public static boolean updateCrmUserReport(String from, String to){
	        String  sql=" UPDATE crmuser SET reportto=? where reportto =?";
	        Connection conn = null;
	        
	        int updates = 0;
	        try{
	            conn = DBConnector.getConnection();
	            QueryRunner run = new QueryRunner();
	            updates += run.update(conn, sql, to, from);
	            logger.debug("updateCrmUserReport success!");
	        } catch (Exception e){
	            logger.error("failed to updateCrmUserReport",e);
	        }finally{
	            DBHelper.closeConnection(conn);
	        }
	        if(updates>0){
	            return true;
	        }
	        return false;
	    }
	    
	    public static void doneRecord(String id,String entityName, String time ) {
	    	String sql = "";
	      
	    	sql = "UPDATE  activity SET status= 2 ,act_endtime = '"+ time +"'  where id = " + id;
	    	logger.debug("UPDATE sql is:"+sql);
	    	Connection conn = null;
	    	try {
	    		conn = DBConnector.getConnection();
	    		QueryRunner run = new QueryRunner();
	    		int inserts = 0;
	    		inserts += run.update(conn, sql);
	    		System.out.println("inserted:" + inserts);
	    	} catch (Exception e) {
	           logger.error("failed to add new calendar event", e);
	    	} finally {
	           DBHelper.closeConnection(conn);
	    	}
	   }
	    //reset password
	    public static int  resetUserPassword(int entityId){
	    	System.out.println("reset password");
	    	String  sql=" UPDATE userinfo SET password= ?, num_of_signIn = 0 where id =?";
	        Connection conn = null;
	        int insert = 0;
	        try {
	            conn = DBConnector.getConnection();
	            QueryRunner run = new QueryRunner();
	            ResultSetHandler<User> h = new BeanHandler<User>(User.class);
	            insert = run.update(conn, sql,DigestUtils.md5Hex("12345"),entityId);
	    		logger.debug("reset password success!");
	        } catch (SQLException e) {
	            logger.error("failed to get all accounts", e);
	        } finally {
	            DBHelper.closeConnection(conn);
	        }
	        return insert;
	    }
	  //修改用户激活状态
	    public static void updateUserActivited(int entityId){
	    	String  sql=" UPDATE userinfo SET isActivited=? where id =?";
	        Connection conn = null;
	        try {
	            conn = DBConnector.getConnection();
	            QueryRunner run = new QueryRunner();
	            ResultSetHandler<User> h = new BeanHandler<User>(User.class);
	            run.update(conn, sql,1,entityId);
	    		logger.debug("update activited success!");
	        } catch (SQLException e) {
	            logger.error("failed to get all accounts", e);
	        } finally {
	            DBHelper.closeConnection(conn);
	        }
	    }
	  //修改活动状态为未执行
	    public static boolean updateActivityStatusById(int entityId){
	    	String sql = "UPDATE activity SET status=3 where id=?";
	        Connection conn = null;
	        int inserts = 0;
	        try {
	            conn = DBConnector.getConnection();
	            QueryRunner run = new QueryRunner();
	            inserts += run.update(conn, sql,entityId);
	        } catch (Exception e) {
	            logger.error("failed to activity", e);
	        } finally {
	            DBHelper.closeConnection(conn);
	        }
	        if(inserts>0){
	    		return true;
	    	}
	    	return false;
	    }
	    public static List<Choice> queryPickListByFilter(String picklist,String filterName, String filterValue) {
	        String query = null;
	        if(picklist.equalsIgnoreCase("product")){
	           query = "select id, name from " + picklist + " where "+filterName+"=?";
	        }else{
	          query = "select id, val from " + picklist + " where "+filterName+"=?"; 
	        }
	          List<Choice> choices = Lists.newArrayList();
	          Connection conn = null;
	          try {
	              conn = DBConnector.getConnection();
	              QueryRunner run = new QueryRunner();
	              ResultSetHandler<List<Choice>> h = new BeanListHandler<Choice>(Choice.class);
	              choices = run.query(conn, query, h,filterValue);

	          } catch (SQLException e) {
	              logger.error("failed to get queryPickListById", e);
	          } finally {
	              DBHelper.closeConnection(conn);
	          }

	          return choices;

	      }
	    public static boolean updateRecord(String id,String entityName, List<String> fieldNames, List<String> values ) {
	    	 String sql = "";
	    	 int i=0;
	         for(String name:fieldNames){
	             if(i==0){
	            	 sql = name +" = "+ values.get(i)  ;
	             }else{
	        	    sql = sql + "," + name +" = "+ values.get(i)  ;	 
	             }
	        	 i++;
	         }
	        	sql = "UPDATE  "+entityName+ " SET "+sql+" where id = " + id;
	        logger.debug("UPDATE sql is:"+sql);
	        Connection conn = null;
	        try {
	            conn = DBConnector.getConnection();
	            QueryRunner run = new QueryRunner();
	            int inserts = 0;
	            inserts += run.update(conn, sql);

	            System.out.println("inserted:" + inserts);
	            return true;
	        } catch (Exception e) {
	            logger.error("failed to add new calendar event", e);
	            return false;
	        } finally {
	            DBHelper.closeConnection(conn);
	        }
	    }
	  //查询所有用户登录名
	     public static List<String> getLoginNames(String entityId){
	     	Connection conn = null;
	         List<String> loginNames = Lists.newArrayList();
	         List<User> users = Lists.newArrayList();
	         try {
	             conn = DBConnector.getConnection();
	             QueryRunner run = new QueryRunner();
	             ResultSetHandler<List<User>> h = new BeanListHandler<User>(User.class);
	             users = run.query(conn, "SELECT * FROM user where id!=?",h,entityId);
	         } catch (SQLException e) {
	             logger.error("failed to get all accounts", e);
	         } finally {
	             DBHelper.closeConnection(conn);
	         }
	         for(User userInfo:users){
	         	loginNames.add(userInfo.getName());
	         }
	         return loginNames;
	     }
	     public static Entity getEntityById(String tableName,String id) {
	         Connection conn = null;
	         Entity entity = null;
	         try {
	             conn = DBConnector.getConnection();
	             QueryRunner run = new QueryRunner();
	             ResultSetHandler<Entity> h = new BeanHandler<Entity>(Entity.class);

	             entity = run.query(conn, "SELECT * FROM "+tableName + " where id=?", h, id);

	         } catch (SQLException e) {
	             logger.error("failed to get all entity", e);
	         } finally {
	             DBHelper.closeConnection(conn);
	         }

	         return entity;
	     }
	     //修改历史
	     public static void insertAudit(String entityName, String columnName,String oldValue,String newValue,String entityId,String userName) throws Exception {
	            Connection conn = null;
	            long ts= System.currentTimeMillis();
	            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            String date_value = dateformat.format(ts);
	            try {
	                conn = DBHelper.getConnection();
	                QueryRunner run = new QueryRunner();
	                int inserts = run.update(conn, "INSERT INTO data_audit (entity_name,record_id,modify_time,modifier,column_name,old_value,new_value) VALUES (?,?,?,?,?,?,?)",entityName,entityId,date_value,userName,columnName,oldValue,newValue);

	                logger.info(String.format("%s row inserted into insertRelationOfAccountIDCRMUserID!", inserts));

	            } catch (SQLException e) {
	                logger.error("failed to insertRelationOfAccountIDCRMUserID", e);
	            } finally {
	                DBHelper.closeConnection(conn);
	        }

	    }
	     //查询所有用户登录名
	     public static List<String> getAllLoginNames(){
	     	Connection conn = null;
	         List<String> loginNames = Lists.newArrayList();
	         List<User> users = Lists.newArrayList();
	         try {
	             conn = DBConnector.getConnection();
	             QueryRunner run = new QueryRunner();
	             ResultSetHandler<List<User>> h = new BeanListHandler<User>(User.class);
	             users = run.query(conn, "SELECT * FROM userInfo ",h);
	         } catch (SQLException e) {
	             logger.error("failed to get all accounts", e);
	         } finally {
	             DBHelper.closeConnection(conn);
	         }
	         for(User userInfo:users){
	         	loginNames.add(userInfo.getName());
	         }
	         return loginNames;
	     }
	     
	     public static long createNewUser(String entityName,List<String> fieldNames,List<String> values,String userId){
	     	String fieldssql = Joiner.on(",").join(fieldNames);
	         String valuesql = Joiner.on(",").join(values);
	         fieldssql = fieldssql + ",num_of_signIn,password";
	    	 	valuesql =  valuesql + ",0,'"+DigestUtils.md5Hex("12345")+"'";
	    	 	logger.debug("fieldssql sql is:"+fieldssql);
	    	 	logger.debug("valuesql sql is:"+valuesql);
	    	 	String sql = "INSERT INTO "+entityName+" ("+fieldssql+") VALUES ("+valuesql+")";
	     
	    	 	logger.debug("insert sql is:"+sql);

	 	    Connection conn = null;
	 	    //PreparedStatement statement = null;
	 	    ResultSet generatedKeys = null;
	 	    PreparedStatement statement = null;
	 	    long key=-1;
	         try {
	 			conn = DBConnector.getConnection();
	 			statement  = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	 	        int affectedRows = statement.executeUpdate();
	 	     
	 	        generatedKeys = statement.getGeneratedKeys();
	             if (generatedKeys.next()) {
	                 //user.setId(generatedKeys.getLong(1));
	                  key = generatedKeys.getLong(1);
	             } else {
	                 logger.error("failed to insert data");
	                 return -1;
	             }
	 	        System.out.println("add crmuser is True");
	 	        return key;
	 		} catch (SQLException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	         } finally {
	             if (generatedKeys != null) try { generatedKeys.close(); } catch (SQLException logOrIgnore) {}
	             if (statement != null) try { statement.close(); } catch (SQLException logOrIgnore) {}
	             if (conn != null) try { conn.close(); } catch (SQLException logOrIgnore) {}
	         }
	         return -1L;
	     }
	     public static long createNewRecord(String entityName, List<String> fieldNames, List<String> values,String userId){   
	         String fieldssql = Joiner.on(",").join(fieldNames);
	         String valuesql = Joiner.on(",").join(values);
	         logger.debug("fieldssql sql is:"+fieldssql);
	         logger.debug("valuesql sql is:"+valuesql);
	         String sql = "INSERT INTO "+entityName+" ("+fieldssql+") VALUES ("+valuesql+")";
	        
	        Connection conn = null;
	        //PreparedStatement statement = null;
	        ResultSet generatedKeys = null;
	        PreparedStatement statement = null;
	        long key = -1;
	        try {
	            conn = DBConnector.getConnection();
	            
	            statement  = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	            
	            int affectedRows = statement.executeUpdate();
	            if (affectedRows == 0) {
	                logger.error("Failed to insert data");
	                return -1;
	            }
	            
	            generatedKeys = statement.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                //user.setId(generatedKeys.getLong(1));
	                 key = generatedKeys.getLong(1);
	            } else {
	                logger.error("failed to insert data");
	                return -1;
	            }
	            
	        } catch (Exception e) {
	            logger.error("failed to add new calendar event", e);
	        } finally {
	            if (generatedKeys != null) try { generatedKeys.close(); } catch (SQLException logOrIgnore) {}
	            if (statement != null) try { statement.close(); } catch (SQLException logOrIgnore) {}
	            if (conn != null) try { conn.close(); } catch (SQLException logOrIgnore) {}
	        }
	        
	        return key;

	    }
	     //
	     public static String getCreateRecordByEntity(String entityName){
	            String result;
	            String sql="select Max(id) as target from "+entityName+"";
	            List<Map<String,Object>> lt=DAOImpl.queryEntityRelationList(sql);
	            result=String.valueOf(lt.get(0).get("target").toString());
	            return result;
	     }
	     
	     public static void insert2UserRelationTable(String entityName,String userId,String entityId){
	         String sql = null;
	         long ts= System.currentTimeMillis();
	         SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	         String date_value = dateformat.format(ts);
	         if(entityName.equalsIgnoreCase("account_delect")){//暂不需要
	             sql = "INSERT INTO accountcrmuser ( accountId, userId) VALUES ("+entityId+","+userId+")";
	         }else if(entityName.equalsIgnoreCase("contact_delect")){//暂不需要
	             sql = "INSERT INTO contactcrmuser ( contactId, userId) VALUES ("+entityId+","+userId+")";
	         }else if(entityName.equalsIgnoreCase("activity")){
	             sql = "INSERT INTO activitycrmuser ( activityId, userId) VALUES ("+entityId+","+userId+")";
	         }else if (entityName.equalsIgnoreCase("user")){
	           sql = "INSERT INTO user_position ( userId,status,isPrimary,whenadded) VALUES ("+userId+",1,1,'"+date_value+"')";
	         }
	         if(sql == null) {
	             logger.error("entityName error");
	             return;
	         }
	         
	         Connection conn = null;
	         try {
	             conn = DBConnector.getConnection();
	             QueryRunner run = new QueryRunner();
	             int inserts = 0;
	             inserts += run.update(conn, sql);

	             System.out.println("inserted:" + inserts);
	         } catch (Exception e) {
	             logger.error("failed to add new calendar event", e);
	         } finally {
	             DBHelper.closeConnection(conn);
	         }
	     }
	     //添加用户
	     public static List searchUser(String search_target) {
	         	if(search_target == null|| search_target.equalsIgnoreCase("*")){
	     	          search_target = "";
	         	}
	             String sql = "select * from (  select * from user where  (user.id !=-1) AND (name like '%"+search_target+"%' OR employee_number like '%"+search_target+"%' OR report_to like '%"+search_target+"%')) as a";
	             logger.debug(sql );
	             Connection conn = null;
	             List lMap = Lists.newArrayList();
	             try {
	                 conn = DBConnector.getConnection();
	                 QueryRunner run = new QueryRunner();
	                 lMap = (List) run.query(conn, sql, new MapListHandler());

	             } catch (SQLException e) {
	                 logger.error("failed to get user", e);
	             } finally {
	                 DBHelper.closeConnection(conn);
	             }
	             return lMap;
	         }
	     //查询客户
	     public static List searchCRMAccount(String search_target) {
	         if(search_target == null|| search_target.equalsIgnoreCase("*")){
	           search_target = "";
	       }
	           String sql = "select * from (select * from account where (account.id > 0 ) AND  (name like '%"+search_target+"%' OR tel like '%"+search_target+"%' OR fax like '%"+search_target+"%')) as a";
	           logger.debug(sql );
	           Connection conn = null;
	           List lMap = Lists.newArrayList();
	           try {
	               conn = DBConnector.getConnection();
	               QueryRunner run = new QueryRunner();
	               lMap = (List) run.query(conn, sql, new MapListHandler());

	           } catch (SQLException e) {
	               logger.error("failed to get user", e);
	           } finally {
	               DBHelper.closeConnection(conn);
	           }

	           return lMap;
	       }
	       //查询联系人
	       public static List searchCRMContact(String search_target) {
	         if(search_target == null|| search_target.equalsIgnoreCase("*")){
	           search_target = "";
	       }
	         String sql = "select * from (select * from contact where (contact.id > 0) AND (name like '%"+search_target+"%' OR office_tel like '%"+search_target+"%' OR cellphone like '%"+search_target+"%')) as a";
	         logger.debug(sql );
	         Connection conn = null;
	         List lMap = Lists.newArrayList();
	         try {
	             conn = DBConnector.getConnection();
	             QueryRunner run = new QueryRunner();
	             lMap = (List) run.query(conn, sql, new MapListHandler());

	         } catch (SQLException e) {
	             logger.error("failed to get user", e);
	         } finally {
	             DBHelper.closeConnection(conn);
	         }

	         return lMap;
	     }
	       //添加团队关系
	       public static void insertRelationOfEntityIDCRMUserID(String entityName, String cId, String userId ,int type) throws Exception {
	           int contactId = Integer.parseInt(cId);
	           int uid = Integer.parseInt(userId);
	           if (contactId != 0 && uid != 0) {
	               String sql = "";
	               long ts= System.currentTimeMillis();
	               SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	               String date_value = dateformat.format(ts);
	               if(entityName.equalsIgnoreCase("contact")){
	                   sql = "INSERT INTO contactcrmuser (userId,contactId) VALUES (?,?)";
	               }else if(entityName.equalsIgnoreCase("account")){
	                   sql = "INSERT INTO accountcrmuser (userId,accountId) VALUES (?,?)";
	               }else if(entityName.equalsIgnoreCase("user")){
	                   sql = "INSERT INTO user_position (userId,whenadded,isPrimary) VALUES (?,?,?)";
	               }else if(entityName.equalsIgnoreCase("user")){
	                   if(type == 0||type==4){
	                       sql = "INSERT INTO accountcrmuser (accountId,userId) VALUES (?,?)";
	                   }else if(type == 1){
	                       sql = "INSERT INTO contactcrmuser (contactId,userId) VALUES (?,?)";
	                   }else if(type == 2){
	                       sql = "INSERT INTO user_position (userId,whenadded,isPrimary) VALUES (?,?,?)";
	                   }else{
	                       sql = "update  crmuser set reportto = ?  where id = "+userId+" ";
	                   }
	               }
	               int isPrimary = 1;
	               Connection conn = null;
	               try {
	                   conn = DBConnector.getConnection();
	                   QueryRunner run = new QueryRunner();
	                   int inserts = 0;
	                   if(entityName.equalsIgnoreCase("user")||type==2){
	                     inserts = run.update(conn, sql, userId,date_value,isPrimary);
//	                   }else if(type == 3){
//	                   	inserts = run.update(conn, sql, userId);
	                   } else {
	                     inserts = run.update(conn, sql, userId,cId);
	                   }

	                   logger.info(String.format("%s row inserted into insertRelationOfEntityIDCRMUserID!", inserts));

	               } catch (SQLException e) {
	                   logger.error("failed to insertRelationOfEntityIDCRMUserID", e);
	               } finally {
	                   DBHelper.closeConnection(conn);
	               }

	           }
	       }
	       //查询客户与用户关系
	       public static AccountUserTeam getAccountsByAccountUserTeamId(int id) {
	    	   AccountUserTeam accounts = null;
	          ResultSetHandler<AccountUserTeam> h = new BeanHandler<AccountUserTeam>(AccountUserTeam.class);
	          Connection conn = null;
	          try {
	              QueryRunner run = new QueryRunner();
	              conn = DBConnector.getConnection();
	              accounts = run.query(conn, "select * from accountuserteam  where id=?", h, id);
	          } catch (Exception e) {
	              logger.error("failed to get city table data", e);
	          } finally {
	              DBHelper.closeConnection(conn);
	          }
	          return accounts;
	      }
	       public static ContactUserTeam getUserPositionById(int uid) {
	           Connection conn = null;
	           ContactUserTeam cut = new ContactUserTeam();
	           try {
	               conn = DBConnector.getConnection();
	               QueryRunner run = new QueryRunner();
	               ResultSetHandler<ContactUserTeam> h = new BeanHandler<ContactUserTeam>(ContactUserTeam.class);
	               cut = run.query(conn, "SELECT * from  contactuserteam  where id=?  ", h,  uid);

	           } catch (SQLException e) {
	               logger.error("failed to get all accounts", e);
	           } finally {
	               DBHelper.closeConnection(conn);
	           }

	           return cut;
	       }
	       //删除记录
	       public static void removeEntityFromTeam(String teamtable, String id) {
	           String sql = "delete from "+teamtable+" where id="+id;
	           if(teamtable.equalsIgnoreCase("crmuser")){
	           	sql = "update  "+teamtable+" set reportto = 0 where id="+id;
	           }
	           logger.debug("sserserserser"+ sql);
	           Connection conn = null;
	           try {
	               conn = DBConnector.getConnection();
	               QueryRunner run = new QueryRunner();
	               int inserts = 0;
	               inserts += run.update(conn, sql);

	               System.out.println("removed:" + inserts);
	           } catch (Exception e) {
	               logger.error("removeContactFromTeam", e);
	           } finally {
	               DBHelper.closeConnection(conn);
	           }
	           
	       }
	       //关系历史
	       public static void insertRealtionHestory(String teamtable,String user,int positionId,int otherId) {
	       	String sql = null;
	       	long ts= System.currentTimeMillis();
	           SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	           String date_value = dateformat.format(ts);
	           if(teamtable.equalsIgnoreCase("userinfo")||teamtable.equalsIgnoreCase("user_position")){
	           		sql = "insert into userposition_relation_history (position_id, user_id, modify_time, modifier) values("+positionId+","+otherId+",'"+date_value+"','"+user+"') ";
	           }else if(teamtable.equalsIgnoreCase("accountcrmuser")){
	               	sql = "insert into accountcrmuser_relation_history (position_id,account_id,modify_time,modifier) values("+positionId+","+otherId+",'"+date_value+"','"+user+"') ";
	           }
	           logger.debug("sserserserser"+ sql);
	           Connection conn = null;
	           try {
	               conn = DBConnector.getConnection();
	               QueryRunner run = new QueryRunner();
	               int inserts = 0;
	               inserts += run.update(conn, sql);

	               System.out.println("removed:" + inserts);
	           } catch (Exception e) {
	               logger.error("removeContactFromTeam", e);
	           } finally {
	               DBHelper.closeConnection(conn);
	           }
	           
	       }
	       //查询联系人
	       public static List searchContact(String search_target) {
	           if(search_target == null|| search_target.equalsIgnoreCase("*")){
	               search_target = "";
	           }
	           String sql = "select * from (select contact.*,contact.name as cname,account.name as aname from contact left join account ON contact.account_id = account.id left join contactuserteam ON contact.id = contactuserteam.contact_id where contact.name like '%"+search_target+"%' OR account.name like '%"+search_target+"%' )  as a";
	           Connection conn = null;
	           List lMap = Lists.newArrayList();
	           try {
	               conn = DBConnector.getConnection();
	               QueryRunner run = new QueryRunner();
	               lMap = (List) run.query(conn, sql, new MapListHandler());

	           } catch (SQLException e) {
	               logger.error("failed to get user", e);
	           } finally {
	               DBHelper.closeConnection(conn);
	           }

	           return lMap;
	       }
}
