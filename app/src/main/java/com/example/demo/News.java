package com.example.demo;

public class News {
    public String title; // 标题
    public float content; //内容
    public News(String title, float content) {
        this.title = title;
        this.content = content;
    }

    public void setContent(float content) {
        this.content = content;
    }
}

