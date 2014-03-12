package com.bxy.salestrategies;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;

import com.bxy.salestrategies.common.EntityDetailPage;
import com.bxy.salestrategies.db.DAOImpl;
import com.bxy.salestrategies.model.Account;
import com.bxy.salestrategies.model.User;
import com.bxy.salestrategies.userlog.LogInOut;
import com.bxy.salestrategies.util.Utility;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * @author Sam Sun
 */
public class Index extends WebPage
{
	String search_target;
    private static final long serialVersionUID = 1L;
    protected static ImmutableMap<String,MenuItem>  pageMenuMap;
    
    
     static{
    	 ImmutableMap.Builder<String, MenuItem> builder = new ImmutableMap.Builder<String,MenuItem>();
         MenuItem item = new MenuItem();
         item.setCaption("主页");
         item.setDestination(HomePage.class);
         item.setId("navitem-homepage");
         builder.put("home", item);
         
         item = new MenuItem();
         item.setCaption(" Account");
         item.setDestination(AccountPage.class);
         item.setId("navitem-account");
         builder.put("account", item);
         
         item = new MenuItem();
         item.setCaption("Contact");
         item.setDestination(ContactPage.class);
         item.setId("navitem-contact");
         builder.put("contact", item);
         
         item = new MenuItem();
         item.setCaption("Opportunity");
         item.setDestination(OpportunityPage.class);
         item.setId("navitem-opportunity");
         builder.put("opportunity", item);

         
         item = new MenuItem();
         item.setCaption("Competitor");
         item.setDestination(CompetitorPage.class);
         item.setId("navitem-competitor");
         builder.put("competitor", item);
         
         item = new MenuItem();
         item.setCaption("User");
         item.setDestination(UserPage.class);
         item.setId("navitem-user");
         builder.put("user", item);
         
         item = new MenuItem();
         item.setCaption("Tactics");
         item.setDestination(TacticsPage.class);
         item.setId("navitem-Tactics");
         builder.put("tactics", item);
         
         item = new MenuItem();
         item.setCaption("Activity");
         item.setDestination(ActivityPage.class);
         item.setId("navitem-activity");
         builder.put("activity", item);
         pageMenuMap = builder.build();
       
   }
    public Index()
    {
		User user = DAOImpl.getUserInfoById(Integer.parseInt(((SignInSession) getSession()).getUserId()));
		//TODO get function work with real id
		List<String> menulist = new ArrayList();
		menulist.add("home");
		menulist.add("account");
		menulist.add("contact");
		menulist.add("opportunity");
		menulist.add("competitor");
		menulist.add("user");
		menulist.add("tactics");
		menulist.add("activity");
		
		ArrayList<MenuItem> menu = Lists.newArrayList();
	    for(String key:menulist){
	        menu.add(pageMenuMap.get(key));
	    }
        
        //@SuppressWarnings("unchecked")
        ListView lv = new ListView("menu", menu) {
            @Override
            protected void populateItem(ListItem item) {
                MenuItem menuitem = (MenuItem) item.getModelObject();
                BookmarkablePageLink link = new BookmarkablePageLink("link", menuitem.getDestination());
                link.add(new AttributeAppender("id", Model.of(menuitem.getId())));
                link.add(new Label("caption", menuitem.getCaption()).setEscapeModelStrings(false));
                item.add(link);
            }
        };  
        add(lv);
        
        add(new Link("signout_link"){

            @Override
            public void onClick() {
                SignInSession   session =  (SignInSession) getSession();
                LogInOut loginout = new LogInOut();
                loginout.setLoginName(session.getUser());
                loginout.setLogints(System.currentTimeMillis());
                loginout.setSessionId(session.getId());
                Utility.printStat(Utility.STAT_LOG_IN_OUT,loginout,LogInOut.class);
                session.invalidate();
                this.setResponsePage(Login.class);
            }
            
        });
        BookmarkablePageLink user_settings_link = new BookmarkablePageLink("user_settings_link",UserDeatialInfo.class);
        add(user_settings_link);
        user_settings_link.add(new Label("loginName",user.getName()));
        
        
        Form form = new Form("searchform"){
       	 protected void onSubmit(){
   			 List<Map> maplist = null;
                	String sql = "select * from account ";
                	 maplist = DAOImpl.queryEntityRelationList(sql);
                	 setResponsePage(new AccountPage());
       	 }
       };
       
       form.add(new TextField<String>("search_input", new PropertyModel<String>(this, "search_target")));  
       add(form);
    }
    
    
    
    
}

class MenuItem implements  IModel {
    /** the caption of the menu item */
    private String caption;
    /** the (bookmarkable) page the menu item links to */
    private Class destination;
    private String id;
    
    public String getCaption() {
        return caption;
    }
    public void setCaption(String caption) {
        this.caption = caption;
    }
    public Class getDestination() {
        return destination;
    }
    public void setDestination(Class destination) {
        this.destination = destination;
    }
    @Override
    public void detach() {
        // TODO Auto-generated method stub
        
    }
    @Override
    public Object getObject() {
        // TODO Auto-generated method stub
        return this;
    }
    @Override
    public void setObject( Object item) {
        this.caption = ((MenuItem)item).getCaption();
        this.destination = ((MenuItem)item).getDestination();
        
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}

