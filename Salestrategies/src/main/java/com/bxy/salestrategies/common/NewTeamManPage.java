package com.bxy.salestrategies.common;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import com.bxy.salestrategies.Index;
import com.bxy.salestrategies.SelectEntryPage;
import com.bxy.salestrategies.SignInSession;
import com.bxy.salestrategies.db.DAOImpl;
import com.bxy.salestrategies.model.Choice;
import com.bxy.salestrategies.util.Configuration;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class NewTeamManPage extends WebPage {
    private static final Logger logger = Logger.getLogger(NewTeamManPage.class);
    private static final long serialVersionUID = -2613412283023068638L;
    private static String NewRecordId = "-1";
    private Map<String, List<Field>> addFieldGroupMap = Maps.newHashMap();
    private Map<String, IModel> parentModels = Maps.newHashMap();
    private Map<String, DropDownChoiceFragment> childDropDownComponent = Maps.newHashMap();
    private List<FileUploadField> fileFields = Lists.newArrayList();
    private static Map<String,Object> paramsForProduct;
    private static int NUM_OF_COLUMN  = 2;
    
    public NewTeamManPage(){
        
        StringValue entityName = getRequest().getRequestParameters().getParameterValue("entityName");
        String name = entityName.toString();
        Set<String> names = getRequest().getRequestParameters().getParameterNames();
        Map<String,Object> map = Maps.newHashMap();
        for(String nm:names){
            StringValue sv = getRequest().getRequestParameters().getParameterValue(nm);
            map.put(nm, sv.toString());
        }
        logger.debug("param-map:"+ map);
        initPage(name,map,null,null);
    }
   
    public NewTeamManPage(String entityName ,String entityId,final Map<String,Object> params){
        initPage(entityName,params,entityId,null);
    }
    
    public NewTeamManPage(String entityName,final Map<String,Object> params,String createAddress){
        initPage(entityName,params,null,createAddress);
    }
    
    private void initPage(String entityName,Map<String,Object> params,String entityId ,String createAddress){
        Map<String, Entity> entities = Configuration.getEntityTable();
        //if (entityName == null) entityName="contact";
        final Entity entity = entities.get(entityName);
        if(null!=entity){
        	
        	newdate("formPanel",entity,params,entityId,createAddress);
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
    
    private void newdate(final String id, final Entity entity, final Map<String, Object> params, final String entityId,final String createAddress){

    	        final Map<String, IModel> models = Maps.newHashMap();
    	        final String userName = ((SignInSession) getSession()).getUser();
    	        final String userId = ((SignInSession) getSession()).getUserId();
    	        List<Field> fields = entity.getFields();
    	        for (Field f : fields)
    	        {
    	            if (addFieldGroupMap.get(f.getFieldGroup()) != null)
    	            {
    	                addFieldGroupMap.get(f.getFieldGroup()).add(f);
    	            }
    	            else
    	            {
    	                List<Field> fs = Lists.newArrayList();
    	                fs.add(f);
    	                addFieldGroupMap.put(f.getFieldGroup(), fs);
    	            }

    	        }
    	        List<String> groupNames = Configuration.getSortedFieldGroupNames();
    	        RepeatingView fieldGroupRepeater = new RepeatingView("fieldGroupRepeater");
    	        add(fieldGroupRepeater);
    	        paramsForProduct=params;
    	        for (String gn : groupNames) {
    	            List<Field> groupfields = addFieldGroupMap.get(gn);
    	            if (groupfields == null || gn.equalsIgnoreCase("附加信息"))
    	            {
    	                continue;
    	            }
    	            RepeatingView dataRowRepeater = new RepeatingView("dataRowRepeater");
    	            AbstractItem groupitem = new AbstractItem(fieldGroupRepeater.newChildId());
    	            groupitem.setOutputMarkupId(true);
    	            fieldGroupRepeater.add(groupitem);
    	            groupitem.add(dataRowRepeater);
    	            int numOfField = 0;
    	            List<Field> visibleFields = Lists.newArrayList();
    	            for (Field f : groupfields)
    	            {
    	                if (!f.isVisible() || !f.isEditable() || (f.getFieldType() != null && f.getFieldType().equalsIgnoreCase("auto")))
    	                {
    	                    continue;
    	                }
    	                numOfField++;
    	                visibleFields.add(f);
    	            }
    	            groupitem.add(new Label("groupname", gn));
    	            int num_of_row = (numOfField / NUM_OF_COLUMN) + 1;
    	            for (int i = 0; i < num_of_row; i++)
    	            {
    	                AbstractItem item = new AbstractItem(dataRowRepeater.newChildId());
    	                dataRowRepeater.add(item);
    	                RepeatingView columnRepeater = new RepeatingView("columnRepeater");
    	                item.add(columnRepeater);
    	                for (int j = 0; j < 2 * NUM_OF_COLUMN; j++)
    	                {
    	                    AbstractItem columnitem = new AbstractItem(columnRepeater.newChildId(), new Model());
    	                    if ((i * NUM_OF_COLUMN + j / 2) >= visibleFields.size())
    	                    {
    	                        if ((i * NUM_OF_COLUMN + j / 2) >= visibleFields.size())
    	                        {
    	                            continue;
    	                        }
    	                        columnitem.add(new LayoutFragment("celldatafield", "layoutFragment", this, " ").setEscapeModelStrings(false));
    	                        columnRepeater.add(columnitem);

    	                        continue;
    	                    }
    	                    final Field currentField = visibleFields.get(i * NUM_OF_COLUMN + j / 2);
    	                    if (currentField.getPicklist() != null)
    	                    {
    	                        if (j % 2 == 0)
    	                        {
    	                            TextFragment textField = new TextFragment("celldatafield", "textFragment", this, currentField.getDisplay() + ":");
    	                            textField.add(new AttributeAppender("style", new Model("font-weight:bold;"), ";"));
    	                            columnitem.add(textField);
//    	                            if (currentField.isRequired())
//    	                            {
    	                                columnitem.add(new AttributeAppender("class", new Model("tag"), " ")).add(new AttributeAppender("style", new Model(currentField.getPattern()), ";"));
//    	                            }
//    	                            else
//    	                            {
//    	                                columnitem.add(new AttributeAppender("class", new Model("tag"), " "));
//    	                            }
    	                        }
    	                        else
    	                        {
    	                            long default_key = 1L;
    	                            if (currentField.isExistsDefaultValue())
    	                            {
    	                                //	default_key = 0L;
    	                            }

    	                            if (currentField.getDefault_value_type() != null && currentField.getDefault_value_type().equalsIgnoreCase("var"))
    	                            {
    	                                if (params != null)
    	                                {
    	                                    if (entity.getName().toString().equals("city"))
    	                                    {
    	                                        Iterable<String> splits = Splitter.on(",").split(currentField.getDefault_value());
    	                                        Iterator<String> it = splits.iterator();;
    	                                        String choiceId = it.next();
    	                                        String choiceValue = it.next();
    	                                        if (choiceId != null && params.get(choiceId.trim()) != null && params.get(choiceValue.trim()) != null)
    	                                        {
    	                                            default_key = Long.parseLong((params.get(choiceId.trim()).toString()));
    	                                        }
    	                                    }
    	                                    else
    	                                    {
    	                                        String choiceId = currentField.getDefault_value();
    	                                        if (choiceId != null && params.get(choiceId.trim()) != null)
    	                                        {
    	                                            default_key = Long.parseLong(String.valueOf(params.get(choiceId.trim())));
    	                                        }
    	                                    }
    	                                }
    	                            }
    	                            else if (currentField.getDefault_value_type() != null && currentField.getDefault_value_type().equalsIgnoreCase("val"))
    	                            {
    	                                default_key = Long.parseLong(currentField.getDefault_value());
    	                            }

    	                            if (currentField.getParentNode() != null || currentField.getChildNode() != null)
    	                            {
    	                                IModel<Choice> selected_model = new Model(new Choice(default_key, ""));
    	                                IModel<List<? extends Choice>> choices_models = null;
    	                                if (currentField.getChildNode() != null && currentField.getParentNode() == null)
    	                                {
    	                                    choices_models = Model.ofList(DAOImpl.queryPickList(currentField.getPicklist()));
    	                                }


    	                                DropDownChoiceFragment selector = new DropDownChoiceFragment("celldatafield", "dropDownFragment", this, choices_models, selected_model, entity, currentField);
    	                                columnitem.add(selector);
    	                                selector.setOutputMarkupId(true);
    	                                if (currentField.getParentNode() != null)
    	                                {
    	                                    childDropDownComponent.put(currentField.getName(), selector);
    	                                }

    	                                models.put(currentField.getName(), selected_model);
    	                                columnitem.add(selector);
    	                            }
    	                            else
    	                            {
    	                                List<Choice> pickList = DAOImpl.queryPickList(currentField.getPicklist());
    	                                Map<Long, String> list = Maps.newHashMap();
    	                                List<Long> ids = Lists.newArrayList();
    	                                for (Choice p : pickList)
    	                                {
    	                                        list.put(p.getId(), p.getVal());
    	                                        ids.add(p.getId());
    	                                }
    	                                IModel choiceModel = new Model(default_key);
    	                                models.put(currentField.getName(), choiceModel);
    	                                columnitem.add(new DropDownChoiceFragment("celldatafield", "dropDownFragment", this, ids, list, choiceModel, entity, currentField));
    	                            }

    	                        }
    	                    }
    	                    else if (currentField.getDataType().equals("radio"))
    	                    {
    	                        if (j % 2 == 0)
    	                        {
    	                            columnitem.add(new TextFragment("celldatafield", "textFragment", this, currentField.getDisplay() + ":").add(new AttributeAppender("style", new Model("font-weight:bold"), ";")));
//    	                            if (currentField.isRequired())
//    	                            {
    	                                columnitem.add(new AttributeAppender("class", new Model("tag"), " ")).add(new AttributeAppender("style", new Model(currentField.getPattern()), ";"));
//    	                            }
//    	                            else
//    	                            {
//    	                                columnitem.add(new AttributeAppender("class", new Model("tag"), " "));
//    	                            }
    	                        }
    	                        else
    	                        {
    	                            //new Radio("radio");
    	                            Model visibleModel = new Model();
    	                            List lsVisible = Arrays.asList(new String[]
    	                            {
    	                                "否", "是"
    	                            });
    	                            RadioChoice<String> raVisible = new RadioChoice("celldatafield", visibleModel, lsVisible).setSuffix(" ");
    	                            raVisible.setModelValue(new String[]
    	                            {
    	                                "0", "1"
    	                            });
    	                            visibleModel.setObject("否");
    	                            models.put(currentField.getName(), visibleModel);
    	                            columnitem.add(raVisible);
    	                        }
    	                    }
    	                    else if (currentField.getRelationTable() != null)
    	                    {
    	                        if (j % 2 == 0)
    	                        {
    	                            columnitem.add(new TextFragment("celldatafield", "textFragment", this, currentField.getDisplay() + ":").add(new AttributeAppender("style", new Model("font-weight:bold"), ";")));
//    	                            if (currentField.isRequired())
//    	                            {
    	                                columnitem.add(new AttributeAppender("class", new Model("tag"), " ")).add(new AttributeAppender("style", new Model(currentField.getPattern()), ";"));
//    	                            }
//    	                            else
//    	                            {
//    	                                columnitem.add(new AttributeAppender("class", new Model("tag"), " "));
//    	                            }
    	                        }
    	                        else
    	                        {
    	                            long foreignKey = -1L;
    	                            String defaultValue = "";
    	                            if (currentField.getDefault_value_type() != null && currentField.getDefault_value_type().equalsIgnoreCase("var"))
    	                            {
    	                                if (params != null)
    	                                {
    	                                    Iterable<String> splits = Splitter.on(",").split(currentField.getDefault_value());
    	                                    Iterator<String> it = splits.iterator();;
    	                                    String choiceId = it.next();
    	                                    String choiceValue = it.next();
    	                                    if (choiceId != null && params.get(choiceId.trim()) != null && params.get(choiceValue.trim()) != null)
    	                                    {
    	                                        foreignKey = Long.parseLong(String.valueOf(params.get(choiceId.trim())));
    	                                        defaultValue = String.valueOf(params.get(choiceValue.trim()));
    	                                    }
    	                                }
    	                            }
    	                            IModel choiceModel = new Model(foreignKey);
    	                            String fn = "";
    	                            if (currentField.getAlias() != null)
    	                            {
    	                                fn = currentField.getAlias();
    	                            }
    	                            else
    	                            {
    	                                fn = currentField.getName();
    	                            }
    	                            models.put(fn, choiceModel);
    	                            columnitem.add(new RelationTableSearchFragment("celldatafield", "relationTableSearchFragment", this, currentField, entity.getName(), defaultValue, choiceModel));
    	                        }
    	                    }
    	                    else
    	                    {
    	                        if (j % 2 == 0)
    	                        {
    	                            columnitem.add(new TextFragment("celldatafield", "textFragment", this, currentField.getDisplay() + ":").add(new AttributeAppender("style", new Model("font-weight:bold;"), ";")));
//    	                            if (currentField.isRequired())
//    	                            {
    	                                columnitem.add(new AttributeAppender("class", new Model("tag"), " ")).add(new AttributeAppender("style", new Model(currentField.getPattern()), ";"));
//    	                            }
//    	                            else
//    	                            {
//    	                                columnitem.add(new AttributeAppender("class", new Model("tag"), " "));
//    	                            }
    	                        }
    	                        else
    	                        {
    	                            //set default value
    	                            String defaultValue = "";

    	                            if (currentField.getDefault_value_type() != null && currentField.getDefault_value_type().equalsIgnoreCase("var"))
    	                            {

    	                                if (params != null)
    	                                {
    	                                    Object obj = params.get(currentField.getDefault_value());
    	                                    defaultValue = String.valueOf(obj);

    	                                }
    	                            }
    	                            else if (currentField.getDefault_value_type() != null && currentField.getDefault_value_type().equalsIgnoreCase("val"))
    	                            {

    	                                defaultValue = currentField.getDefault_value();
    	                            }
    	                            //set the default Model
    	                            IModel<String> defaultModel = new Model<String>(defaultValue);
    	                            if (currentField.getDataType().equalsIgnoreCase("textarea"))
    	                            {

    	                                columnitem.add(new TextareaFrag("celldatafield", "textAreaFragment", this, defaultModel, currentField));
    	                            }
    	                            else if (currentField.getDataType().equalsIgnoreCase("bjgtextarea"))
    	                            {
    	                                columnitem.add(new BigtextareaFrag("celldatafield", "bjgtextAreaFragment", this, defaultModel, currentField));
    	                            }
    	                            else if (currentField.getDataType().equals("datetime-local"))
    	                            {
    	                                if (defaultValue.isEmpty()||"null".equals(defaultValue))
    	                                {
    	                                    Date date = new Date();
    	                                    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    	                                    String date_value = dateformat.format(date);
    	                                    defaultModel = new Model<String>(date_value);
    	                                }
    	                                columnitem.add(new TextInputFragment("celldatafield", "textInputFragment", this, defaultModel, currentField));
    	                            }
    	                            else
    	                            {
    	                                columnitem.add(new TextInputFragment("celldatafield", "textInputFragment", this, defaultModel, currentField));
    	                            }

    	                            models.put(currentField.getName(), defaultModel);
    	                        }
    	                    }
    	                    columnRepeater.add(columnitem);
    	                }
    	            }
    	        }

    	        Form form = new Form("form");
    	        form.add(new Button("save")
    	        {
    	            public void onSubmit()
    	            {
    	                logger.debug(models);
    	                saveEntity(models, entity, userId, userName,entityId);
    	                String entityName=entity.getName().toString();
    	                setResponsePage(new EntityDetailPage("opportunity",entityId));
    	            }
    	        });
    	        form.add(fieldGroupRepeater);
    	        form.setMultiPart(true);
    	        add(form);
    	    }

    	    public static long saveEntity(Map<String, IModel> models, Entity entity, String userId, String userName,String entityId)
    	    {
    	        logger.debug("the form was submitted!");
    	        logger.debug(models);
    	        String fileName = "";
    	        String srcForFile = "";
    	        List<String> fieldNames = Lists.newArrayList();
    	        List<String> values = Lists.newArrayList();
    	        int total_score = 0;
    	        String productlineId = "0";
    	        StringBuffer endDate = new StringBuffer();
    	        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    	        //找出title属性，判断实体名称，先声明
    	        Long daypart = 0l;
    	        StringBuffer title = new StringBuffer();
    	        String loginName = "";
    	        if (entity.getName().equals("user"))
    	        {
    	            loginName = models.get("login_name").getObject().toString();
    	        }
    	        for (String key : models.keySet())
    	        {
    	            fieldNames.add(key);
    	            logger.debug("currentFieldkey:" + key);
    	            Field field = entity.getFieldByName(key);
    	            logger.debug("currentField:" + field);
    	            if (models.get(key).getObject() instanceof String)
    	            {
    	                    values.add("'" + (String) models.get(key).getObject() + "'");
    	            }
    	            else if (models.get(key).getObject() instanceof Choice)
    	            {
    	                values.add(String.valueOf(((Choice) models.get(key).getObject()).getId()));
    	            }
    	            else
    	            {
    	                    values.add(String.valueOf(models.get(key).getObject()));
    	            }

    	        }
    	        //modify_datetime whenadded response_person 
    	        List<Field> autoFields = entity.getAutoFields();
    	        for (Field f : autoFields)
    	        {
    	            if (f.getName().equalsIgnoreCase("modifier_date") || f.getName().equalsIgnoreCase("create_date"))
    	            {
    	                values.add("'" + dateformat.format(new Date()) + "'");
    	            }

    	            if (f.getName().equalsIgnoreCase("modified_by") || f.getName().equalsIgnoreCase("created_by"))
    	            {
    	                values.add("'" + userId + "'");
    	            }
    	            
    	            fieldNames.add(f.getName());

    	        }
    	        if ("user".equals(entity.getName()))
    	        {
    	            long crmuserkey = -1;
    	            List<String> loginNames = DAOImpl.getAllLoginNames();
    	            if (loginNames.contains(loginName))
    	            {
    	                return crmuserkey;
    	            }
    	            else
    	            {
    	                crmuserkey = DAOImpl.createNewUser(entity.getName(), fieldNames, values, userId);
    	                return crmuserkey;
    	            }
    	        }
    	        else
    	        {
    	        	long generatedId =-1;
    	            generatedId = DAOImpl.createOpportunityTeam(entity.getName(), fieldNames, values, userId,entityId);
    	            return generatedId;
    	        }
    	    }


    	    private class TextFragment extends Fragment
    	    {

    	        public TextFragment(String id, String markupId,
    	          MarkupContainer markupProvider, String label)
    	        {
    	            super(id, markupId, markupProvider);
    	            add(new Label("celldata", label));
    	            // TODO Auto-generated constructor stub
    	        }
    	    }

    	    private class LayoutFragment extends Fragment
    	    {

    	        public LayoutFragment(String id, String markupId,
    	          MarkupContainer markupProvider, String label)
    	        {
    	            super(id, markupId, markupProvider);
    	            add(new Label("add", label));
    	            // TODO Auto-generated constructor stub
    	        }
    	    }

    	    private class DropDownChoiceFragment extends Fragment
    	    {

    	        public DropDownChoiceFragment(String id, String markupId,
    	          MarkupContainer markupProvider, final List<Long> ids,
    	          final Map<Long, String> list, IModel model, final Entity entity, final Field currentField)
    	        {
    	            super(id, markupId, markupProvider);
    	            DropDownChoice dropDownChoice = (new DropDownChoice<Long>("dropDownInput", model, ids,
    	              new IChoiceRenderer<Long>()
    	              {
    	                  @Override
    	                  public Object getDisplayValue(Long id)
    	                  {
    	                      // TODO Auto-generated method stub
    	                      return list.get(id);
    	                  }

    	                  @Override
    	                  public String getIdValue(Long id, int index)
    	                  {
    	                      return String.valueOf(id);
    	                  }
    	              }));
    	                dropDownChoice.setNullValid(true);
    	            if (entity.getName().equals("activity") )
    	            {
    	                dropDownChoice.add(new AttributeAppender("class", new Model("required-pickList"), " "));
    	            }
    	            if (currentField.getName().equals("activity_daypart"))
    	            {
    	                dropDownChoice.add(new AttributeModifier("id", "daypart"));
    	                dropDownChoice.setNullValid(true);
    	            }
    	            add(dropDownChoice);
    	        }

    	        public DropDownChoiceFragment(String id, String markupId, MarkupContainer markupProvider,
    	          IModel choices, IModel default_model, final Entity entity, final Field currentField)
    	        {
    	            super(id, markupId, markupProvider);
    	            DropDownChoice dropDown = createDropDownListFromPickList("dropDownInput", choices, default_model);
    	            if (currentField.getName().equals("activity_type"))
    	            {
    	                dropDown.setNullValid(false);
    	            }
    	            else
    	            {
    	                dropDown.setNullValid(true);
    	            }
    	            add(dropDown);

    	            if (currentField.getChildNode() != null)
    	            {
    	                parentModels.put(currentField.getName(), default_model);
    	                dropDown.add(new AjaxFormComponentUpdatingBehavior("onchange")
    	                {

    	                    @Override
    	                    protected void onUpdate(AjaxRequestTarget target)
    	                    {
    	                        target.add(childDropDownComponent.get(currentField.getChildNode()));
    	                        if (entity.getFieldByName(currentField.getChildNode()).getChildNode() != null)
    	                        {

    	                            parentModels.get(entity.getFieldByName(currentField.getChildNode()).getName()).setObject(new Choice(-1L, ""));
    	                            target.add(childDropDownComponent.get(entity.getFieldByName(currentField.getChildNode()).getChildNode()));
    	                            // target.
    	                        }
    	                    }
    	                });
    	            }
    	            if (entity.getName().equals("activity") )
    	            {
    	                dropDown.add(new AttributeAppender("class", new Model("required-pickList"), " "));
    	            }
    	        }
    	    }

    	    private DropDownChoice createDropDownListFromPickList(String markupId, IModel choices, IModel default_model)
    	    {
    	        return new DropDownChoice<Choice>(markupId, default_model, choices,
    	          new IChoiceRenderer<Choice>()
    	          {
    	              @Override
    	              public Object getDisplayValue(Choice choice)
    	              {
    	                  // TODO Auto-generated method stub
    	                  return choice.getVal();
    	              }

    	              @Override
    	              public String getIdValue(Choice choice, int index)
    	              {
    	                  // TODO Auto-generated method stub
    	                  return String.valueOf(choice.getId());
    	              }

    	          });
    	    }

    	    private class TextareaFrag extends Fragment
    	    {

    	        public TextareaFrag(String id, String markupId, MarkupContainer markupProvider, IModel model, Field currentField)
    	        {
    	            super(id, markupId, markupProvider);
    	            // TODO Auto-generated constructor stub
    	            TextArea<String> textArea = new TextArea<String>("address", model);
    	            add(textArea);
    	            if (currentField.isRequired())
    	            {
    	                textArea.add(new AttributeAppender("class", new Model("required-field"), " "));
    	            }
    	            /*if(currentField.getName().equals("description")){
    	             textArea.add(new AttributeAppender("")
    	             }*/
    	        }
    	    }

    	    private class BigtextareaFrag extends Fragment
    	    {

    	        public BigtextareaFrag(String id, String markupId, MarkupContainer markupProvider, IModel model, Field currentField)
    	        {
    	            super(id, markupId, markupProvider);
    	            // TODO Auto-generated constructor stub
    	            TextArea<String> textArea = new TextArea<String>("description", model);
    	            add(textArea);
    	            if (currentField.isRequired())
    	            {
    	                textArea.add(new AttributeAppender("class", new Model("required-field"), " "));
    	            }
    	            /*if(currentField.getName().equals("description")){
    	             textArea.add(new AttributeAppender("")
    	             }*/
    	        }
    	    }

    	    private class RelationTableSearchFragment extends Fragment
    	    {

    	        public RelationTableSearchFragment(String id, String markupId,
    	          MarkupContainer markupProvider, Field field, String entityName, final String defaultValue, final IModel defaultModel)
    	        {
    	            super(id, markupId, markupProvider);
    	            PageParameters params = new PageParameters();
    	            params.set("en", field.getRelationTable());
    	            params.set("excludeName", entityName);
    	            params.set("target", (Long) defaultModel.getObject());
    	            params.set("key",field.getName());
    	            add(new BookmarkablePageLink<Void>("search_btn", SelectEntryPage.class, params));
    	            HiddenField<?> hidden = new HiddenField<String>("selected_id_hidden", defaultModel);
    	            hidden.add(new AttributeModifier("id", field.getRelationTable() + "_id"+field.getName()));
    	            add(hidden);
    	            TextField<String> text = new TextField<String>("selected_value_input", new Model(defaultValue));
    	            text.add(new AttributeModifier("id", field.getRelationTable()  + "_name"+field.getName()));
    	            add(text);
    	        }
    	    }


    	    private class TextInputFragment extends Fragment
    	    {

    	        public TextInputFragment(String id, String markupId,
    	          MarkupContainer markupProvider, IModel model, Field currentField)
    	        {
    	            super(id, markupId, markupProvider);
    	            TextField<String> text = new TextField<String>("input", model);

    	            if (currentField.getDataType().equals("tel") || currentField.getDataType().equals("fax"))
    	            {
    	                text.add(new AttributeModifier("pattern", new Model("^((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)")));
    	            }
    	            if (currentField.isRequired())
    	            {
    	                text.add(new AttributeAppender("class", new Model("required-field"), " "));
    	            }
    	            if (currentField.getName().equals("title"))
    	            {
    	                text.add(new AttributeModifier("placeholder", "系统可自动生成"));
    	            }
    	            if (currentField.getDataType().equals("datetime-local"))
    	            {
    	                text.add(new AttributeModifier("value", new Model((String) model.getObject())));

    	            }
    	            if (currentField.getDataType().equalsIgnoreCase("number"))
    	            {
    	                text.add(new AttributeModifier("value", new Model("0")));
    	            }
    	            add(text);
    	            text.add(new AttributeModifier("type", new Model(currentField.getDataType())));

    	            text.add(new AttributeModifier("id", new Model(currentField.getName())));

    	        }
    	    }
   

}
