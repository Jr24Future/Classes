package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String POST_URL = "http://coms-309-056.class.las.iastate.edu:8080/expenses/" + LoginActivity.UUID.replace("\"", "");
    private static final String GET_URL = "http://coms-309-056.class.las.iastate.edu:8080/" + LoginActivity.UUID.replace("\"", "") + "/financial-report";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);

        try {
            Map<String, Integer> categories = readAndSumCategories();
            List<Map.Entry<String, Integer>> sortedCategories = new ArrayList<>(categories.entrySet());
            Collections.sort(sortedCategories, (a, b) -> b.getValue().compareTo(a.getValue()));

            StringBuilder sortedText = new StringBuilder();
            for (Map.Entry<String, Integer> entry : sortedCategories) {
                sortedText.append(entry.getKey()).append(" ").append(entry.getValue()).append("\n");
            }
            textView.setText(sortedText.toString());

            sendPostRequest(categories);
        } catch (IOException e) {
            e.printStackTrace();
            textView.setText("Error reading file");
        }
    }

    private Map<String, Integer> readAndSumCategories() throws IOException {
        Map<String, Integer> categories = new HashMap<>();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(getAssets().open("yourfile.txt")));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            if (parts.length == 2) {
                String category = parts[0].trim();
                int number = Integer.parseInt(parts[1].trim());
                categories.put(category, categories.getOrDefault(category, 0) + number);
            }
        }
        reader.close();

        return categories;
    }

    private void sendPostRequest(Map<String, Integer> categories) {
        RequestQueue queue = Volley.newRequestQueue(this);
        try {
            JSONObject jsonBody = new JSONObject();
            for (Map.Entry<String, Integer> entry : categories.entrySet()) {
                // Convert integer values to double
                jsonBody.put(entry.getKey(), Double.valueOf(entry.getValue()));
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    POST_URL,
                    jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("POST RESPONSE", response.toString());
                            fetchFinancialReport();
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("POST ERROR", error.toString());
                        }
                    }
            );
            queue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fetchFinancialReport() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        double value = Double.parseDouble(response);
                        View rootView = getWindow().getDecorView().getRootView();
                        if (value < 0) {
                            rootView.setBackgroundColor(Color.RED);
                        } else if (value > 200) {
                            rootView.setBackgroundColor(Color.GREEN);
                        } else {
                            rootView.setBackgroundColor(Color.YELLOW);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                    }
                }
        );
        queue.add(stringRequest);
    }
}