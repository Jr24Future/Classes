package com.example.card

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener
import com.braintreepayments.cardform.view.CardForm
import android.widget.Button
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private lateinit var cardForm : CardForm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cardForm = findViewById(R.id.cardForm)
        cardForm.cardRequired(true)
            .expirationRequired(true)
            .cvvRequired(true)
            .cardholderName(CardForm.FIELD_REQUIRED)
            .actionLabel("Purchase")
            .setup(this@MainActivity)
        cardForm.cvvEditText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD

        val btnBuy: Button = findViewById(R.id.btnBuy)
        btnBuy.setOnClickListener {
            if(cardForm.isValid) {
                Toast.makeText(
                    this@MainActivity,
                    "Card number: ${cardForm.cardNumber} " +
                            "Card expiry date: ${cardForm.expirationDateEditText.text} " +
                            "Card cvv: ${cardForm.cvvEditText.text} " +
                            "Card holder name: ${cardForm.cardholderNameEditText.text}",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(this@MainActivity, "Please complete the form", Toast.LENGTH_LONG).show()
            }
        }
    }
}