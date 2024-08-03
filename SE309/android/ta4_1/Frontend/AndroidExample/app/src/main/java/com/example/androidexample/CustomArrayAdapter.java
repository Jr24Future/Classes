package com.example.androidexample;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class CustomArrayAdapter extends RecyclerView.Adapter<CustomArrayAdapter.CustomViewHolder> {

    private final List<Double> Finalamount;
    private final List<Double> amounts;
    private List<String> items;
    private List<String> goalIds;
    private List<String> goalStrings;
    private String userId;
    private Context context;
    private OnItemLongClickListener longClickListener;

    public CustomArrayAdapter(Context context, List<String> items, List<String> goalIds, List<String> goalStrings, List<Double> Finalamount, List<Double> amounts, String userId) {
        this.context = context;
        this.items = items;
        this.goalIds = goalIds;
        this.goalStrings = goalStrings;
        this.Finalamount = Finalamount;
        this.amounts = amounts;
        this.userId = userId;
    }

    public void clear() {
        items.clear();
        goalIds.clear();
        goalStrings.clear();
        notifyDataSetChanged();
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.cardNumberTextView.setText(items.get(holder.getAdapterPosition()));

        // Calculate progress percentage
        double progressPercentage = calculateProgressPercentage(position);
        holder.progressBar.setProgress((int) progressPercentage);

        holder.editButton.setOnClickListener(view -> {
            // Intent to start AddGoalProgressActivity, passing the goal ID, user ID, and goalString for adding progress
            Intent intent = new Intent(context, AddGoalProgressActivity.class);
            intent.putExtra("goalId", goalIds.get(holder.getAdapterPosition()));
            intent.putExtra("userId", MainActivity.selectedMemberId);
            intent.putExtra("goalString", goalStrings.get(holder.getAdapterPosition())); // Pass goalStringx`
            context.startActivity(intent); // Start activity without expecting result
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListener != null) {
                    longClickListener.onItemLongClick(holder.getAdapterPosition());
                    return true;
                }
                return false;
            }
        });
    }

    private double calculateProgressPercentage(int position) {
        // Retrieve the total final amount and the current amount for the given position
        double totalFinalAmount = Finalamount.get(position);
        Log.d("FinalAMT", String.valueOf(totalFinalAmount));
        double currentAmount = amounts.get(position); // Assuming you have a List<Double> amounts
        Log.d("CurrentAMT", String.valueOf(currentAmount));

        // Calculate the progress percentage
        double progressPercentage = ((totalFinalAmount - currentAmount)/ totalFinalAmount)* 100.0;
        Log.d("progressPercent", String.valueOf(progressPercentage));

        return progressPercentage;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView cardNumberTextView;
        ProgressBar progressBar;
        Button editButton;

        CustomViewHolder(View view) {
            super(view);
            cardNumberTextView = view.findViewById(R.id.cardNumberTextView);
            progressBar = view.findViewById(R.id.progressBar);
            editButton = view.findViewById(R.id.addProg);
        }
    }
}
