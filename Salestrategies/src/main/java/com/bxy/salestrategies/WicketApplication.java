package com.bxy.salestrategies;

import org.apache.wicket.protocol.http.WebApplication;

public class WicketApplication extends WebApplication {

	public Class getHomePage() {
		return Index.class;
	}

	public WicketApplication() {
              
	}
}