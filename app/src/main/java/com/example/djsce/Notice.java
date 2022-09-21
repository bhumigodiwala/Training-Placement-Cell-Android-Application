package com.example.djsce;

public class Notice
{
    String Head;
    String Content;

    public Notice() {

    }

    public Notice(String head, String content) {
        Head = head;
        Content = content;
    }

    public String getHead() {
        return Head;
    }

    public void setHead(String head) {
        Head = head;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
