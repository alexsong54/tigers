package com.bxy.salestrategies;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.panel.Panel;

import com.bxy.salestrategies.common.EditPageableTablePanel;
import com.bxy.salestrategies.common.Entity;
import com.bxy.salestrategies.common.PageableTablePanel;
import com.bxy.salestrategies.db.DAOImpl;
import com.bxy.salestrategies.util.Configuration;


public class TargetPanel extends Panel{
	private static final Logger logger = Logger.getLogger(TargetPanel.class);
	private String search_target = "";
	public TargetPanel(String id,String opportunity_id) {
		super(id);
		initPage(null,null,opportunity_id);
	}
	private void initPage(final Map<String, Boolean> filter, List tdata,String opportunity_id){
		   Map<String, Entity> entities = Configuration.getEntityTable();
		   final Entity entity = entities.get("target_acquisition");
		   String sql = "select * from opportunity where id ="+opportunity_id;
		   tdata = DAOImpl.queryEntityRelationList(sql);
		   add(new EditPageableTablePanel("datalist", entity, tdata, null));
	}
	
}
