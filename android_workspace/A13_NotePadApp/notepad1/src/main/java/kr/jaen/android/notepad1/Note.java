package kr.jaen.android.notepad1;

import java.io.Serializable;

public class Note implements Serializable {

    private int _id;  // 식별자
    private String title;  // 제목
    private String body;  // 내용

    public Note() {}

    public Note(int _id, String title, String body) {
        this._id = _id;
        this.title = title;
        this.body = body;
    }

    public Note(String title, String body) {
        this(0, title, body);
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Note{" +
                "_id=" + _id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
