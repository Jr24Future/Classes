package com.example.testgpt;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.content.Intent;
import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.io.IOException;
import com.android.volley.*;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity{


    private static final String URL = "http://coms-309-056.class.las.iastate.edu:8080/login";
    private String URLEP = "https://api.openai.com/v1/chat/completions";

    private String APIKEY = "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";

    private TextView output;

    private Button b;
    
    public String r;
    
    protected void onCreate(Bundle savedInstanceState){


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        output = (TextView) findViewById(R.id.output);
        b = (Button) findViewById(R.id.start);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "These are my spending amounts per month per category:\n" +
                        "\n" +
                        "Personal - 2000\n" +
                        "Work - 5000\n" +
                        "Home - 12000\n" +
                        "Other - 2500\n" +
                        "\n" +
                        "This is my estimated annual income: 10,000\n" +
                        "This is how much money I made this month: 208,000\n" +
                        "In Total, this is how much money I went in debt this month : 10,000\n" +
                        "\n" +
                        "What will be your recommendation on how much I should be spending with the amount I make monthly for the specific categories that I provided.\n" +
                        "\n" +
                        "Can you answer this question exactly with this template in a short paragraph:\n" +
                        "\n" +
                        "Welcome back user, Here is a recommendation I can provide you with:\n" +
                        "\n" +
                        "{Insert Recommendation Here}\n" +
                        "\n" +
                        "\n" +
                        "I hope this will help you with your future spendings. If you have any trouble, reach out to our Support Team";

                Recommend(s);
            }
        });

    }



    public void Recommend(String prompt) {
        try {
            JSONObject jsonObjectMessage = new JSONObject();
            jsonObjectMessage.put("role", "user");
            jsonObjectMessage.put("content", prompt);

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObjectMessage);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("model", "gpt-3.5-turbo-0125");
            jsonObject.put("messages", jsonArray);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLEP, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_LONG).show();
                    try {
                        r = response.getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");
                        // Update the TextView on UI thread
                        output.setText(r);
                    } catch (JSONException e) {
                        Log.e("ChatGPT Error", "JSON Exception", e);
                        Toast.makeText(MainActivity.this, "JSON Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    Log.e("ChatGPT Error", "Error response: " + responseBody);
                    Toast.makeText(MainActivity.this, "Invalid: " + responseBody, Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> mapHeader = new HashMap<>();
                    mapHeader.put("Authorization", "Bearer "+APIKEY);
                    mapHeader.put("Content-Type", "application/json");
                    return mapHeader;
                }

                @Override
                protected VolleyError parseNetworkError(VolleyError volleyError) {
                    if (volleyError.networkResponse != null) {
                        String errorResponse = new String(volleyError.networkResponse.data, StandardCharsets.UTF_8);
                        Log.e("NetworkError", "Error response: " + errorResponse);
                    }
                    return super.parseNetworkError(volleyError);
                }
            };

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
        } catch (JSONException e) {
            Log.e("ChatGPT Error", "JSON Exception", e);
            Toast.makeText(MainActivity.this, "JSON Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }





    }
