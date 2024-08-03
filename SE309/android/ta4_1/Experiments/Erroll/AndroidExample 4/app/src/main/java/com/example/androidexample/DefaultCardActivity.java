package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.cardform.view.CardForm;

import org.json.JSONObject;

public class DefaultCardActivity extends AppCompatActivity {
    private static final String DEFAULT_CARD_URL = "http://coms-309-056.class.las.iastate.edu:8080/cards/userId/" + LoginActivity.UUID.replace("\"", "") + "/default";

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
                .setup(DefaultCardActivity.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        retrieveDefaultCardInfo();

        Button btnBuy = findViewById(R.id.btnBuy);
        // Your existing code for the btnBuy click listener
    }

    private void retrieveDefaultCardInfo() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DEFAULT_CARD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Auto-fill the card form with the response
                        try {
                            JSONObject cardDetails = new JSONObject(response);
                            cardForm.getCardEditText().setText(cardDetails.optString("cardNumber"));
                            cardForm.getExpirationDateEditText().setText(cardDetails.optString("expirationDate"));
                            cardForm.getCvvEditText().setText(cardDetails.optString("cvv"));
                            cardForm.getCardholderNameEditText().setText(cardDetails.optString("name"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DefaultCardActivity.this, "Error retrieving default payment info: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
