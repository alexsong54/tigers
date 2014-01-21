package com.bxy.salestrategies.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.util.string.StringValue;

import com.bxy.salestrategies.Index;
import com.bxy.salestrategies.util.Configuration;
import com.google.common.collect.Maps;

public class CreateDataPage extends Index {
    private static final Logger logger = Logger.getLogger(CreateDataPage.class);
    private static final long serialVersionUID = -2613412283023068638L;

    private static int NUM_OF_COLUMN  = 2;
    
    public CreateDataPage(){
        
        StringValue entityName = getRequest().getRequestParameters().getParameterValue("entityName");
        String name = entityName.toString();
        Set<String> names = getRequest().getRequestParameters().getParameterNames();
        Map<String,Object> map = Maps.newHashMap();
        for(String nm:names){
            StringValue sv = getRequest().getRequestParameters().getParameterValue(nm);
            map.put(nm, sv.toString());
        }
        logger.debug("param-map:"+ map);
        initPage(name,map,null);
    }
   
    public CreateDataPage(String entityName,final Map<String,Object> params){
        initPage(entityName,params,null);
    }
    
    public CreateDataPage(String entityName,final Map<String,Object> params,String createAddress){
        initPage(entityName,params,createAddress);
    }
    private void initPage(String entityName,Map<String,Object> params,String createAddress){
        Map<String, Entity> entities = Configuration.getEntityTable();
        //if (entityName == null) entityName="contact";
        final Entity entity = entities.get(entityName);
        if(null!=entity){
        	 add(new NewDataFormPanel("formPanel",entity,params,createAddress));
	         add(new AbstractAjaxBehavior(){
	
	            @Override
	            public void onRequest() {
	            }
	
	            @Override
	            public void renderHead(Component component, IHeaderResponse response) {
	                super.renderHead(component, response);
	                response.render(OnDomReadyHeaderItem.forScript("$(\"#navitem-"+entity.getName()+"\").addClass(\"active\");"));
	            }  
	             
	         });
        }
    }

}
