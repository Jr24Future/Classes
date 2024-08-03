package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecommendationActivity extends AppCompatActivity {
    private static final String M_I_URL = "http://coms-309-056.class.las.iastate.edu:8080/" + LoginActivity.UUID.replace("\"", "") + "/monthlyIncome";
    private static final String A_I_URL = "http://coms-309-056.class.las.iastate.edu:8080/" + LoginActivity.UUID.replace("\"", "") + "/annualIncome";
    private static final String BASE_URL = "http://coms-309-056.class.las.iastate.edu:8080/expenses/" + LoginActivity.UUID.replace("\"", "");
    private static final String Save_URL = "http://coms-309-056.class.las.iastate.edu:8080/recommendations/" + LoginActivity.UUID.replace("\"", "");
    private static final String URLEP = "https://api.openai.com/v1/chat/completions";
    private static final String APIKEY = "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";
    private TextView output;
    private double PBud, HBud, WBud, OBud;
    private Button save, Return, b, PrevRec;
    public static double monIncome; // Monthly income
    public static double annIncome; // Annual income

    private ArrayList<String> goals = new ArrayList<>();
    private boolean goalFound = false; // Track if a goal is found
    private double P, H, W, O; // Expense categories
    private String r; // Recommendation result string

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        save = findViewById(R.id.save_b);
        Return = findViewById(R.id.return_b);
        output = findViewById(R.id.output);
        b = findViewById(R.id.start);
        PrevRec = findViewById(R.id.previous_rec);

        save.setVisibility(View.GONE);

        PrevRec.setOnClickListener(v -> {
            Intent intent = new Intent(RecommendationActivity.this, PrevRecommendationActivity.class);
            startActivity(intent);
        });

        Return.setOnClickListener(v -> {
            Intent intent = new Intent(RecommendationActivity.this, MainActivity.class);
            startActivity(intent);
        });

        save.setOnClickListener(v -> {
            String recommendationtext = output.getText().toString();
            Log.d("recommend", recommendationtext);
            sendPostRequest(recommendationtext);
        });

        b.setOnClickListener(v -> fetchAllData());
    }

    private void fetchAllData() {
        GetMonIncomeRequest(() ->
                GetAnnIncomeRequest(() ->
                        GetRequest(() ->
                                GetBudgetsRequest(() ->
                                        FindGoals(() -> {
                                            String prompt = constructPrompt();
                                            Recommend(prompt);
                                        })
                                )
                        )
                )
        );
    }


    private String constructPrompt() {
        StringBuilder prompt = new StringBuilder("These are my expense amounts per month for the categories listed:\n" +
                "\n" +
                "Personal - " + P + "\n" +
                "Work - " + W + "\n" +
                "Home - " + H + "\n" +
                "Other - " + O + "\n" +
                "\n" +
                "This is my estimated annual income: " + annIncome + "\n" +
                "This is how much money I made this month: " + monIncome + "\n");

        if (goalFound) {
            prompt.append("\nThese are my financial goals:\n").append(ListAllGoals());
        }

        prompt.append("\nThese are my Budgeting Values: If my values are 0, don't involve them in the response you will make soon.\n")
                .append("personalBudget: ").append(PBud).append("\n")
                .append("workBudget: ").append(WBud).append("\n")
                .append("otherBudget: ").append(OBud).append("\n")
                .append("homeBudget: ").append(HBud).append("\n");

        prompt.append("\nWhat will be your recommendation on how much I should be spending with the amount I make monthly for the specific categories that I provided. Make sure to list my categories.\n")
                .append("\nCan you answer this question exactly with this template in a short paragraph:\n")
                .append("\nWelcome back user, Here is a recommendation I can provide you with:\n")
                .append("\n{Insert Recommendation Here}\n")
                .append("\nI hope this will help you with your future spendings. If you have any trouble, reach out to our Support Team");
        Log.e("Necati", String.valueOf(prompt));
        return prompt.toString();
    }

    private String ListAllGoals() {
        StringBuilder values = new StringBuilder();
        for (String goal : goals) {
            values.append(goal).append("\n");
        }
        return values.toString();
    }

    public void Recommend(String prompt) {
        try {
            JSONObject jsonObjectMessage = new JSONObject();
            jsonObjectMessage.put("role", "user");
            jsonObjectMessage.put("content", prompt);

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObjectMessage);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("model", "gpt-3.5-turbo");
            jsonObject.put("messages", jsonArray);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLEP, jsonObject, response -> {
                try {
                    r = response.getJSONArray("choices")
                            .getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content");
                    output.setText(r);
                    save.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    Log.e("ChatGPT Error", "JSON Exception", e);
                    Toast.makeText(RecommendationActivity.this, "JSON Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }, error -> {
                String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                Log.e("ChatGPT Error", "Error response: " + responseBody);
                Toast.makeText(RecommendationActivity.this, "Invalid: " + responseBody, Toast.LENGTH_LONG).show();
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> mapHeader = new HashMap<>();
                    mapHeader.put("Authorization", "Bearer " + APIKEY);
                    mapHeader.put("Content-Type", "application/json");
                    return mapHeader;
                }
            };

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);
        } catch (JSONException e) {
            Log.e("ChatGPT Error", "JSON Exception", e);
            Toast.makeText(RecommendationActivity.this, "JSON Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void GetRequest(final Runnable onSuccess) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL, null, response -> {
            try {
                P = response.getDouble("personal");
                W = response.getDouble("work");
                O = response.getDouble("other");
                H = response.getDouble("home");
                Log.d("Personal", String.valueOf(P));
                Log.d("Work", String.valueOf(W));
                Log.d("Other", String.valueOf(O));
                Log.d("Home", String.valueOf(H));
                onSuccess.run();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> Toast.makeText(RecommendationActivity.this, "Get Error: " + error.toString(), Toast.LENGTH_LONG).show());

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void GetAnnIncomeRequest(final Runnable onSuccess) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                A_I_URL,
                response -> {
                    annIncome = Double.parseDouble(response);
                    onSuccess.run();
                },
                error -> Toast.makeText(RecommendationActivity.this, "Get Error: " + error.toString(), Toast.LENGTH_LONG).show()
        );
        queue.add(stringRequest);
    }

    private void GetMonIncomeRequest(final Runnable onSuccess) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                M_I_URL,
                response -> {
                    monIncome = Double.parseDouble(response);
                    onSuccess.run();
                },
                error -> Toast.makeText(RecommendationActivity.this, "Get Error: " + error.toString(), Toast.LENGTH_LONG).show()
        );
        queue.add(stringRequest);
    }

    private void sendPostRequest(String recommendtext) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Save_URL,
                response -> Toast.makeText(RecommendationActivity.this, "Success", Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(RecommendationActivity.this, "Error: " + error.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            public byte[] getBody() {
                return recommendtext.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "text/plain; charset=utf-8");
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void FindGoals(Runnable onSuccess) {
        String URL = "http://coms-309-056.class.las.iastate.edu:8080/goals/" + LoginActivity.UUID.replace("\"", "");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, response -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);
                    String goalString = jsonObject.getString("goalString");
                    goals.add(goalString);
                    goalFound = true; // Set flag if any goals are found
                }
                onSuccess.run();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(RecommendationActivity.this, "Get Error: " + error.toString(), Toast.LENGTH_LONG).show());

        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }


    private void GetBudgetsRequest(final Runnable onSuccess) {
        String url = "http://coms-309-056.class.las.iastate.edu:8080/budget/" + LoginActivity.UUID.replace("\"", "");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                PBud = response.getDouble("personalLimit");
                WBud = response.getDouble("workLimit");
                HBud = response.getDouble("homeLimit");
                OBud = response.getDouble("otherLimit");
                onSuccess.run();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(RecommendationActivity.this, "Failed to parse budgets data", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            error.printStackTrace();
            Toast.makeText(RecommendationActivity.this, "Failed to fetch budgets data", Toast.LENGTH_SHORT).show();
        });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    public void Recommend(View view) {
    }
}
