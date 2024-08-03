package com.example.androidexample;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.cardform.view.CardForm;

import org.json.JSONException;
import org.json.JSONObject;

public class EditCardActivity extends AppCompatActivity {

    private String cardId;
    private CardForm cardForm;
    private Card card; // Keep a reference to the original card to update

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        cardForm = findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .actionLabel("Add Card")
                .setup(this);

        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        if (getIntent().hasExtra("card_data")) {
            String cardJsonString = getIntent().getStringExtra("card_data");
            Log.d("EditCardActivity", "Received card data: " + cardJsonString);
            try {
                JSONObject cardDetails = new JSONObject(cardJsonString);
                populateCardForm(cardDetails);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load card details", Toast.LENGTH_SHORT).show();
            }
        }
        if (getIntent().hasExtra("card_id")) {
            cardId = getIntent().getStringExtra("card_id");
        } else {
            cardId = null; // Indicate that a new card is being added
        }


        Button btnSaveCard = findViewById(R.id.btn_save_card);
        btnSaveCard.setOnClickListener(v -> {
            if (cardForm.isValid()) {
                String cardholderName = cardForm.getCardholderName();
                String cardNumber = cardForm.getCardNumber();
                String expirationDate = cardForm.getExpirationMonth() + "/" + cardForm.getExpirationYear();
                String cvv = cardForm.getCvv();

                if (card == null) {
                    // If the card is null, create a new card
                    card = new Card(cardholderName, cardNumber, expirationDate, cvv);
                } else {
                    // If the card is not null, update its details
                    card.setName(cardholderName);
                    card.setCardNumber(cardNumber);
                    card.setExpirationDate(expirationDate);
                    card.setCvv(cvv);
                }
                sendCardToBackend(card);
                finish();
            } else {
                Toast.makeText(EditCardActivity.this, "Please fill out the card form correctly.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populateCardForm(JSONObject cardDetails) {
        try {
            Log.d("EditCardActivity", "Populating card form with: " + cardDetails.toString());
            String cardNumber = cardDetails.getString("cardNumber");
            String expirationDate = cardDetails.getString("expirationDate");
            String cvv = cardDetails.getString("cvv");
            String name = cardDetails.getString("name");

            // Log the individual details
            Log.d("EditCardActivity", "Card Number: " + cardNumber);
            Log.d("EditCardActivity", "Expiration Date: " + expirationDate);
            Log.d("EditCardActivity", "CVV: " + cvv);
            Log.d("EditCardActivity", "Name: " + name);

            // Populate the form fields
            cardForm.getCardEditText().setText(cardNumber);
            cardForm.getExpirationDateEditText().setText(expirationDate);
            cardForm.getCvvEditText().setText(cvv);
            cardForm.getCardholderNameEditText().setText(name);

            // Set the card's ID
            if (cardDetails.has("id")) {
                if (card == null) {
                    card = new Card(name, cardNumber, expirationDate, cvv);
                }
                card.setId(cardDetails.getString("id")); // Use the setId method to set the ID
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("EditCardActivity", "JSONException while populating form", e);
            Toast.makeText(this, "Failed to parse card details", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendCardToBackend(Card card) {
        if (LoginActivity.UUID == null || LoginActivity.UUID.isEmpty()) {
            Toast.makeText(this, "User ID is not available", Toast.LENGTH_LONG).show();
            return;
        }
        String userId = LoginActivity.UUID.replace("\"", "");
        String url;

        JSONObject cardJson = card.toJson();

        // Determine if this is an add or update operation based on whether a card ID is available
        if (cardId == null || cardId.isEmpty()) {
            url = "http://coms-309-056.class.las.iastate.edu:8080/cards/" + userId;
        } else {
            url = "http://coms-309-056.class.las.iastate.edu:8080/cards/id/" + userId + "/" + cardId;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                cardId == null || cardId.isEmpty() ? Request.Method.POST : Request.Method.PUT, // Use POST for add and PUT for update
                url,
                cardJson,
                response -> Toast.makeText(EditCardActivity.this, "Card saved successfully!", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(EditCardActivity.this, "Error saving card: " + error.toString(), Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }


}
