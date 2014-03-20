package com.bxy.salestrategies.common;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.bxy.salestrategies.AccountPage;
import com.bxy.salestrategies.ActivityPage;
import com.bxy.salestrategies.ContactPage;
import com.bxy.salestrategies.DnaPanel;
import com.bxy.salestrategies.Index;
import com.bxy.salestrategies.OpportunityPage;
import com.bxy.salestrategies.PoliticalPanel;
import com.bxy.salestrategies.SignInSession;
import com.bxy.salestrategies.SwotPanel;
import com.bxy.salestrategies.TargetPanel;
import com.bxy.salestrategies.TargetPanel;
import com.bxy.salestrategies.UserPage;
import com.bxy.salestrategies.db.DAOImpl;
import com.bxy.salestrategies.model.Activity;
import com.bxy.salestrategies.util.CRMUtility;
import com.bxy.salestrategies.util.Configuration;
import com.google.common.collect.Maps;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;

public class EntityDetailPage extends Index {
    private static final Logger logger = Logger.getLogger(EntityDetailPage.class);
    
    private static final long serialVersionUID = -2613412283023068638L;
    private final String user = ((SignInSession)getSession()).getUser();
    private static int NUM_OF_COLUMN  = 3;
    
    public EntityDetailPage(){
    	String entityId = this.getRequest().getRequestParameters().getParameterValue("id").toString();
  	  	String nameForEntity = this.getRequest().getRequestParameters().getParameterValue("entityName").toString();
  	  	String fromPage = this.getRequest().getRequestParameters().getParameterValue("formPage").toString();
  	  	if(null==entityId&&null==nameForEntity){
	    	RepeatingView div = new RepeatingView("promptDiv");
	        AbstractItem groupitem = new AbstractItem(div.newChildId());
	        Label promptButton = new Label("promptButton","X");
	        groupitem.add(promptButton);
	        final Label promptLabel = new Label("prompt","提示:操作已成功！");
	        groupitem.add(promptLabel);
	        div.add(new AttributeAppender("style",new Model("display:none"),";"));
	        groupitem.add(new AttributeAppender("style",new Model("display:none"),";"));
	        div.add(groupitem);
	        add(div);
	        add(new Label("name","null"));
	        add(new Label("entityName","null"));
	        WebMarkupContainer operationBar = new WebMarkupContainer("operationBar");
	    	add(operationBar);
	        WebMarkupContainer detailed = new WebMarkupContainer("detailed");
	    	add(detailed);
	    	WebMarkupContainer teamPanel = new WebMarkupContainer("teamPanel");
	    	add(teamPanel);
	    	WebMarkupContainer teamPanel2 = new WebMarkupContainer("teamPanel2");
	    	add(teamPanel2);
	    	WebMarkupContainer teamPanel3 = new WebMarkupContainer("teamPanel3");
	    	add(teamPanel3);
//	    	WebMarkupContainer teamPanel4 = new WebMarkupContainer("teamPanel4");
//	    	add(teamPanel4);
  	  	}else{
  	  		initPage(nameForEntity.toString(),entityId.toString(),fromPage);
  	  	}

    }
    public EntityDetailPage(final String entityName, final String id){
    	initPage(entityName,id,null);
    }
    public void initPage(final String entityName, final String id,final String fromPage){
        //check the permission
       
        
        Map<String, Entity> entities = Configuration.getEntityTable();
        final Entity entity = entities.get(entityName);
        final RepeatingView div = new RepeatingView("promptDiv");
        final AbstractItem groupitem = new AbstractItem(div.newChildId());
        final Label promptButton = new Label("promptButton","X");
        groupitem.add(promptButton);
        final Label promptLabel = new Label("prompt","提示:操作已成功！");
        groupitem.add(promptLabel);
        div.add(new AttributeAppender("style",new Model("display:none"),";"));
        groupitem.add(new AttributeAppender("style",new Model("display:none"),";"));
        div.add(groupitem);
        add(div);
        long lid = Long.parseLong(id);
        Map map = DAOImpl.queryEntityById(entity.getSql_ent(), String.valueOf(lid));
        
        Label HomeIcon=new Label("HomeIcon"," ");
        HomeIcon.add(new AttributeAppender("class", new Model<String>("icon-home"), " "));
        add(HomeIcon);
        
        String fieldName =  String.valueOf(map.get("name"));
        if("null".equals(fieldName)){
        	add(new Label("name",""));
        	add(new Label("entityName",String.valueOf(map.get("name"))));
        }else{
        	add(new Label("name",fieldName));
        	add(new Label("entityName",String.valueOf(map.get("name"))));
        }
       
        add(new EntityDetailPanel("detailed",entity,map,id,3,entityName));

         if(entityName.equalsIgnoreCase("account")){

             String dynamicHtml1 = " <a href=\"#teamPanel\"><i class=\"icon-user\"> </i><span style=\"margin: 25px\">用户</span></a>";
             Label divLabel1 = new Label("divLabel1",dynamicHtml1 );
             divLabel1.setEscapeModelStrings(false);
             add(divLabel1);
             
             String dynamicHtml2 = " <a href=\"#relation2\"><i class=\"icon-briefcase\"></i><span style=\"margin: 25px\">商机</span></a>";
             Label divLabel2 = new Label("divLabel2",dynamicHtml2);
             divLabel2.setEscapeModelStrings(false);
             add(divLabel2);
             
             
             
             Label empty1 = new Label("divLabel3", "");
             IModel<String> Model1 = new AbstractReadOnlyModel<String>() {
                  public String getObject() {
                       return "dispaly: none;"; 
                   }
              }; 
             empty1.add(new AttributeModifier("style", true, Model1));
             add(empty1);
             
             Label empty2 = new Label("divLabel4", "");
             IModel<String> Model2 = new AbstractReadOnlyModel<String>() {
                  public String getObject() {
                       return "dispaly: none;"; 
                   }
              }; 
             empty2.add(new AttributeModifier("style", true, Model2));
             add(empty2);
             
             Label empty3 = new Label("divLabel5", "");
             IModel<String> Model3= new AbstractReadOnlyModel<String>() {
                  public String getObject() {
                       return "dispaly: none;"; 
                   }
              }; 
             empty3.add(new AttributeModifier("style", true, Model3));
             add(empty3);
             
             
             
             add(new TeamManPanel("teamPanel",entityName,String.valueOf(lid),0));
             add(new TeamManPanel("teamPanel2",entityName,String.valueOf(lid),1));
             add(new EmptyPanel("teamPanel3"));
             add(new EmptyPanel("teamPanel4"));
             add(new EmptyPanel("teamPanel5"));
         }
         else if(entityName.equalsIgnoreCase("contact")){
             
             String dynamicHtml1 = " <a href=\"#teamPanel\"><i class=\"icon-user\"> </i><span style=\"margin: 25px\">用户</span></a>";
             Label divLabel1 = new Label("divLabel1",dynamicHtml1 );
             divLabel1.setEscapeModelStrings(false);
             add(divLabel1);
             
             String dynamicHtml2 = " <a href=\"#relation2\"><i class=\"icon-briefcase\"></i><span style=\"margin: 25px\">商机</span></a>";
             Label divLabel2 = new Label("divLabel2",dynamicHtml2);
             divLabel2.setEscapeModelStrings(false);
             add(divLabel2);
             
             Label empty1 = new Label("divLabel3", "");
             IModel<String> Model1 = new AbstractReadOnlyModel<String>() {
                  public String getObject() {
                       return "dispaly: none;"; 
                   }
              }; 
             empty1.add(new AttributeModifier("style", true, Model1));
             add(empty1);
             
             Label empty2 = new Label("divLabel4", "");
             IModel<String> Model2 = new AbstractReadOnlyModel<String>() {
                  public String getObject() {
                       return "dispaly: none;"; 
                   }
              }; 
             empty2.add(new AttributeModifier("style", true, Model2));
             add(empty2);
             
             Label empty3 = new Label("divLabel5", "");
             IModel<String> Model3= new AbstractReadOnlyModel<String>() {
                  public String getObject() {
                       return "dispaly: none;"; 
                   }
              }; 
             empty3.add(new AttributeModifier("style", true, Model3));
             add(empty3);
             
             add(new TeamManPanel("teamPanel",entityName,String.valueOf(lid),0));
             add(new TeamManPanel("teamPanel2",entityName,String.valueOf(lid),1));
             add(new EmptyPanel("teamPanel3"));
             add(new EmptyPanel("teamPanel4"));
             add(new EmptyPanel("teamPanel5"));
//             add(new TeamManPanel("teamPanel4",entityName,String.valueOf(lid),3));
         }
         else if(entityName.equalsIgnoreCase("opportunity")){
        
             String dynamicHtml1 = " <a href=\"#teamPanel\"><i class=\"icon-group\"></i><span style=\"margin: 25px\">团队</span></a>";
             Label divLabel1 = new Label("divLabel1",dynamicHtml1 );
             divLabel1.setEscapeModelStrings(false);
             add(divLabel1);
             
             String dynamicHtml2 = " <a href=\"#relation2\"><i class=\"icon-user-md\"> </i><span style=\"margin: 25px\">联系人</span></a>";
             Label divLabel2 = new Label("divLabel2",dynamicHtml2);
             divLabel2.setEscapeModelStrings(false);
             add(divLabel2);
             
             String dynamicHtml3 = " <a href=\"#relation3\"><i class=\"icon-resize-horizontal\"></i><span style=\"margin: 25px\">DNA</span></a>";
             Label divLabel3 = new Label("divLabel3",dynamicHtml3);
             divLabel3.setEscapeModelStrings(false);
             add(divLabel3);
             
             String dynamicHtml4 = " <a href=\"#relation4\"><i class=\"icon-key\"></i><span style=\"margin: 25px\">战术策略</span></a>";
             Label divLabel4 = new Label("divLabel4",dynamicHtml4);
             divLabel4.setEscapeModelStrings(false);
             add(divLabel4);
             Label icon4=new Label("icon4"," ");
             icon4.add(new AttributeAppender("class", new Model<String>("  icon-screenshot"), ""));
             add(icon4);
             add(new Label("relationName4","战术策略"));
             
             String dynamicHtml5 = " <a href=\"#relation5\"><i class=\"icon-font\"></i><span style=\"margin: 25px\">活动</span></a>";
             Label divLabel5 = new Label("divLabel5",dynamicHtml5);
             divLabel5.setEscapeModelStrings(false);
             add(divLabel5);
             
	     add(new TeamManPanel("teamPanel",entityName,String.valueOf(lid),0));
	     add(new TeamManPanel("teamPanel2",entityName,String.valueOf(lid),1));
	     add(new TeamManPanel("teamPanel3",entityName,String.valueOf(lid),4));
	     add(new TeamManPanel("teamPanel4",entityName,String.valueOf(lid),5));
         add(new EmptyPanel("teamPanel5"));
       }else if(entityName.equalsIgnoreCase("user")){
          
           
           
         String dynamicHtml1 = " <a href=\"#teamPanel\"><i class=\"icon-road\"> </i><span style=\"margin: 25px\">客户</span></a>";
         Label divLabel1 = new Label("divLabel1",dynamicHtml1 );
         divLabel1.setEscapeModelStrings(false);
         add(divLabel1);
             
         String dynamicHtml2 = " <a href=\"#relation2\"><i class=\"icon-user-md\"> </i><span style=\"margin: 25px\">联系人</span></a>";
         Label divLabel2 = new Label("divLabel2",dynamicHtml2);
         divLabel2.setEscapeModelStrings(false);
         add(divLabel2);

         Label empty1 = new Label("divLabel3", "");
         IModel<String> Model1 = new AbstractReadOnlyModel<String>() {
                  public String getObject() {
                       return "dispaly: none;"; 
                   }
              }; 
         empty1.add(new AttributeModifier("style", true, Model1));
         add(empty1);
             
         Label empty2 = new Label("divLabel4", "");
         IModel<String> Model2 = new AbstractReadOnlyModel<String>() {
                  public String getObject() {
                       return "dispaly: none;"; 
                   }
              }; 
         empty2.add(new AttributeModifier("style", true, Model2));
         add(empty2);
             
         Label empty3 = new Label("divLabel5", "");
         IModel<String> Model3= new AbstractReadOnlyModel<String>() {
                  public String getObject() {
                       return "dispaly: none;"; 
                   }
              }; 
         empty3.add(new AttributeModifier("style", true, Model3));
         add(empty3);
          
       add(new TeamManPanel("teamPanel",entityName,String.valueOf(lid),0));
       add(new TeamManPanel("teamPanel2",entityName,String.valueOf(lid),1));
       add(new TeamManPanel("teamPanel3",entityName,String.valueOf(lid),2));
       add(new EmptyPanel("teamPanel4"));
       add(new EmptyPanel("teamPanel5"));
  	   
     }else{
         
         Label empty1 = new Label("divLabel1", "");
         IModel<String> Model1 = new AbstractReadOnlyModel<String>() {
                  public String getObject() {
                       return "dispaly: none;"; 
                   }
              }; 
         empty1.add(new AttributeModifier("style", true, Model1));
         add(empty1);
             
         Label empty2 = new Label("divLabel2", "");
         IModel<String> Model2= new AbstractReadOnlyModel<String>() {
                  public String getObject() {
                       return "dispaly: none;"; 
                   }
              }; 
         empty2.add(new AttributeModifier("style", true, Model2));
         add(empty2);
         
         Label empty3 = new Label("divLabel3", "");
         IModel<String> Model3 = new AbstractReadOnlyModel<String>() {
                  public String getObject() {
                       return "dispaly: none;"; 
                   }
              }; 
         empty3.add(new AttributeModifier("style", true, Model3));
         add(empty3);
             
         Label empty4 = new Label("divLabel4", "");
         IModel<String> Model4 = new AbstractReadOnlyModel<String>() {
                  public String getObject() {
                       return "dispaly: none;"; 
                   }
              }; 
         empty4.add(new AttributeModifier("style", true, Model4));
         add(empty4);
             
         Label empty5 = new Label("divLabel5", "");
         IModel<String> Model5= new AbstractReadOnlyModel<String>() {
                  public String getObject() {
                       return "dispaly: none;"; 
                   }
              }; 
         empty5.add(new AttributeModifier("style", true, Model5));
         add(empty5);
           add(new EmptyPanel("teamPanel"));
           add(new EmptyPanel("teamPanel2"));
           add(new EmptyPanel("teamPanel3"));
           add(new EmptyPanel("teamPanel4"));
           add(new EmptyPanel("teamPanel5"));
//            add(new EmptyPanel("teamPanel4"));
        }

         add(new AbstractAjaxBehavior(){

            @Override
            public void onRequest() {
            }

            @Override
            public void renderHead(Component component, IHeaderResponse response) {
                super.renderHead(component, response);
                response.render(OnDomReadyHeaderItem.forScript("$(\"#navitem-"+entityName+"\").addClass(\"active\");"));
            }  
             
         });
         final Page this_page =this;
         ICRUDActionListener actionListener = new DefaultCRUDActionListener(){
            @Override
            public void delete() {
                if(entityName.equals("account")){
                	DAOImpl.deleteRecord(id, entityName);
                    setResponsePage(new AccountPage());
                }else if(entityName.equals("contact")){
                    DAOImpl.deleteRecord(id, entityName);
                    setResponsePage(new ContactPage());
                }else if(entityName.equals("activity")) {
                	DAOImpl.deleteRecord(id, entityName);
                    setResponsePage(new ActivityPage());
              }else if(entityName.equalsIgnoreCase("user")){
                    if(DAOImpl.deleteRecord(id, entityName)>0){
                       DAOImpl.updateCrmUserReport(id, "-1");
                    }
                    setResponsePage(new UserPage());
              }else if(entityName.equals("opportunity")){
            	  DAOImpl.deleteRecord(id, entityName);
            	  setResponsePage(new OpportunityPage());
              }else if(entityName.equals("target_acquisition")){
            	  //根据id获取对象从而获取业务机会ID
            	  String opportunityId = DAOImpl.queryEntityById("select * from target_acquisition where id = ?", id).get("opportunity_id").toString(); 
            	  DAOImpl.deleteRecord(id, entityName);
            	  //跳转界面
            	  setResponsePage(new EntityDetailPage("opportunity",opportunityId));
              }
            }

            @Override
            public void update() {
            	if(entityName.equals("activity")){
            		//根据id获取对象如果是计划状态则进行操作
                    Activity activity =DAOImpl.getActivityById(Integer.parseInt(id));
                    if(activity.getStatus()==1){
                    	PageParameters pp = new PageParameters();
                        pp.add("id", id);
                        pp.add("entityName", entityName);
                        setResponsePage(new EditDataPage(entityName,id,this_page.getClass(),pp));
                    }
            	}else{
                    PageParameters pp = new PageParameters();
                    pp.add("id", id);
                    pp.add("entityName", entityName);
                    setResponsePage(new EditDataPage(entityName,id,this_page.getClass(),pp));
            	}
            	
            }
            @Override
            public void doneBtn(){
              //根据id获取对象如果是计划状态则进行操作
              Activity activity =DAOImpl.getActivityById(Integer.parseInt(id));
              if(activity.getStatus()==1){
            	  final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                  Date time = new Date();
                  String d = dateformat.format(time);
                  DAOImpl.doneRecord(id, entityName, d);
                  setResponsePage(new EntityDetailPage(entityName,id));
              }
            }
            @Override
            public void resetPassword(int userId){
            	if(DAOImpl.resetUserPassword(userId)>0){
                		div.add(new AttributeAppender("style",new Model("display:block"),";"));
                		groupitem.add(new AttributeAppender("style",new Model("display:block"),";"));
                		promptLabel.add(new AttributeAppender("style",new Model("display:block"),";"));
                		promptButton.add(new AttributeAppender("style",new Model("display:block"),";"));
                	};
            }

            @Override
            public void downLoadBtn() throws Exception
            {
              // TODO Auto-generated method stub
              
            }
            @Override
            public void noExecute(String entityName,int entityId){
            	//修改活动状态为未执行
            	//根据id获取对象如果是计划状态则进行操作
                Activity activity =DAOImpl.getActivityById(Integer.parseInt(id));
                if(activity.getStatus()==1){
	            	if(DAOImpl.updateActivityStatusById(entityId)){
	            		setResponsePage(new EntityDetailPage(entityName,String.valueOf(entityId)));
	            	};
                }
            }

			@Override
			public void merge() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void ineffective() {
				// TODO Auto-generated method stub
				
			}
         };
         if(entity.getName().equals("opportunity")){
  	  	     add(new DnaPanel("dnaPanel",id));
  	  	     final Entity target_entity = entities.get("target_acquisition");
  	  	     String sql = "select * from target_acquisition where opportunity_id ="+id;
  	  	     List target_data = DAOImpl.queryEntityRelationList(sql);
  	  	     Map<String,Object> params = new HashMap();
  	  	     params.put("opportunityId",id);
  	  	     add(new PageableTablePanel("targetPanel", target_entity, target_data, params));
  	  	     add(new PoliticalPanel("politicalPanel",id));
  	  	     add(new SwotPanel("swotPanel",id));
         }else{
        	 add(new Label("dnaPanel",""));
        	 add(new Label("targetPanel",""));
        	 add(new Label("politicalPanel",""));
        	 add(new Label("swotPanel",""));
         }
	  	   add(new CRUDPanel("operationBar",entity.getName(),id, CRMUtility.getPermissionForEntity(entity.getName()),actionListener));
    }

}
