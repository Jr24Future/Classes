package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class StockMarketActivity extends AppCompatActivity {

    private WebSocket webSocket;
    private RecyclerView stocksRecyclerView;
    private StockAdapter stockAdapter;
    private List<StockData> stocksList = new ArrayList<>();
    private Map<String, StockData> stockDataBuffer = new HashMap<>();

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable updateStocksRunnable = new Runnable() {
        @Override
        public void run() {
            // Process each stock data for UI update
            for (StockData stock : stockDataBuffer.values()) {
                updateStockData(stock, false);
            }
            sendStockUpdatesToBackend(); // Send the updates to backend
            stockDataBuffer.clear(); // Clear the buffer after processing
            stockAdapter.notifyDataSetChanged(); // Notify the adapter to refresh the view
            handler.postDelayed(this, 5000); // Schedule next update after 5 seconds
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_market);

        stocksRecyclerView = findViewById(R.id.stocksRecyclerView);
        stocksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stockAdapter = new StockAdapter(stocksList, this);
        stocksRecyclerView.setAdapter(stockAdapter);

        startWebSocket();
        handler.postDelayed(updateStocksRunnable, 5000);

        Button backToMainButton = findViewById(R.id.btnBackToMain);
        backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                Intent intent = new Intent(StockMarketActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
    }

    private void startWebSocket() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("wss://ws.finnhub.io?token=co7n979r01qgik2hbdkgco7n979r01qgik2hbdl0").build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                // Subscribe to stock symbols
                webSocket.send("{\"type\":\"subscribe\",\"symbol\":\"AAPL\"}");
                webSocket.send("{\"type\":\"subscribe\",\"symbol\":\"AMZN\"}");
                webSocket.send("{\"type\":\"subscribe\",\"symbol\":\"BINANCE:BTCUSDT\"}");
                webSocket.send("{\"type\":\"subscribe\",\"symbol\":\"BINANCE:DOGEUSDT\"}");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                try {
                    JSONObject jsonObject = new JSONObject(text);
                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    if (dataArray != null) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObject = dataArray.getJSONObject(i);
                            String symbol = dataObject.getString("s");
                            double price = dataObject.getDouble("p");
                            StockData newStockData = new StockData(symbol, price);
                            updateStockData(newStockData, true);
                        }
                    }
                } catch (JSONException e) {
                    Log.e("WebSocket", "Error parsing data", e);
                }
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                Log.e("WebSocket", "Error during WebSocket communication", t);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                Log.d("WebSocket", "WebSocket closed: " + reason);
            }
        });
    }

    private void updateStockData(StockData stockData, boolean buffer) {
        if (buffer) {
            stockDataBuffer.put(stockData.getSymbol(), stockData);
        } else {
            boolean exists = false;
            for (int i = 0; i < stocksList.size(); i++) {
                StockData existingStock = stocksList.get(i);
                if (existingStock.getSymbol().equals(stockData.getSymbol())) {
                    existingStock.setPrice(stockData.getPrice());
                    stockAdapter.notifyItemChanged(i);
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                stocksList.add(stockData);
                stockAdapter.notifyItemInserted(stocksList.size() - 1);
            }
        }
    }

    private void sendStockUpdatesToBackend() {
        OkHttpClient client = new OkHttpClient();
        String url = "http://coms-309-056.class.las.iastate.edu:8080/portfolio/" + MainActivity.selectedMemberId;

        JSONObject stockPrices = new JSONObject();
        try {
            for (Map.Entry<String, StockData> entry : stockDataBuffer.entrySet()) {
                stockPrices.put(entry.getKey() + "Price", entry.getValue().getPrice());
            }

            RequestBody body = RequestBody.create(stockPrices.toString(), MediaType.parse("application/json; charset=utf-8"));
            Request request = new Request.Builder().url(url).put(body).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("StockMarketActivity", "Failed to send stock updates", e);
                    runOnUiThread(() -> Toast.makeText(StockMarketActivity.this, "Failed to update stocks on backend", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        Log.e("StockMarketActivity", "Error: " + response.code() + " " + response.body().string());
                    } else {
                        Log.i("StockMarketActivity", "Stock updates successfully sent to the backend.");
                    }
                }
            });
        } catch (JSONException e) {
            Log.e("StockMarketActivity", "JSON building error", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateStocksRunnable);
        if (webSocket != null) {
            webSocket.close(1000, "Activity destroyed");
            webSocket = null;
        }
    }
}
