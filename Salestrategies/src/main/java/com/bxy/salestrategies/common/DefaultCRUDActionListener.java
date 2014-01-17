package com.bxy.salestrategies.common;


import java.io.Serializable;

public abstract class DefaultCRUDActionListener implements ICRUDActionListener, Serializable {

    @Override
    public void create() { 
        
    }

    @Override
    public void delete() {
      
    }

    @Override
    public void update() {
     
    }
    @Override
    public void resetPassword(int userId){
    	
    }
    @Override
    public void doneBtn() {
      
    }
    @Override
    public void downLoadBtn() throws Exception{
      
    }
    @Override
    public void noExecute(String entityName,int entityId) {
    	
    }
}
