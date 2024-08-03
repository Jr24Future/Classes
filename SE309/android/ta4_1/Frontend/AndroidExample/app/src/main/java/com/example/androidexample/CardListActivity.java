package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class CardListActivity extends AppCompatActivity {
    private static final int EDIT_CARD_REQUEST_CODE = 1;

    private RecyclerView recyclerView;
    private CardAdapter adapter;
    private ArrayList<Card> cardList;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        if (LoginActivity.UUID != null) {
            userId = LoginActivity.UUID.replace("\"", "");
        } else {
            // Handle the null case appropriately (e.g., return to login screen)
            Toast.makeText(this, "User ID is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Button btnAddCard = findViewById(R.id.btn_add_card);
        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start EditCardActivity with no extra, indicating a new card is being added
                Intent intent = new Intent(CardListActivity.this, EditCardActivity.class);
                startActivityForResult(intent, EDIT_CARD_REQUEST_CODE);
            }
        });

        Button btnReturnMain = findViewById(R.id.btn_return_main);
        btnReturnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to the MainActivity when the button is clicked
                Intent intent = new Intent(CardListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.cardListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardList = new ArrayList<>();

        adapter = new CardAdapter(cardList, this);
        recyclerView.setAdapter(adapter);

        fetchCards();
    }

    private void fetchCards() {
        String cardsUrl = "http://coms-309-056.class.las.iastate.edu:8080/cards/userId/" + userId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, cardsUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray cardsJsonArray = new JSONArray(response);
                            cardList.clear();
                            for (int i = 0; i < cardsJsonArray.length(); i++) {
                                cardList.add(Card.fromJson(cardsJsonArray.getJSONObject(i)));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CardListActivity.this, "Error parsing card data", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CardListActivity.this, "Error retrieving cards: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                });

        Volley.newRequestQueue(this).add(stringRequest);
    }
    public void startEditCardActivity(Card card) {
        Intent intent = new Intent(this, EditCardActivity.class);
        intent.putExtra("card_id", card.getId()); // Assuming `getId()` returns a String representation of the ID
        // Other card data can be put as extras if necessary
        startActivityForResult(intent, EDIT_CARD_REQUEST_CODE);
    }
    protected void onResume() {
        super.onResume();
        fetchCards();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_CARD_REQUEST_CODE && resultCode == RESULT_OK) {
            // A card was edited or added, so refresh the list
            fetchCards();
        }
    }
    public void setDefaultCard(String cardId) {
        String setDefaultCardUrl = "http://yourapi.com/setDefaultCard/" + cardId; // Replace with your actual URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, setDefaultCardUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the successful default card set response
                        // This might include refreshing the list of cards
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error setting the default card
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }
    public void deleteCard(String cardId, int position) {
        String deleteCardUrl = "http://yourapi.com/deleteCard/" + cardId; // Replace with your actual URL
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, deleteCardUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the successful card deletion response
                        // Update your RecyclerView by removing the card from the list and notifying the adapter
                        adapter.removeAt(position);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle the error deleting the card
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }

}
