package com.example.androidexample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GoalListActivity extends AppCompatActivity {

    private ArrayList<String> items = new ArrayList<>();
    private ArrayList<String> goalID = new ArrayList<>();
    private ArrayList<String> goalStrings = new ArrayList<>(); // Added for passing goalString to adapter

    private ArrayList<Double> finalamts = new ArrayList<>();
    private ArrayList<Double> amts = new ArrayList<>();
    private CustomArrayAdapter itemsAdapter; // Changed to CustomArrayAdapter
    private RecyclerView recyclerView;
    private Button button, rtn_home, clr_a;


    private double GoalAmount;
    public String goalString;
    private String URL = "http://coms-309-056.class.las.iastate.edu:8080/goals/" + LoginActivity.UUID.replace("\"", "");
    private final String DelAllURL = "http://coms-309-056.class.las.iastate.edu:8080/goals/deleteAll/" + LoginActivity.UUID.replace("\"", "");
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goalview);
        builder = new AlertDialog.Builder(this);

        recyclerView = findViewById(R.id.recyclerView);
        button = findViewById(R.id.addButton);
        rtn_home = findViewById(R.id.return_home);
        clr_a = findViewById(R.id.clr_all);

        itemsAdapter = new CustomArrayAdapter(this, items, goalID, goalStrings, finalamts, amts, LoginActivity.UUID.replace("\"", ""));
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setUpRecyclerViewListener(); // Set up RecyclerView listener after initializing itemsAdapter

        GetRequest();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GoalListActivity.this, Goal_Add_Activity1.class));
            }
        });

        clr_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllGoals();
            }
        });

        rtn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GoalListActivity.this, MainActivity.class));
            }
        });
    }

    private void GetRequest() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        goalString = jsonObject.getString("goalString");
                        GoalAmount = jsonObject.getDouble("amount");
                        String goalIDSTRING = jsonObject.getString("id");
                        Double amt = jsonObject.getDouble("totalAmount");
                        Double amt2 = jsonObject.getDouble("amount");
                        amts.add(amt2);
                        finalamts.add(amt);
                        items.add(goalString);
                        goalID.add(goalIDSTRING);
                        goalStrings.add(goalString); // Add goalString to the list
                        itemsAdapter.notifyDataSetChanged(); // Notify adapter after adding all items
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GoalListActivity.this, "Get Error: " + error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }





    private void deleteAllGoals() {
        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, DelAllURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful deletion
                        items.clear(); // Clear items list directly
                        goalID.clear(); // Clear goalID list directly
                        goalStrings.clear(); // Clear goalStrings list directly
                        itemsAdapter.notifyDataSetChanged();
                        Toast.makeText(GoalListActivity.this, "All Goals Removed!", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(GoalListActivity.this, "Error deleting goal: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(deleteRequest);
    }

    private void deleteGoal(String goalId) {
        String deleteURL = "http://coms-309-056.class.las.iastate.edu:8080/goals/" + goalId;

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, deleteURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful deletion
                        itemsAdapter.notifyDataSetChanged();
                        Toast.makeText(GoalListActivity.this, "Goal Removed!", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(GoalListActivity.this, "Error deleting goal: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(deleteRequest);
    }


    private void setUpRecyclerViewListener() {
        itemsAdapter.setOnItemLongClickListener(new CustomArrayAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                Context context = getApplicationContext();
                String G_ID = goalID.get(position);
                builder.setTitle("Warning!").setMessage("Are you sure you would like to remove this goal?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                items.remove(position);
                                goalID.remove(position); // Remove corresponding goalID as well
                                goalStrings.remove(position); // Remove corresponding goalString as well
                                deleteGoal(G_ID);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });
    }
}
