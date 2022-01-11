package com.example.notepanion;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static User instance = null;

    private int id;
    private String username;
    private ArrayList<NoteRow> noteRows = new ArrayList<NoteRow>();

    // Constructor
    public User(int i, String u) {
        if (instance == null) {
            id = i;
            username = u;

            instance = this;
        }
    }

    public String getUsername() {
        return username;
    }

    public void addNoteRows(ArrayList<NoteRow> nrs) {
        noteRows.clear();
        noteRows = nrs;
    }

    public NoteRow getNote(String title) {

        for (int i = 0; i < noteRows.size(); i++) {
            if (noteRows.get(i).getTitle().equals(title))
                return noteRows.get(i);
        }
        return null;
    }

    public static User getInstance() {
        return instance;
    }

    public int getId() {
        return id;
    }

    public boolean noteIsDuplicate(NoteRow nr){
        for (int i = 0; i < noteRows.size(); i++) {
            Log.i("NOTE IS DUPLICATE", noteRows.get(i).getTitle() + nr.getTitle());
            Log.i("NOTE IS DUPLICATE", String.valueOf(noteRows.get(i).getId()) + String.valueOf(nr.getId()));
            if (noteRows.get(i).getTitle().equals(nr.getTitle()) && noteRows.get(i).getId() != nr.getId())
                return true;
        }
        return false;
    }
}
