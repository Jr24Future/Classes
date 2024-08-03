package com.example.androidexample;

import static com.example.androidexample.R.id;
import static com.example.androidexample.R.layout;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.groupdocs.cloud.conversion.api.ConvertApi;
import com.groupdocs.cloud.conversion.api.FileApi;
import com.groupdocs.cloud.conversion.client.ApiException;
import com.groupdocs.cloud.conversion.client.Configuration;
import com.groupdocs.cloud.conversion.model.ConvertSettings;
import com.groupdocs.cloud.conversion.model.FilesUploadResult;
import com.groupdocs.cloud.conversion.model.StoredConvertedResult;
import com.groupdocs.cloud.conversion.model.requests.ConvertDocumentRequest;
import com.groupdocs.cloud.conversion.model.requests.DownloadFileRequest;
import com.groupdocs.cloud.conversion.model.requests.UploadFileRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    private String POST_URL;
    private String GET_URL;
    private PieChart pieChart;
    private RequestQueue requestQueue;
    private Spinner spinnerCategories;
    private Map<String, Double> stockPrices = new HashMap<>();

    private Spinner spinnerFinancialDetails;
    private double P, W,O, H;
    private TextView display_p, display_w, display_h, display_o;
    private Double PLimit, HLimit, OLimit, WLimit;
    private Spinner spinnerFamilyMembers;
    private EditText editTextAmount;
    private Button btnSubmit;
    private Button btnChat;
    private String selectedCategory;
    private TextView selectedCurrency1;
    private TextView selectedCurrency2;
    public static String userType;
    private final static String API_KEY = "co7n979r01qgik2hbdkgco7n979r01qgik2hbdl0";
    private static final int PICK_PDF_FILE = 1;
    private AlertDialog.Builder builder;
    private Configuration configuration;
    private FileApi fileApi;
    private ConvertApi convertApi;
    private Map<String, StockData> stockDataBuffer = new HashMap<>();
    public static String selectedMemberId = LoginActivity.UUID.replace("\"", "");
    public String Limited;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    public String G_ID;
    Button btnShowPopup; // At the beginning of your class
    Button btnUpload; // At the beginning of your class
    private Handler exchangeRateHandler = new Handler();
    private Runnable exchangeRateRunnable = new Runnable() {
        @Override
        public void run() {
            fetchCurrencyExchangeRates();
            exchangeRateHandler.postDelayed(this, TimeUnit.SECONDS.toMillis(300));
        }
    };
    private Handler stockUpdateHandler = new Handler(Looper.getMainLooper());
    private Runnable stockUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            stockUpdateHandler.postDelayed(this, 5000); // Re-post the Runnable every 5 seconds
        }
    };
    private Runnable fetchStocksRunnable = new Runnable() {
        @Override
        public void run() {
            // Clear previous prices
            stockPrices.clear();
            // Fetch new stock prices
            fetchAndProcessStock("AAPL", "applePrice");
            fetchAndProcessStock("AMZN", "amazonPrice");
            fetchAndProcessStock("BINANCE:BTCUSDT", "bitcoinPrice");
            fetchAndProcessStock("BINANCE:DOGEUSDT", "dogecoinPrice");
            // Schedule the next run
            handler.postDelayed(this, 5000);
        }
    };

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            updateChartDataBasedOnSelection();
            handler.postDelayed(this, 5000);  // Update every 5 seconds
        }
    };

    private static final String CserverUrl = "ws://coms-309-056.class.las.iastate.edu:8080/chat/AppUser";
    private static final String BudURL = "http://coms-309-056.class.las.iastate.edu:8080/budget/check/"+ selectedMemberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetRequest();
        String clientId = "edc9fbff-8efd-429f-96ff-166b70b31302";
        String clientSecret = "7c7debf3f85e9e8675f834fe43089126";
        configuration = new Configuration(clientId, clientSecret);
        fileApi = new FileApi(configuration);
        convertApi = new ConvertApi(configuration);

//        display_p = findViewById(id.p_display);
//        display_w = findViewById(id.w_display);
//        display_o = findViewById(id.o_display);
//        display_h = findViewById(id.h_display);


        requestQueue = Volley.newRequestQueue(this);  // Initialize Volley Request Queue


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        builder = new AlertDialog.Builder(this);

        Log.d("ID", LoginActivity.UUID);

        if (LoginActivity.UUID != null && !LoginActivity.UUID.isEmpty()) {
            POST_URL = "http://coms-309-056.class.las.iastate.edu:8080/expenses/" + LoginActivity.UUID.replace("\"", "");
            GET_URL = "http://coms-309-056.class.las.iastate.edu:8080/" + LoginActivity.UUID.replace("\"", "") + "/financial-report";
        } else {
            POST_URL = "http://coms-309-056.class.las.iastate.edu:8080/expenses/default";
            GET_URL = "http://coms-309-056.class.las.iastate.edu:8080/default/financial-report";
        }

        pieChart = findViewById(R.id.pieChart);
        setupPieChart();
        fetchChartData();

        btnShowPopup = findViewById(R.id.btn_show_popup);
        btnShowPopup.setOnClickListener(this::showPopupMenu);
        btnUpload = findViewById(R.id.btn_upload);
        btnUpload.setOnClickListener(v -> {
            if (userType.equals("guest")) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                Toast.makeText(getApplicationContext(), "You must sign up to access this feature!", Toast.LENGTH_LONG).show();
                startActivity(intent);
            } else {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent, PICK_PDF_FILE);
            }
        });

        spinnerCategories = findViewById(R.id.spinner_categories);
        editTextAmount = findViewById(R.id.editText_amount);
        btnSubmit = findViewById(R.id.btn_submit);
        btnChat = findViewById(R.id.btn_chat);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(adapter);

        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerFinancialDetails = findViewById(R.id.spinner_financial_details);
        setupFinancialDetailsSpinner();

        btnChat.setOnClickListener(v -> {
            if (LoginActivity.UUID != null) {
                if (userType.equals("guest")) {
                    Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                    Toast.makeText(getApplicationContext(), "You must sign up to access this feature!", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                } else {
                    WebSocketManager.getInstance().connectWebSocket(CserverUrl);
                    Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                    startActivity(intent);
                }
            }
        });

        btnSubmit.setOnClickListener(v -> {
            if ("guest".equals(userType)) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                Toast.makeText(getApplicationContext(), "You must sign up to access this feature!", Toast.LENGTH_LONG).show();
                startActivity(intent);
            } else {
                String amountString = editTextAmount.getText().toString();
                if (!amountString.isEmpty() && selectedCategory != null) {
                    GetBudVals();
                    if (PLimit != null && HLimit != null && OLimit != null && WLimit != null) {
                        if (PLimit <= 0 && selectedCategory.equals("personal")) {
                            Limited = "Personal";
                            builder.setTitle("Budget Limit Reached!").setMessage("You have reached the limit on adding your " + Limited + " expense.")
                                    .setCancelable(true)
                                    .setPositiveButton("OK", (dialog, which) -> dialog.cancel())
                                    .show();
                        } else if (HLimit <= 0 && selectedCategory.equals("home")) {
                            Limited = "Home";
                            builder.setTitle("Budget Limit Reached!").setMessage("You have reached the limit on adding your " + Limited + " expense.")
                                    .setCancelable(true)
                                    .setPositiveButton("OK", (dialog, which) -> dialog.cancel())
                                    .show();
                        } else if (OLimit <= 0 && selectedCategory.equals("other")) {
                            Limited = "Other";
                            builder.setTitle("Budget Limit Reached!").setMessage("You have reached the limit on adding your " + Limited + " expense.")
                                    .setCancelable(true)
                                    .setPositiveButton("OK", (dialog, which) -> dialog.cancel())
                                    .show();

                        } else if (WLimit <= 0 && selectedCategory.equals("work")) {
                            Limited = "Work";
                            builder.setTitle("Budget Limit Reached!").setMessage("You have reached the limit on adding your " + Limited + " expense.")
                                    .setCancelable(true)
                                    .setPositiveButton("OK", (dialog, which) -> dialog.cancel())
                                    .show();
                        }

                    }
                    double amount = Double.parseDouble(amountString);
                    sendExpenseToBackend(selectedCategory, amount);

                } else {
                    Toast.makeText(MainActivity.this, "Please enter an amount and select a category", Toast.LENGTH_SHORT).show();
                }
            }
        });

        selectedCurrency1 = findViewById(R.id.selectedCurrency1);
        selectedCurrency2 = findViewById(R.id.selectedCurrency2);
        exchangeRateHandler.post(exchangeRateRunnable);


        spinnerFamilyMembers = findViewById(R.id.spinner_family_members);

        fetchFamilyMembers();
        spinnerFamilyMembers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateChartDataBasedOnSelection();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        // Start the repeated task
        handler.post(fetchStocksRunnable);

    }

    private void setupFinancialDetailsSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.chart_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFinancialDetails.setAdapter(adapter);

        spinnerFinancialDetails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateChartDataBasedOnSelection();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }



    private void fetchAndProcessStock(String symbol, String jsonKey) {
        String url = "https://finnhub.io/api/v1/quote?symbol=" + symbol + "&token=" + API_KEY;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        double price = response.getDouble("c"); // 'c' for current price
                        stockPrices.put(jsonKey, price);
                        if (stockPrices.size() == 4) { // Ensure all prices are fetched
                            sendAllStockPricesToBackend();
                        }
                    } catch (Exception e) {
                        Log.e("API Error", "Error parsing stock data for " + symbol, e);
                    }
                },
                this::handleError
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void sendAllStockPricesToBackend() {
        JSONObject pricesJson = new JSONObject(stockPrices);
        Log.d("StockMarketActivity", "JSON to send: " + pricesJson.toString());

        String updateUrl = "http://coms-309-056.class.las.iastate.edu:8080/portfolio/" + LoginActivity.UUID.replace("\"", "");
        JsonObjectRequest updateRequest = new JsonObjectRequest(Request.Method.PUT, updateUrl, pricesJson,
                response -> Log.d("Update Success", "Stock prices updated successfully."),
                this::handleError);
        requestQueue.add(updateRequest);
    }

    private void handleError(VolleyError error) {
        if(!"guest".equals(userType)) {
            Log.e("API Error", "Error: " + error.toString());
        }
    }







    private void updateChartDataBasedOnSelection() {

        if (spinnerFamilyMembers.getSelectedItem() == null) {
            selectedMemberId = LoginActivity.UUID.replace("\"", ""); // Use the main user's ID as default
        } else {
            JSONObject selectedMember = (JSONObject) spinnerFamilyMembers.getSelectedItem();
            try {
                selectedMemberId = selectedMember.getString("id");
            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, "Failed to get the selected member's ID", Toast.LENGTH_SHORT).show();
                return;  // Exit if we cannot get an ID
            }
        }

        int selectedFinancialDetailIndex = spinnerFinancialDetails.getSelectedItemPosition();
        switch (selectedFinancialDetailIndex) {
            case 0: // Expenses
                fetchAndDisplayData("http://coms-309-056.class.las.iastate.edu:8080/expenses/" + selectedMemberId);
                break;
            case 1: // Portfolio
                fetchAndDisplayPortfolioData(selectedMemberId);  // Method to fetch and display portfolio data
                break;
            case 2: // Assets
                fetchIncomeVsAssets(selectedMemberId);  // Method to fetch and display income vs assets data
                break;
            default:
                fetchAndDisplayData("http://coms-309-056.class.las.iastate.edu:8080/expenses/" + selectedMemberId);
                break;
        }
    }


    private void fetchAndDisplayData(String url) {
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            updateChart(response); // Assume updateChart parses the JSON and updates the pie chart
        }, error -> {
            Toast.makeText(MainActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
        });
        Volley.newRequestQueue(this).add(request);
    }

    private void fetchAndDisplayPortfolioData(String memberId) {
        String url = "http://coms-309-056.class.las.iastate.edu:8080/portfolio/" + memberId + "/detailedValues";  // Endpoint that returns detailed portfolio data

        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    updatePortfolioChart(response);
                },
                error -> Toast.makeText(MainActivity.this, "Error fetching portfolio data: " + error.toString(), Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(this).add(request);
    }

    private void fetchIncomeVsAssets(String memberId) {
        String portfolioUrl = "http://coms-309-056.class.las.iastate.edu:8080/portfolio/" + memberId + "/value";
        String incomeUrl = "http://coms-309-056.class.las.iastate.edu:8080/" + memberId + "/monthlyIncome";

        // Request for Portfolio Value
        StringRequest portfolioRequest = new StringRequest(Request.Method.GET, portfolioUrl,
                portfolioResponse -> {
                    try {
                        double portfolioValue = Double.parseDouble(portfolioResponse);
                        // Request for Income
                        StringRequest incomeRequest = new StringRequest(Request.Method.GET, incomeUrl,
                                incomeResponse -> {
                                    try {
                                        double monthlyIncome = Double.parseDouble(incomeResponse);
                                        updateIncomeVsAssetsChart(monthlyIncome, portfolioValue);
                                    } catch (NumberFormatException e) {
                                        Toast.makeText(MainActivity.this, "Number format error in parsing income: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                },
                                error -> Toast.makeText(MainActivity.this, "Error fetching income: " + error.toString(), Toast.LENGTH_SHORT).show());
                        Volley.newRequestQueue(getApplicationContext()).add(incomeRequest);
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "Number format error in parsing portfolio value: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(MainActivity.this, "Error fetching portfolio: " + error.toString(), Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(getApplicationContext()).add(portfolioRequest);
    }

    private void updatePortfolioChart(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            ArrayList<PieEntry> entries = new ArrayList<>();
            if(jsonObject.getDouble("Apple") > 0) {
                entries.add(new PieEntry((float) jsonObject.getDouble("Apple"), "Apple"));
            }
            if(jsonObject.getDouble("Amazon") > 0) {
                entries.add(new PieEntry((float) jsonObject.getDouble("Amazon"), "Amazon"));
            }
            if(jsonObject.getDouble("Bitcoin") > 0) {
                entries.add(new PieEntry((float) jsonObject.getDouble("Bitcoin"), "Bitcoin"));
            }
            if(jsonObject.getDouble("Dogecoin") > 0) {
                entries.add(new PieEntry((float) jsonObject.getDouble("Dogecoin"), "Dogecoin"));
            }

            PieDataSet dataSet = new PieDataSet(entries, "Portfolio Distribution");
            dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
            PieData data = new PieData(dataSet);
            pieChart.setData(data);
            pieChart.invalidate();
        } catch (JSONException e) {
            Toast.makeText(this, "Failed to parse portfolio data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private void updateIncomeVsAssetsChart(double monthlyIncome, double portfolioValue) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        if (portfolioValue > 0) {
            entries.add(new PieEntry((float) portfolioValue, "Portfolio Value"));
        }
        if (monthlyIncome > 0) {
            entries.add(new PieEntry((float) monthlyIncome, "Monthly Income"));
        }

        if (!entries.isEmpty()) {
            PieDataSet dataSet = new PieDataSet(entries, "Income vs Portfolio");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS); // Use a colorful theme
            PieData data = new PieData(dataSet);
            pieChart.setData(data);
            pieChart.invalidate(); // Refresh the chart
        }
    }



    private void GetBudVals() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BudURL, null,
                response -> {
                    try {
                        if (response.has("Personal")) {
                            PLimit = response.getDouble("Personal");
                        } else {
                            PLimit = 1000.0;
                        }

                        if (response.has("Home")) {
                            HLimit = response.getDouble("Home");
                        } else {
                            HLimit = 1000.0;
                        }

                        if (response.has("Other")) {
                            OLimit = response.getDouble("Other");
                        } else {
                            OLimit = 1000.0;
                        }

                        if (response.has("Work")) {
                            WLimit = response.getDouble("Work");
                        } else {
                            WLimit = 1000.0;
                        }
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, "Error parsing JSON response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (error.networkResponse != null) {
                        String responseBody;
                        try {
                            responseBody = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        } catch (UnsupportedEncodingException e) {
                            responseBody = new String(error.networkResponse.data);
                        }
                        Log.d("Budgets", "Error Status code: " + error.networkResponse.statusCode);
                        Log.d("Budgets", "Error Response: " + responseBody);
                    }
                    Toast.makeText(MainActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("Budgets", "Volley Error: " + error.toString());
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonObjectRequest);
    }




    @Override
    protected void onResume() {
        super.onResume();
        fetchChartData(); // Fetch the latest chart data and update the pie chart
        fetchCurrencyExchangeRates();
        handler.post(updateRunnable);
        stockUpdateHandler.postDelayed(stockUpdateRunnable, 5000);
    }

    private void setupPieChart() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setEnabled(true);
    }

    private void fetchChartData() {
        if (LoginActivity.UUID != null) {
            String url = POST_URL;
            StringRequest request = new StringRequest(Request.Method.GET, url,
                    response -> updateChart(response),
                    error -> Toast.makeText(MainActivity.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show());
            Volley.newRequestQueue(this).add(request);
        } else {
            Toast.makeText(this, "No user data available", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateChart(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            ArrayList<PieEntry> entries = new ArrayList<>();
            if(jsonObject.getDouble("work") > 0) {
                entries.add(new PieEntry((float) jsonObject.getDouble("work"), "Work"));
//                display_w.setText(String.valueOf(W));
            }
            if(jsonObject.getDouble("home") > 0) {
                entries.add(new PieEntry((float) jsonObject.getDouble("home"), "Home"));
//                display_h.setText(String.valueOf(H));
            }
            if(jsonObject.getDouble("personal") > 0) {
                entries.add(new PieEntry((float) jsonObject.getDouble("personal"), "Personal"));
//                display_p.setText(String.valueOf(P));
            }
            if(jsonObject.getDouble("other") > 0) {
                entries.add(new PieEntry((float) jsonObject.getDouble("other"), "Other"));
//                display_o.setText(String.valueOf(O));
            }

            PieDataSet dataSet = new PieDataSet(entries, "Expense Categories");
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS); // You can choose a different color template if you prefer
            PieData data = new PieData(dataSet);
            pieChart.setData(data);
            pieChart.invalidate();
        } catch (JSONException e) {
            Toast.makeText(this, "Failed to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void showPopupMenu(View anchor) {
        View popupView = LayoutInflater.from(this).inflate(layout.popup_menu, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);

        Button btnPrimeUser = popupView.findViewById(id.btn_prime_user);
        btnPrimeUser.setOnClickListener(v -> {
            if ("guest".equals(userType)) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                Toast.makeText(getApplicationContext(), "You must sign up to access this feature!", Toast.LENGTH_LONG).show();
                startActivity(intent);
                popupWindow.dismiss();
            } else {
                startActivity(new Intent(MainActivity.this, CardListActivity.class));
                popupWindow.dismiss();
            }

        });

        Button btnSettings = popupView.findViewById(id.btn_settings);
        btnSettings.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            popupWindow.dismiss();

        });

        Button btnStockMarket = popupView.findViewById(R.id.btn_stock_market);
        btnStockMarket.setOnClickListener(v -> {
            if (!"guest".equals(userType)) {
                startActivity(new Intent(MainActivity.this, StockMarketActivity.class));
                popupWindow.dismiss();
            } else {
                Toast.makeText(getApplicationContext(), "You must sign up to access this feature!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, SignupActivity.class));
                popupWindow.dismiss();
            }
        });

        Button btnRecommend = popupView.findViewById(id.btn_recommend);
        btnRecommend.setOnClickListener(v -> {
            if ("guest".equals(userType)) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                Toast.makeText(getApplicationContext(), "You must sign up to access this feature!", Toast.LENGTH_LONG).show();
                startActivity(intent);
                popupWindow.dismiss();
            } else if (userType.equals("regular")) {
                Intent intent = new Intent(MainActivity.this, CardListActivity.class);
                Toast.makeText(getApplicationContext(), "You must upgrade to premium to access this feature!", Toast.LENGTH_LONG).show();
                startActivity(intent);
            } else {
                startActivity(new Intent(MainActivity.this, RecommendationActivity.class));
                popupWindow.dismiss();
            }

        });

        Button btnFamily = popupView.findViewById(id.btn_Family_Plan);
        btnFamily.setOnClickListener(v -> {
            if ("guest".equals(userType)) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                Toast.makeText(getApplicationContext(), "You must sign up to access this feature!", Toast.LENGTH_LONG).show();
                startActivity(intent);
                popupWindow.dismiss();
            } else {
                startActivity(new Intent(MainActivity.this, FamilyPlanActivity.class));
                popupWindow.dismiss();
            }

        });

        Button btnGoal = popupView.findViewById(id.btn_Goals);
        btnGoal.setOnClickListener(v -> {
            if ("guest".equals(userType)) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                Toast.makeText(getApplicationContext(), "You must sign up to access this feature!", Toast.LENGTH_LONG).show();
                startActivity(intent);
                popupWindow.dismiss();
            } else {
                startActivity(new Intent(MainActivity.this, GoalListActivity.class));
                popupWindow.dismiss();
            }
        });
        Button btnBudget = popupView.findViewById(id.btn_Budget);
        btnBudget.setOnClickListener(v -> {
            if ("guest".equals(userType)) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                Toast.makeText(getApplicationContext(), "You must sign up to access this feature!", Toast.LENGTH_LONG).show();
                startActivity(intent);
                popupWindow.dismiss();
            }
            else{
                startActivity(new Intent(MainActivity.this, BudgetingActivity.class));
                popupWindow.dismiss();
            }
        });


        popupWindow.showAsDropDown(anchor, 0, 0, Gravity.END);
    }

    private void sendExpenseToBackend(String category, double amount) {
        if (LoginActivity.UUID == null || LoginActivity.UUID.trim().isEmpty()) {
            Toast.makeText(MainActivity.this, "User is not logged in or UUID is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject putData = new JSONObject();
        try {
            putData.put(category, amount);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, "http://coms-309-056.class.las.iastate.edu:8080/expenses/" + selectedMemberId, putData,
                    response -> {
                        Toast.makeText(MainActivity.this, "Expense updated successfully!", Toast.LENGTH_SHORT).show();
                        fetchChartData(); // Update the pie chart after a successful update
                    },
                    error -> Toast.makeText(MainActivity.this, "Error updating expense: " + error.toString(), Toast.LENGTH_SHORT).show());
            Volley.newRequestQueue(this).add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to create JSON for PUT request", Toast.LENGTH_SHORT).show();
        }
    }


    private void fetchFamilyMembers() {
        String url = "http://coms-309-056.class.las.iastate.edu:8080/family/ofUser/" + LoginActivity.UUID.replace("\"", "");
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray membersArray = jsonObject.getJSONArray("users");
                        ArrayList<JSONObject> membersList = new ArrayList<>();
                        for (int i = 0; i < membersArray.length(); i++) {
                            JSONObject memberObject = membersArray.getJSONObject(i);
                            membersList.add(memberObject);
                        }
                        ArrayAdapter<JSONObject> adapter = new ArrayAdapter<JSONObject>(MainActivity.this, android.R.layout.simple_spinner_item, membersList) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                                try {
                                    textView.setText(getItem(position).getString("firstName") + " " + getItem(position).getString("lastName"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                return view;
                            }

                            @Override
                            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                                try {
                                    textView.setText(getItem(position).getString("firstName") + " " + getItem(position).getString("lastName"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                return view;
                            }
                        };
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerFamilyMembers.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(MainActivity.this, "Error fetching family members: " + error.getMessage(), Toast.LENGTH_SHORT).show());
        Volley.newRequestQueue(this).add(request);
    }



    private void fetchCurrencyExchangeRates() {
        if (LoginActivity.UUID != null) {
            String url = "http://coms-309-056.class.las.iastate.edu:8080/" + LoginActivity.UUID.replace("\"", "") + "/getCurrency";

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {
                        Log.d("CurrencyResponse", "Response from backend: " + response); // Log the response
                        updateCurrencyDisplay(response);
                    },
                    error -> {
                        Log.e("CurrencyError", "Error fetching currency settings: " + error.toString()); // Log any errors
                        Toast.makeText(MainActivity.this, "Error fetching currency settings: " + error.toString(), Toast.LENGTH_SHORT).show();
                    });

            Volley.newRequestQueue(this).add(stringRequest);
        }
    }

    private void updateCurrencyDisplay(String settings) {
        String[] currencies = settings.split(",");
        if (currencies.length > 0) {
            getExchangeRate(currencies[0], selectedCurrency1);
        }
        if (currencies.length > 1) {
            getExchangeRate(currencies[1], selectedCurrency2);
        }
    }

    private void getExchangeRate(String currencyCode, TextView textViewToUpdate) {

        String url = "https://v6.exchangerate-api.com/v6/00db35a8a637ac23306484f0/latest/USD";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("ExchangeRateResponse", response.toString());
                    try {
                        JSONObject conversionRates = response.getJSONObject("conversion_rates");
                        double rate = conversionRates.getDouble(currencyCode);

                        String rateText = String.format("%s/USD: %.3f", currencyCode, rate);
                        runOnUiThread(() -> textViewToUpdate.setText(rateText));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (!("guest".equals(userType))) {
                            Toast.makeText(MainActivity.this, "Failed to parse currency exchange rates", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                error -> Toast.makeText(MainActivity.this, "Error fetching currency exchange rates: " + error.toString(), Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        exchangeRateHandler.removeCallbacks(exchangeRateRunnable);
        stockUpdateHandler.removeCallbacks(stockUpdateRunnable);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_FILE && resultCode == RESULT_OK && data != null) {
            Uri pdfUri = data.getData();
            uploadFileToGroupDocs(pdfUri);
        }
    }

    private void uploadFileToGroupDocs(Uri fileUri) {
        executor.execute(() -> {
            try {
                InputStream iStream = getContentResolver().openInputStream(fileUri);
                byte[] inputData = getBytesFromInputStream(iStream);
                File file = new File(getCacheDir(), "upload.pdf");

                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(inputData);
                }

                if (file.exists() && file.canRead() && file.length() > 0) {
                    UploadFileRequest request = new UploadFileRequest("folder/upload.pdf", file, "319money");
                    FilesUploadResult response = fileApi.uploadFile(request);
                    if (!response.getUploaded().isEmpty()) {
                        convertFile("folder/upload.pdf");
                    }
                } else {
                    Log.e("UploadFile", "File does not exist or cannot be read.");
                }
            } catch (Exception e) {
                Log.e("UploadFile", "Unexpected exception: " + e.getMessage(), e);
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }


    private void convertFile(String uploadedFilePath) {
        try {
            ConvertSettings settings = new ConvertSettings();
            settings.setFilePath(uploadedFilePath);
            settings.setFormat("xlsx");
            settings.setOutputPath("folder/output.xlsx");

            ConvertDocumentRequest request = new ConvertDocumentRequest(settings);
            List<StoredConvertedResult> response = convertApi.convertDocument(request);
            if (!response.isEmpty()) {
                downloadConvertedFile("folder/output.xlsx");
            }
        } catch (ApiException e) {
            Toast.makeText(this, "Conversion failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadConvertedFile(String outputPath) {
        try {
            DownloadFileRequest request = new DownloadFileRequest(outputPath, "319money", null);
            File result = fileApi.downloadFile(request);
            if (result != null) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "File downloaded successfully.", Toast.LENGTH_SHORT).show();
                    // Now upload the file
                    uploadXlsxFile(result);
                });
            }
        } catch (ApiException e) {
            Toast.makeText(this, "Download failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, length);
        }
        return byteArrayOutputStream.toByteArray();
    }

    private void GetRequest() {
          String BASE_URL = "http://coms-309-056.class.las.iastate.edu:8080/expenses/"+selectedMemberId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    P = response.getDouble("personal");
                    W = response.getDouble("work");
                    O = response.getDouble("other");
                    H = response.getDouble("home");
                    Log.d("Personal", String.valueOf(P));
                    Log.d("Work", String.valueOf(W));
                    Log.d("Other", String.valueOf(O));
                    Log.d("Home", String.valueOf(H));



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

    public void uploadXlsxFile(File xlsxFile) {
        String userId = selectedMemberId;
        String url = "http://coms-309-056.class.las.iastate.edu:8080/statement/userId/" + userId;

        Log.d("UploadXlsxFile", "Preparing to upload file: " + xlsxFile.getAbsolutePath());
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                response -> {
                    // Handle the successful response (you can also parse response here)
                    Toast.makeText(this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
                    // Update chart data for this user
                    fetchAndDisplayData("http://coms-309-056.class.las.iastate.edu:8080/expenses/" + userId);
                },
                error -> {
                    // Handle the error
                    Toast.makeText(this, "Error uploading file: " + error.toString(), Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                byte[] byteArray = convertFileToByteArray(xlsxFile);
                Log.d("UploadXlsxFile", "Byte array length for file: " + byteArray.length);
                if (byteArray.length == 0) {
                    Log.e("UploadXlsxFile", "File data is empty.");
                }
                params.put("file", new DataPart(xlsxFile.getName(), byteArray, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                return params;
            }
        };

        Log.d("UploadXlsxFile", "Adding multipart request to queue");
        Volley.newRequestQueue(this).add(multipartRequest);
    }


    private byte[] convertFileToByteArray(File file) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, read);
            }
        } catch (IOException e) {
            Log.e("UploadXlsxFile", "Error reading file to byte array", e);
            return null; // Make sure to handle this case in your calling code.
        }
        return bos.toByteArray();
    }

    public void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted. Request for permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PICK_PDF_FILE);
        } else {
            // Permission has already been granted. You can perform your file operation here.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Call super method first
        switch (requestCode) {
            case PICK_PDF_FILE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }

}