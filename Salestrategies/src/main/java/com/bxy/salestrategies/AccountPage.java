package com.bxy.salestrategies;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import com.bxy.salestrategies.common.Entity;
import com.bxy.salestrategies.common.Field;
import com.bxy.salestrategies.common.PageableTablePanel;
import com.bxy.salestrategies.db.DAOImpl;
import com.bxy.salestrategies.util.Configuration;
import com.google.common.collect.Lists;
/**
 * 
 * @author brenda
 *
 */
public class AccountPage extends Index{
	private static final Logger logger = Logger.getLogger(AccountPage.class);
	private String search_target = "";
	public AccountPage(){
		initPage(null,null);
	}
	public AccountPage(Map<String, Boolean> map, List tdata)
   {
     initPage(map, tdata);
   }
   public AccountPage(List tdata)
   {
     initPage(null, tdata);
   }
   private void initPage(final Map<String, Boolean> filter, List tdata){
	   Map<String, Entity> entities = Configuration.getEntityTable();
	   final Entity entity = entities.get("account");
	   String sql = entity.getSql();
	   tdata = DAOImpl.queryEntityRelationList(sql);
	   add(new PageableTablePanel("datalist", entity, tdata, null));
   }
}
