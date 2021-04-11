package com.example.qxapp.user;

import cn.bmob.v3.BmobObject;

public class Post extends BmobObject {

    //上传对应的用户
    private  User author;

    //对应的信息，对应的时bomb云里post界面每列的名称，因为post内只有nickname。content以及createdAT所以这块只有三个getxxx时被使用的
    private String title,content,nickname,name;

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    //这行我寻思没用，先放着回头看
    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickname;
    }

    public String getContent() {
        return content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setNickName(String nickName) {
        this.nickname = nickName;
    }
}
