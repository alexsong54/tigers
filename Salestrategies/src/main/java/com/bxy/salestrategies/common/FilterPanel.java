package com.bxy.salestrategies.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils.Null;
import org.apache.log4j.Logger;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.bxy.salestrategies.model.Choice;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class FilterPanel extends Panel {
    private static final Logger logger = Logger.getLogger(FilterPanel.class);
    private static final long serialVersionUID = 2501105233172820074L;


    public FilterPanel(String id,List<Choice> choices ,Map<String,Boolean> filter,final Class responsePage,Entity entity) {
        super(id);
        Form form = new Form("side_bar_form");
        add(form);
        CheckGroup group= new CheckGroup("group",new ArrayList());
        form.add(group);
        List<String> ids = Lists.newArrayList();
        long total = 0;
        final Map<String,String> choiceMap =  Maps.newHashMap();
        for(Choice choice:choices){
            ids.add(String.valueOf(choice.getId()));
            choiceMap.put(String.valueOf(choice.getId()),choice.getVal());
           
        }
        final Map<String,IModel> models = Maps.newHashMap();
        //prepare models for the checkbox
        
        if (filter == null) {
            for(Choice choice:choices){
                models.put(String.valueOf(choice.getId()), Model.of(Boolean.TRUE));   
            }
        } else {
            for (String k : filter.keySet()) {
                models.put(k, Model.of(filter.get(k)));
                
            }
        }
       
        
        
        ListView<String> listview = new ListView<String>("rowRepeater",ids) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<String> item) {
                final String key = item.getModelObject();
                String value = String.valueOf(choiceMap.get(key));
                CheckBox chk=new CheckBox("type", models.get(key)){
                    @Override
                    protected void onSelectionChanged(Boolean newSelection) {
                        super.onSelectionChanged(newSelection);
                        Map<String,Boolean> map =  Maps.newHashMap();
                        for(String s:models.keySet()){
                        	map.put(s, (Boolean) models.get(s).getObject());
                        }
                        Page page = null;
                        try {
                            page = (Page)responsePage.newInstance();
                            Constructor cons = responsePage.getDeclaredConstructor(new Class[]{Map.class,List.class});
                            //Constructor cons = responsePage.getDeclaredConstructor(Map.class,null);
                             page = (Page)cons.newInstance(new Object[]{map, Lists.newArrayList()});
                           // page = (Page)cons.newInstance(map,null);
                        } catch (InstantiationException e) {
                            logger.error(e);
                           
                        } catch (IllegalAccessException e) {
                            logger.error(e);
                        } catch (NoSuchMethodException e) {
                            logger.error(e);
                        } catch (SecurityException e) {
                            logger.error(e);
                        } catch (IllegalArgumentException e) {
                            logger.error(e);
                        } catch (InvocationTargetException e) {
                            logger.error(e);
                        }
                       // setResponsePage(new AccountPage(map));
                        setResponsePage(page);
                    }

                    @Override
                    protected boolean wantOnSelectionChangedNotifications() {
                        return true;
                    }
                };
                item.add(chk);
                item.add(new Label("name", new Model(value)));
                item.add(new Label("num", new Model(0)).setVisible(false));
            }
           

        };
        group.add(listview);
    }
    
    
    


}
