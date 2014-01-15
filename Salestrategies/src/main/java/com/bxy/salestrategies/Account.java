/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bxy.salestrategies;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.border.BoxBorder;

/**
 *
 * @author Sam
 */
public class Account extends WebPage
{  
    public Account(){
        Border border = new BoxBorder("border");
       

        border.add(new Label("birthday", "2006-09-09"));
         add(border);
    }
}
