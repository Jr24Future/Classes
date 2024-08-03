package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BudgetingAddActivity extends AppCompatActivity {
    private Spinner spinnerCat;
    private Button CreateBudget;

    private EditText Pin, Win, Oin, Hin;

    private double P, W, O, H;

    private String SelectedCat;

    private final String URL = "http://coms-309-056.class.las.iastate.edu:8080/budget/"+ MainActivity.selectedMemberId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgeting_add);
        GetRequest();
        CreateBudget = (Button) findViewById(R.id.budg_create_btn);
        Hin = (EditText) findViewById(R.id.Home_Lim);
        Pin = (EditText)findViewById(R.id.personal_lim);
        Win = (EditText) findViewById(R.id.Work_Lim);
        Oin = (EditText) findViewById(R.id.Other_Lim);







        CreateBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPostRequest();
            }
        });
    }

    private void sendPostRequest() {

        JSONObject json = new JSONObject();
        try {
                    json.put("personalLimit", Double.parseDouble(Pin.getText().toString()));
                    json.put("workLimit", Double.parseDouble(Win.getText().toString()));
                    json.put("homeLimit", Double.parseDouble(Hin.getText().toString()));
                    json.put("otherLimit", Double.parseDouble(Oin.getText().toString()));

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, json,
                    response -> {
                        Toast.makeText(BudgetingAddActivity.this, "Budget Set!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(BudgetingAddActivity.this, BudgetingActivity.class));
                    },
                    error -> {
                        Toast.makeText(BudgetingAddActivity.this, "Failed: " + error.toString(), Toast.LENGTH_LONG).show();
                        error.printStackTrace(); // Print error details for debugging
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            // Add the request to the RequestQueue
            Volley.newRequestQueue(this).add(jsonObjectRequest);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    private void GetRequest() {
        String url = "http://coms-309-056.class.las.iastate.edu:8080/budget/" + MainActivity.selectedMemberId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                P = response.getDouble("personalLimit");
                W = response.getDouble("workLimit");
                H = response.getDouble("homeLimit");
                O = response.getDouble("otherLimit");

                // Update EditText fields after fetching values
                Pin.setText(Double.toString(P));
                Hin.setText(Double.toString(H));
                Win.setText(Double.toString(W));
                Oin.setText(Double.toString(O));
            } catch (JSONException e) {
                e.printStackTrace(); // Instead of throwing a runtime exception, just print the error
            }
        }, error -> {
            Toast.makeText(BudgetingAddActivity.this, "Failed: " + error.toString(), Toast.LENGTH_LONG).show();
            error.printStackTrace(); // Print error details for debugging
        });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }










}
