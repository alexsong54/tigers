package com.ss.salestraties;

import org.apache.wicket.protocol.http.WebApplication;

public class HelloWorldApplication extends WebApplication {

	public Class getHomePage() {
		return Index.class;
	}

	public HelloWorldApplication() {
              
	}
}