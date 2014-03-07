package com.bxy.salestrategies.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;
import com.bxy.salestrategies.common.Entity;
import com.bxy.salestrategies.common.Field;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Configuration {
    private static final Logger logger = Logger.getLogger(Configuration.class);
    private static Map<String,Entity> entities = null;

   public static Entity getEntityByName(String name){
       Entity entity = null;
       Map<String,Entity> ents = getEntityTable();
       if(ents!=null && ents.size()!=0){
           entity = ents.get(name.toLowerCase());
       }
       return entity;
   } 
    public static Map<String,Entity> getEntityTable(){
        if (entities == null || entities.size() == 0) {
            synchronized (Configuration.class) {
                List<Entity> list = getEntities();
                entities = Maps.newHashMap();
                for(Entity en:list){
                 entities.put(en.getName().toLowerCase(), en);
                }
            }
         }   
        return entities;
    }
    public static List<Entity> getEntities() {
        List<Entity> enitites = new ArrayList<Entity>();
        try {
            XMLConfiguration config = new XMLConfiguration("entity.xml");
            List<HierarchicalConfiguration> hp = config.configurationsAt("entity");

            for (HierarchicalConfiguration sub : hp) {
                Entity entity = new Entity();
                enitites.add(entity);
                entity.setName(sub.getString("name"));
                entity.setDisplay(sub.getString("display"));
                entity.setSql(sub.getString("sql"));
                entity.setSql_ent(sub.getString("sql-ent"));
                String filterField = sub.getString("filterField");
                if(filterField !=null){
                    entity.setFilterField(filterField);
                }
                List<Field> fields = Lists.newArrayList();
                entity.setFields(fields);
                List<HierarchicalConfiguration> hp2 = sub.configurationsAt("fields.field");
                for(HierarchicalConfiguration sub2:hp2){
                    Field field = new Field();
                    fields.add(field);
                    field.setDataType(sub2.getString("data-type"));
                    field.setDisplay(sub2.getString("display"));
                    field.setPattern(sub2.getString("pattern"));
                    field.setName(sub2.getString("name"));
                    field.setPriority(Integer.parseInt(sub2.getString("priority")));
                    entity.setGlobalsearch(Boolean.parseBoolean(sub.getString("globalsearch"))); 
                String picklist = sub2.getString("picklist");
                if(picklist!=null && picklist.trim().length()!=0){
                    field.setPicklist(picklist);
                }else{
                    field.setPicklist(null);
                }
                
                String relationTable = sub2.getString("relationTable");
                if(relationTable!=null && relationTable.trim().length()!=0){
                    field.setRelationTable(relationTable);
                }else{
                    field.setRelationTable(null);
                }
                
                String default_value = sub2.getString("default-value");
                if(default_value !=null){
                    field.setDefault_value(default_value);
                }
                
                String alias = sub2.getString("alias");
                if(alias!=null && alias.trim().length()!=0){
                    field.setAlias(alias);
                }else{
                    field.setAlias(null);
                }
                String formatter = sub2.getString("formatter");
                if(formatter !=null){
                    field.setFormatter(formatter);
                }
                
                
                String fieldGroup = sub2.getString("fieldGroup");
                if(fieldGroup !=null){
                    field.setFieldGroup(fieldGroup);
                }
                
                String default_value_type = sub2.getString("default-value-type");
                if(default_value_type !=null){
                    field.setDefault_value_type(default_value_type);
                }
                
                String childNode = sub2.getString("childNode");
                if(childNode !=null){
                    field.setChildNode(childNode);
                }
                
                String parentNode = sub2.getString("parentNode");
                if(parentNode !=null){
                    field.setParentNode(parentNode);
                }
//                String pattern = sub2.getString("pattern");
//                if(pattern !=null){
//                    field.setPattern(pattern);
//                }
                String fieldtype = sub2.getString("field-type");
                if(fieldtype !=null){
                    field.setFieldType(fieldtype);
                }
                String tooltip = sub2.getString("tooltip");
                if(tooltip!=null){
                	field.setTooltip(Integer.parseInt(tooltip));
                }
                field.setPrimaryKey(Boolean.parseBoolean(sub2.getString("isPrimaryKey")));
                field.setDetailLink(Boolean.parseBoolean(sub2.getString("isDetailLink"))); 
                field.setVisible(Boolean.parseBoolean(sub2.getString("isVisible")));
                field.setEditable(Boolean.parseBoolean(sub2.getString("isEditable")));
                field.setInLineEdit(Boolean.parseBoolean(sub2.getString("isInLineEdit")));
                field.setRequired(Boolean.parseBoolean(sub2.getString("isRequired"))); 
                field.setBaseInfo(Boolean.parseBoolean(sub2.getString("isBaseInfo")));
                field.setSearchable(Boolean.parseBoolean(sub2.getString("isSearchable")));
                field.setParam(Boolean.parseBoolean(sub2.getString("isParam")));
                field.setShow(Boolean.parseBoolean(sub2.getString("isShow")));
                field.setExistsDefaultValue(Boolean.parseBoolean(sub2.getString("isExistsDefaultValue")));
            }
            }
        } catch (ConfigurationException cex) {
            cex.printStackTrace();
        }

        return enitites;
    }
	public static List<String> getSortedFieldGroupNames() {
		// TODO Auto-generated method stub
		List<String> list = Lists.newArrayList();
        list.add("关键信息");
        list.add("基本信息");
        list.add("附加信息");
		return list;
	}
}
