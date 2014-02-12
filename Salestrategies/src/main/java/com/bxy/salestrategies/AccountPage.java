package com.bxy.salestrategies;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bxy.salestrategies.common.Entity;
import com.bxy.salestrategies.common.FilterPanel;
import com.bxy.salestrategies.common.PageableTablePanel;
import com.bxy.salestrategies.db.DAOImpl;
import com.bxy.salestrategies.model.Choice;
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
	   if (tdata == null || tdata.size() == 0)
	    {

	      if (filter == null)
	      {
	    	  tdata = DAOImpl.queryEntityRelationList(sql);
	      }else{
	    	  List<String> ft = Lists.newArrayList();
	          for (String k : filter.keySet())
	          {
	            if (filter.get(k))
	              ft.add(k);
	          }
             tdata = DAOImpl.queryEntityWithFilter(sql, entity.getFilterField(), ft);
	      }
	    }
	   add(new PageableTablePanel("datalist", entity, tdata, null));
	   List<Choice> choices = DAOImpl.queryPickList(entity.getFieldByName(entity.getFilterField()).getPicklist());
	   add(new FilterPanel("filterPanel", choices, filter, AccountPage.class,entity));
   }
}