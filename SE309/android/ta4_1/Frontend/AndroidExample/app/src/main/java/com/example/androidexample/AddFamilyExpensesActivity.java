package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddFamilyExpensesActivity extends AppCompatActivity {

    private EditText p, w, h, o;
    private Button conf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_family_expenses);

        conf = findViewById(R.id.btn_conf_fam);
        p = findViewById(R.id.fam_personal);
        w = findViewById(R.id.fam_work);
        h = findViewById(R.id.fam_home);
        o = findViewById(R.id.fam_other);

        conf.setOnClickListener(v -> {
            try {
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("personal", Double.parseDouble(p.getText().toString()));
                jsonBody.put("work", Double.parseDouble(w.getText().toString()));
                jsonBody.put("home", Double.parseDouble(h.getText().toString()));
                jsonBody.put("other", Double.parseDouble(o.getText().toString()));

                sendPostRequest(jsonBody);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void sendPostRequest(JSONObject jsonBody) {
        String exp_URL = "http://coms-309-056.class.las.iastate.edu:8080/expenses/" + FamilyPlanActivity.famID;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, exp_URL,
                response -> {
                    Toast.makeText(AddFamilyExpensesActivity.this, "Expenses added successfully", Toast.LENGTH_LONG).show();
                    // Redirect to MainActivity
                    Intent intent = new Intent(AddFamilyExpensesActivity.this, MainActivity.class);
                    startActivity(intent);
                },
                error -> Toast.makeText(AddFamilyExpensesActivity.this, "Error: " + error.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            public byte[] getBody() {
                return jsonBody.toString().getBytes();
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
