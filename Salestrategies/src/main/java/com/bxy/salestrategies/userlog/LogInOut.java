package com.bxy.salestrategies.userlog;


public class LogInOut extends LogObj {

     private String loginName;
     private String sessionId;
     private long logints;
     private long logoutts;
     
    public String getLoginName() {
        return loginName;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public long getLogints() {
        return logints;
    }
    public void setLogints(long logints) {
        this.logints = logints;
    }
    public long getLogoutts() {
        return logoutts;
    }
    public void setLogoutts(long logoutts) {
        this.logoutts = logoutts;
    }
     
}
