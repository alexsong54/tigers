package com.bxy.salestrategies;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
/**
 * 
 * @author brenda.yuan
 *
 */
public class UserInfoSettingPage extends Index{

	private static final long serialVersionUID = 1L;
	protected static ImmutableMap<String,MenuItem>  pageMenuMap;
	static{
        //TODO Load them from configuration
        ImmutableMap.Builder<String, MenuItem> builder = new ImmutableMap.Builder<String,MenuItem>();
        
        MenuItem item = new MenuItem();
        item.setCaption("<i class=\"icon-user icon-large\"></i>个人信息");
        item.setDestination(UserDeatialInfo.class);
        item.setId("navitem-userdetailInfo");
        builder.put("userDetailInfo", item);
        
        item = new MenuItem();
        item.setCaption("<i class=\"icon-lock icon-large\"></i>修改密码");
        item.setDestination(UpdateSignPassword.class);
        item.setId("navitem-updateSignPassword");
        builder.put("updateSignPassword", item);
        
        pageMenuMap = builder.build();
    }
	public UserInfoSettingPage(){
		List<String> menulist = Lists.newArrayList();
    	menulist.add("userDetailInfo");
        menulist.add("updateSignPassword");
		ArrayList<MenuItem> menu = Lists.newArrayList();
	    for(String key:menulist){
	        menu.add(pageMenuMap.get(key));
	    }
	    ListView lv = new ListView("userMenu", menu) {
            @Override
            protected void populateItem(ListItem item) {
                MenuItem menuitem = (MenuItem) item.getModelObject();
                BookmarkablePageLink link = new BookmarkablePageLink("link", menuitem.getDestination());
                link.add(new Label("caption", menuitem.getCaption()).setEscapeModelStrings(false));
                item.add(link);
                item.add(new AttributeAppender("id", Model.of(menuitem.getId())));
               // item.add(new SimpleAttributeModifier("class", "my-css-class"));
                
            }
        };  
        add(lv);
	}
}
