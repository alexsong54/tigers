package com.bxy.salestrategies;

import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.bxy.salestrategies.db.DAOImpl;
import com.bxy.salestrategies.model.User;
import com.google.common.collect.Maps;

public class UpdateSignPassword extends UserInfoSettingPage {

    private static final long serialVersionUID = -317099683219015628L;
    private static final Logger logger = Logger.getLogger(UpdateSignPassword.class);
    final Map<String, IModel> models = Maps.newHashMap();

    public UpdateSignPassword() {
        initPage();
    }

    public void initPage() {
        //获取到登录对象，将用户名传入页面，然后如何获取
        final int userId = Integer.parseInt(((SignInSession) getSession()).getUserId());
        User user = DAOImpl.getUserInfoById(userId);
         //add prompt 
        final RepeatingView errordiv = new RepeatingView("errorpromptDiv");
        final AbstractItem errorgroup = new AbstractItem(errordiv.newChildId());
        final Label errorpromptButton = new Label("errorpromptButton","X");
        errorgroup.add(errorpromptButton);
        final Label errorpromptLabel = new Label("errorprompt","提示:旧密码输入错误！");
        errorgroup.add(errorpromptLabel);
        errordiv.add(new AttributeAppender("style",new Model("display:none"),";"));
        errorgroup.add(new AttributeAppender("style",new Model("display:none"),";"));
        errordiv.add(errorgroup);
        add(errordiv);
        //add prompt 
        final RepeatingView div = new RepeatingView("promptDiv");
        final AbstractItem group = new AbstractItem(div.newChildId());
        final Label promptButton = new Label("promptButton","X");
        group.add(promptButton);
        final Label promptLabel = new Label("prompt","提示:操作已成功！");
        group.add(promptLabel);
        div.add(new AttributeAppender("style",new Model("display:none"),";"));
        group.add(new AttributeAppender("style",new Model("display:none"),";"));
        div.add(group);
        add(div);
        Form form = new Form("form") {
            @Override
            protected void onSubmit() {
                //首先获取form表单数据。验证旧密码是否正确。然后修改密码
                String oldPassword = models.get("oldPassword").getObject() == null ? null : String.valueOf(models.get("oldPassword").getObject());
                String newPassword = models.get("newPassword").getObject() == null ? null : String.valueOf(models.get("newPassword").getObject());
                User crmUser = DAOImpl.getUserInfoById(Integer.parseInt(((SignInSession) getSession()).getUserId()));
                logger.debug("oldPwd:" + oldPassword);
                logger.debug("old:" + DigestUtils.md5Hex(oldPassword));
                logger.debug("password:" + crmUser.getPassword());
                //判断旧密码是否正确,修改密码
                if (DigestUtils.md5Hex(oldPassword).equals(crmUser.getPassword())) {
                    logger.debug("true");
                    //修改密码
                    if (DAOImpl.updateCrmUserPassword(userId, newPassword)) {
                    	div.add(new AttributeAppender("style",new Model("display:block"),";"));
                    	group.add(new AttributeAppender("style",new Model("display:block"),";"));
                		promptLabel.add(new AttributeAppender("style",new Model("display:block"),";"));
                		promptButton.add(new AttributeAppender("style",new Model("display:block"),";"));
                    }
                } else {
                    logger.debug("false");
                    errordiv.add(new AttributeAppender("style",new Model("display:block"),";"));
                    errorgroup.add(new AttributeAppender("style",new Model("display:block"),";"));
                    errorpromptLabel.add(new AttributeAppender("style",new Model("display:block"),";"));
                    errorpromptButton.add(new AttributeAppender("style",new Model("display:block"),";"));
                }
            }
        };
		add(form);
        form.add(new Label("userName", user.getName()));
        IModel<String> textModel = new Model<String>("");
        PasswordTextField oldPassword = new PasswordTextField("oldPassword", textModel);
        oldPassword.add(new AttributeAppender("value", ""));
        models.put("oldPassword", textModel);
        IModel<String> newPasswordModel = new Model<String>("");
        PasswordTextField newPassword = new PasswordTextField("newPassword", newPasswordModel);
        newPassword.add(new AttributeAppender("value", ""));
        models.put("newPassword", newPasswordModel);
        IModel<String> checkNewPasswordModel = new Model<String>("");
        PasswordTextField checkPassword = new PasswordTextField("checkNewPassword", checkNewPasswordModel);
        checkPassword.add(new AttributeAppender("value", ""));
        models.put("checkNewPassword", checkNewPasswordModel);
        form.add(oldPassword);
        form.add(newPassword);
        form.add(checkPassword);
    }
}
