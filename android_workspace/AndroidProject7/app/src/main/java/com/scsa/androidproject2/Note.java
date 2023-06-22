package com.scsa.androidproject2;

import java.io.Serializable;

public class Note implements Serializable {

    private int _id;  // 식별자
    private String title;  // 제목
    private String body;  // 내용

    private int status; //상태 컬럼

    private String date; //날짜 컬럼

    public Note() {}

    public Note(int _id, String title, String body) {
        this._id = _id;
        this.title = title;
        this.body = body;
        this.status = 0;
        this.date = "";
    }

    public Note(int _id, String title, String body, String date) {
        this._id = _id;
        this.title = title;
        this.body = body;
        this.status = 0;
        this.date = date;
    }

    public Note(int _id, String title, String body, int status){
        this._id = _id;
        this.title = title;
        this.body = body;
        this.status = status;
        this.date = "";
    }

    public Note(int _id, String title, String body, int status, String date){
        this._id = _id;
        this.title = title;
        this.body = body;
        this.status = status;
        this.date = date;
    }

    public Note(String title, String body, String date) {
        this(0, title, body, date);
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Note{" +
                "_id=" + _id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", status=" + status +
                ", date=" + date +
                '}';
    }
}
