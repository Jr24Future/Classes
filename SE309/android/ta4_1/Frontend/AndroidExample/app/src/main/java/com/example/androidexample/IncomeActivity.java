package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Collections;

public class IncomeActivity extends AppCompatActivity {

    private static final String INCOME_PATCH_URL = "http://coms-309-056.class.las.iastate.edu:8080/users/" + MainActivity.selectedMemberId + "/income";

    private static final String INCOME_M_URL = "http://coms-309-056.class.las.iastate.edu:8080/users/" + MainActivity.selectedMemberId + "/income";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        final EditText etMonthlyIncome = findViewById(R.id.etMonthlyIncome);
        Button btnSubmitIncome = findViewById(R.id.btnSubmitIncome);

        btnSubmitIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String monthlyIncome = etMonthlyIncome.getText().toString();
                try {
                    double income = Double.parseDouble(monthlyIncome);
                    sendIncomeData(income);
                } catch (NumberFormatException e) {
                    Toast.makeText(IncomeActivity.this, "Please enter a valid income", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * This methods takes in a double "Monthly Income" and uses the Patch method to update the income value
     * @param monthlyIncome
     */

    private void sendIncomeData(final double monthlyIncome) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PATCH,
                INCOME_PATCH_URL,
                new JSONObject(Collections.singletonMap("income", monthlyIncome)),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Navigate to MainActivity after successful PATCH request
                        startActivity(new Intent(IncomeActivity.this, LoginActivity.class));
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(IncomeActivity.this, "Failed to update income", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        queue.add(jsonObjectRequest);
    }
}