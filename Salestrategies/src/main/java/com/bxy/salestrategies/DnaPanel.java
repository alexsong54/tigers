package com.bxy.salestrategies;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class DnaPanel extends Panel{

	private static final long serialVersionUID = 7050475224663541217L;

	public DnaPanel(String id) {
		super(id);
		add(new Label("title","The Strategic Selling DNA Strand "));
	}
}
