package com.example.androidexample;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    private CheckBox checkBoxEUR, checkBoxUSD, checkBoxJPY, checkBoxGBP, checkBoxAUD, checkBoxCAD, checkBoxCHF, checkBoxCNY, checkBoxSEK, checkBoxNZD;
    private Button btnApplyChanges;
    private ArrayList<String> selectedCurrencies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        checkBoxEUR = findViewById(R.id.checkBoxEUR);
        checkBoxUSD = findViewById(R.id.checkBoxUSD);
        checkBoxJPY = findViewById(R.id.checkBoxJPY);
        checkBoxGBP = findViewById(R.id.checkBoxGBP);
        checkBoxAUD = findViewById(R.id.checkBoxAUD);
        checkBoxCAD = findViewById(R.id.checkBoxCAD);
        checkBoxCHF = findViewById(R.id.checkBoxCHF);
        checkBoxCNY = findViewById(R.id.checkBoxCNY);
        checkBoxSEK = findViewById(R.id.checkBoxSEK);
        checkBoxNZD = findViewById(R.id.checkBoxNZD);
        btnApplyChanges = findViewById(R.id.btnApplyChanges);

        CompoundButton.OnCheckedChangeListener listener = (buttonView, isChecked) -> {
            String currency = buttonView.getText().toString();
            if (isChecked) {
                if (selectedCurrencies.size() < 2) {
                    selectedCurrencies.add(currency);
                } else {
                    buttonView.setChecked(false);
                    Toast.makeText(SettingsActivity.this, "You can select only up to two currencies", Toast.LENGTH_SHORT).show();
                }
            } else {
                selectedCurrencies.remove(currency);
            }
        };

        checkBoxEUR.setOnCheckedChangeListener(listener);
        checkBoxUSD.setOnCheckedChangeListener(listener);
        checkBoxJPY.setOnCheckedChangeListener(listener);
        checkBoxGBP.setOnCheckedChangeListener(listener);
        checkBoxAUD.setOnCheckedChangeListener(listener);
        checkBoxCAD.setOnCheckedChangeListener(listener);
        checkBoxCHF.setOnCheckedChangeListener(listener);
        checkBoxCNY.setOnCheckedChangeListener(listener);
        checkBoxSEK.setOnCheckedChangeListener(listener);
        checkBoxNZD.setOnCheckedChangeListener(listener);

        btnApplyChanges.setOnClickListener(v -> {
            if (!selectedCurrencies.isEmpty()) {
                sendSelectedCurrencies();
                finish(); // Close the SettingsActivity and go back to MainActivity
            } else {
                Toast.makeText(SettingsActivity.this, "Please select at least one currency", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendSelectedCurrencies() {
        // Join the selected currencies with a comma to match the backend's expected format
        String selectedSettings = TextUtils.join(",", selectedCurrencies);

        String url = "http://coms-309-056.class.las.iastate.edu:8080/" + LoginActivity.UUID.replace("\"", "") + "/setCurrency/" + selectedSettings;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> Toast.makeText(SettingsActivity.this, "Settings updated successfully!", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(SettingsActivity.this, "Error updating settings: " + error.toString(), Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}