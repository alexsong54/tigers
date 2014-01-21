package com.bxy.salestrategies;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

import com.bxy.salestrategies.model.User;
import com.bxy.salestrategies.db.DAOImpl;
/**
 * 
 * @author brenda
 *
 */

public class SignInSession  extends AuthenticatedWebSession{
	/** Trivial user representation */
	private String user;
	private String userId;

	/**
	 * Constructor
	 * 
	 * @param request
	 *            The current request object
	 */
	public SignInSession(Request request)
	{
		super(request);

	}
	/**
	 * Checks the given username and password, returning a User object if if the username and
	 * password identify a valid user.
	 * 
	 * @param username
	 *            The username
	 * @param password
	 *            The password
	 * @return True if the user was authenticated
	 */
	@Override
	public final boolean authenticate(final String username, final String password)
	{
		if (user == null)
		{
		    
		    User userinfo = DAOImpl.login(username, password);
		    if(userinfo!=null && userinfo.getId() != 0){
		        user = userinfo.getName();
		        userId = String.valueOf(userinfo.getId());
		    }
		}
		return user != null;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Override
	public Roles getRoles() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
