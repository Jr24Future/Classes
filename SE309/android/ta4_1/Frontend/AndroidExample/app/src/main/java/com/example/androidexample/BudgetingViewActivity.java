package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

public class BudgetingViewActivity extends AppCompatActivity {

    private double P, W, H, O;
    private double PBud, WBud, HBud, OBud;
    private int Personal, Work, Home, Other;
    private TextView Pin, Win, Oin, Hin;
    private Button backhome;
    private ProgressBar pProgress, wProgress, hProgress, oProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgeting_view);

        // Initialize views
        Hin = findViewById(R.id.HomLim);
        Pin = findViewById(R.id.persLim);
        Win = findViewById(R.id.WorkLim);
        Oin = findViewById(R.id.OthLim);
        pProgress = findViewById(R.id.personalprog);
        wProgress = findViewById(R.id.workprog);
        hProgress = findViewById(R.id.homeprog);
        oProgress = findViewById(R.id.otherprog);
        backhome = findViewById(R.id.bkhome);

        // Fetch data and set progress
        GetRequest();
        GetExpensesRequest();
        GetBudgetsRequest();

        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BudgetingViewActivity.this, BudgetingActivity.class));
            }
        });
    }

    private void GetRequest() {
        String url = "http://coms-309-056.class.las.iastate.edu:8080/budget/check/" + MainActivity.selectedMemberId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                if (response.has("Personal")) {
                    P = response.getDouble("Personal");
                    Pin.setText(Double.toString(P));
                }
                if (response.has("Work")) {
                    W = response.getDouble("Work");
                    Win.setText(Double.toString(W));
                }
                if (response.has("Home")) {
                    H = response.getDouble("Home");
                    Hin.setText(Double.toString(H));
                }
                if (response.has("Other")) {
                    O = response.getDouble("Other");
                    Oin.setText(Double.toString(O));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            Toast.makeText(BudgetingViewActivity.this, "Failed: " + error.toString(), Toast.LENGTH_LONG).show();
            error.printStackTrace(); // Print error details for debugging
        });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
    private void GetExpensesRequest() {
        String url = "http://coms-309-056.class.las.iastate.edu:8080/expenses/" + MainActivity.selectedMemberId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                if (response.has("personal")) {
                    Personal = (int) Math.round(response.getDouble("personal"));
                }
                if (response.has("work")) {
                    Work = (int) Math.round(response.getDouble("work"));
                }
                if (response.has("home")) {
                    Home = (int) Math.round(response.getDouble("home"));
                }
                if (response.has("other")) {
                    Other = (int) Math.round(response.getDouble("other"));
                }

                // After expenses are fetched, fetch budgets
                GetBudgetsRequest();

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(BudgetingViewActivity.this, "Failed to parse expenses data", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            error.printStackTrace();
            Toast.makeText(BudgetingViewActivity.this, "Failed to fetch expenses data", Toast.LENGTH_SHORT).show();
        });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void GetBudgetsRequest() {
        String url = "http://coms-309-056.class.las.iastate.edu:8080/budget/" + MainActivity.selectedMemberId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                PBud = response.getDouble("personalLimit");
                WBud = response.getDouble("workLimit");
                HBud = response.getDouble("homeLimit");
                OBud = response.getDouble("otherLimit");

                // After budgets are fetched, set progress
                setProgress();

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(BudgetingViewActivity.this, "Failed to parse budgets data", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            error.printStackTrace();
            Toast.makeText(BudgetingViewActivity.this, "Failed to fetch budgets data", Toast.LENGTH_SHORT).show();
        });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void setProgress() {
        // Calculate percentages
        double pPercentage = (double) Personal / PBud * 100;
        double wPercentage = (double) Work / WBud * 100;
        double hPercentage = (double) Home / HBud * 100;
        double oPercentage = (double) Other / OBud * 100;

        // Set progress
        pProgress.setProgress((int) pPercentage);
        wProgress.setProgress((int) wPercentage);
        hProgress.setProgress((int) hPercentage);
        oProgress.setProgress((int) oPercentage);
    }
}
