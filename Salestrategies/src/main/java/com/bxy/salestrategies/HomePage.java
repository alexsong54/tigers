package com.bxy.salestrategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

import com.bxy.salestrategies.common.Entity;
import com.bxy.salestrategies.common.Field;
import com.bxy.salestrategies.common.PageableTablePanel;
import com.bxy.salestrategies.db.DAOImpl;
import com.bxy.salestrategies.util.Configuration;


/**
 *
 * @author Sam
 */
public class HomePage extends Index implements AuthenticatedWebPage
{  
//	String serachInput = getRequest().getRequestParameters().getParameterValue("search_input").toString();
	String relationTableName = "account";
	final Map<String, Entity> entities = Configuration.getEntityTable();
    final Entity entity = entities.get(relationTableName);
    String search_target;
    public HomePage(){
    	 Form form = new Form("form"){
    		 protected void onSubmit(){
    			 List<Map> maplist = null;
                 	String sql = "select * from account ";
                 	 maplist = DAOImpl.queryEntityRelationList(sql);
                 	 setResponsePage(new HomePage());
    		 }
    	 };
        form.add(new TextField<String>("search_input", new PropertyModel<String>(this, "search_target")));  
        add(form);
        List list = new ArrayList<>();
//		if (list == null) {
            list=DAOImpl.queryEntityRelationList("Select * from account where 1=0 ");
//        }
		
       add(new PageableTablePanel("datalist", entity, list, null));
    }
    private String assembleSearchingSQL( final Entity entity) {
        String sql = entity.getSql();
        
        search_target = (search_target==null || search_target.equalsIgnoreCase("*"))? "":search_target;
        
        List<Field> searchableFields = entity.getSearchableFields();
        String joint = " like '%"+search_target+"%'";
        String likequery = "";
        for(Field sf:searchableFields){
            likequery = likequery + " OR "+ sf.getName() + joint;
        }
         sql =  sql + " where name like '%"+search_target+"%' " + likequery ;
         System.out.println(sql);
        return sql;
    }
}
