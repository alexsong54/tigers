package com.bxy.salestrategies.common;
/**
*
* @author Dong
*/
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import javax.mail.Session;

import org.apache.log4j.Logger;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;

import com.bxy.salestrategies.db.DAOImpl;
import com.bxy.salestrategies.model.User;
import com.bxy.salestrategies.util.Configuration;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;

public class TeamManPanel extends Panel {
    private static final long serialVersionUID = 2501105233172820074L;
    private static final Logger logger = Logger.getLogger(TeamManPanel.class);
    private String etId;
    private String currentEntityName;
    List<String> selectedRowIds = Lists.newArrayList();
    public List<Field> fieldsd;
    public TeamManPanel(String id,final String en,final String entityId,final int type) {
        super(id);
        etId = entityId;
        currentEntityName = en;
        final String userId = ((SignInSession)getSession()).getUserId();
        final String user = ((SignInSession)getSession()).getUser();
        //team sql
        String teamSql = "";
        if(en.equalsIgnoreCase("account")){
           
        }else if(en.equalsIgnoreCase("contact")){
        }else if(en.equalsIgnoreCase("user")){
        }
        List mapList = new ArrayList();
       if(type ==4){
    	    mapList = DAOImpl.queryEntityRelationList(teamSql);
       } else {
    	    mapList = DAOImpl.queryEntityRelationList(teamSql, entityId); 
       }
        Entity entity=null ;
        if(en.equalsIgnoreCase("account")||en.equalsIgnoreCase("contact")){
          if(type == 0){
            entity = Configuration.getEntityByName("crmuser");
            add(new Label("title","岗位"));
            }else if(type == 1){
              entity = Configuration.getEntityByName("userinfo");
              add(new Label("title","用户"));
            } 
          }
        else if(en.equalsIgnoreCase("userInfo")){
        	 entity = Configuration.getEntityByName("user_position");
             add(new Label("title"," 用户岗位关系"));
        }
        else{
            if(type == 0){
        	  entity = Configuration.getEntityByName("account");
        	  add(new Label("title","医院"));
            }else if(type == 1){
                entity = Configuration.getEntityByName("contact");
                add(new Label("title","医生"));
            }
            else if (type == 2){
              entity = Configuration.getEntityByName("userInfo");
              add(new Label("title","用户"));
            }
            else if (type == 3){
              entity = Configuration.getEntityByName("crmuser");
              add(new Label("title","下属岗位"));
            }else if (type == 4){
                entity = Configuration.getEntityByName("regionManage");
                add(new Label("title","区域管理"));
              }
        }
        
        List<Field> fields = entity.getFields();
        
        final String entityName = entity.getName();
        Form form = new Form("form");
        add(form);
        if(roleId != 1){
          WebMarkupContainer con = new WebMarkupContainer("remove_team_member_click");
            add(con);
            con.setVisible(false);
            //con.add(new AttributeAppender("style", new Model("display:none;"), ";"));
       
        }else{
        
           add(new SubmitLink("remove_team_member_click",form){
           @Override      
           public void onSubmit(){
             String teamtable = "";
           if(currentEntityName.equalsIgnoreCase("account")){
               teamtable = "accountcrmuser";
           }else if(currentEntityName.equalsIgnoreCase("contact")){
               teamtable = "contactcrmuser";
           }else if(currentEntityName.equalsIgnoreCase("crmuser")){
               if(type == 0){
                   teamtable = "accountcrmuser";
               }else if(type == 1){
                   teamtable = "contactcrmuser";
               }else if(type == 2){
                   teamtable = "user_position";
               }else if(type ==4){
            	   try{
                    	 delete(Integer.parseInt(entityId));
                     }catch(Exception e){
                     }    
               }else {
                 teamtable = "crmuser";
               }
           }else if(currentEntityName.equalsIgnoreCase("userinfo")){
        	   teamtable = "user_position";
           }
            
           for(String rid:selectedRowIds){
             try{
            	 int positionId = 0;
            	 int otherId = 0;
            	 String fromtable = teamtable;
            	  if(teamtable.equalsIgnoreCase("accountcrmuser")){
            		  AccountCRMUserRelation acr =  DAOImpl.getAccountsByAccountCrmuserId(Integer.parseInt(rid));
            		  positionId =  acr.getCrmuserId();
            		  otherId = acr.getAccountId();
            		  fromtable = teamtable;
            	  }else if(teamtable.equalsIgnoreCase("user_position")){
            		  UserPosition up =  DAOImpl.getUserPositionById(Integer.parseInt(rid));
            		  positionId =  up.getPositionId();
            		  otherId = up.getUserId();
            		  fromtable = "userinfo";
            	  }
                  if((type==3)&&fromtable.equalsIgnoreCase("crmuser")){
                	  User userinfo = DAOImpl.getCrmUserById(entityId);
                	  DAOImpl.updateCrmUserReportById(rid, String.valueOf(userinfo.getReportto()));
                	  User reporttouser = DAOImpl.getCrmUserById(userinfo.getReportto());
                	  DAOImpl.insertAudit(entityName, "上级岗位", userinfo.getName(),reporttouser.getName(), rid, user);
                  }else{
                	  DAOImpl.removeEntityFromTeam(teamtable,rid);
                	  DAOImpl.insertRealtionHestory(fromtable,user,positionId,otherId);
                  }
                
             }catch(Exception e){
                 
             }
         }
        	   if(selectedRowIds.size()>0){
        		   setResponsePage(new EntityDetailPage(currentEntityName,etId));
        	   }
           
      
             }
           });
       }
        //add button submission
        if(roleId != 1){
        	 WebMarkupContainer con = new WebMarkupContainer("add_users_link");
             add(con);
             con.setVisible(false);
        }else{
        	add(new Link<Void>("add_users_link"){

                @Override
                public void onClick() {
                  this.setResponsePage(new SearchCRMUserPage(currentEntityName,entityId,userId,type));
                }
                
            });

        }
        CheckGroup group = new CheckGroup("group",new PropertyModel(this,"selectedRowIds"));
        form.add(group); 
        if(roleId == 1){
            if(type!=4){
            	CheckGroupSelector chks = new CheckGroupSelector("checkboxs");
                group.add(chks);
                WebMarkupContainer container_label = new WebMarkupContainer("checkboxs_label");
                group.add(container_label);
                container_label.add(new AttributeAppender("for", new Model(chks.getMarkupId()), " ")); 
            }else{
                WebMarkupContainer container = new WebMarkupContainer("checkboxs");
                container.setVisible(false);
                WebMarkupContainer container_label = new WebMarkupContainer("checkboxs_label");
                container_label.setVisible(false);
                group.add(container);
                group.add(container_label);
            }
        }else{
            WebMarkupContainer container = new WebMarkupContainer("checkboxs");
            container.setVisible(false);
            WebMarkupContainer container_label = new WebMarkupContainer("checkboxs_label");
            container_label.setVisible(false);
            group.add(container);
            group.add(container_label);
        }
        //set column name
        RepeatingView columnNameRepeater = new RepeatingView("columnNameRepeater");
        group.add(columnNameRepeater);
        int count=0;
        for(Field f:entity.getFields()){
            if (!f.isVisible()|| f.getPriority() >1)
                continue;
            AbstractItem item = new AbstractItem(columnNameRepeater.newChildId());
            if(count==0){
                item.add(new AttributeAppender("class", new Model("table-first-link"), " "));
                count++;
            }
            columnNameRepeater.add(item);
            item.add(new Label("columnName", f.getDisplay())); 
        }
        
                

        
        
        
         final Map<String, Map> tableData = Maps.newHashMap();
        List<String> ids = Lists.newArrayList();
        for (Map map : (List<Map>) mapList) {
            String key = String.valueOf(map.get("id"));
            ids.add(key);
            tableData.put(key, map);
        }
        fieldsd=fields;
        final PageableListView<String> listview = new PageableListView<String>("dataRowRepeater", ids, 10) {
            
          
            @Override          
            protected void populateItem(ListItem<String> item) {
                String key = item.getDefaultModelObjectAsString();
                RepeatingView columnRepeater = new RepeatingView("columnRepeater");
                Map map = tableData.get(key);
                item.add(columnRepeater);
                final String realId =  String.valueOf(map.get("id"));
                final String rowId =  String.valueOf(map.get("rid")); 
            for (Field f : fieldsd) {
                if (!f.isVisible() || f.getPriority() >1)
                    continue;
                AbstractItem columnitem = new AbstractItem(columnRepeater.newChildId(), new Model() {
                    @Override
                    public Serializable getObject() {
                        Param p = new Param();
                        p.setId(rowId);
                        p.setExtraId(realId);
                        p.setEntityName(entityName);
                        return p;
                    }
                });
                if (f.isDetailLink()) {
                    String value = CRMUtility.formatValue(f.getFormatter(), String.valueOf(map.get(f.getName())));
                    if(value.equals("null")||value.equals("")||value.equals("dummy")){
                      value = "无";
                    }
                    columnitem.add(new AttributeAppender("class", new Model("table-first-link"), " "));
                    // columnitem.add(new Label("celldata", value));
                    columnitem.add(new DetailLinkFragment("celldata","detailFragment",this.getParent().getParent().getParent(),value));
//                    columnitem.add(new   DetailLinkFragment("celldata", "detailFragment", this.getParent().getParent().getParent(),value,f.getRelationTable(),String.valueOf(map.get(f.getName()))));
                } else {
                    if (f.getPicklist() != null) {
                        // get option from picklist
                        String value = CRMUtility.formatValue(f.getFormatter(), DAOImpl.queryPickListByIdCached(f.getPicklist(), String.valueOf(map.get(f.getName()))));
                        if(value.equals("null")||value.equals("")||value.equals("dummy")){
                          value = "无";
                        }
                        columnitem.add(new Label("celldata", value));
                    } else if(f.getRelationTable() != null){
                    	System.out.println("hrere");
                        String value = CRMUtility.formatValue(f.getFormatter(), DAOImpl.queryCachedRelationDataById(f.getRelationTable(), String.valueOf(map.get(f.getName()))));
                        if(value.equals("null")||value.equals("")||value.equals("dummy")){
                          value = "无";
                        }
                        if (roleId==1) {
                        	columnitem.add(new DetailLinkFragment("celldata", "detailFragment", this.getParent().getParent().getParent(),value,f.getRelationTable(),String.valueOf(map.get(f.getName()))));
                        }else{
                        	if(!f.getRelationTable().equalsIgnoreCase("crmuser")){
                        		columnitem.add(new DetailLinkFragment("celldata", "detailFragment", this.getParent().getParent().getParent(),value,f.getRelationTable(),String.valueOf(map.get(f.getName()))));
                        	}else{
                        		columnitem.add(new Label("celldata", value)).setEscapeModelStrings(false);
                        	}
                        }
                        
                    }else {
                        String value = CRMUtility.formatValue(f.getFormatter(), String.valueOf(map.get(f.getName())));
                        if(value.equals("null")||value.equals("")||value.equals("dummy")){
                          value = "无";
                        }
                        columnitem.add(new Label("celldata", value));
                    }
                }
                columnRepeater.add(columnitem);
              }
            WebMarkupContainer container_label = new WebMarkupContainer("checkbox_label");
//            if(type!=4){
                item.add(container_label);
//            }else{
//            	container_label.setVisible(false);
//                item.add(container_label);
//            }
            
            if(roleId == 1){
                if(type!=4){
                	Check chk = new Check("checkbox", new Model(String.valueOf(rowId)));
                    container_label.add(new AttributeAppender("for", new Model(chk.getMarkupId()), " "));        
                    item.add(chk);
                }else{
                    WebMarkupContainer container = new WebMarkupContainer("checkbox");
                    container.setVisible(false);
                    item.add(container);
                }
            }else{
                WebMarkupContainer container = new WebMarkupContainer("checkbox");
                container.setVisible(false);
                item.add(container);
            }
         
            
        }
            
        };
        group.add(listview);
        //PagingNavigator nav = new PagingNavigator("navigator", listview);
        AjaxPagingNavigator nav =new AjaxPagingNavigator("navigator", listview);
        nav.setOutputMarkupId(true);
        
        group.setOutputMarkupId(true);
        group.setRenderBodyOnly(false);
        
        group.add(nav);
        
        group.setVersioned(false);
        
        
        
        add(new NewDataFormPanel("formPanel",entity,null));
    }

    public TeamManPanel(String id, IModel<?> model) {
        super(id, model);
    }
    
    private class DetailLinkFragment extends Fragment {

        public DetailLinkFragment(String id, String markupId, MarkupContainer markupProvider, String name,final String entityName ,final String eid) {
            super(id, markupId, markupProvider);
//            final String str = DAOImpl.queryEntityByName(caption);
            add(new Link("detailclick") {
                @Override
                public void onClick() {

                    setResponsePage(new EntityDetailPage(entityName, eid));
                }
            }.add(new Label("caption", new Model<String>(name))));
        }
        public DetailLinkFragment( String id, String markupId, MarkupContainer markupProvider, String caption) {
            super(id, markupId, markupProvider);
            add(new Link("detailclick") {

                @Override
                public void onClick() {
                	Param p = (Param)getParent().getParent().getDefaultModelObject();
                   setResponsePage(new EntityDetailPage(p.getEntityName(),p.getExtraId())); 
                }
            }.add(new Label("caption", new Model<String>(caption))));
        }
    }
    
    
    private class checkboxFragment extends Fragment {

      public checkboxFragment(String id, String markupId, MarkupContainer markupProvider,Model Imodel)
      {
        super(id, markupId, markupProvider);
        // TODO Auto-generated constructor stub
        add(new Check("checkbox",Imodel));
      }
      
    }
    private ArrayList<Integer> queryPosition(int positionId)
    {
	  	ArrayList<Integer> ids = new ArrayList<>();
	  	
	  	int level = queryPositionLevel(positionId);
	  	
	  	if(level == 11)
	  	{
	  		ids.add(positionId);
	  	}
	  	else if(level > 11)
	  	{
	  		for(int id : queryPositionByParent(positionId))
	  		{
	  			level = queryPositionLevel(positionId);
	  			
	  			if(level == 11)
	  			{
	  				ids.add(level);
	  			}
	  			else if(level > 11)
	  			{
	  				ids.addAll(queryPosition(id));
	  			}
	  		}
	  	}
	  	
	  	return ids;
    }
    
    private String getCondition(int positionId)
    {
  	  ArrayList<Integer> ids = queryPosition(positionId);
  	  StringBuilder sb = new StringBuilder();
  	  if(ids != null && ids.size() > 0)
  	  {
  		  sb = new StringBuilder();
  		  sb.append("position_id in(");
  		  sb.append(ids.get(0));
  		  ids.remove(0);
  		    		  
  		  for(int id : ids)
  		  {
  			  sb.append(",");
  			  sb.append(id);
  		  }
  		  
  		  sb.append(")");
  	  }


  	  return sb.toString();
    }
    private int queryPositionLevel(int positionId)
    {
    	int level = 100;
    	
    	Map position = DAOImpl.queryEntityById("select * from crmuser where id = ?", String.valueOf(positionId));
    	if(position.size() > 0)
    	{
    		level = (int) position.get("level");
    	}
    	
    	return level;
    }
    
    private ArrayList<Integer> queryPositionByParent(int positionId)
    {
    	List list = DAOImpl.searchCRMUserByManager(String.valueOf(positionId), "");
    	ArrayList<Integer> ids = new ArrayList<>();
    	for(Object o : list)
    	{
    		Map map = (Map) o;
    		ids.add(Integer.valueOf((Integer)map.get("id")));
    	}
    	
    	return ids;
    }
    private void delete(int positionId)
    {
    	int level = queryPositionLevel(positionId);
    	
    	if(level == 11)
    	{
    		DAOImpl.deleteAccountTeamWithPositionId(positionId);
    	}
    	else if(level > 1)
    	{
    		ArrayList<Integer> ids = queryPositionByParent(positionId);
    		for(int id : ids)
    		{
    			delete(id);
    		}
    	}
    }
   
}
