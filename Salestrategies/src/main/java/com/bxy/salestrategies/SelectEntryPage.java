package com.bxy.salestrategies;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import com.bxy.salestrategies.common.Entity;
import com.bxy.salestrategies.common.Field;
import com.bxy.salestrategies.db.DAOImpl;
import com.bxy.salestrategies.util.Configuration;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;

/**
 * 
 * 
 * @author Dong
 */
public class SelectEntryPage extends WebPage {
    private static final Logger logger = Logger.getLogger(SelectEntryPage.class);

    private String search_target;

    
    /**
     * Constructor
     * 
     * @param parameters
     *            Page parameters (ignored since this is the home page)
     */
    public SelectEntryPage() {
        String relationTableName = getRequest().getRequestParameters().getParameterValue("en").toString();
        String tragetEntity = getRequest().getRequestParameters().getParameterValue("excludeName").toString();
        final String excludeId = getRequest().getRequestParameters().getParameterValue("eid").toString();
        String target = getRequest().getRequestParameters().getParameterValue("target").toString();
        //区分是哪一个数据
        String key = getRequest().getRequestParameters().getParameterValue("key").toString();
        String accountID = getRequest().getRequestParameters().getParameterValue("accountID").toString();
        System.out.println("accountID:"+accountID);
        initPage(null,relationTableName,tragetEntity,excludeId,target,key,accountID);
    }

    public SelectEntryPage(List<Map> maplist,String relationTableName,String tragetEntity,String excludeId,String target,String key,String accountID) {
        initPage(maplist,relationTableName,tragetEntity,excludeId,target,key,accountID);
    }

    public void initPage(List<Map> list,final String relationTableName,final String tragetEntity,final String excludeId,final String target,final String key,final String accountID) {
        final String userId = ((SignInSession)getSession()).getUserId();
        final Map<String, Entity> entities = Configuration.getEntityTable();
        final Entity entity = entities.get(relationTableName);
        Form form = new Form("form") {
            @Override
            protected void onSubmit() {
                List<Map> maplist = null;
                if(relationTableName.equalsIgnoreCase("account")){
                    
                	String sql = entity.getSql();
                	 maplist = DAOImpl.queryEntityRelationList(sql);
                }else if(relationTableName.equalsIgnoreCase("contact")){
                    
                    if (tragetEntity.equalsIgnoreCase("activity")) {

                    	String sql = entity.getSql();

                        maplist = DAOImpl.queryEntityRelationList(sql);
                                 Entity activityEnt = entities.get("activity");
                                 String actSQL = activityEnt.getSql();
                                 actSQL  = "select contactName,count(contactName) as ct from ("+ actSQL + ") as bact where status=2 group by contactName";
                                 logger.debug("number_of_act:"+actSQL);
                                 
                                 List<Map> num_of_act_per_contact = DAOImpl.queryEntityRelationList(actSQL);
           
                                 Map<String,Map> activity_contact_map = Maps.newHashMap();
                                 for(Map map:num_of_act_per_contact){
                                     activity_contact_map.put(String.valueOf(map.get("contactName")),map);
                                 }
                                 
                                 
                                // Map<String,Map> contact_map = Maps.newHashMap();
                                 for(Map map:maplist){
                                    String contactId = String.valueOf(map.get("id"));
                                    if(activity_contact_map.containsKey(contactId)){
                                        map.put("num_of_visiting", activity_contact_map.get(contactId).get("ct"));
                                    }
                                 }
                    } else if (tragetEntity.equalsIgnoreCase("user")) {
                        // maplist = DAOImpl.searchCRMUser(search_target);
                        maplist = DAOImpl.searchUser(search_target);
                        Map dummy = Maps.newHashMap();
                        dummy.put("id", 0);
                        dummy.put("name", "无");
                        maplist.add(dummy);

                    } else if (tragetEntity.equalsIgnoreCase("account")) {
                    	String sql = assembleSearchingSQL( entity);
                        maplist  = DAOImpl.queryEntityRelationList(sql);
                        Map dummy = Maps.newHashMap();
                        dummy.put("id", -1);
                        dummy.put("name", "无");
                        maplist.add(dummy);
                    } else if (tragetEntity.equalsIgnoreCase("tactics")) {
                    	String sql = assembleSearchingSQL( entity);
                        maplist  = DAOImpl.queryEntityRelationList(sql);
                        Map dummy = Maps.newHashMap();
                        dummy.put("id", -1);
                        dummy.put("name", "无");
                        maplist.add(dummy);
                    }else if (tragetEntity.equalsIgnoreCase("opportunitycontactteam")) {
                    	String sql = assembleSearchingSQL( entity);
                        maplist  = DAOImpl.queryEntityRelationList(sql);
                        Map dummy = Maps.newHashMap();
                        dummy.put("id", -1);
                        dummy.put("name", "无");
                        maplist.add(dummy);
                    }else{
                    	String sql = assembleSearchingSQL( entity)+" and account_id = "+accountID;
                    	System.out.println("sql:"+sql);
                        maplist  = DAOImpl.queryEntityRelationList(sql);
                        Map dummy = Maps.newHashMap();
                        dummy.put("id", -1);
                        dummy.put("name", "无");
                        maplist.add(dummy);
                    }
                }else if (relationTableName.equalsIgnoreCase("user")) {
                    String sql = assembleSearchingSQL( entity);
                    maplist  = DAOImpl.queryEntityRelationList(sql);
                    Map dummy = Maps.newHashMap();
                    dummy.put("id", 0);
                    dummy.put("name", "无");
                    maplist.add(dummy);
                                                              
                }else if (relationTableName.equalsIgnoreCase("opportunity")||relationTableName.equalsIgnoreCase("competitor")||
                		relationTableName.equalsIgnoreCase("activity")) {
                    String sql = assembleSearchingSQL(entity);
                    maplist  = DAOImpl.queryEntityRelationList(sql);
                    Map dummy = Maps.newHashMap();
                    dummy.put("id", -1);
                    dummy.put("name", "无");
                    maplist.add(dummy);
                }                
                setResponsePage(new SelectEntryPage(maplist,relationTableName,tragetEntity,excludeId,target,key,accountID));

            }
        };
        form.add(new TextField<String>("search_input", new PropertyModel<String>(this, "search_target")));
        add(form);
        
        
  //      RepeatingView dataRowRepeater = new RepeatingView("dataRowRepeater");
  //      add(dataRowRepeater);
        
          final  List<Field> searchableFields = entity.getSearchableFields();
            
          //初始化的时候查不出数据
          if (list == null) {
                list=DAOImpl.queryEntityRelationList("Select * from account where 1=0");
            }
          
          final Map<String, Map> tableData = Maps.newHashMap();
          List<String> ids = Lists.newArrayList();
            for (Map map : (List<Map>) list) {
               String key1 = String.valueOf(map.get("id"));
               ids.add(key1);
               tableData.put(key1, map);
            }
            
         
         
        
        final PageableListView<String> listview = new PageableListView<String>("dataRowRepeater", ids, 20) {
            
            @Override          
            protected void populateItem(ListItem<String> item) {
                String key1 = item.getDefaultModelObjectAsString();
                Map map = tableData.get(key1);
                int uid = ((Number) map.get("id")).intValue();
                String name = (String) map.get("name");
           
                item.add(new AttributeAppender("data-id",new Model(uid)));
                item.add(new AttributeAppender("data-name",new Model(name)));
                item.add(new AttributeAppender("data-ename",relationTableName));
                item.add(new AttributeAppender("data-key", new Model(key)));
                Label cap = new Label("name_span", new Model(name));
                item.add(cap);
                
                
                RepeatingView column_repeater = new RepeatingView("column_repeater");
                item.add(column_repeater);
                for(Field f:searchableFields){
                    Object obj = map.get(f.getName());
                    if(!f.getName().equalsIgnoreCase("name") && obj != null){
                        AbstractItem column_item = new AbstractItem(column_repeater.newChildId());
                        column_repeater.add(column_item); 
                        String celldata =String.valueOf(obj);
                        column_item.add(new Label("celldata","<strong>"+f.getDisplay()+": </strong>"+celldata).setEscapeModelStrings(false));
                    }
                } 
                
               

                Object obj =  map.get("num_of_visiting");               
                if (obj != null) {
                    AbstractItem column_item = new AbstractItem(column_repeater.newChildId());
                    column_repeater.add(column_item);
                    String num_of_visiting = String.valueOf(obj);
                    column_item.add(new Label("celldata", num_of_visiting + "（拜访次数）"));
                }
            }
        };
  
        add(listview);
        AjaxPagingNavigator nav =new AjaxPagingNavigator("navigator", listview);
        nav.setOutputMarkupId(true); 

        add(nav);
        setVersioned(false);
        
    }
    private String assembleSearchingSQL( final Entity entity) {
        String sql = entity.getSql();
        
        search_target = (search_target==null || search_target.equalsIgnoreCase("*"))? "":search_target;
        
        List<Field> searchableFields = entity.getSearchableFields();
        String joint = " like '%"+search_target+"%'";
        String likequery = "";
        for(Field sf:searchableFields){
            likequery = likequery + " OR "+ sf.getName() + joint;
        }
         sql =  sql + " where name like '%"+search_target+"%' " + likequery ;
        return sql;
    }

}
