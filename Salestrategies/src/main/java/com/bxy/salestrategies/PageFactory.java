package com.bxy.salestrategies;

import org.apache.wicket.Page;

import com.bxy.salestrategies.common.EntityDetailPage;


public class PageFactory {
  
    public static Page createPage(String name){
        if(name.equalsIgnoreCase("account")){
            return new AccountPage();
        }else if(name.equalsIgnoreCase("contact")){
            return new ContactPage();
        }else if(name.equalsIgnoreCase("activity")){
            return new ActivityPage();
        }else if(name.equalsIgnoreCase("user")){
          return new UserPage();
        }
        else{
            return new HomePage();
        }
    }
     public static Page createPageToDetail(String EntityName,String id){
            return new EntityDetailPage(EntityName,id);
     }
     
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
