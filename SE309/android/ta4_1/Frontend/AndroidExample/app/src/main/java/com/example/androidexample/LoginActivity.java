package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    public String username;
    public String password;
    public Button loginButton;
    public String check;
    private Button signupButton;
    private static final String URL = "http://coms-309-056.class.las.iastate.edu:8080/login";

    public static String UUID;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.login_username_edt);
        passwordEditText = findViewById(R.id.login_password_edt);
        loginButton = findViewById(R.id.login_login_btn);
        signupButton = findViewById(R.id.login_signup_btn);

        loginButton.setOnClickListener(v -> {
             username = usernameEditText.getText().toString();
             password = passwordEditText.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill out all fields.", Toast.LENGTH_LONG).show();
            } else {
                check = "Success";
                sendPostRequest(username, password);
            }
        });

        signupButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Takes a user's Email and Password and Checks to see if both email and password match in the database
     * @param email
     * @param password
     */
    private void sendPostRequest(String email, String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL + "?email=" + email + "&password=" + password,
                response -> {
                    Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_LONG).show();
                    UUID = response;
                    GetUserTypeRequest();
                    Log.d("UUID", UUID + " is the ID!");
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                },
                error -> Toast.makeText(LoginActivity.this, "Login Failed, please try again", Toast.LENGTH_LONG).show());

        Volley.newRequestQueue(this).add(stringRequest);
    }


    private void GetUserTypeRequest() {
        if (UUID != null) {
            final String type_URL = "http://coms-309-056.class.las.iastate.edu:8080/userType/" + UUID.replace("\"", "");
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(
                    Request.Method.GET,
                    type_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("usertype", response);
                            MainActivity.userType = response;

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle the error
                            Toast.makeText(LoginActivity.this, "Error: " + error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
            );
            queue.add(stringRequest);
        }
    }
}