package com.bxy.salestrategies.common;
/**
*
* @author Dong
*/
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import javax.mail.Session;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
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
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableChoiceLabel;

import com.bxy.salestrategies.SignInSession;
import com.bxy.salestrategies.SearchCRMUserPage;
import com.bxy.salestrategies.db.DAOImpl;
import com.bxy.salestrategies.model.Account;
import com.bxy.salestrategies.model.AccountUserTeam;
import com.bxy.salestrategies.model.Activity;
import com.bxy.salestrategies.model.ContactUserTeam;
import com.bxy.salestrategies.model.Opportunity;
import com.bxy.salestrategies.model.Tactics;
import com.bxy.salestrategies.model.User;
import com.bxy.salestrategies.util.CRMUtility;
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
        	if(type==0){
        		teamSql = "select * from (select user.*,accountuserteam.id as rid from accountuserteam left join user on user.id = accountuserteam.user_id where account_id = ?) as atable";
        	}else if(type==1){
        		teamSql = "select * from (select contact.*,contact.id as rid from contact  where account_id = ?) as atable";
        	}else if(type==2){
        		teamSql = "select * from (select opportunity.*,opportunity.id as rid from opportunity left join account on account.id=opportunity.account_id where account.id = ?) as atable";
        	}
        	
        }else if(en.equalsIgnoreCase("contact")){
        	if(type==0){
        		teamSql = "select * from (select user.*,user.id as rid from user  left join contactuserteam on user.id = contactuserteam.user_id left join contact on  contactuserteam.contact_id = contact.id  where contact.id = ?) as atable";
        	}else if(type==1){
        		teamSql = "select * from (select opportunity.*,opportunity.id as rid from opportunity left join opportunitycontactteam on opportunitycontactteam.opportunity_id = opportunity.id left join contact on contact.id = opportunitycontactteam.contact_id  where contact.id = ?) as atable";
        	}
        }else if(en.equalsIgnoreCase("user")){
        	if(type == 0){
        		teamSql = "select * from (select account.*,account.id as rid  from account left join accountuserteam on account.id = accountuserteam.account_id left join user on accountuserteam.user_id = user.id where user.id = ? ) as atable";
        	}else if(type == 1){
        		teamSql = "select * from (select contact.* ,contact.id as rid from contact left join contactuserteam on contact.id = contactuserteam.contact_id left join user on contactuserteam.user_id = user.id where user.id = ?) as atable ";
        	}else if(type == 2){                                                                                 
        		teamSql = "select * from (select opportunity.* ,opportunity.id as rid from opportunity left join opportunityuserteam on opportunity.id = opportunityuserteam.opportunity_id left join user on opportunityuserteam.user_id = user.id where user.id = ?) as atable ";
        	}
        }else if(en.equalsIgnoreCase("opportunity")){
        	if(type==0){
        		teamSql = "select * from (select user.id as user_id ,user.id as rid,opportunityuserteam.id,user.job_title,opportunity.id as opportunity_id ,user.office_tel,user.telephone,opportunityuserteam.team_role  from opportunityuserteam left join user on user.id = opportunityuserteam.user_id left join opportunity on opportunity.id = opportunityuserteam.opportunity_id where opportunity.id =?) as a";
        	}else if(type==1){
        		teamSql = "select * from (select opportunitycontactteam.*,contact.id as rid from contact left join opportunitycontactteam on opportunitycontactteam.contact_id = contact.id where opportunitycontactteam.opportunity_id = ?) as atable";
        	}else if(type==4){
        		teamSql = "select * from (select tactics.*,tactics.id as rid from tactics left join opportunity on opportunity.id = tactics.opportunity_id where tactics.opportunity_id = ?) as atable";
        	}else if(type==5){
        		teamSql = "select * from (select activity.*,account.id as account_name ,activity.id as rid from activity  left join contact on contact.id = activity.contact_id left join account on account.id = contact.account_id left join opportunity on opportunity.id = activity.opportunity_id where activity.opportunity_id = ?) as atable";
        	}
        }else if(en.equalsIgnoreCase("tactics")){
        	if(type==0){
        		teamSql = "select * from (select activity.*,account.id as account_name ,activity.id as rid from activity left join tactics on activity.tactics_id = tactics.id left join contact on contact.id = activity.contact_id left join account on account.id = contact.account_id where tactics.id =?) as a";
        	}
        }
        List mapList = new ArrayList();
    	    mapList = DAOImpl.queryEntityRelationList(teamSql, entityId); 
        Entity entity=null ;
        if(en.equalsIgnoreCase("account")){
        	if(type == 0){
        		entity = Configuration.getEntityByName("user");
                add(new Label("title","用户"));
              }else if(type == 1){
                  entity = Configuration.getEntityByName("contact");
                  add(new Label("title","联系人"));
              } else if(type == 2){
                  entity = Configuration.getEntityByName("opportunity");
                  add(new Label("title","商机"));
              } 
        	
        }else if(en.equalsIgnoreCase("contact")){
                 if(type==0){
                	 entity = Configuration.getEntityByName("user");
                     add(new Label("title","用户"));
                 }else if(type==1){
                  entity = Configuration.getEntityByName("opportunity");
                  add(new Label("title","商机"));}
        }else if(en.equalsIgnoreCase("opportunity")){
        	if(type == 0){
        		entity = Configuration.getEntityByName("opportunityuserteam");
                add(new Label("title","团队"));
              }else if(type == 1){
                  entity = Configuration.getEntityByName("opportunitycontactteam");
                  add(new Label("title","联系人"));
              }else if(type == 4){
                  entity = Configuration.getEntityByName("tactics");
                  add(new Label("title","战略计划"));
              }else if(type == 5){
                  entity = Configuration.getEntityByName("activity");
                  add(new Label("title","活动"));
              }
        }else if(en.equalsIgnoreCase("user")){
        	if(type == 0){
        		entity = Configuration.getEntityByName("account");
                add(new Label("title","客户"));
              }else if(type == 1){
                  entity = Configuration.getEntityByName("contact");
                  add(new Label("title","联系人"));
              }else if(type == 2){
                  entity = Configuration.getEntityByName("opportunity");
                  add(new Label("title","商机"));
              }
        }else if(en.equalsIgnoreCase("tactics")){
        	entity = Configuration.getEntityByName("activity");
            add(new Label("title","活动"));
        }
        
        List<Field> fields = entity.getFields();
        final String entityName = entity.getName();
        Form form = new Form("form");
        add(form);
           add(new SubmitLink("remove_team_member_click",form){
           @Override      
           public void onSubmit(){
             String teamtable = "";
           if(currentEntityName.equalsIgnoreCase("account")){
               if(type == 0){
            	   teamtable = "accountuserteam";
               }else if(type == 1){
            	   teamtable = "contact";
               }else if(type == 2){
            	   teamtable = "opportunity";
               }
           }else if(currentEntityName.equalsIgnoreCase("contact")){
        	   if(type == 0){
        		   teamtable = "contactuserteam";
               }else if(type == 1){
            	   teamtable = "opportunirycontactteam";
               }
        	   
           }else if(currentEntityName.equalsIgnoreCase("user")){
               if(type == 0){
                   teamtable = "accountuserteam";
               }else if(type == 1){
                   teamtable = "contactuserteam";
               }else if(type == 2){
                   teamtable = "opportunityuserteam";
               }
           }else if(currentEntityName.equalsIgnoreCase("opportunity")){
               if(type == 0){
                   teamtable = "opportunityuserteam";
               }else if(type == 1){
                   teamtable = "opportunitycontactteam";
               }else if(type == 4){
                   teamtable = "tactics";
               }else if(type == 5){
                   teamtable = "activity";
               }
           }else if(currentEntityName.equalsIgnoreCase("tactics")){
        	   teamtable = "activity";
           }
           for(String rid:selectedRowIds){
             try{
            	 int positionId = 0;
            	 int otherId = 0;
            	 String fromtable = teamtable;
            	  if(teamtable.equalsIgnoreCase("accountuserteam")){
            		  AccountUserTeam aut =  DAOImpl.getAccountsByAccountUserTeamId(Integer.parseInt(rid));
            		  otherId = aut.getAccount_id();
            		  fromtable = teamtable;
            	  }else if(teamtable.equalsIgnoreCase("countactuserteam")){
            		  ContactUserTeam cut =  DAOImpl.getUserPositionById(Integer.parseInt(rid));
            		  otherId = cut.getContact_id();
            		  fromtable = "user";
            	  }
                	  DAOImpl.removeEntityFromTeam(teamtable,rid);
//                	  DAOImpl.insertRealtionHestory(fromtable,user,otherId);
                 
             }catch(Exception e){
                 
             }
         }
           		
        	   if(selectedRowIds.size()>0){
        		   setResponsePage(new EntityDetailPage(currentEntityName,etId));
        	   }
             }
           });
//       }
        //add button submission
//        if(roleId != 1){
//        	 WebMarkupContainer con = new WebMarkupContainer("add_users_link");
//             add(con);
//             con.setVisible(false);
//        }else{
           long lid = Long.parseLong(etId);
           final Account ac = DAOImpl.getAccountById(String.valueOf(lid));
           final Opportunity ot = DAOImpl.getOpportunityById(String.valueOf(lid));
           final Tactics at = DAOImpl.getTacticsById(String.valueOf(lid));
           final Opportunity op = DAOImpl.getOpportunityByTacticsId(String.valueOf(lid));
        	add(new Link<Void>("add_users_link"){
                @Override
                public void onClick() {
                 if(!currentEntityName.equalsIgnoreCase("opportunity")){
                	 final Map<String,Object> params = Maps.newHashMap();
                	 if(currentEntityName.equalsIgnoreCase("account")&&(type==1)){
                             params.put("account.id", ac.getId());
                             params.put("account.name", ac.getName());
                		     this.setResponsePage(new CreateDataPage("contact",params));
                	 }else if(currentEntityName.equalsIgnoreCase("account")&&(type==2)){
                         params.put("account.id", ac.getId());
                         params.put("account.name", ac.getName());
            		     this.setResponsePage(new CreateDataPage("opportunity",params));
            	 }else if(currentEntityName.equalsIgnoreCase("tactics")){
		            		 params.put("opportunity.id", op.getId());
		                     params.put("opportunity.name", op.getName());	     
		            		 params.put("tactics.id", at.getId());
                             params.put("tactics.name", at.getName());
            		     this.setResponsePage(new CreateDataPage("activity",params));
                	 }else{
                		 this.setResponsePage(new SearchCRMUserPage(currentEntityName,entityId,userId,type));
                	 }
                 }else{
                	 if(type==0){
                		 this.setResponsePage(new NewTeamManPage("opportunityuserteam",entityId,null)); 
                	 }else{
                		 final Map<String,Object> params = Maps.newHashMap();
                         params.put("opportunity.id", ot.getId());
                         params.put("opportunity.name",ot.getName());
                		 if(type==4){
                			 this.setResponsePage(new CreateDataPage("tactics",params));
                		 }else if(type == 5){
                			 this.setResponsePage(new CreateDataPage("activity",params));
                		 }else{
                			 this.setResponsePage(new NewTeamManPage("opportunitycontactteam",entityId,null));
                			 }
                	 }
                 }
                }
                
            });

//        }
        CheckGroup group = new CheckGroup("group",new PropertyModel(this,"selectedRowIds"));
        form.add(group); 
            	CheckGroupSelector chks = new CheckGroupSelector("checkboxs");
                group.add(chks); 
                WebMarkupContainer container_label = new WebMarkupContainer("checkboxs_label");
                group.add(container_label);
                container_label.add(new AttributeAppender("for", new Model(chks.getMarkupId()), " ")); 
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
                            columnitem.add(new Label("celldata", value ));
                    } else if(f.getRelationTable() != null){
                        String value = CRMUtility.formatValue(f.getFormatter(), DAOImpl.queryCachedRelationDataById(f.getRelationTable(), String.valueOf(map.get(f.getName()))));
                        if(value.equals("null")||value.equals("")||value.equals("dummy")){
                          value = "无";
                        }
                        if (!value.equals("无")) {
                        	columnitem.add(new DetailLinkFragment("celldata", "detailFragment", this.getParent().getParent().getParent(),value,f.getRelationTable(),String.valueOf(map.get(f.getName()))));
                        }else{
//                        	if(!f.getRelationTable().equalsIgnoreCase("crmuser")){
//                        		columnitem.add(new DetailLinkFragment("celldata", "detailFragment", this.getParent().getParent().getParent(),value,f.getRelationTable(),String.valueOf(map.get(f.getName()))));
//                        	}else{
                        		columnitem.add(new Label("celldata", value)).setEscapeModelStrings(false);
                        	}
//                        }
                        
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
                item.add(container_label);
                final PageableListView<String> this_page =this;
                Check chk = new Check("checkbox", new Model(String.valueOf(realId)));
                container_label.add(new AttributeAppender("for", new Model(chk.getMarkupId()), " "));        
                item.add(chk);
                if(en.equals("opportunity")){
                    if(entityName.equals("opportunityuserteam")||entityName.equals("opportunitycontactteam")){
                    	Button button1  = new Button("editbtn") {
                          	public void onSubmit() {
                          		 Opportunity  op =  DAOImpl.getOpportunityByEntityId(entityName,realId);
                          		PageParameters pp = new PageParameters();
                                  pp.add("id", String.valueOf(op.getId()) );
                                  pp.add("realId",realId);
                                  pp.add("entityName", "opportunity");
                                  setResponsePage(new EditDataPage(entityName,realId,EntityDetailPage.class,pp));
                          			}
                          		};
                         item.add(button1);
                    }else{
                        WebMarkupContainer editbtn = new WebMarkupContainer("editbtn");
                        editbtn.setVisible(false);
                        item.add(editbtn);
                    }
                }else{
                    WebMarkupContainer editbtn = new WebMarkupContainer("editbtn");
                    editbtn.setVisible(false);
                    item.add(editbtn);
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
}
