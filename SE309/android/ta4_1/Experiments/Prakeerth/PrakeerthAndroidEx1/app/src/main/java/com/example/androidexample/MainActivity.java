package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;// define message textview variable
    private TextView secondText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */
        messageText = findViewById(R.id.main_msg_txt);// link to message textview in the Main activity XML
        secondText = findViewById(R.id.second_msg_txt);
        messageText.setText("Welcome to MoneyFlow Insight!");
        secondText.setText("MADE BY TA4_1");
    }
}