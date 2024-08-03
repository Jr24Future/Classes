package com.example.frontendextraexpenses;


import static android.app.PendingIntent.getActivity;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{
        //Titles
        private TextView ExtraExpensesTitle,PersonalTitle,WorkTitle,HomeTitle,OthersTitle;



        //"http://coms-309-056.class.las.iastate.edu:8080/expenses";
        private static String URL = "http://coms-309-056.class.las.iastate.edu:8080/expenses/5165496b-3a9b-4da6-a9b8-2ac52b89a206";
        private static String userURL = "http://coms-309-056.class.las.iastate.edu:8080/users";
        private String Selection;

        private Button UpdateResults, SelectExpense;

        //Amounts
        private TextView PersonalAmt, WorkAmt, HomeAmt, OthersAmt;
        private double P,W,H,O;
        private Spinner DropDown;

        private String UUID;







        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                ExtraExpensesTitle =findViewById(R.id.textView10);
                PersonalTitle = findViewById(R.id.textView3);
                WorkTitle = findViewById(R.id.textView5);
                HomeTitle = findViewById(R.id.textView6);
                OthersTitle = findViewById(R.id.textView7);
                DropDown = (Spinner) findViewById(R.id.spinner);

                PersonalAmt = findViewById(R.id.textView);
                WorkAmt = findViewById(R.id.textView4);
                HomeAmt = findViewById(R.id.textView2);
                OthersAmt = findViewById(R.id.textView8);
                DropDown = (Spinner) findViewById(R.id.spinner);
                UpdateResults = (Button)findViewById(R.id.button2);
                SelectExpense = (Button)findViewById(R.id.button);



                UpdateResults.setText("Update");
                SelectExpense.setText("Select");

                ExtraExpensesTitle.setText("Add Expense");
                PersonalTitle.setText("Personal:");
                WorkTitle.setText("Work:");
                HomeTitle.setText("Home:");
                OthersTitle.setText("Other:");


                PersonalAmt.setText("$" +String.format("%.2f",P));
                WorkAmt.setText("$"+String.format("%.2f",W));
                HomeAmt.setText("$"+String.format("%.2f",H));
                OthersAmt.setText("$"+String.format("%.2f",O));




                ExtraExpensesTitle.setText("Extra Expenses");

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                        this,
                        R.array.extra_expenses_array,
                        android.R.layout.simple_spinner_item
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                DropDown.setAdapter(adapter);



                DropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Selection = (String) adapter.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                });






                UpdateResults.setOnClickListener(new View.OnClickListener() {
                        @Override
                                public void onClick(View v) {
                                GetRequest();
                                PersonalAmt.setText("$" +String.format("%.2f",P));
                                WorkAmt.setText("$"+String.format("%.2f",W));
                                HomeAmt.setText("$"+String.format("%.2f",H));
                                OthersAmt.setText("$"+String.format("%.2f",O));
                        }
                });


                SelectExpense.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Intent intent =new Intent(MainActivity.this, SpinnerActivity.class);
                                intent.putExtra("Selection", Selection);
                                startActivity(intent);
                        }
                });






        }






        private void GetRequest() {

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
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


        //Bundle extras = getIntent().getExtras();
        //if(extras == null) {
        //        ExtraExpensesTitle.setText("Extra Expenses");
        //}











                                                        // Populate text views with the parsed data





}

       