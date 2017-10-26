package com.watchmecoding.eazynote.data;

import java.util.Calendar;

public class NoteItem {

    private String key;
    private String text;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static NoteItem getNew() {
        NoteItem note = new NoteItem();
        note.setKey(String.valueOf(Calendar.getInstance().getTimeInMillis()));
        note.setText("");
        note.setTitle("");
        return note;
    }

    @Override
    public String toString() {
        return this.getText();
    }
}
