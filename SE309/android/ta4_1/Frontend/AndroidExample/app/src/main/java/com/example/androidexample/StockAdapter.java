package com.example.androidexample;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {

    private List<StockData> stocksList;
    private Context context;

    public StockAdapter(List <StockData> stocksList, Context context) {
        this.stocksList = stocksList;
        this.context = context;
    }
    static final Map<String, String> symbolMap;
    static {
        symbolMap = new HashMap<>();
        symbolMap.put("binance:btcusdt", "bitcoin");
        symbolMap.put("binance:dogeusdt", "dogecoin");
        symbolMap.put("aapl", "apple");
        symbolMap.put("amzn", "amazon");
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_item, parent, false);
        return new StockViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        StockData stockData = stocksList.get(position);
        String displayText = stockData.getSymbol() + ": $" + String.format("%.2f", stockData.getPrice());
        holder.textViewStockSymbolPrice.setText(displayText);
        holder.buttonBuy.setOnClickListener(v -> {
            String numberOfSharesText = holder.editTextNumberShares.getText().toString();
            if (!numberOfSharesText.isEmpty()) {
                double shares = Double.parseDouble(numberOfSharesText);
                sendDataToBackend(stockData.getSymbol(), shares, stockData.getPrice());
            } else {
                Toast.makeText(context, "Please enter the number of shares", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return stocksList.size();
    }

    public static class StockViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewStockSymbolPrice;
        public EditText editTextNumberShares;
        public Button buttonBuy;

        public StockViewHolder(View view) {
            super(view);
            textViewStockSymbolPrice = view.findViewById(R.id.textViewStockSymbolPrice);
            editTextNumberShares = view.findViewById(R.id.editTextNumberShares);
            buttonBuy = view.findViewById(R.id.buttonBuy);
        }
    }

    private void sendDataToBackend(String stockSymbol, double shares, double price) {
        OkHttpClient client = new OkHttpClient();

        String backendSymbol = getBackendSymbol(stockSymbol);
        String sharesKey = backendSymbol + (isCrypto(stockSymbol) ? "" : "Shares");
        String priceKey = backendSymbol + "Price";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(sharesKey, shares);
            jsonObject.put(priceKey, price);
            Log.d("StockAdapter", "JSON to send: " + jsonObject.toString());
        } catch (JSONException e) {
            Log.e("StockAdapter", "Failed to create JSON", e);
            return;
        }

        RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url("http://coms-309-056.class.las.iastate.edu:8080/portfolio/"+ MainActivity.selectedMemberId +"/buy")
                .put(body)
                .build();
        Log.d("StockAdapter", "Sending request to URL: " + request.url());

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("StockAdapter", "Network call failed", e);
                performPostFailureAction("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string(); // Read the response body once

                Log.d("StockAdapter", "Response body: " + responseBody);

                if (response.isSuccessful()) {
                    performPostSuccessAction("Purchase successful");
                } else {
                    Log.e("StockAdapter", "Network call unsuccessful with response code: " + response.code() + " and body: " + responseBody);
                    performPostFailureAction("Server error: " + response.code() + " - " + responseBody);
                }
            }
        });
    }

    private boolean isCrypto(String stockSymbol) {
        return stockSymbol.contains(":");
    }

    private String getBackendSymbol(String stockSymbol) {
        return symbolMap.getOrDefault(stockSymbol.toLowerCase(), stockSymbol.toLowerCase());

    }

    private void performPostSuccessAction(String message) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(() -> {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            });
        }
    }

    private void performPostFailureAction(String message) {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(() -> {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            });
        }
    }
}
