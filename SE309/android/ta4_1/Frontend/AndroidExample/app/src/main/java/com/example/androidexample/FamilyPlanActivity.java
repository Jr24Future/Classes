package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FamilyPlanActivity extends AppCompatActivity {

    private String fam_URL = "http://coms-309-056.class.las.iastate.edu:8080/family/addMember/" + LoginActivity.UUID.replace("\"", "");
    private EditText fn, ln, m, a;
    private Button confirm;

    public static String famID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_plan);

        fn = findViewById(R.id.fam_first);
        ln = findViewById(R.id.fam_last);
        m = findViewById(R.id.fam_mon);
        a = findViewById(R.id.fam_ann);

        confirm = findViewById(R.id.btn_confirm);

        confirm.setOnClickListener(v -> {
            try {
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("firstName", fn.getText().toString());
                jsonBody.put("lastName", ln.getText().toString());
                jsonBody.put("monthlyIncome", m.getText().toString());
                jsonBody.put("annualIncome", a.getText().toString());

                sendPostRequest(jsonBody);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void setInitialPortfolio(UUID userId) {
        String url = "http://coms-309-056.class.las.iastate.edu:8080/portfolio/" + userId.toString();
        JSONObject postData = new JSONObject();
        try {

            postData.put("AAPLShares", 0.0);
            postData.put("AMZNShares", 0.0);
            postData.put("BTCUSDTShares", 0.0);
            postData.put("DOGEUSDTShares", 0.0);
            postData.put("AAPLPrice", 0.0);
            postData.put("AMZNPrice", 0.0);
            postData.put("BTCUSDTPrice", 0.0);
            postData.put("DOGEUSDTPrice", 0.0);
            postData.put("portfoliovalue", 0.0);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("InitialExpenses", "Portfolio set");
                    // Automatically log in the user after successful signup and initial expenses setup
                },
                error -> Log.e("InitialExpenses", "Error: " + error.getMessage())) {
            @Override
            public byte[] getBody() {
                return postData.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void sendInitialBudget(UUID userId) {
        String url = "http://coms-309-056.class.las.iastate.edu:8080/budget/" + userId.toString();
        JSONObject postData = new JSONObject();
        try {
            postData.put("personalLimit", 1000.0); // Default budget for personal expenses
            postData.put("workLimit", 1000.0);     // Default budget for work-related expenses
            postData.put("homeLimit", 1000.0);     // Default budget for home expenses
            postData.put("otherLimit", 1000.0);    // Default budget for other expenses
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> Log.d("InitialBudget", "Budget set successfully"),
                error -> Log.e("InitialBudget", "Error setting initial budget: " + error.getMessage())) {
            @Override
            public byte[] getBody() {
                return postData.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        // Add the request to the RequestQueue.
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void sendPostRequest(JSONObject jsonBody) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, fam_URL,
                response -> {
                    famID = response.replace("\"", "");  // Remove any double quotes from the response
                    UUID userId = UUID.fromString(famID);
                    setInitialPortfolio(userId);
                    sendInitialBudget(userId);
                    // Redirect to AddFamilyExpensesActivity
                    Intent intent = new Intent(FamilyPlanActivity.this, AddFamilyExpensesActivity.class);
                    startActivity(intent);
                },
                error -> Toast.makeText(FamilyPlanActivity.this, "Error: " + error.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            public byte[] getBody() {
                return jsonBody.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
