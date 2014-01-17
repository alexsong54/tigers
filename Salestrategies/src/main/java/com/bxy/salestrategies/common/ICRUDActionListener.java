package com.bxy.salestrategies.common;


public interface ICRUDActionListener {

    public void create();
    public void update();
    public void delete();
    public void resetPassword(int userId);
    public void doneBtn() ;
    public void downLoadBtn() throws Exception;
    public void noExecute(String entityName,int entityId);
    public void merge();
	public void ineffective();
}

