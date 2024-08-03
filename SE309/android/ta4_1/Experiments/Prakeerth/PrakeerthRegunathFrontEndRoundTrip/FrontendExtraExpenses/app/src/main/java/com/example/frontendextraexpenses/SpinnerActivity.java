package com.example.frontendextraexpenses;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SpinnerActivity extends AppCompatActivity {
    private TextView Expense;

    private static final String url = "http://coms-309-056.class.las.iastate.edu:8080/expenses/5165496b-3a9b-4da6-a9b8-2ac52b89a206";

    private EditText Input;

    private double p,w,h,o,in;
    private String title;

    RequestQueue requestQueue;

    private String value;


    /**
     * 
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);
        Bundle extra = getIntent().getExtras();

        Expense = findViewById(R.id.textView9);
        Input =findViewById(R.id.editTextText);

        Input.setText("");

        if(extra != null) {
            title = extra.getString("Selection");
        }
        Expense.setText("Enter your extra " + title + " Expenses:");

        Input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                in = Double.parseDouble(Input.getText().toString());
                sendPutRequest();
                Intent intent = new Intent(SpinnerActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });




    }
    private void sendPutRequest()
    {
        try {


            JSONObject requestBody = new JSONObject();
            requestBody.put(title, in);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, requestBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        switch (title) {
                            case "personal":
                                response.put("personal", in);
                                break;
                            case "work":
                                response.put("work", in);
                                break;
                            case "other":
                                response.put("other", in);
                                break;
                            case "home":
                                response.put("home", in);
                                break;
                        }


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonObjectRequest);
        }
        catch(JSONException e)
        {
            throw new RuntimeException(e);
        }

    }


}
