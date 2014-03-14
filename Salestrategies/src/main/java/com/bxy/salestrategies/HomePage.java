package com.bxy.salestrategies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.bxy.salestrategies.common.Entity;
import com.bxy.salestrategies.common.Field;
import com.bxy.salestrategies.common.PageableTablePanel;
import com.bxy.salestrategies.db.DAOImpl;
import com.bxy.salestrategies.model.Choice;
import com.bxy.salestrategies.util.Configuration;
import com.google.common.collect.Maps;


/**
 *
 * @author Sam
 */
public class HomePage extends Index implements AuthenticatedWebPage
{  
//	String serachInput = getRequest().getRequestParameters().getParameterValue("search_input").toString();
    private final Map<String, IModel> models = Maps.newHashMap();
    String search_target;
    public HomePage(){
    	
    	
    	 Map<String, Entity> entities = Configuration.getEntityTable();
         final Entity entity = entities.get("alert");
        List<Entity> entitys = Configuration.getEntities();
        IModel<Choice> selected_model =  new Model(new Choice(0l,"account"));
    	 List<Choice> entityChoice = new ArrayList<Choice>();
    	    final Map<String, Entity> entityList = new HashMap();
    	    for(Entity entityName :entitys){
    	    	if(entityName.isGlobalsearch()){
    	    		Choice choice =new Choice();
    	        	choice.setVal(entityName.getDisplay());
    	        	choice.setName(entityName.getName());
    	        	entityChoice.add(choice);
    	            entityList.put(String.valueOf(entityName.getName()), entityName); 
    	    	}
    	    }
    	TextField search_input = new TextField("search_input", new PropertyModel(this, "search_target"));
        DropDownChoice search_select = new	DropDownChoice("search_select",selected_model,entityChoice, new IChoiceRenderer<Choice>() {
            @Override
            public Object getDisplayValue(Choice choice) {
                // TODO Auto-generated method stub
                return choice.getVal();
            }
            @Override
            public String getIdValue(Choice choice, int index) {
                // TODO Auto-generated method stub
                return String.valueOf(choice.getName());
            }
            
            
        });
        models.put("entityName", selected_model);
        search_select.setNullValid(false);
       
        Form form = new Form("form")
        {
        	
            @Override
            protected void onSubmit()
            {  
            	String entityName = String.valueOf(((Choice) models.get("entityName").getObject()).getName());
            	Entity  en =  entityList.get(entityName);
                String sql   = en.getSql();
                search_target = (search_target == null || search_target.equalsIgnoreCase("*")) ? "" : search_target;
                
                List<Field> searchableFields = en.getSearchableFields();
                String joint = " like '%" + search_target + "%'";
                String likequery = "";
                for (Field sf : searchableFields)
                {
                    likequery = likequery + " OR " + sf.getName() + joint;
                }
                if(en.getName().equalsIgnoreCase("coaching")||en.getName().equalsIgnoreCase("activity")||en.getName().equalsIgnoreCase("willcoaching")){
                	sql = sql + " where title like '%" + search_target + "%' " + likequery;
                } else{
                	sql = sql + " where name like '%" + search_target + "%' " + likequery;
                }
                System.out.println(sql);
                List datalist = null;
                
                       
                    	if(en.getName().equalsIgnoreCase("account")){
                    		datalist = DAOImpl.queryEntityRelationList(sql);	
                    	}else if(en.getName().equalsIgnoreCase("contact")){
                    		datalist = DAOImpl.queryEntityRelationList(sql);
                    	}else if(en.getName().equalsIgnoreCase("coaching")||en.getName().equalsIgnoreCase("activity")||en.getName().equalsIgnoreCase("willcoaching")){
                    		datalist = DAOImpl.queryEntityRelationList(sql);
                    	}
                
                if(entityName.equals("account")){
                	setResponsePage(new AccountPage(datalist));
                }else if(entityName.equals("contact")){
                	setResponsePage(new ContactPage(datalist));
                }else if(entityName.equals("activity")){
                	setResponsePage(new ActivityPage(datalist));
                }
                
            }
            
        };
//        ChoiceRenderer choiceRenderer = new ChoiceRenderer<Entity>("name","URL");
        
        add(form);
        form.add(search_select);
        form.add(search_input);
}}
