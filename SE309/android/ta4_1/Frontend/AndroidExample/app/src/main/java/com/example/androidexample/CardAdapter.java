package com.example.androidexample;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<Card> cardList;
    private CardListActivity activity;

    public CardAdapter(List<Card> cardList, CardListActivity activity) {
        this.cardList = cardList;
        this.activity = activity;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Card card = cardList.get(position);
        holder.cardNumberTextView.setText(card.getMaskedNumber()); // Assume Card class has getMaskedNumber method

        holder.editButton.setOnClickListener(view -> {
            // Intent to start EditCardActivity, passing the card info for editing
            Intent intent = new Intent(activity, EditCardActivity.class);
            String cardDataJson = card.toJson().toString();
            Log.d("CardAdapter", "Card data being sent: " + cardDataJson);
            intent.putExtra("card_data", cardDataJson);
            intent.putExtra("card_id", card.getId()); // Pass the card ID as an extra
            activity.startActivity(intent);
        });

        holder.useButton.setOnClickListener(view -> {
            // Get the user ID
            String userId = LoginActivity.UUID.replace("\"", "");
            // Call the useCard method
            useCard(userId);
        });

        holder.deleteButton.setOnClickListener(view -> {
            // Logic to delete the card
            deleteCard(card, position);
        });
        holder.defaultButton.setOnClickListener(view -> {
            // Get the user ID and card ID
            String userId = LoginActivity.UUID.replace("\"", "");
            String cardId = card.getId();
            // Call the setDefaultCard method
            setDefaultCard(userId, cardId);
        });
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView cardNumberTextView;
        public Button editButton, useButton, defaultButton, deleteButton;

        public CardViewHolder(View view) {
            super(view);
            cardNumberTextView = view.findViewById(R.id.cardNumberTextView);
            editButton = view.findViewById(R.id.addProg);
            useButton = view.findViewById(R.id.CompleteAct);
            deleteButton = view.findViewById(R.id.deleteButton);
            defaultButton = view.findViewById(R.id.defaultButton);
        }
    }

    // Helper method to remove a card from the RecyclerView
    public void removeAt(int position) {
        cardList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cardList.size());
    }
    public void deleteCard(Card card, int position) {
        String url = "http://coms-309-056.class.las.iastate.edu:8080/cards/id/" + LoginActivity.UUID.replace("\"", "") + "/" + card.getId();

        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    // Response handling code
                    cardList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, cardList.size());
                    Toast.makeText(activity, "Card deleted successfully", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Error handling code
                    Toast.makeText(activity, "Error deleting card: " + error.toString(), Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(activity).add(stringRequest);
    }
    public void useCard(String userId) {
        String url = "http://coms-309-056.class.las.iastate.edu:8080/upgradeType/" + userId;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Handle the response
                    Toast.makeText(activity, "Card used successfully", Toast.LENGTH_SHORT).show();
                    MainActivity.userType = "premium";

                },
                error -> {
                    // Handle the error
                    Toast.makeText(activity, "Error using card: " + error.toString(), Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(activity).add(stringRequest);
    }

    public void setDefaultCard(String userId, String cardId) {
        String url = "http://coms-309-056.class.las.iastate.edu:8080/cards/id/" + userId + "/" + cardId + "/setDefault";

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                response -> {
                    // Handle the response
                    Toast.makeText(activity, "Card set as default successfully", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Handle the error
                    Toast.makeText(activity, "Error setting card as default: " + error.toString(), Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(activity).add(stringRequest);
    }

}
