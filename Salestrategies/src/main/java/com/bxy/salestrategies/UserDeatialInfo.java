package com.bxy.salestrategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.bxy.salestrategies.common.Entity;
import com.bxy.salestrategies.common.Field;
import com.bxy.salestrategies.db.DAOImpl;
import com.bxy.salestrategies.model.Choice;
import com.bxy.salestrategies.model.User;
import com.bxy.salestrategies.util.CRMUtility;
import com.bxy.salestrategies.util.Configuration;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class UserDeatialInfo extends UserInfoSettingPage {

    private static final long serialVersionUID = 7459440666591284026L;
    private static final Logger logger = Logger.getLogger(UserDeatialInfo.class);
    final Map<String, IModel> fieldNameToModel = Maps.newHashMap();
    final Map<String, IModel> models = Maps.newHashMap();
    //设置显示行数
    private int number_of_column = 1;

    /**
     * Contract
     */
    public UserDeatialInfo() {
        logger.debug("init userManager");
        initPage("");
    }
    public UserDeatialInfo(String result){
    	logger.debug("init userManager");
        initPage(result);
    }
    private void initPage(String result) {
        logger.debug("sessionID:" + ((SignInSession) getSession()).getUserId());
        int userId = Integer.parseInt(((SignInSession) getSession()).getUserId());
        Entity entity = Configuration.getEntityByName("user");
        String primaryKeyName = entity.getPrimaryKeyName();
        //get crmUser 
        User user = DAOImpl.getUserInfoById(userId); 
        //add prompt 
        final RepeatingView div = new RepeatingView("promptDiv");
        final AbstractItem group = new AbstractItem(div.newChildId());
        final Label promptButton = new Label("promptButton","X");
        group.add(promptButton);
        final Label promptLabel = new Label("prompt","提示:操作已成功！");
        group.add(promptLabel);
        if("".equals(result)||"failure".equals(result)){
        	 div.add(new AttributeAppender("style",new Model("display:none"),";"));
             group.add(new AttributeAppender("style",new Model("display:none"),";"));
        }
        div.add(group);
        add(div);
      //add prompt 
        final RepeatingView errordiv = new RepeatingView("alertDiv");
        final AbstractItem errorgroup = new AbstractItem(div.newChildId());
        final Label errorpromptButton = new Label("alertButton","X");
        errorgroup.add(errorpromptButton);
        final Label errorpromptLabel = new Label("alert","提示:用户登录名已存在！");
        errorgroup.add(errorpromptLabel);
        if("".equals(result)||"success".equals(result)){
        	errordiv.add(new AttributeAppender("style",new Model("display:none"),";"));
            errorgroup.add(new AttributeAppender("style",new Model("display:none"),";"));
        }
        errordiv.add(errorgroup);
        add(errordiv);
        final List<String> fieldNames = Lists.newArrayList();
        //得到基本信息信息
        RepeatingView fieldGroupRepeater = new RepeatingView("fieldGroupRepeater");
        add(fieldGroupRepeater);
        List<Field> groupfields = new ArrayList<Field>();
        List<Field> fields = entity.getFields();
        for (Field f : fields) {
            if (f.isBaseInfo() == true) {
                groupfields.add(f);
            }
        }
        AbstractItem groupitem = new AbstractItem(fieldGroupRepeater.newChildId());
        fieldGroupRepeater.add(groupitem);
        RepeatingView dataRowRepeater = new RepeatingView("dataRowRepeater");
        groupitem.add(dataRowRepeater);
        int numOfField = 0;
        List<Field> visibleFields = Lists.newArrayList();
        for (Field f : groupfields) {
            if (!f.isVisible()) {
                continue;
            }
            numOfField++;
            visibleFields.add(f);
        }
        logger.debug("numOfField:" + numOfField);
        int num_of_row = (numOfField / number_of_column);
        for (int i = 0; i < num_of_row; i++) {
            AbstractItem item = new AbstractItem(dataRowRepeater.newChildId());
            dataRowRepeater.add(item);
            //添加了一行
            RepeatingView columnRepeater = new RepeatingView("columnRepeater");
            item.add(columnRepeater);
            //行内添加两列元素
            for (int j = 0; j < 2 * number_of_column; j++) {
                AbstractItem columnitem = new AbstractItem(columnRepeater.newChildId(), new Model(String.valueOf(primaryKeyName)));
                Field currentField = visibleFields.get(i * number_of_column + j / 2);
                if (j % 2 == 0) {
                    columnitem.add(new TextFragment("editdata", "textFragment", this, currentField.getDisplay() + ":"));
                    if (currentField.isRequired()) {
                        columnitem.add(new AttributeAppender("class", new Model("tag"), " ")).add(new AttributeAppender("style", new Model("color:red"), ";"));
                    }else{
                        columnitem.add(new AttributeAppender("class", new Model("tag"), " "));
                    }
                    fieldNames.add(currentField.getName());
                } else {
                    String fieldName = null;
                    if (currentField.getName().equals("name")) {
                        fieldName = user.getName();
                    } else if (currentField.getName().equalsIgnoreCase("telePhone")) {
                        fieldName = user.getTelephone();
                    } else if (currentField.getName().equals("login_name")) {
                        fieldName = user.getLogin_name();
                    } else if (currentField.getName().equals("gender")) {
                        fieldName = "" + user.getGender();
                    } else if (currentField.getName().equals("office_tel")){
                    	fieldName = "" + user.getOffice_tel();
                    }else {
                        fieldName = user.getEmail();
                    }
                    if (currentField.getName().equals("sex")) {
                        List<Choice> pickList = DAOImpl.queryPickList(currentField.getPicklist());
                        Map<Long, String> list = Maps.newHashMap();
                        List<Long> ids = Lists.newArrayList();
                        for (Choice p : pickList) {
                            list.put(p.getId(), p.getVal());
                            ids.add(p.getId());
                        }
                        IModel choiceModel = new Model(Long.parseLong("" + user.getGender()));
                        models.put(currentField.getName(), choiceModel);
                        fieldNameToModel.put(currentField.getName(), choiceModel);
                        columnitem.add(new DropDownChoiceFragment("editdata", "dropDownFragment", this, ids, list, choiceModel));
                    } else {
                        IModel<String> textModel = new Model<String>("");
                        models.put(CRMUtility.formatValue(currentField.getFormatter(), String.valueOf(fieldName)), textModel);
                        fieldNameToModel.put(currentField.getName(), textModel);
                        columnitem.add(new TextInputFragment("editdata", "textInputFragment", this, textModel, fieldName, currentField));
                    }
                }
                columnRepeater.add(columnitem);
            }
        }
        //add user photo
        //create edit userForm
        
        Form form = new Form("form") {
            @Override
            protected void onSubmit() {
                logger.debug("the form was submitted!");
                String userName = "";
                String cellPhone = "";
                String email = "";
                String photo = "";
                String loginName = "";
                String office = "";
                int sex = 0;
                for (String k : fieldNameToModel.keySet()) {
                    logger.debug("k" + k);
                    String value = fieldNameToModel.get(k).getObject() == null ? null : String.valueOf(fieldNameToModel.get(k).getObject());
                    if (k.equals("name")) {
                        userName = value;
                    } else if (k.equals("cellPhone")) {
                        cellPhone = value;
                    } else if (k.equals("photo")) {
                        photo = value;
                    } else if (k.equals("sex")) {
                        sex = Integer.parseInt(value);
                    } else if (k.equals("loginName")) {
                        loginName = value;
                    } else if(k.equals("email")){
                        email = value;
                    }else if(k.equals("office_tel")){
                    	office = value;
                    }
                }
                int userId = Integer.parseInt(((SignInSession) getSession()).getUserId());
                User userinfo = DAOImpl.getUserInfoById(userId);
                List<String> loginNames =DAOImpl.getLoginNames(String.valueOf(userId));
                if(loginNames.contains(loginName)){
                	errordiv.add(new AttributeAppender("style",new Model("display:block"),";"));
                	errorgroup.add(new AttributeAppender("style",new Model("display:block"),";"));
                	errorpromptLabel.add(new AttributeAppender("style",new Model("display:block"),";"));
                	errorpromptButton.add(new AttributeAppender("style",new Model("display:block"),";"));
                	setResponsePage(new UserDeatialInfo("failure"));
                }else{
	                if(DAOImpl.updateStatusOfInternalMeeting(userId, userName, cellPhone, email, photo, sex, loginName,office)){
	                	div.add(new AttributeAppender("style",new Model("display:block"),";"));
	                	group.add(new AttributeAppender("style",new Model("display:block"),";"));
	            		promptLabel.add(new AttributeAppender("style",new Model("display:block"),";"));
	            		promptButton.add(new AttributeAppender("style",new Model("display:block"),";"));
	            		setResponsePage(new UserDeatialInfo("success"));
	                };
                }
            }
        };
        form.add(fieldGroupRepeater);
        add(form);
    }

    private class TextInputFragment extends Fragment {

        public TextInputFragment(String id, String markupId,
                MarkupContainer markupProvider, IModel model, String value, Field currentField) {
            super(id, markupId, markupProvider);
            TextField<String> text = new TextField<String>("input", model);
            text.add(new AttributeModifier("value", new Model(value)));
            if (!currentField.isEditable()) {
                text.add(new AttributeAppender("disabled", new Model("disabled"), ";"));
            }
            if (currentField.isRequired()) {
              //text.add(new AttributeModifier("required", new Model("required")));
            	text.add(new AttributeAppender("class",new Model("required-field")," "));
            }
            if (currentField.getDataType().equals("tel") || currentField.getDataType().equals("fax")) {
                text.add(new AttributeModifier("pattern", new Model("^((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)")));
            }
            text.add(new AttributeModifier("type", new Model(currentField.getDataType())));
            text.add(new AttributeModifier("id", new Model(currentField.getName())));
            add(text);
        }
    }

    private class TextFragment extends Fragment {

        public TextFragment(String id, String markupId,
                MarkupContainer markupProvider, String label) {
            super(id, markupId, markupProvider);
            add(new Label("celldata", label));
        }
    }

    private class DropDownChoiceFragment extends Fragment {

        public DropDownChoiceFragment(String id, String markupId,
                MarkupContainer markupProvider, final List<Long> ids,
                final Map<Long, String> list, IModel model) {
            super(id, markupId, markupProvider);
            add(new DropDownChoice<Long>("dropDownInput", model, ids,
                    new IChoiceRenderer<Long>() {
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
        }
    }
}
