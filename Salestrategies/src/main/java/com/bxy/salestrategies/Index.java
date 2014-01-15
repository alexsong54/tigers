package com.bxy.salestrategies;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author Sam Sun
 */
public class Index extends WebPage
{
  
    private static final long serialVersionUID = 1L;
    protected static ImmutableMap<String,MenuItem>  pageMenuMap;
    
    
     static{
       
        ImmutableMap.Builder<String, MenuItem> builder = new ImmutableMap.Builder<String,MenuItem>();
        MenuItem item = new MenuItem();
       // item.setCaption("Account");
        item.setDestination(Account.class);
        item.setId("navitem-homepage");
        builder.put("home", item);
        pageMenuMap = builder.build();
   }
    public Index()
    {
//        List<String> menulist = Lists.newArrayList();
//        menulist.add("home");;
//        ArrayList<MenuItem> menu = Lists.newArrayList();
//        for(String key:menulist){
//	        menu.add(pageMenuMap.get(key));
//	  }
//        
//        ListView lv = new ListView("menu", menu) {
//            @Override
//            protected void populateItem(ListItem item) {
//                MenuItem menuitem = (MenuItem) item.getModelObject();
//                BookmarkablePageLink link = new BookmarkablePageLink("link", menuitem.getDestination());
//                link.add(new Label("caption", menuitem.getCaption()).setEscapeModelStrings(false));
//                item.add(link);
//                item.add(new AttributeAppender("id", Model.of(menuitem.getId())));
//                
//            }
//        };  
//        add(lv);
        
        
        
        
//        BookmarkablePageLink Account = new BookmarkablePageLink("Account",Account.class );
//        add(Account);
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
