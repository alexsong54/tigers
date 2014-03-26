package com.bxy.salestrategies.common;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.file.File;

import com.bxy.salestrategies.SignInSession;
import com.bxy.salestrategies.SelectEntryPage;
import com.bxy.salestrategies.db.DAOImpl;
import com.bxy.salestrategies.model.Choice;
import com.bxy.salestrategies.util.CRMUtility;
import com.bxy.salestrategies.util.Configuration;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

public class EditDataFormPanel extends Panel {
	private static final Logger logger = Logger.getLogger(EditDataFormPanel.class);
	private static final long serialVersionUID = -2613412283023068638L;
	private Map<String, List<Field>> fieldGroupMap = Maps.newHashMap(); 
	private static int NUM_OF_COLUMN = 1;
    private Map<String, IModel> parentModels = Maps.newHashMap();
    private Map<String, DropDownChoiceFragment> childDropDownComponent = Maps.newHashMap();
    private List<FileUploadField> fileFields = Lists.newArrayList();
    private static FileUploadField fileUploadField= new FileUploadField("fileUplode");
    private static String alertFileName = "";
	/**
	 * 
	 * @param id
	 * @param schema
	 * @param data
	 * @param entityId
	 * @param relationIds
	 */ 
	public EditDataFormPanel(String id, final Entity schema,  final Map data,final String entityId ,final Class previousPageClass,final PageParameters prePageParams) {
		super(id);
		String entityName =null;
		if(!schema.getName().equalsIgnoreCase("opportunityuserteam")){
			 entityName = String.valueOf(data.get("name"));
		}
		if("null".equals(entityName)){
			add(new Label("name",""));
		}else{
			add(new Label("name",entityName));
		}
      	//final Map<String, IModel> models = Maps.newHashMap();
		final Map<String, IModel> fieldNameToModel = Maps.newLinkedHashMap();
		final Map<String, IModel> modifyNameToModel = Maps.newLinkedHashMap();
	    final String userName = ((SignInSession) getSession()).getUser();
	    final String userId = ((SignInSession) getSession()).getUserId();
		String primaryKeyName = schema.getPrimaryKeyName();
		List<Field> fields = schema.getFields();// 得到所有fields
		final List<String> fieldNames = Lists.newArrayList();
		//add prompt 
        final RepeatingView div = new RepeatingView("promptDiv");
        final AbstractItem group = new AbstractItem(div.newChildId());
        final Label promptButton = new Label("promptButton","X");
        group.add(promptButton);
        final Label promptLabel = new Label("prompt","提示:用户登录名已存在！");
        group.add(promptLabel);
        final Label promptLabelForDateOfEstablish = new Label("promptForDateOfEstablish", "提示:请填入公司成立时间！");
        group.add(promptLabelForDateOfEstablish);
        final Label promptLabelForAccount = new Label("promptForAccount", "提示:客户名称已存在！");
        group.add(promptLabelForAccount);
        final Label promptFordate = new Label("promptFordate", "提示:时间格式不正确！");
        group.add(promptFordate);
        div.add(new AttributeAppender("style",new Model("display:none"),";"));
        group.add(new AttributeAppender("style",new Model("display:none"),";"));
        div.add(group);
        add(div);
		// List<String> fn = schema.getFieldNames();
		for (Field f : fields) {
			if (fieldGroupMap.get(f.getFieldGroup()) != null) {
				fieldGroupMap.get(f.getFieldGroup()).add(f);// 以fieldgroup为条件查询，并将其添加入新的集合中
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
		for (String gn : groupNames) {
			logger.info("gn = "+gn+ "groupNames = "+groupNames);
			List<Field> groupfields = fieldGroupMap.get(gn);
			
			if (groupfields == null ||  gn.equalsIgnoreCase("附加信息")) continue;
			AbstractItem groupitem = new AbstractItem(fieldGroupRepeater.newChildId());
			fieldGroupRepeater.add(groupitem);
			RepeatingView dataRowRepeater = new RepeatingView("dataRowRepeater");
			groupitem.add(dataRowRepeater);
			int numOfField = 0;
			List<Field> visibleFields = Lists.newArrayList();
			for (Field f : groupfields) {
				if (!f.isVisible() || !f.isEditable() || (f.getFieldType()!=null && f.getFieldType().equalsIgnoreCase("auto"))) continue;
				numOfField++;
				visibleFields.add(f);
			}
				groupitem.add(new Label("groupname", gn));	
			
			int num_of_row = (numOfField / NUM_OF_COLUMN) + 1;
			
            for (int i = 0; i < num_of_row; i++) {
                //logger.debug("for i = " + i + ",dataRowRepeater.newChildId() = " + dataRowRepeater.newChildId() + ",value = " + dataRowRepeater.get(dataRowRepeater.newChildId()));
                AbstractItem item = new AbstractItem(dataRowRepeater.newChildId());
                dataRowRepeater.add(item);
                RepeatingView columnRepeater = new RepeatingView("columnRepeater");
                item.add(columnRepeater);

                for (int j = 0; j < 2 * NUM_OF_COLUMN; j++) {

                    AbstractItem columnitem = new AbstractItem(columnRepeater.newChildId(), new Model(String.valueOf(data.get(primaryKeyName))));

                    if ((i * NUM_OF_COLUMN + j / 2) >= visibleFields.size()) {
                        if ((i * NUM_OF_COLUMN + j / 2) >= visibleFields.size()) {
                            continue;
                        }
                        columnitem.add(new LayoutFragment("editdata", "layoutFragment", this, "&nbsp;"));
                        columnRepeater.add(columnitem);
                        continue;
                    }
                    final Field currentField = visibleFields.get(i * NUM_OF_COLUMN + j / 2);
                    if (currentField.getPicklist() != null) {
                        if (j % 2 == 0) {
                        	TextFragment textField = new TextFragment("editdata", "textFragment", this, currentField.getDisplay() + ":");
                            textField.add(new AttributeAppender("style", new Model("font-weight:bold;"), ";"));
                            if(currentField.getFieldGroup().equals("Why – Will this decision be made?")||currentField.getFieldGroup().equals("How- Will a decision be made?")||currentField.getFieldGroup().equals("Who – Will really make the decision?")){
                              String message = CRMUtility.getToolTipById(String.valueOf(currentField.getTooltip()));
                              textField.add(new AttributeModifier("data-content",message));
                        	  textField.add(new AttributeModifier("title",currentField.getDisplay()));
                          	  textField.add(new AttributeAppender("class",new Model("tooltip-test")," "));
                            }
                            columnitem.add(textField);
                            if (currentField.isRequired()) {
                                columnitem.add(new AttributeAppender("class", new Model("tag"), " ")).add(new AttributeAppender("style", new Model("color:red"), ";"));
                            } else {
                                columnitem.add(new AttributeAppender("class", new Model("tag"), " "));
                            }
                            fieldNames.add(currentField.getName());
                        } else {
                            
                            // it is a cascading picklist
                            if (currentField.getParentNode() != null || currentField.getChildNode() != null) {
                                String value = "-1";
                                if(data.get(currentField.getName()) != null){ 
                                   value = data.get(currentField.getName()).toString();
                                }
                                
                                IModel<Choice> selected_model = new Model(new Choice(Long.parseLong(value), ""));
                                IModel<List<? extends Choice>> choices_models = null;

                                if (currentField.getChildNode() != null && currentField.getParentNode() == null) {
                                    choices_models = Model.ofList(DAOImpl.queryPickList(currentField.getPicklist()));
                                }

                                if (currentField.getParentNode() != null) {
                                    choices_models = new AbstractReadOnlyModel<List<? extends Choice>>() {
                                        List<Choice> choices;

                                        @Override
                                        public List<? extends Choice> getObject() {
                                            IModel pm = parentModels.get(currentField.getParentNode());
                                            if (pm != null && pm.getObject() !=null && pm.getObject() instanceof Choice) {
                                                choices = DAOImpl.queryPickListByFilter(currentField.getPicklist(), "parentId", String.valueOf(((Choice) pm.getObject()).getId()));

                                            } else {
                                                choices = Lists.newArrayList();
                                            }
                                            return choices;
                                        }

                                        @Override
                                        public void detach() {
                                            choices = null;
                                            // System.out.println("detached");
                                        }

                                    };
                                }

                                DropDownChoiceFragment selector = new DropDownChoiceFragment("editdata", "dropDownFragment", this, choices_models, selected_model, schema, currentField);
                                columnitem.add(selector);
                                selector.setOutputMarkupId(true);

                                if (currentField.getParentNode() != null) {

                                    childDropDownComponent.put(currentField.getName(), selector);
                                }

                                modifyNameToModel.put(currentField.getDisplay(), selected_model);
                                fieldNameToModel.put(currentField.getName(), selected_model);
                                columnitem.add(selector);

                            } else {

                                List<Choice> pickList = DAOImpl.queryPickList(currentField.getPicklist());
                                Map<Long, String> list = Maps.newHashMap();
                                List<Long> ids = Lists.newArrayList();
                                for (Choice p : pickList) {
                                    list.put(p.getId(), p.getVal());
                                    ids.add(p.getId());
                                }
                                 
                                String value = "-1";
                                if(data.get(currentField.getName())!=null){
                                   value = data.get(currentField.getName()).toString();
                                }
                                IModel choiceModel = new Model(Long.parseLong(value));
                                modifyNameToModel.put(currentField.getDisplay(), choiceModel);
                                fieldNameToModel.put(currentField.getName(), choiceModel);
                                columnitem.add(new DropDownChoiceFragment("editdata", "dropDownFragment", this, ids, list, choiceModel,currentField));
                            }
                        }
                    } else if (currentField.getRelationTable() != null) {
                        if (j % 2 == 0) {
                            columnitem.add(new TextFragment("editdata", "textFragment", this, currentField.getDisplay() + ":").add(new AttributeAppender("style", new Model("font-weight:bold;"), ";")));
                            if (currentField.isRequired()) {
                                columnitem.add(new AttributeAppender("class", new Model("tag"), " ")).add(new AttributeAppender("style", new Model("color:red"), ";"));
                            } else {
                                columnitem.add(new AttributeAppender("class", new Model("tag"), " "));
                            }
                            fieldNames.add(currentField.getName());
                        } else {
                            long foreignKey = 1L;
         
                            if(data.get(currentField.getName())!=null){
                             foreignKey = ((Number)data.get(currentField.getName())).longValue();
                            }else{
                                foreignKey = -1L;   //
                            }
                            IModel choiceModel = new Model(foreignKey);
                            String fn = "";
                            if (currentField.getAlias() != null) {
                                fn = currentField.getAlias();
                            } else {
                                fn = currentField.getName();
                            }
                            final Entity ent = Configuration.getEntityByName(currentField.getRelationTable());
                            Map et = DAOImpl.queryEntityById(ent.getSql_ent(), String.valueOf(foreignKey));
                            String value = "";
                            if (et != null && et.get("name") != null) {
                                value = (String) et.get("name");
                            }
                            modifyNameToModel.put(currentField.getDisplay(), choiceModel);
                            fieldNameToModel.put(fn, choiceModel);
                            columnitem.add(new RelationTableSearchFragment("editdata", "relationTableSearchFragment", this, currentField, schema, value, choiceModel, entityId));
                        }
                    } else if (currentField.getDataType().equals("radio")){
                		if (j % 2 == 0) {
                            columnitem.add(new TextFragment("editdata", "textFragment", this, currentField.getDisplay() + ":").add(new AttributeAppender("style", new Model("font-weight:bold"), ";")));
                            if (currentField.isRequired()) {
                              columnitem.add(new AttributeAppender("class", new Model("tag"), " ")).add(new AttributeAppender("style", new Model("color:red"), ";"));
                            }else{
                              columnitem.add(new AttributeAppender("class", new Model("tag"), " "));
                            }
                		} else {
                			String value = (String) data.get(currentField.getName());
                			IModel<String> textModel = new Model<String>(value);
                			RadioChoiceFragment RadioChoice = new RadioChoiceFragment("editdata", "radioChoiceFragment", this,textModel,value,currentField);
                			modifyNameToModel.put(currentField.getDisplay(), textModel);
                			fieldNameToModel.put(currentField.getName(),textModel);
                			columnitem.add(RadioChoice);
                		}
                    } else {
                        if (j % 2 == 0) {
                            columnitem.add(new TextFragment("editdata", "textFragment", this, currentField.getDisplay() + ":").add(new AttributeAppender("style", new Model("font-weight:bold;"), ";")));
                            if (currentField.isRequired()) {
                                columnitem.add(new AttributeAppender("class", new Model("tag"), " ")).add(new AttributeAppender("style", new Model("color:red"), ";"));
                            } else {
                                columnitem.add(new AttributeAppender("class", new Model("tag"), " "));
                            }
                            fieldNames.add(currentField.getName());
                        } else {
                            Object rawvalue = data.get(currentField.getName());
                            rawvalue = (rawvalue == null) ? "" : rawvalue;
                            String value = CRMUtility.formatValue(currentField.getFormatter(), String.valueOf(rawvalue));
                            value = (value == null) ? "" : value;
                            if (currentField.getDataType().equalsIgnoreCase("textarea")) {
                                IModel<String> textModel = new Model<String>(value);
                                modifyNameToModel.put(currentField.getDisplay(), textModel);
                                fieldNameToModel.put(currentField.getName(), textModel);
                                columnitem.add(new Textarea("editdata", "textAreaFragment", this, textModel,currentField));
                            }else if(currentField.getDataType().equals("bjgtextarea")){
                            	IModel<String> textModel = new Model<String>(value);
                            	modifyNameToModel.put(currentField.getDisplay(), textModel);
                                fieldNameToModel.put(currentField.getName(), textModel);
                                columnitem.add(new BigtextareaFrag("editdata", "bjgtextAreaFragment", this, textModel,currentField));
                            }else if(currentField.getDataType().equalsIgnoreCase("file")){
                            	alertFileName = value;
                            	IModel<String> textModel = new Model<String>(value);
                            	fieldNameToModel.put(currentField.getName(), textModel);
                                columnitem.add(new FileUploadFragment("editdata", "fileUplodeFragment", this, textModel));
                            } else if(currentField.getDataType().equals("datetime-local")){
                                
                                 SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                                 value = dateformat.format(rawvalue);
                                 IModel<String> textModel = new Model<String>(value);
                                //textModel = new Model<String>(date_value);
                                modifyNameToModel.put(currentField.getDisplay(), textModel);
                                fieldNameToModel.put(currentField.getName(), textModel);
                                TextInputFragment textInput = new TextInputFragment("editdata", "textInputFragment", this, textModel,value, currentField);
                                columnitem.add(textInput);
                                
                            }else {
                                IModel<String> textModel = new Model<String>("");
                            	fieldNameToModel.put(currentField.getName(), textModel);
                            	TextInputFragment  textInput = new TextInputFragment("editdata", "textInputFragment", this, textModel, value, currentField);
                            	columnitem.add(textInput);
//                                }
                            }
                        }
                    }
                    columnRepeater.add(columnitem);
                }
            }// end of set the detailed info
        }// end of groupNames loop
		Form form = new Form("form");
		form.add(new Button("save"){
            @Override
            public void onSubmit() {
            	 try {
					if(!saveEntity(fieldNameToModel,modifyNameToModel,data,schema,entityId,userId)){
					 	div.add(new AttributeAppender("style",new Model("display:block"),";"));
					 	group.add(new AttributeAppender("style",new Model("display:block"),";"));
					 	if(schema.getName().equals("account")){
		                  	promptLabel.add(new AttributeAppender("style", new Model("display:none"), ";"));
	                        promptLabelForAccount.add(new AttributeAppender("style", new Model("display:block"), ";"));
	                        promptLabelForDateOfEstablish.add(new AttributeAppender("style", new Model("display:block"), ";"));
	                        promptFordate.add(new AttributeAppender("style", new Model("display:none"), ";"));
	                    }else if (schema.getName().equals("userinfo")){
	                    	promptLabel.add(new AttributeAppender("style", new Model("display:block"), ";"));
	                    	promptLabelForAccount.add(new AttributeAppender("style", new Model("display:none"), ";"));
	                    	promptLabelForDateOfEstablish.add(new AttributeAppender("style", new Model("display:none"), ";"));
	                    	promptFordate.add(new AttributeAppender("style", new Model("display:none"), ";"));
	                    }else if(schema.getName().equals("activity")){
	                    	promptLabel.add(new AttributeAppender("style", new Model("display:none"), ";"));
	                        promptLabelForAccount.add(new AttributeAppender("style", new Model("display:none"), ";"));
	                        promptLabelForDateOfEstablish.add(new AttributeAppender("style", new Model("display:none"), ";"));
	                        promptFordate.add(new AttributeAppender("style", new Model("display:block"), ";"));
	                    }
						promptButton.add(new AttributeAppender("style",new Model("display:block"),";"));
					 }else{
					    if(!prePageParams.isEmpty()){
						    setResponsePage(previousPageClass,prePageParams);
						}else{
							setResponsePage(new EntityDetailPage(schema.getName(),entityId));
						}
					 }
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
		Button button = new Button("saveAndNew"){
			@Override
			public void onSubmit() {
                try {
                	if(!saveEntity(fieldNameToModel,modifyNameToModel,data,schema,entityId,userId)){
					 	div.add(new AttributeAppender("style",new Model("display:block"),";"));
					 	group.add(new AttributeAppender("style",new Model("display:block"),";"));
					 	if(schema.getName().equals("account")){
		                  	promptLabel.add(new AttributeAppender("style", new Model("display:none"), ";"));
	                        promptLabelForAccount.add(new AttributeAppender("style", new Model("display:block"), ";"));
	                        promptLabelForDateOfEstablish.add(new AttributeAppender("style", new Model("display:block"), ";"));
	                        promptFordate.add(new AttributeAppender("style", new Model("display:none"), ";"));
	                    }else if (schema.getName().equals("userinfo")){
	                    	promptLabel.add(new AttributeAppender("style", new Model("display:block"), ";"));
	                    	promptLabelForAccount.add(new AttributeAppender("style", new Model("display:none"), ";"));
	                    	promptLabelForDateOfEstablish.add(new AttributeAppender("style", new Model("display:none"), ";"));
	                    	promptFordate.add(new AttributeAppender("style", new Model("display:none"), ";"));
	                    }else if(schema.getName().equals("activity")){
	                    	promptLabel.add(new AttributeAppender("style", new Model("display:none"), ";"));
	                        promptLabelForAccount.add(new AttributeAppender("style", new Model("display:none"), ";"));
	                        promptLabelForDateOfEstablish.add(new AttributeAppender("style", new Model("display:none"), ";"));
	                        promptFordate.add(new AttributeAppender("style", new Model("display:block"), ";"));
	                    }
						promptButton.add(new AttributeAppender("style",new Model("display:block"),";"));
					 }
                	else{
						 setResponsePage(new CreateDataPage(schema.getName(),null));
					 }
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		form.add(fieldGroupRepeater);
		form.add(button);
		add(form);
		// set navigation bar active
		add(new AbstractAjaxBehavior() {
			@Override
			public void onRequest() {
			}
			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				super.renderHead(component, response);
				response.render(OnDomReadyHeaderItem.forScript("$(\"#navitem-"
						+ schema.getName() + "\").addClass(\"active\");"));
			}
		});
	}
	public EditDataFormPanel(String id, IModel<?> model) {
		super(id, model);
	}
    
	@SuppressWarnings("null")
	public  boolean saveEntity(Map<String, IModel> fieldNameToModel,Map<String, IModel> modifyNameToModel, final Map data,Entity schema,String entityId,String userId ) throws Exception{
		logger.debug("the form was submitted!");
		logger.debug(fieldNameToModel);
		String fileName = "";

		List<String> values = Lists.newArrayList();
		List<String> names = Lists.newArrayList();
		//List<String> modifyNames = Lists.newArrayList();
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		int total_score = 0;
		Long daypart = 0l;
                String productlineId="-1";
		String loginName = "";
		StringBuffer endDate = new StringBuffer();
//		if(schema.getName().equals("activity")){
//       	 	daypart = (Long) fieldNameToModel.get("activity_daypart").getObject();
//        }
		if(schema.getName().equals("user")){
       	 	loginName = fieldNameToModel.get("login_name").getObject().toString();
        }
		for (String k : fieldNameToModel.keySet()) {
			names.add(k);
			Field field = schema.getFieldByName(k);
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
            	if(field.getPicklist()!=null||field.getDataType().equalsIgnoreCase("number")){
            		value = "0";
            	}else{
            		value = "'"+fileName+"'";
            	}
            }
			values.add(value);
	}

		
		 List<Field> autoFields = schema.getAutoFields();
         for(Field f:autoFields){
            
           if(f.getName().equalsIgnoreCase("modifier_date")||f.getName().equalsIgnoreCase("modified_date")){
               names.add(f.getName());
               
             values.add("'"+dateformat.format(new Date())+"'");
           }
           
           if(f.getName().equalsIgnoreCase("modified_by")||f.getName().equalsIgnoreCase("owner")){
               names.add(f.getName());
			values.add("'"+userId+"'");
           }
         }
		
        String table_name  = schema.getName();
		if(!table_name.equalsIgnoreCase("user")){
			System.out.println("names:"+names.toString()+values.toString()+entityId);
                if(DAOImpl.updateRecord(entityId,table_name,names,values)){
					recordValueChanges(data, schema, entityId, userId, values, names,table_name);
					return true;
				} else{
					return false;
				}
		}else{
			List<String> loginNames =DAOImpl.getLoginNames(entityId);
            if(loginNames.contains(loginName)){
            	return false;
            }else{
            	DAOImpl.updateRecord(entityId,table_name,names,values);
            	recordValueChanges(data, schema, entityId, userId, values,names, table_name);
        		return true;
            }
       }
	}
public static  void recordValueChanges(final Map data, Entity schema,
			String entityId, String userName, List<String> values,
			List<String> names, String table_name) throws Exception {
		String valuebefore;
		for(int i=0;i<names.size();i++){
			String field_name = names.get(i);
			Field fld = schema.getFieldByName(field_name);
		    if(!(fld.getFieldType().equals("auto")||fld.getDataType().equalsIgnoreCase("datetime-local"))){
		    	String val = values.get(i);
				
				valuebefore = data.get(field_name)==null?null:String.valueOf(data.get(field_name));
				
				boolean isChanged = false;
				if(fld.getPicklist()!=null){
					if(val!=null){
					   if(!val.equals("0")){
						   if(!val.equalsIgnoreCase(valuebefore)) isChanged=true;  
					   } 
					}else{
						if(valuebefore != null) isChanged = true;
					}
		    		val = DAOImpl.queryPickListByIdCached(fld.getPicklist(), val);
		    		valuebefore =  DAOImpl.queryPickListByIdCached(fld.getPicklist(), valuebefore);
		    	}else if(fld.getRelationTable()!=null){
		    		val = val.substring(1, val.length()-1);
		    		Entity et = DAOImpl.getEntityById(fld.getRelationTable(), val);
                    if (et != null && et.getName() != null) {
                    	val =  et.getName() ;
                    }
                    Entity etbefore = DAOImpl.getEntityById(fld.getRelationTable(), valuebefore);
                    if (etbefore != null && etbefore.getName() != null) {
                    	valuebefore =  etbefore.getName();
                    }
					if(val!=null){
							if(!val.equalsIgnoreCase(valuebefore)) isChanged=true;  
						}else{
							if(valuebefore != null) isChanged = true;
						}
			    }else{
					    if(val!=null){
					    	logger.debug("val is:"+val);
					    	if(!val.equalsIgnoreCase("0")){
					    		if(!val.equalsIgnoreCase("''")){
					    			val = val.substring(1, val.length()-1);
					    			if (!val.equalsIgnoreCase(valuebefore)) {
										isChanged = true;
									  }
					    		}
					    	}
					    }else{
					    	if(valuebefore != null){
					    		if(!valuebefore.equalsIgnoreCase("0"))isChanged = true;
					    	} 
					    }
					
		    	}
				
				if(isChanged){
					DAOImpl.insertAudit(table_name,fld.getDisplay(),valuebefore,val,entityId,userName);
					}
				}
			}
	}
    private class TextFragment extends Fragment {

        public TextFragment(String id, String markupId, MarkupContainer markupProvider, String label) {
            super(id, markupId, markupProvider);
            add(new Label("celldata", label));
            // TODO Auto-generated constructor stub
        }
    }
    private class FileUploadFragment extends Fragment
    {

        public FileUploadFragment(String id, String markupId, MarkupContainer markupProvider, IModel model)
        {
            super(id, markupId, markupProvider);
            // TODO Auto-generated constructor stub 
            fileUploadField = new FileUploadField("fileUplode", model);
            fileUploadField.add(new AttributeModifier("value",model));
            add(fileUploadField);
            fileFields.add(fileUploadField);
        }
    }
    private class LayoutFragment extends Fragment {

        public LayoutFragment(String id, String markupId, MarkupContainer markupProvider, String label) {
            super(id, markupId, markupProvider);
            add(new Label("add", label).setEscapeModelStrings(false));
            // TODO Auto-generated constructor stub
        }

    }

    private class RelationTableSearchFragment extends Fragment {
        public RelationTableSearchFragment(String id, String markupId, MarkupContainer markupProvider, final Field field, final Entity entity ,final String value, IModel model, final String eid) {
            super(id, markupId, markupProvider);

            PageParameters params = new PageParameters();
            params.set("en",field.getRelationTable());
            params.set("excludeName", entity.getName());
            params.set("target", model.getObject());
            params.set("eid", eid);
            //根据eid获取opportunityID
            if(entity.getName().equals("opportunitycontactteam")){
            	int relationEntityID =  DAOImpl.getOppContactTeamById(eid).getOpportunity_id();
	            params.set("key",field.getName());
	            params.set("relationEntityID", relationEntityID);
	            BookmarkablePageLink bookLink =new BookmarkablePageLink<Void>("search_btn", SelectEntryPage.class, params);
	            PopupSettings popupSettings = new PopupSettings("查找");
	            add(bookLink.setPopupSettings(popupSettings));
	            HiddenField<?> hidden = new HiddenField<String>("selected_id_hidden", model);
	            hidden.add(new AttributeModifier("id", field.getRelationTable() + "_id"+field.getName()));
	            add(hidden);
	            TextField<String> text = new TextField<String>("selected_value_input", new Model(value));
	            text.add(new AttributeModifier("id", field.getRelationTable()  + "_name"+field.getName()));
	            add(text);
            }else if(entity.getName().equals("contact")||entity.getName().equals("tactics")||entity.getName().equals("activity")){
            	params.set("key",field.getName());
	            params.set("relationEntityID", eid);
	            BookmarkablePageLink bookLink =new BookmarkablePageLink<Void>("search_btn", SelectEntryPage.class, params);
	            bookLink.add(new AttributeModifier("id",field.getName()+"_id"));
	            PopupSettings popupSettings = new PopupSettings("查找");
	            add(bookLink.setPopupSettings(popupSettings));
	            if(field.getName().equals("contact_id")||field.getName().equals("individual_met")||field.getName().equals("report_to")){
	            	bookLink.add(new AttributeModifier("onclick","getAccountId('"+field.getName()+"','"+entity.getName()+"')"));
	            }
	            HiddenField<?> hidden = new HiddenField<String>("selected_id_hidden", model);
	            hidden.add(new AttributeModifier("id", field.getRelationTable() + "_id"+field.getName()));
	            add(hidden);
	            TextField<String> text = new TextField<String>("selected_value_input", new Model(value));
	            text.add(new AttributeModifier("id", field.getRelationTable()  + "_name"+field.getName()));
	            add(text);
            }else{
	            BookmarkablePageLink bookLink = new BookmarkablePageLink<Void>("search_btn", SelectEntryPage.class, params);
	            bookLink.add(new AttributeModifier("id",field.getName()+"_id"));
	            PopupSettings popupSettings = new PopupSettings("查找");
	            add(bookLink.setPopupSettings(popupSettings));
	            HiddenField<?> hidden = new HiddenField<String>("selected_id_hidden", model);
	            hidden.add(new AttributeAppender("id",field.getRelationTable() + "_id"));
	            add(hidden);
	            TextField<String> text = new TextField<String>("selected_value_input", new Model(value));
	            text.add(new AttributeAppender("id", field.getRelationTable() + "_name"));
	            add(text);
            }
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
            if(currentField.getPriority()==5){
            	dropDown.setNullValid(false);
            }else{
            	dropDown.setNullValid(true);
            }
            if (currentField.isRequired()) {
                //text.add(new AttributeModifier("required", new Model("required")));
            	dropDown.add(new AttributeAppender("class",new Model("required-pickList")," "));
            }
            if(currentField.getName().equals("activity_daypart")){
            	dropDown.add(new AttributeModifier("id","daypart"));
            	dropDown.setNullValid(true);
            }
            add(dropDown);
            
        }
		
        public DropDownChoiceFragment(String id, String markupId,MarkupContainer markupProvider,
                IModel choices,IModel default_model,final Entity entity, final Field currentField){
            super(id, markupId, markupProvider);
            DropDownChoice dropDown = createDropDownListFromPickList("dropDownInput",choices,default_model);
            add(dropDown);
            
            if(currentField.getChildNode()!=null){
                parentModels.put(currentField.getName(), default_model);
                //childDropDownComponent.put(field.get("child-pl"), null);
                
                dropDown.add(new AjaxFormComponentUpdatingBehavior("onchange"){

                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        target.add(childDropDownComponent.get( currentField.getChildNode()));
                            if( entity.getFieldByName(currentField.getChildNode()).getChildNode() != null ){
                              
                               parentModels.get(entity.getFieldByName(currentField.getChildNode()).getName()).setObject(new Choice(-1L,""));
                               target.add(childDropDownComponent.get(entity.getFieldByName(currentField.getChildNode()).getChildNode()));
                               // target.
                            }
                        
                    }
                    
                 });
             }
            if(entity.getName().equals("activity")||entity.getName().equals("coaching")||entity.getName().equals("willcoaching")){
            	dropDown.add(new AttributeAppender("class",new Model("required-pickList")," "));
            }
            if(currentField.getPriority()==5){
            	dropDown.setNullValid(false);
            }else{
            	dropDown.setNullValid(true);
            }
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
    private class RadioChoiceFragment extends Fragment{
    	public  RadioChoiceFragment(String id, String markupId,MarkupContainer markupProvider, IModel model,String value,final Field currentField){
    		super(id, markupId, markupProvider);
			List lsVisible = Arrays.asList(new String[]{"否", "是"});
			RadioChoice<String> raVisible = new RadioChoice("radioChoiceinput", model, lsVisible).setSuffix(" "); 
			raVisible.setModelValue(new String[]{"0", "1"});
			model.setObject(value);
			add(raVisible);
    	}
    }
    private class Textarea extends Fragment {

        public Textarea(String id, String markupId, MarkupContainer markupProvider, IModel<String> value,Field currentField) {
            super(id, markupId, markupProvider);
            // TODO Auto-generated constructor stub
            TextArea textArea = new TextArea<String>("address", value);            
            if (currentField.isRequired()) {
                textArea.add(new AttributeAppender("class",new Model("required-field")," "));
              }
            add(textArea);
        }

    }
    
    private class  BigtextareaFrag extends Fragment {

        public BigtextareaFrag(String id, String markupId, MarkupContainer markupProvider, IModel model, Field currentField) {
            super(id, markupId, markupProvider);
            // TODO Auto-generated constructor stub
            TextArea<String> textArea =  new TextArea<String>("description", model);
            add(textArea);
            if (currentField.isRequired()) {
              textArea.add(new AttributeAppender("class",new Model("required-field")," "));
            }
        }
    }
    
    private class TextInputFragment extends Fragment {
        public TextInputFragment(String id, String markupId, MarkupContainer markupProvider, IModel model, String value, Field currentField) {
            super(id, markupId, markupProvider);
            TextField<String> text = new TextField<String>("input", model);
            text.add(new AttributeModifier("value", new Model(value)));
            add(text);
            text.add(new AttributeAppender("type", new Model(currentField.getDataType()), ";"));
            if (currentField.getDataType().equals("tel") || currentField.getDataType().equalsIgnoreCase("fax")) {
                text.add(new AttributeModifier("pattern", new Model(
                        "^((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)")));
            }
            if (currentField.isRequired()) {
                //text.add(new AttributeModifier("required", new Model("required")));
                text.add(new AttributeAppender("class",new Model("required-field")," "));
            }
            if(currentField.getDataType().equals("datetime-local")){
            	text.add(new AttributeModifier("id",currentField.getName()));
            }
            
        }
    }

}
