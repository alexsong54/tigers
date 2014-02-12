package com.bxy.salestrategies;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.component.IRequestableComponent;
import org.apache.wicket.request.resource.SharedResourceReference;
import org.apache.wicket.util.file.File;

import com.bxy.salestrategies.util.Utility;

public final class WicketApplication extends WebApplication {
	
	
	public WicketApplication()
    {
    }


	@Override
    public Class<? extends Page> getHomePage()
    {
        return HomePage.class;
    }

    /**
     * @see org.apache.wicket.protocol.http.WebApplication#newSession(Request, Response)
     */
    @Override
    public Session newSession(Request request, Response response)
    {
        return new SignInSession(request);
    }
    
    @Override
	protected void init()
    {
        super.init();
        getRequestCycleSettings().setResponseRequestEncoding("UTF-8"); 
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8"); 
       // add your configuration here
        getSharedResources().add("image", new FolderResource(new File(getServletContext().getRealPath("image"))));
        mountResource("/image", new SharedResourceReference("image"));
        mountPage("/mount/AccountPage",AccountPage.class);
        
        // Register the authorization strategy
        getSecuritySettings().setAuthorizationStrategy(new IAuthorizationStrategy()
        {
            public boolean isActionAuthorized(Component component, Action action)
            {
                // authorize everything
                return true;
            }

            public <T extends IRequestableComponent> boolean isInstantiationAuthorized(
                Class<T> componentClass)
            {
                // Check if the new Page requires authentication (implements the marker interface)
                if (AuthenticatedWebPage.class.isAssignableFrom(componentClass))
                {
                    // Is user signed in?
                    if (((SignInSession)Session.get()).isSignedIn())
                    {
                        // okay to proceed
                        return true;
                    }

                    // Intercept the request, but remember the target for later.
                    // Invoke Component.continueToOriginalDestination() after successful logon to
                    // continue with the target remembered.

                    throw new RestartResponseAtInterceptPageException(Login.class);
                }

                // okay to proceed
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
       Utility.getThreadPoolExecutor().shutdown();
        super.onDestroy();
    }
}