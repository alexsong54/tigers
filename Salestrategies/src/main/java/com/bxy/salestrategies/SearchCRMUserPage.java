package com.bxy.salestrategies;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import com.bxy.salestrategies.db.DAOImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;

/**
 *
 *
 * @author Dong
 */
public class SearchCRMUserPage extends WebPage {

    private static final Logger logger = Logger.getLogger(SearchCRMUserPage.class);
    private String search_target;
    List<String> selectedUserIds = Lists.newArrayList();
    private String entityId;
    private String uid;
    private int type = 0;

    /**
     * Constructor
     *
     * @param parameters Page parameters (ignored since this is the home page)
     */
    public SearchCRMUserPage() {
        StringValue value = this.getRequest().getRequestParameters().getParameterValue("cid");
        StringValue entityname = getRequest().getRequestParameters().getParameterValue("entityname");
        StringValue postId = this.getRequest().getRequestParameters().getParameterValue("positionId");
        
        if (value != null) {
            entityId = value.toString();
        }
        if(postId != null){
          uid = postId.toString();
        }
        initPage(entityname.toString(), null, entityId,uid, type);
    }
    public SearchCRMUserPage(String entityName, final String entityId, String uid, int type) {
        this.entityId = entityId;
        this.type = type;
        this.uid=uid;
        initPage(entityName, null, entityId,uid, type);
    }

    public SearchCRMUserPage(List<Map> maplist, String entityName, final String entityId, String uid,int type) {
        this.entityId = entityId;
        this.type = type;
        this.uid = uid;
        initPage(entityName, maplist, entityId,uid, type);
    }

    public void initPage(final String entityname, List<Map> list, final String entityId, final String uid, final int type) {
        Form form = new Form("form") {
            @SuppressWarnings("unchecked")
			@Override
            protected void onSubmit() {
                logger.debug("the form was submitted!");
                List<Map> maplist = null;

                if (entityname.equals("account") || entityname.equals("contact")) {
                    maplist = DAOImpl.searchUser(search_target);
                }else {
                    if (type == 0||type==4) {
                        maplist = DAOImpl.searchCRMAccount(search_target);
                    }else if (type == 1) {
                      maplist = DAOImpl.searchCRMContact(search_target);
                    }else if(type == 2){
                      maplist = DAOImpl.searchUser(search_target);
                    } else{
                      maplist = DAOImpl.searchUser(search_target);
                    }
                }


                setResponsePage(new SearchCRMUserPage(maplist, entityname, entityId,uid, type));

            }
        };
        form.add(new TextField<String>("search_input", new PropertyModel<String>(this, "search_target")));
        add(form);




        Form users_sbumission_form = new Form("users_sbumission_form");
        
        users_sbumission_form.add(new AjaxButton("ajax-button", users_sbumission_form)
        {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
            {
                logger.debug("seletedUserIds:" + selectedUserIds);
              for (String positionId : selectedUserIds) {
                  try {
                      DAOImpl.insertRelationOfEntityIDCRMUserID(entityname, entityId,positionId ,type);

                  } catch (Exception e) {
                  
                  }
              }
              
              target.appendJavaScript(" window.opener.location.href='./EntityDetailPage?entityName="+entityname+"&id="+entityId+"'; window.close();");
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form)
            {
                // repaint the feedback panel so errors are shown
                //target.add(feedback);
            }
        });
        
        add(users_sbumission_form);
        CheckGroup group = new CheckGroup("group", new PropertyModel(this, "selectedUserIds"));
//        if (roleId == 1) {
            CheckGroupSelector chks = new CheckGroupSelector("checkboxs");
            group.add(chks);
            WebMarkupContainer container_label = new WebMarkupContainer("checkboxs_label");
            group.add(container_label);
            container_label.add(new AttributeAppender("for", new Model(chks.getMarkupId()), " "));
//        } else {
//            WebMarkupContainer container = new WebMarkupContainer("checkboxs");
//            container.setVisible(false);
//            group.add(container);
//        }
        users_sbumission_form.add(group);

        
        
        
     //   RepeatingView dataRowRepeater = new RepeatingView("dataRowRepeater");
     //   group.add(dataRowRepeater);
        
        
        
        
         
            
          //初始化的时候查不出数据
          if (list == null) {
                list=DAOImpl.queryEntityRelationList("Select * from account where 1=0");
            }
          
          final Map<String, Map> tableData = Maps.newHashMap();
          List<String> ids = Lists.newArrayList();
            for (Map map : (List<Map>) list) {
               String key = String.valueOf(map.get("id"));
               ids.add(key);
               tableData.put(key, map);
            }

         final PageableListView<String> listview = new PageableListView<String>("dataRowRepeater", ids, 20) {
            
            @Override          
            protected void populateItem(ListItem<String> item) {
                String key = item.getDefaultModelObjectAsString();
                Map map = tableData.get(key);

            

                int positionId = ((Number) map.get("id")).intValue();
                String name = (String) map.get("name");
                String cellPhone = (String) map.get("cellPhone");
                String email = (String) map.get("email");
               // AbstractItem item = new AbstractItem(dataRowRepeater.newChildId());
               // dataRowRepeater.add(item);
                Check chk = new Check("checkbox", new Model(String.valueOf(positionId)));
                item.add(chk);
                WebMarkupContainer container_label = new WebMarkupContainer("checkboxs_label");
                item.add(container_label);
                container_label.add(new AttributeAppender("for", new Model(chk.getMarkupId()), " "));
                item.add(new Label("name", name));
                item.add(new Label("cellPhone", cellPhone));
                item.add(new Label("email", email));

            }
         };
        group.add(listview);
        AjaxPagingNavigator nav =new AjaxPagingNavigator("navigator", listview);
        nav.setOutputMarkupId(true);
        
        group.setOutputMarkupId(true);
        group.setRenderBodyOnly(false);
        
        group.add(nav);
        
        group.setVersioned(false);
        
         
    }
}
