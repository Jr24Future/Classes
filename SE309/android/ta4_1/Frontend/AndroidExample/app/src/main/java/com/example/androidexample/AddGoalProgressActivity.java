package com.example.androidexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


public class AddGoalProgressActivity extends AppCompatActivity {

    private TextView promptview;
    private Button Sb;
    private EditText amtChange, dayPass;

    private String goalId, prompt;


    private double amount;

    private int timeFrame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal_progress);

        Intent intent = getIntent();
        if (intent != null) {
            goalId = intent.getStringExtra("goalId");
            prompt = intent.getStringExtra("goalString");
        }

        promptview = findViewById(R.id.prompt_display2);
        amtChange = findViewById(R.id.editTextText);
        dayPass = findViewById(R.id.editTextText2);
        Sb = findViewById(R.id.button);

        promptview.setText(prompt);

        Sb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from EditText fields when the button is clicked
                String amountText = amtChange.getText().toString().trim();
                String timeFrameText = dayPass.getText().toString().trim();

                // Check if EditText fields are not empty before parsing
                if (!amountText.isEmpty() && !timeFrameText.isEmpty()) {
                    // Parse the strings into double and int respectively
                    amount = Double.parseDouble(amountText);
                    timeFrame = Integer.parseInt(timeFrameText);

                    // Make the PATCH request
                    PutRequest();
                    startActivity(new Intent(AddGoalProgressActivity.this, GoalListActivity.class));
                } else {
                    // Display a toast message indicating that fields are empty
                    Toast.makeText(AddGoalProgressActivity.this, "Please enter both amount and time frame", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void PutRequest()
    {
        String url = "http://coms-309-056.class.las.iastate.edu:8080/goals/user/" + LoginActivity.UUID.replace("\"", "") + "/goal/" + goalId + "/progress/" + amount + "/" + timeFrame;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null, response -> {
            // Handle successful response
            Toast.makeText(getApplicationContext(), "Goal Updated!", Toast.LENGTH_LONG).show();

            // Pass the updated progress amount back to CustomArrayAdapter
            Intent resultIntent = new Intent();
            resultIntent.putExtra("positionToUpdate", getIntent().getIntExtra("positionToUpdate", -1)); // Pass the position
            resultIntent.putExtra("updatedProgress", amount); // Pass the updated progress amount
            setResult(Activity.RESULT_OK, resultIntent);

            // Finish the activity
            finish();
        }, error -> {
            // Handle error
            Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_LONG).show();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }



}
