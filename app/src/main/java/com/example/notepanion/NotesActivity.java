package com.example.notepanion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotesActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    User user = User.getInstance();
    String URL = "https://metalraiders.com/notepanion/getNotes.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes);

        linearLayout = findViewById(R.id.notesLayout);

        getNotes();
    }

    private void getNotes() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("NOTES", response);
                if (!response.equals("failure")) {
                    JSONArray array = null;

                    ArrayList<String> tutorials = new ArrayList<>();
                    try {
                        array = new JSONArray(response);
//                            Log.d("OBJ", array.get(0).toString());
//
//                            JSONObject obj = new JSONObject(array.get(0).toString());
//                            Log.d("OBJ", obj.getString("title"));

                        ArrayList<NoteRow> notes = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject note = new JSONObject(array.get(i).toString());

                            int id = note.getInt("id");
                            String title = note.getString("title");
                            String description = note.getString("description");

                            NoteRow nr = new NoteRow(id, title, description);
                            notes.add(nr);

                            tutorials.add(title);
                        }
                        user.addNoteRows(notes);
                        Log.d("TUTORIALS", tutorials.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("EXCEPTION", e.toString());
                    }

                    ListView simpleList = (ListView) findViewById(R.id.notesListView);
                    ArrayAdapter<String> arr;
                    arr = new ArrayAdapter<String>(NotesActivity.this, R.layout.support_simple_spinner_dropdown_item, tutorials);
                    simpleList.setAdapter(arr);
                    simpleList.setOnItemClickListener(new ListViewListener());

                } else
                    Toast.makeText(NotesActivity.this, "Invalid Login Id/Password", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(NotesActivity.this, message, Toast.LENGTH_SHORT).show();
                Log.i("MESSAGE", message);
                Log.i("ERROR", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("email", User.getInstance().getUsername());
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private class ListViewListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Log.i("ON ITEM CLICK", "Position = " + position);

            Intent intent = new Intent(NotesActivity.this, NoteInstanceActivity.class);
            intent.putExtra("position", position);
            intent.putExtra("title", adapterView.getItemAtPosition(position).toString());
            startActivity(intent);
        }
    }

    public void NewNote(View view) {
        Intent intent = new Intent(NotesActivity.this, NoteInstanceActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        System.out.println("ON STOP");

        super.onStop();
    }

    @Override
    protected void onResume() {
        System.out.println("ON RESUME");
        getNotes();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        System.out.println("ON RESTART");

        super.onRestart();
    }
}
