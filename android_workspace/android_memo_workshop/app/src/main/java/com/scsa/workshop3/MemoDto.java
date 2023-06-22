package com.scsa.workshop3;

import java.io.Serializable;


public class MemoDto implements Serializable { //serializable 해여 activity 사이로 정보를 주고받을 수 있음
    private String title;
    private String contents;
    private String date;

    public MemoDto(String title, String contents, String date) {
        this.title = title;
        this.contents = contents;
        this.date = date;
    }
    public MemoDto(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "MemoDto{" +
                "title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
