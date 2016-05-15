package com.example.lcarrasco.hello;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lcarrasco on 5/12/16.
 */
public class Note implements Serializable{

    private String title;
    private String note;
    private Date date;

    public Note(String title, String note, Date date) {
        this.date = date;
        this.note = note;
        this.title = title;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
