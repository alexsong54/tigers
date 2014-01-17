package com.bxy.salestrategies;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;
import com.bxy.salestrategies.model.User;
import com.bxy.salestrategies.common.SignInSession;
import com.bxy.salestrategies.db.DAOImpl;

public final class Login extends WebPage
{
	public Login()
	{
		//create feedback panel and add to page
		add(new FeedbackPanel("feedback"));
		//add sign-in form to page
		add(new SignInForm("signInForm"));
   
	}
    public final class SignInForm extends Form<Void>
    {
    	private static final String USERNAME ="username";
    	private static final String PASSWORD = "password";
    	private final ValueMap properties = new ValueMap();
    	/**
    	 * Constructor
    	 * 
    	 * @param id
    	 * 			 id of the form component
    	 */
    	public SignInForm(final String id)
    	{
    		super(id);
    		add(new TextField<String>(USERNAME,new PropertyModel<String>(properties,USERNAME)));
    		add(new PasswordTextField(PASSWORD,new PropertyModel<String>(properties,PASSWORD)));
    	}
    	/**
         * sign 
         */
        @SuppressWarnings("unused")
		public final void onSubmit()
        {
        	//get session info
        	SignInSession session = getMysession();
        	//clear session user
        	session.setUser(null);
        	//判断用户是否激活密码是否存在
        	if("".equals(getUsername())||"".equals(getPassword())){
        		 String errmsg = getString("loginError", null, "用户名和密码不能为空!");
                 error(errmsg);
        	}else{
            	User user = DAOImpl.getUserByLoginName(getUsername());
            	if(null!=user){
      	            if (session.signIn(getUsername(),getPassword()))
                    {
      	            	setResponsePage(new Index());
                    }
      	            else
      	            {
      	                // Get the error message from the properties file associated with the Component
      	                String errmsg = getString("loginError", null, "用户名或密码错误!");
      	                // Register the error message with the feedback panel
      	                error(errmsg);
      	            }
            	}else{
            		String errmsg = getString("loginError", null, "用户名不存在!");
   	                error(errmsg);
            	}
        	}
        }
    	private String getUsername()
    	{
    		return properties.getString(USERNAME);
    	}
    	private String getPassword()
    	{
    		return properties.getString(PASSWORD);
    	}
    	private SignInSession getMysession()
    	{
    		return (SignInSession)getSession();
    	}
    	
    }
}
