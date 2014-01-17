package com.bxy.salestrategies;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bxy.salestrategies.common.Entity;
import com.bxy.salestrategies.common.PageableTablePanel;
import com.bxy.salestrategies.db.DAOImpl;
import com.bxy.salestrategies.util.Configuration;

public class ActivityPage extends Index{
	private static final Logger logger = Logger.getLogger(ContactPage.class);
	private String search_target = "";
	public ActivityPage(){
		initPage(null,null);
	}
	public ActivityPage(Map<String, Boolean> map, List tdata)
   {
     initPage(map, tdata);
   }
   public ActivityPage(List tdata)
   {
     initPage(null, tdata);
   }
   private void initPage(final Map<String, Boolean> filter, List tdata){
	   Map<String, Entity> entities = Configuration.getEntityTable();
	   final Entity entity = entities.get("activity");
	   String sql = entity.getSql();
	   tdata = DAOImpl.queryEntityRelationList(sql);
	   add(new PageableTablePanel("datalist", entity, tdata, null));
   }
}
