package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class PaymentOptionsActivity extends AppCompatActivity {

    private Button btnAddNewCard, btnDefaultPayment;
    private static final String DEFAULT_CARD_URL = "http://localhost:8080/cards/userId/" + LoginActivity.UUID.replace("\"", "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);

        btnAddNewCard = findViewById(R.id.btnAddNewCard);
        btnDefaultPayment = findViewById(R.id.btnDefaultPayment);

        btnAddNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the card form activity
                Intent intent = new Intent(PaymentOptionsActivity.this, MainActivity.class);
                startActivity(intent);
                Log.d("UUID", DEFAULT_CARD_URL + " is the ID!");
            }
        });

        btnDefaultPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to DefaultCardActivity
                Intent intent = new Intent(PaymentOptionsActivity.this, DefaultCardActivity.class);
                startActivity(intent);
            }
        });
    }
}
