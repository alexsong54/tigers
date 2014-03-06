package com.bxy.salestrategies.common;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableChoiceLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.encoding.UrlEncoder;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;																										

import com.bxy.salestrategies.SelectEntryPage;
import com.bxy.salestrategies.db.DAOImpl;
import com.bxy.salestrategies.model.Choice;
import com.bxy.salestrategies.util.CRMUtility;
import com.bxy.salestrategies.util.Configuration;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class EditPageableTablePanel extends Panel{
	private static final long serialVersionUID = 2501105233172820074L;
    private static final Logger logger = Logger.getLogger(EditPageableTablePanel.class);
    private Map<String, List<Field>> fieldGroupMap = Maps.newHashMap(); 
	public EditPageableTablePanel(String id, final Entity entity, final List mapList,final Map<String,Object> params) {
	        super(id);
	        final List<String> fieldNames = Lists.newArrayList();
	        add(new Label("table_title",entity.getDisplay()));
	        final String primaryKeyName = entity.getPrimaryKeyName();
	        final List<Field> fields = entity.getFields();
	        final List<String> fn = entity.getFieldNames();
	        final String entityName = entity.getName();
	        // RepeatingView dataRowRepeater = new RepeatingView("dataRowRepeater");
	        // add(dataRowRepeater);

	        final Map<String, Map> tableData = Maps.newHashMap();
	        List<String> ids = Lists.newArrayList();
	        Map targetData = null;
	        final WebMarkupContainer datacontainer = new WebMarkupContainer("data");
	        datacontainer.setOutputMarkupId(true);
	        add(datacontainer);
	        for (Field f : fields) {
	  				if (fieldGroupMap.get(f.getFieldGroup()) != null) {
	  					fieldGroupMap.get(f.getFieldGroup()).add(f);// 以fieldgroup为条件查询，并将其添加入新的集合中
	  				} else {
	  					List<Field> fs = Lists.newArrayList();
	  					fs.add(f);
	  					fieldGroupMap.put(f.getFieldGroup(), fs);
	  				}
	  			}

	  	        // set column name
	  	        RepeatingView columnNameRepeater = new RepeatingView("columnNameRepeater");
	  	        datacontainer.add(columnNameRepeater);
	  	        int count=0;
	  	        for (Field f : entity.getFields()) {
	  	            if (!f.isVisible()|| f.getPriority() >1)
	  	                continue;
	  	            AbstractItem item = new AbstractItem(columnNameRepeater.newChildId());
	  	            if(count==0){
	  	                item.add(new AttributeAppender("class", new Model("table-first-link"), " "));
	  	                count++;
	  	            }
	  	            columnNameRepeater.add(item);
	  	            Label label = new Label("columnName",f.getDisplay());
	  	            if(f.getDataType().equals("date")){
	  	            	item.add(new AttributeAppender("class", new Model("table-first-link"), " "));
	  	            }
	  	            item.add(label);
	  	           // item.add(new Label("columnName", f.getDisplay()));
	  	        }
	  	        final Form form = new Form("form");
	  	        RepeatingView fieldGroupRepeater = new RepeatingView("fieldGroupRepeater");
		        form.add(fieldGroupRepeater);
				datacontainer.add(form);
	        for (Map map : (List<Map>) mapList) {
		        final Map<String, IModel> fieldNameToModel = Maps.newLinkedHashMap();
				final Map<String, IModel> modifyNameToModel = Maps.newLinkedHashMap();
	            String key = String.valueOf(map.get("id"));
	            ids.add(key);
	            tableData.put(key, map);
	            targetData = map;
	            final Map data = targetData;
				List<String> groupNames = Configuration.getSortedFieldGroupNames();// 得到分组信息
				RepeatingView dataRowRepeater = new RepeatingView("dataRowRepeater");
	            AbstractItem groupitem = new AbstractItem(fieldGroupRepeater.newChildId());
	            groupitem.setOutputMarkupId(true);
	            fieldGroupRepeater.add(groupitem);
	            groupitem.add(dataRowRepeater);
                AbstractItem item = new AbstractItem(dataRowRepeater.newChildId());
                dataRowRepeater.add(item);
                RepeatingView columnRepeater = new RepeatingView("columnRepeater");
                item.add(columnRepeater);
                List<Field> visibleFields = Lists.newArrayList();
                for (Field f : fields)
                {
                	if (!f.isVisible() || !f.isEditable() || (f.getFieldType()!=null && f.getFieldType().equalsIgnoreCase("auto"))) continue;
                	visibleFields.add(f);
                }
	                for (int j = 0; j <visibleFields.size(); j++) {
	                    AbstractItem columnitem = new AbstractItem(columnRepeater.newChildId(), new Model(String.valueOf(data.get(primaryKeyName))));
	                    final Field currentField = visibleFields.get(j);
	                    if (currentField.getPicklist() != null) {
	                                List<Choice> pickList = DAOImpl.queryPickList(currentField.getPicklist());
	                                Map<Long, String> list = Maps.newHashMap();
	                                List<Long> ids1 = Lists.newArrayList();
	                                for (Choice p : pickList) {
	                                    list.put(p.getId(), p.getVal());
	                                    ids1.add(p.getId());
	                                }
	                                String value = "-1";
	                                if(data.get(currentField.getName())!=null){
	                                   value = data.get(currentField.getName()).toString();
	                                }
	                                IModel choiceModel = new Model(Long.parseLong(value));
	                                modifyNameToModel.put(currentField.getDisplay(), choiceModel);
	                                fieldNameToModel.put(currentField.getName(), choiceModel);
	                                //columnitem.add(new AjaxEditableChoiceLabel("editdata",pickList));
	                                //columnitem.add(new AjaxEditableChoiceLabelFragment("editdata","ajaxEditableChoiceLabel",this,pickList));
	                                columnitem.add(new DropDownChoiceFragment("editdata", "dropDownFragment", this, ids1, list, choiceModel,currentField));
	                    } else if (currentField.getRelationTable() != null) {
	                            long foreignKey = 1L;
	         
	                            if(data.get(currentField.getName())!=null){
	                             foreignKey = ((Number)data.get(currentField.getName())).longValue();
	                            }else{
	                                foreignKey = -1L;   //
	                            }
	                            IModel choiceModel = new Model(foreignKey);
	                            String fn1 = "";
	                            if (currentField.getAlias() != null) {
	                                fn1 = currentField.getAlias();
	                            } else {
	                                fn1 = currentField.getName();
	                            }
	                            final Entity ent = Configuration.getEntityByName(currentField.getRelationTable());
	                            Map et = DAOImpl.queryEntityById(ent.getSql_ent(), String.valueOf(foreignKey));
	                            String value = "";
	                            if (et != null && et.get("name") != null) {
	                                value = (String) et.get("name");
	                            }
	                            modifyNameToModel.put(currentField.getDisplay(), choiceModel);
	                            fieldNameToModel.put(fn1, choiceModel);
	                            columnitem.add(new RelationTableSearchFragment("editdata", "relationTableSearchFragment", this, currentField.getRelationTable(), entity.getName(), value, choiceModel,key));
	                    }else if(currentField.getDataType().equals("submit")){
	                        	ButtonFragment button = new ButtonFragment("editdata","buttonFragment",this,entity,String.valueOf(data.get(primaryKeyName)),fieldNameToModel,modifyNameToModel);
	                        	columnitem.add(button);
	                    } else {
	                            Object rawvalue = data.get(currentField.getName());
	                            rawvalue = (rawvalue == null) ? "" : rawvalue;
	                            String value = CRMUtility.formatValue(currentField.getFormatter(), String.valueOf(rawvalue));
	                            value = (value == null) ? "" : value;
	                            IModel<String> textModel = new Model<String>("");
	                            fieldNameToModel.put(currentField.getName(), textModel);
	                            TextInputFragment  textInput = new TextInputFragment("editdata", "textInputFragment", this, textModel, value, currentField);
	                            columnitem.add(textInput);
	                    }
	                    columnRepeater.add(columnitem);
	                }
	            }
	        }

	    public EditPageableTablePanel(String id, IModel<?> model) {
	        super(id, model);
	    }

	    
	    private class DownloadLinkFragment extends Fragment {
	        public DownloadLinkFragment( String id, String markupId, MarkupContainer markupProvider,final String filename) {
	            super(id, markupId, markupProvider);
	            final File tempFile = new File(filename);
	            DownloadLink downloadlink = new DownloadLink("downloadlink",
	                    new AbstractReadOnlyModel<File>(){
	                        @Override
	                        public File getObject() {
	                            

	                            return tempFile;
	                        }
	               
	            },tempFile.getName());
	            downloadlink.add(new Label("fn",tempFile.getName()));
	            
	            add(downloadlink);
	            
	        }
	        
	    }
	    
	    private class RelationTableSearchFragment extends Fragment {
	        public RelationTableSearchFragment(String id, String markupId, MarkupContainer markupProvider, final String entityName, String excludeEntityName ,final String value, IModel model,String key) {
	            super(id, markupId, markupProvider);

	            PageParameters params = new PageParameters();
	            params.set("en", entityName);
	            params.set("excludeName", excludeEntityName);
	            params.set("target", model.getObject());
	            params.set("key", key);
	            PopupSettings popupSettings = new PopupSettings("查找");
	            add(new BookmarkablePageLink<Void>("search_btn", SelectEntryPage.class, params).setPopupSettings(popupSettings));
	            HiddenField<?> hidden = new HiddenField<String>("selected_id_hidden", model);
	            hidden.add(new AttributeAppender("id", entityName + "_id"+key));
	            add(hidden);
	            TextField<String> text = new TextField<String>("selected_value_input", new Model(value));
	            text.add(new AttributeAppender("id", entityName + "_name"+key));
	            add(text);
	        }
	    }
	    private class DropDownChoiceFragment extends Fragment {
	        public DropDownChoiceFragment(String id, String markupId, MarkupContainer markupProvider, final List<Long> ids, final Map<Long, String> list, IModel model,Field currentField) {
	            super(id, markupId, markupProvider);
	            DropDownChoice dropDown = (new DropDownChoice<Long>("dropDownInput", model, ids, new IChoiceRenderer<Long>() {
	                @Override
	                public Object getDisplayValue(Long id) {
	                    // TODO Auto-generated method stub
	                    return list.get(id);
	                }

	                @Override
	                public String getIdValue(Long id, int index) {
	                    return String.valueOf(id);
	                }
	            }));
	            if (currentField.isRequired()) {
	                //text.add(new AttributeModifier("required", new Model("required")));
	            	dropDown.add(new AttributeAppender("class",new Model("required-pickList")," "));
	            }
	            add(dropDown);
	            
	        }
			
	        public DropDownChoiceFragment(String id, String markupId,MarkupContainer markupProvider,
	                IModel choices,IModel default_model,final Entity entity, final Field currentField){
	            super(id, markupId, markupProvider);
	            DropDownChoice dropDown = createDropDownListFromPickList("dropDownInput",choices,default_model);
	            add(dropDown);
	        } 
		}
	    private DropDownChoice createDropDownListFromPickList(String markupId,IModel choices,IModel default_model) {
	        
	        DropDownChoice<Choice> choice = new DropDownChoice<Choice>(markupId, default_model, choices,
	                new IChoiceRenderer<Choice>() {
	            @Override
	            public Object getDisplayValue(Choice choice) {
	                // TODO Auto-generated method stub
	                return choice.getVal();
	            }
	            @Override
	            public String getIdValue(Choice choice, int index) {
	                // TODO Auto-generated method stub
	                return String.valueOf(choice.getId());
	            }
	            
	            
	        });
	        
	        choice.setNullValid(true);
	        return choice;
	    }
	    private class TextInputFragment extends Fragment {
	        public TextInputFragment(String id, String markupId, MarkupContainer markupProvider, IModel model, String value, Field currentField) {
	            super(id, markupId, markupProvider);
	            TextField<String> text = new TextField<String>("input", model);
	            text.add(new AttributeModifier("value", new Model(value)));
	            text.add(new AttributeAppender("type", new Model(currentField.getDataType()), ";"));
	            if(currentField.getDataType().equals("date")){
	            	 text.add(new AttributeAppender("style", new Model("width:65%"),";"));
	            }
	            add(text);
	        }
	    }
	    private class DetailLinkFragment extends Fragment {
	        /**
	         * Construct.
	         * 
	         * @param id
	         *            The component Id
	         * @param markupId
	         *            The id in the markup
	         * @param markupProvider
	         *            The markup provider
	         */
	        public DetailLinkFragment( String id, String markupId, MarkupContainer markupProvider, String caption, final Entity entity) {
	            super(id, markupId, markupProvider);
	            add(new Link("detailclick") {

	                @Override
	                public void onClick() {
	                    Param p = (Param) getParent().getParent().getDefaultModelObject();
	                    String entityName = p.getEntityName();
	                    setResponsePage(new EntityDetailPage(entityName, p.getId()));
	                }
	            }.add(new Label("caption", new Model<String>(caption))));
	        }
	        
	    }
	    private class LayoutFragment extends Fragment {

	        public LayoutFragment(String id, String markupId, MarkupContainer markupProvider, String label) {
	            super(id, markupId, markupProvider);
	            add(new Label("add", label).setEscapeModelStrings(false));
	            // TODO Auto-generated constructor stub
	        }
	    }
	    private class ButtonFragment extends Fragment{
	    	public ButtonFragment(String id,String markupId,MarkupContainer markupPrivider,final Entity entity,final String entityId,final Map<String, IModel> fieldNameToModel,final Map<String, IModel>  modifyNameToModel){
	    		super(id, markupId, markupPrivider);
	    		Button button = new Button("save"){
		    			@Override
						public void onSubmit() {
		    				System.out.println("fieldNameToModel:"+fieldNameToModel);
							List<String> values = Lists.newArrayList();
							List<String> names = Lists.newArrayList();
							String fileName = "";
							for (String k : fieldNameToModel.keySet()) {
								names.add(k);
								Field field = entity.getFieldByName(k);
								IModel currentModel = fieldNameToModel.get(k);
					            //判断filed是否能为空，若为空则给出提示，不执行保存事件，若不为空在执行保存事件
								Object obj = currentModel.getObject() ;
								String value = null;
								String modifyValue = null;
								if(obj!=null){
					                if (obj instanceof String) {
					                    if (field.getDataType().equalsIgnoreCase("datetime-local")||field.getDataType().equalsIgnoreCase("Date")) {
					                        // if the filed has formatter, we guess the
					                        // field saved in data base is in long
					                        Date date = new Date();
					                        String dateTime = String.valueOf(fieldNameToModel.get(k).getObject());
					                       value = String.valueOf("'"+dateTime+"'");

					                    } else {
					                    	value = "'" + (String)obj + "'";
					                    }

					                } else if (obj instanceof Choice) {
					                    value = String.valueOf(((Choice) obj).getId());
					                } else {
					                	if(field.getDataType().equals("file")){
					                    	value = "'"+fileName+"'";
					                    }else{
					                    	value = String.valueOf(obj) ;
					                    }
					                }
					            }else{  
					            	if(field.getPicklist()!=null){
					            		value = "0";
					            	}else{
					            		value = "'"+fileName+"'";
					            	}
					            }
								values.add(value);
						}
							DAOImpl.updateRecord(entityId,"target_acquisition",names,values);
						}
	    		};
	    		add(button);
	    	}
	    }
}
