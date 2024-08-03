package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class BudgetingActivity extends AppCompatActivity{

    private Button cr_btn, hm_btn, v_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budgeting_layout);


        cr_btn = findViewById(R.id.budg_create_btn);
        hm_btn = findViewById(R.id.budget_hm);
        v_btn = findViewById(R.id.budg_view_btn);


        cr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BudgetingActivity.this, BudgetingAddActivity.class));
            }
        });

        hm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BudgetingActivity.this, MainActivity.class));
            }
        });
        v_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BudgetingActivity.this, BudgetingViewActivity.class));
            }
        });



    }

}
