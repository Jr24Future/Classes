package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class Goal_Add_Activity2 extends AppCompatActivity {

    private Button redirect;
    private TextView prompt_d, e_amt, e_Mon;
    private String category;
    private String prompt_response;

    private boolean TaskComplete = false;

        private String URL = "http://coms-309-056.class.las.iastate.edu:8080/goals/" + LoginActivity.UUID.replace("\"", "");

    private String amt, days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_add_2);

        category = Goal_Add_Activity1.selectedGoalCategory;
        prompt_d = (TextView) findViewById(R.id.prompt_display);

        switch (category) {
            case "savings":
                prompt_d.setText("I want to save [amount] in the next [timeframe] days.");
                break;
            case "investment":
                prompt_d.setText("I aim to invest [amount] over the course of [timeframe] days.");
                break;
            case "vacation":
                prompt_d.setText("I plan to save [amount] every [timeframe] days for a vacation.");
                break;
            case "education":
                prompt_d.setText("I will save [amount] every [timeframe] days for education costs.");
                break;
            case "charity":
                prompt_d.setText("I plan to donate [amount] every [timeframe] days to a chosen charity.");
                break;
            case "lifestyle":
                prompt_d.setText("For lifestyle upgrades, I will allocate [amount] every [timeframe] days.");
                break;
        }

        e_amt = findViewById(R.id.editAmt);
        e_Mon = findViewById(R.id.editMon);




        redirect = (Button) findViewById(R.id.set_goal);

        redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amt = e_amt.getText().toString();
                days = e_Mon.getText().toString();

                switch (category) {
                    case "savings":
                        prompt_response = "I want to save " + amt + " in the next " + days + " days.";
                        break;
                    case "investment":
                        prompt_response = "I aim to invest " + amt + " over the course of " + days + " days.";
                        break;
                    case "vacation":
                        prompt_response = "I plan to save " + amt + " every " + days + " days for a vacation.";
                        break;
                    case "education":
                        prompt_response = "I will save " + amt + " every " + days + " days for education costs.";
                        break;
                    case "charity":
                        prompt_response = "I plan to donate " + amt + " every " + days + " days to a chosen charity.";
                        break;
                    case "lifestyle":
                        prompt_response = "For lifestyle upgrades, I will allocate " + amt + " every " + days + " days.";
                        break;
                }
                TaskComplete = false;
                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("goalString", prompt_response);
                    jsonObject.put("amount", Double.parseDouble(amt));
                    jsonObject.put("Totalamount", Double.parseDouble(amt));
                    jsonObject.put("timeFrame", Integer.parseInt(days));
                    jsonObject.put("isCompleted", TaskComplete);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                sendPostRequest(jsonObject);
            }
        });
    }

    private void sendPostRequest(JSONObject json) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, json,
                response -> {
                    Toast.makeText(Goal_Add_Activity2.this, "Goal Created!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Goal_Add_Activity2.this, GoalListActivity.class));
                },
                error -> {
                    Toast.makeText(Goal_Add_Activity2.this, "Failed: " + error.toString(), Toast.LENGTH_LONG).show();
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
    }

}
