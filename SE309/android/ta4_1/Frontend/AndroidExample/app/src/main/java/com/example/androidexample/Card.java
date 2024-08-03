package com.example.androidexample;

import org.json.JSONException;
import org.json.JSONObject;

public class Card {
    private String id;
    private String name;
    private String cardNumber;
    private String expirationDate;
    private String cvv;

    public Card(String name, String cardNumber, String expirationDate, String cvv) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }


    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("cardNumber", cardNumber);
            jsonObject.put("expirationDate", expirationDate);
            jsonObject.put("cvv", cvv);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static Card fromJson(JSONObject jsonObject) throws JSONException {
        Card card = new Card(
                jsonObject.getString("name"),
                jsonObject.getString("cardNumber"),
                jsonObject.getString("expirationDate"),
                jsonObject.getString("cvv")
        );
        if (jsonObject.has("id")) {
            card.id = jsonObject.getString("id");
        }
        return card;
    }

    public String getMaskedNumber() {
        // Mask all but the last 4 digits of the card number for security
        return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
    }
}
