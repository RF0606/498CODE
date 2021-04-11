package com.example.qxapp.user;

import cn.bmob.v3.BmobObject;

public class Product extends BmobObject {
    private String name,infor;
    private User user;

    public String getName() {
        return name;
    }

    public String getInfo() {
        return infor;
    }

    public User getUser() {
        return user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInfo(String infor) {
        this.infor = infor;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
