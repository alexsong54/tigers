package com.bxy.salestrategies.model;


import java.io.Serializable;

public class Choice implements Serializable {
    private long id;
    private String key;
    private String val;
    private String name;

    public Choice(){
        
    }
    public Choice(Long id, String val){
        this.id = id;
        this.val = val;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
