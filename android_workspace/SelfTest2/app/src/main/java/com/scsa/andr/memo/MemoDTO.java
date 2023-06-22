package com.scsa.andr.memo;

import java.io.Serializable;

public class MemoDTO implements Serializable {
    private final String title;
    private final String content;
    private final String date;

    public MemoDTO(String title, String content, String date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }
}

