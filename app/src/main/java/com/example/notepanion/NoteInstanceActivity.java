package com.example.notepanion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class NoteInstanceActivity extends AppCompatActivity {
    User user = User.getInstance();
    EditText titleText, descriptionText;
    int position = 0;
    String saveNoteURL = "https://metalraiders.com/notepanion/saveNote.php", closeNoteURL = "https://metalraiders.com/notepanion/closeNote.php", title = "";
    NoteRow noteRow = new NoteRow(0, "", "");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_instance);
        titleText = findViewById(R.id.niTitle);
        descriptionText = findViewById(R.id.niDes);


        try {

            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            position = extras.getInt("position");
            title = extras.getString("title");

            noteRow = user.getNote(title);

            titleText.setText(noteRow.getTitle());
            descriptionText.setText(noteRow.getDescription());

            Log.d("POSITION", String.valueOf(position));
            Log.d("TITLE", title);
            Log.d("ID", String.valueOf(noteRow.getId()));
            Log.d("DESCRIPTION", noteRow.getDescription());
        } catch (Exception e) {

        }

        if (noteRow.getId() == 0) {
            LinearLayout ll = findViewById(R.id.niBtnLayout);
            ll.removeView(findViewById(R.id.niDoneBtn));
        }

    }

    public void Save(View view) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, saveNoteURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("res", response);
                if (!response.contains("failure")) {
                    Toast.makeText(NoteInstanceActivity.this, "Saved successfully", Toast.LENGTH_SHORT).show();
                    if (noteRow.getId() == 0) {
                        Intent intent = new Intent(NoteInstanceActivity.this, NotesActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    if (response.contains("duplicate")) {
                        Toast.makeText(NoteInstanceActivity.this, "Note titles must be unique.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Toast.makeText(NoteInstanceActivity.this, message, Toast.LENGTH_SHORT).show();
                Log.i("MESSAGE", message);
                Log.i("ERROR", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                Log.d("POSITION", String.valueOf(position));
                Log.d("TITLE", title);
                Log.d("ID", String.valueOf(noteRow.getId()));


                Log.i("NOTE IS DUPLICATE", String.valueOf(User.getInstance().noteIsDuplicate(noteRow)));
                if (User.getInstance().noteIsDuplicate(new NoteRow(noteRow.getId(), titleText.getText().toString(), descriptionText.getText().toString()))) {
                    data.put("title_is_duplicate", String.valueOf(true));
                    return data;
                }
//
                noteRow.setTitle(titleText.getText().toString());
                noteRow.setDescription(descriptionText.getText().toString());
                data.put("id", String.valueOf(noteRow.getId()));
                data.put("user_id", String.valueOf(user.getId()));
                data.put("title", titleText.getText().toString());
                data.put("description", descriptionText.getText().toString());
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void CloseNote(View view) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, closeNoteURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("CLOSE NOTE ON RESPONSE", response);
                if (response.equals("success")) {
                    Intent intent = new Intent(NoteInstanceActivity.this, NotesActivity.class);
                    startActivity(intent);
                    finish();
                } else if (response.equals("failure")) {
                    Toast.makeText(NoteInstanceActivity.this, "Invalid Login Id/Password", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Toast.makeText(NoteInstanceActivity.this, message, Toast.LENGTH_SHORT).show();
                Log.i("MESSAGE", message);
                Log.i("ERROR", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                Log.d("POSITION", String.valueOf(position));
                Log.d("TITLE", title);
                Log.d("ID", String.valueOf(noteRow.getId()));
//
                data.put("id", String.valueOf(noteRow.getId()));
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onStop() {
        System.out.println("ON STOP");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        System.out.println("ON DESTROY");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        System.out.println("ON PAUSE");
//        Intent intent = new Intent(NoteInstance.this, Notes.class);
//        startActivity(intent);
//        finish();
        super.onPause();
    }
}