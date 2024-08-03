package com.example.testgpt;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import java.nio.charset.StandardCharsets;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity{

<<<<<<< HEAD:Experiments/Prakeerth/PKTEST/app/src/main/java/com/example/testgpt/MainActivity.java
    public static String UUID;
    private static final String IncomeURL = "http://coms-309-056.class.las.iastate.edu:8080/users/";
    private static  final String ExpensesURL = "http://coms-309-056.class.las.iastate.edu:8080/expenses/5165496b-3a9b-4da6-a9b8-2ac52b89a206";
    private String URLEP = "https://api.openai.com/v1/chat/completions";
    private static final String PostURL = "http://coms-309-056.class.las.iastate.edu:8080/";

=======

    private static final String URL = "http://coms-309-056.class.las.iastate.edu:8080/expenses/2810aec4-4257-457d-8d62-dfb872260412";
    private static String userBudURL = "http://coms-309-056.class.las.iastate.edu:8080/2810aec4-4257-457d-8d62-dfb872260412/financial-report";
    private String URLEP = "https://api.openai.com/v1/chat/completions";
>>>>>>> origin/main:Experiments/Prakeerth/Recommendation/app/src/main/java/com/example/testgpt/MainActivity.java
    private String APIKEY = "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";
    private TextView output;
    private double P, W, H, O;



    private double AnnIncome;
    private double MonIncome;
    private double Debt;


    private Button save, Return;

    public static double budget; // budget
    public static double totalexp; // total expenses
    public static double annIncome; // Annual icome


    public double P,H,W,O;
    private Button b;
    public String r;

    public String N;
    
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        save = (Button) findViewById(R.id.save_b);
        Return = (Button) findViewById(R.id.return_b);
        output = (TextView) findViewById(R.id.output);
        b = (Button) findViewById(R.id.start);

        save.setVisibility(View.GONE);
        Return.setVisibility(View.GONE);




        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
<<<<<<< HEAD:Experiments/Prakeerth/PKTEST/app/src/main/java/com/example/testgpt/MainActivity.java
               // GetExpensesRequest();
               // GetIncomeRequest();

                String s = "These are my spending amounts per month per category:\n" +
                        "\n" +
                        "Personal - "+"2000"+"\n" +
                        "Work - "+"5000"+"\n" +
                        "Home - "+"3000"+"\n" +
                        "Other - "+"4000"+"\n" +
                        "\n" +
                        "This is my estimated annual income: 10,000\n" +
                        "This is how much money I made this month: "+"5000"+"\n" +
                        "\n" +
=======
                GetRequest();
                GetBudgetRequest();
                annIncome = totalexp + budget;

                String s = "These are my spending amounts per month per category:\n" +
                        "\n" +
                        "Personal - "+P+"\n" +
                        "Work - "+W+"\n" +
                        "Home - "+H+"\n" +
                        "Other - "+O+"\n" +
                        "\n" +
                        "This is my estimated annual income: "+annIncome+"\n" +
                        "This is how much money I made this month: "+budget+"\n" +
                        "In Total, this is how much money I went in debt this month : 10,000\n" +
>>>>>>> origin/main:Experiments/Prakeerth/Recommendation/app/src/main/java/com/example/testgpt/MainActivity.java
                        "\n" +
                        "What will be your recommendation on how much I should be spending with the amount I make monthly for the specific categories that I provided. Make sure to list my categories.\n" +
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
                    Toast.makeText(MainActivity.this, "Generating Message", Toast.LENGTH_LONG).show();
                    try {
                        r = response.getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");
                        // Update the TextView on UI thread
                        output.setText(r);
                        save.setVisibility(View.VISIBLE);

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

    private void GetExpensesRequest() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ExpensesURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(MainActivity.this, "Login response: " + response.toString(), Toast.LENGTH_LONG).show();
                try {
                    UUID = response.getString("id");
                    P = response.getDouble("personal");
                    W = response.getDouble("work");
                    O = response.getDouble("other");
                    H = response.getDouble("home");





                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Get Error: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);


    }

    private void GetIncomeRequest() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, IncomeURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(MainActivity.this, "Login response: " + response.toString(), Toast.LENGTH_LONG).show();
                try {
                    UUID = response.getString("id");
                    MonIncome = response.getDouble("income");





                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Get Error: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);


    }

//    private void sendPostRequest(JSONObject jsonBody) {
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Toast.makeText(MainActivity.this, "Response: " + response, Toast.LENGTH_LONG).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(MainActivity.this, "Error: " + error.toString(), Toast.LENGTH_LONG).show();
//                    }
//                }) {
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                return jsonBody.toString().getBytes();
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json; charset=utf-8");
//                return headers;
//            }
//        };
//        Volley.newRequestQueue(this).add(stringRequest);
//    }










    private void GetRequest() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    P = response.getDouble("personal");
                    W = response.getDouble("work");
                    O = response.getDouble("other");
                    H = response.getDouble("home");
                    totalexp = response.getDouble("totalExpenses");


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Get Error: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);


    }


    private void GetBudgetRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                userBudURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        budget = Double.parseDouble(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        Toast.makeText(MainActivity.this, "Get Error: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        queue.add(stringRequest);
    }


    }


