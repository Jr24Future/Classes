package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

import com.braintreepayments.cardform.view.CardForm;

public class MainActivity extends AppCompatActivity {
    private static String postUrl = "http://coms-309-056.class.las.iastate.edu:8080/cards/" + LoginActivity.UUID.replace("\"", "");

    private CardForm cardForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardForm = findViewById(R.id.cardForm);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .actionLabel("Purchase")
                .setup(MainActivity.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        Button btnBuy = findViewById(R.id.btnBuy);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardForm.isValid()) {
                    Toast.makeText(MainActivity.this,
                            "Card number: " + cardForm.getCardNumber() +
                                    " Card expirationDate: " + cardForm.getExpirationDateEditText().getText() +
                                    " Card cvv: " + cardForm.getCvvEditText().getText() +
                                    " Card holder name: " + cardForm.getCardholderNameEditText().getText(),
                            Toast.LENGTH_LONG).show();
                            Log.d("DEFAULT_CARD_URL", postUrl + " is the ID!");

                    // Prepare the POST request
                    try {
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("cardNumber", cardForm.getCardNumber());
                        jsonBody.put("expirationDate", cardForm.getExpirationDateEditText().getText().toString());
                        jsonBody.put("cvv", cardForm.getCvvEditText().getText().toString());
                        jsonBody.put("name", cardForm.getCardholderNameEditText().getText().toString());

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                Request.Method.POST,
                                postUrl,
                                jsonBody,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Handle the response
                                        Toast.makeText(MainActivity.this, "Payment successful!", Toast.LENGTH_SHORT).show();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // Handle the error
                                        Toast.makeText(MainActivity.this, "Payment failed: " + error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                        );

                        // Add the request to the RequestQueue.
                        Volley.newRequestQueue(MainActivity.this).add(jsonObjectRequest);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Please complete the form", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}