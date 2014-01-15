package com.bxy.salestrategies;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import com.bxy.salestrategies.common.Entity;
import com.bxy.salestrategies.common.Field;
import com.bxy.salestrategies.util.Configuration;
import com.google.common.collect.Lists;

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
	   //setPageTitle(entity.getDisplay());
	   /*Form form = new Form("form")
	    {

	      @Override
	      protected void onSubmit()
	      {
	        String sql = entity.getSql();
	        search_target = (search_target == null || search_target.equalsIgnoreCase("*")) ? "" : search_target;

	        List<Field> searchableFields = entity.getSearchableFields();
	        String joint = " like '%" + search_target + "%'";
	        String likequery = "";
	        for (Field sf : searchableFields)
	        {
	          likequery = likequery + " OR " + sf.getName() + joint;
	        } 
	        sql = sql + " where name like '%" + search_target + "%' " + likequery;
	        System.out.println(sql);
	        List datalist = null;
	        //datalist = DAOImpl.queryEntityRelationList(sql);
	        setResponsePage(new AccountPage(filter, datalist));

	      }
	    };
	    add(form);
	    
	    TextField search_input = new TextField("search_input", new PropertyModel(this, "search_target"));
	    form.add(search_input);
	    String sql = entity.getSql();
	    if (tdata == null || tdata.size() == 0)
	    {

	      if (filter == null)
	      {
	    	  //tdata = DAOImpl.queryEntityRelationList(sql);
	      }
	      else
	      {
	        List<String> ft = Lists.newArrayList();
	        for (String k : filter.keySet())
	        {
	          if (filter.get(k))
	            ft.add(k);
	        }
	        //tdata = DAOImpl.queryEntityWithFilter(sql, entity.getFilterField(), ft);

	      }
	    }*/
	   // add(new AdvancedSearchPanel("advancedSearch","account"));
	    
	    //add(new PageableTablePanel("datalist", entity, tdata, null));

	   // List<Choice> choices = DAOImpl.queryPickList(entity.getFieldByName(entity.getFilterField()).getPicklist());
	    
	   // add(new FilterPanel("filterPanel", choices, filter, AccountPage.class,entity));
	  }
}
