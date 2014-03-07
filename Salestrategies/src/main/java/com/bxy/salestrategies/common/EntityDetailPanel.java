package com.bxy.salestrategies.common;


import java.awt.Button;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.file.File;

import com.bxy.salestrategies.db.DAOImpl;
import com.bxy.salestrategies.util.CRMUtility;
import com.bxy.salestrategies.util.Configuration;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

public class EntityDetailPanel extends Panel {

    private static final Logger logger = Logger.getLogger(EntityDetailPanel.class);
    private static final long serialVersionUID = -2613412283023068638L;
    private Map<String, List<Field>> fieldGroupMap = Maps.newHashMap();
    private int number_of_column = 1;
    
    public EntityDetailPanel(String id, final Entity schema, final Map data, String entityId, int number_of_column, final String pageName) {
        super(id);
//        this.number_of_column = number_of_column;
        // TODO Get permission info of user from database.
        // add(new
        // CRUDPanel("operationBar",EnumSet.of(CRUDPanel.Permissions.DEL,CRUDPanel.Permissions.EDIT)));


        // TODO Get the table definition from database or configuration
        String primaryKeyName = schema.getPrimaryKeyName();
        List<Field> fields = schema.getFields();//得到所有fields
        for (Field f : fields) {
            if (fieldGroupMap.get(f.getFieldGroup()) != null) {
                fieldGroupMap.get(f.getFieldGroup()).add(f);//以fieldgroup为条件查询，并将其添加入新的集合钟
            } else {
                List<Field> fs = Lists.newArrayList();
                fs.add(f);
                fieldGroupMap.put(f.getFieldGroup(), fs);
            }
        }
        List<String> groupNames = new ArrayList<String>();
        if(schema.getName().equals("target_acquisition")){
        	groupNames.add("关键信息");
        	groupNames.add("Why – Will this decision be made?");
        	groupNames.add("How- Will a decision be made?");
        	groupNames.add("Who – Will really make the decision?");
        }else{
        	groupNames = Configuration.getSortedFieldGroupNames();//得到分组信息
        }
        RepeatingView fieldGroupRepeater = new RepeatingView("fieldGroupRepeater");
        add(fieldGroupRepeater);
        int gNum = 0;
        for (String gn : groupNames) {
            List<Field> groupfields = fieldGroupMap.get(gn);
            if (groupfields == null) {
                continue;
            }
            AbstractItem groupitem = new AbstractItem(fieldGroupRepeater.newChildId());
            fieldGroupRepeater.add(groupitem);
            WebMarkupContainer container = new WebMarkupContainer("divButton");
            groupitem.add(container);
            container.add(new Label("groupname", gn));
            container.add(new AttributeModifier("href", "#collapse" + (gNum)));
            WebMarkupContainer panel_body_div = new WebMarkupContainer("panel_body_div");
            panel_body_div.add(new AttributeModifier("id", "collapse" + (gNum)));
            if(gNum == 0){
              panel_body_div.add(new AttributeAppender("class", new Model<String>("in")," "));
            }
            groupitem.add(panel_body_div);
            RepeatingView divRepeater = new RepeatingView("divRepeater");
            panel_body_div.add(divRepeater);
            AbstractItem div = new AbstractItem(divRepeater.newChildId());
            divRepeater.add(div);
            gNum++;
            RepeatingView dataRowRepeater = new RepeatingView("dataRowRepeater");
            div.add(dataRowRepeater);
            int numOfField = 0;
            List<Field> visibleFields = Lists.newArrayList();

            for (Field f : groupfields) {
                if (!f.isVisible()||!f.isShow()) {
                    continue;
                }

                numOfField++;
                visibleFields.add(f);
            }

           

            int num_of_row = (numOfField / this.number_of_column)+1;

            for (int i = 0; i < num_of_row; i++) {
                AbstractItem item = new AbstractItem(dataRowRepeater.newChildId());
                item.add(new AttributeAppender("class", new Model(" "+i)));
                dataRowRepeater.add(item);
                RepeatingView columnRepeater = new RepeatingView("columnRepeater");
                item.add(columnRepeater);
                if (data == null) {
                    continue;
                }
                for (int j = 0; j < 2 * this.number_of_column; j++) {
                    AbstractItem columnitem = new AbstractItem(columnRepeater.newChildId(), new Model(String.valueOf(data.get(primaryKeyName))));

                    if ((i * this.number_of_column + j / 2) >= visibleFields.size()) {
                        if ((i * this.number_of_column + j / 2) >= visibleFields.size()) {
                            continue;
                        }
                        columnitem.add(new Label("celldata", "&nbsp;").setEscapeModelStrings(false));
                        columnRepeater.add(columnitem);
                        continue;
                    }
                    Field currentField = visibleFields.get(i * this.number_of_column + j / 2);
                    if (currentField.getPicklist() != null) {

                        if (j % 2 == 0) {
                            columnitem.add(new Label("celldata", currentField.getDisplay() + ":").add(new AttributeAppender("style", new Model("font-weight:bold;"), ";")));
                            columnitem.add(new AttributeAppender("class", new Model("tag"), " "));
                        } else {
                            String value = CRMUtility.formatValue(currentField.getFormatter(), DAOImpl.queryPickListByIdCached(currentField.getPicklist(), String.valueOf(data.get(currentField.getName()))));
                            value = (value == null) ? "" : value;
                            Label label = (Label) new Label("celldata",value).setEscapeModelStrings(false);
                            label.add(new AttributeAppender("id",new Model(currentField.getName()),";"));
                            columnitem.add(label);
                        }
                    } else if (currentField.getRelationTable() != null) {
                        if (j % 2 == 0) {
                            columnitem.add(new Label("celldata", currentField.getDisplay() + ":").add(new AttributeAppender("style", new Model("font-weight:bold;"), ";")));
                            columnitem.add(new AttributeAppender("class", new Model("tag"), " "));
                        } else {
                            String value = CRMUtility.formatValue(currentField.getFormatter(), DAOImpl.queryCachedRelationDataById(currentField.getRelationTable(), String.valueOf(data.get(currentField.getName()))));
                            value = (value == null) ? "" : value;
//                            if (roleId==1) {
//                            	columnitem.add(new DetailLinkFragment("celldata", "detailFragment", this,value,currentField.getRelationTable(),String.valueOf(data.get(currentField.getName()))));
//                            }else{
                            	if(!currentField.getRelationTable().equalsIgnoreCase("crmuser")){
                            		columnitem.add(new DetailLinkFragment("celldata", "detailFragment", this,value,currentField.getRelationTable(),String.valueOf(data.get(currentField.getName()))));
                            	}else{
                            		columnitem.add(new Label("celldata", value).setEscapeModelStrings(false));
                            	}
//                            }
                             
                        }
                    }else {
                        if (j % 2 == 0) {
                            columnitem.add(new Label("celldata", currentField.getDisplay() + ":").add(new AttributeAppender("style", new Model("font-weight:bold;"), ";")));
                            columnitem.add(new AttributeAppender("class", new Model("tag"), " "));
                        } else {
                            Object rawvalue = data.get(currentField.getName());
                            rawvalue = (rawvalue == null) ? "" : rawvalue;
                            String value = CRMUtility.formatValue(currentField.getFormatter(), String.valueOf(rawvalue));
                            value = (value == null) ? "" : value;
                            if (currentField.getDataType().equalsIgnoreCase("bjgtextarea")){
                            	Label label = new Label("celldata", value);
                            	label.setEscapeModelStrings(false);
                            	label.add(new AttributeAppender("class",new Model("labelWidth")," "));
                            	columnitem.add(label);
                            }else{
                            	columnitem.add(new Label("celldata", value).setEscapeModelStrings(false));
                            }
                        }
                    }
                    columnRepeater.add(columnitem);
                }
            }// end of set the detailed info
        }// end of groupNames loop
        add(new AbstractAjaxBehavior() {
            @Override
            public void onRequest() {
            }

            @Override
            public void renderHead(Component component, IHeaderResponse response) {
                super.renderHead(component, response);
                response.render(OnDomReadyHeaderItem.forScript("$(\"#navitem-" + pageName + "\").addClass(\"active\");"));
            }
        });
    }
    private class DetailLinkFragment extends Fragment {

        public DetailLinkFragment(String id, String markupId, MarkupContainer markupProvider, String name,final String entityName ,final String eid) {
            super(id, markupId, markupProvider);
//            final String str = DAOImpl.queryEntityByName(caption);
            add(new Link("detailclick") {
                @Override
                public void onClick() {
                    setResponsePage(new EntityDetailPage(entityName, eid));
                }
            }.add(new Label("caption", new Model<String>(name))));
        }
    }
}
