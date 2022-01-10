package com.example.notepanion;

public class NoteRow {
    private int id;
    private String title, description;

    public NoteRow(int i, String t, String d){
            id = i;
            title = t;
            description = d;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }
}
