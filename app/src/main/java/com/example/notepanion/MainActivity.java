package com.example.notepanion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText usernameTextField, passwordTextField;
    Button loginBtn;
    private String URL = "https://metalraiders.com/notepanion/login.php";

    // http://10.0.2.2:xxxx is the IP that is a special alias to your host loopback interface (127.0.0.1 on your dev machine)
    // Reference: https://stackoverflow.com/questions/54810579/com-android-volley-noconnectionerror-java-net-connectexception-connection-refu
    // On internet connection error: check URL, reinstall app, make sure internet permission and access network state
//    private String URL = "http://10.0.2.2/androidStudioMySqlDemo/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameTextField = findViewById(R.id.unTxt);
        passwordTextField = findViewById(R.id.pwTxt);
        loginBtn = findViewById(R.id.loginBtn);
    }

    public void Login(View view) {
        Log.i("USERNAME", usernameTextField.getText().toString());
        Log.i("PASSWORD", passwordTextField.getText().toString());

        String username = usernameTextField.getText().toString(), password = passwordTextField.getText().toString();

        if (!username.equals("") && !password.equals("")) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (!response.equals("failure")) {
                        Log.d("res", response);
                        JSONArray array = null;
                        try {
                            array = new JSONArray(response);
                            JSONObject row = new JSONObject(array.get(0).toString());

                            int id = row.getInt("id");
                            new User(id, username);

                            Intent intent = new Intent(MainActivity.this, NotesActivity.class);
                        startActivity(intent);
                        finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("EXCEPTION", e.toString());
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid Login Id/Password", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    Log.i("MESSAGE", message);
                    Log.i("ERROR", error.toString());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("email", username);
                    data.put("password", password);
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
        }
    }

    // Handling states: https://developer.android.com/guide/components/activities/state-changes
}