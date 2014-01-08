package com.ss.salestraties;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.border.BoxBorder;



public class Index extends WebPage
{

    private static final long serialVersionUID = 1L;
 
    public Index()
    {
        //	Label label =new Label("message", "<h3>dsa</h3>");
        // label.setEscapeModelStrings(false);
        //  add(label);
        super();
        Border border = new BoxBorder("border");
        add(border);

        border.add(new Label("birthday", "2006-09-09"));
      
    }
}
