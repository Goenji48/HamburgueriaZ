package com.goenji48.hamburgueriaz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private CheckBox bacon, cheese, onionRings;
    private EditText usernameInput;
    private TextView orderPrice, orderSizeText;
    private int orderSize = 1;
    private float finalBurgerPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameInput = findViewById(R.id.username);
        orderPrice = findViewById(R.id.order_price);
        orderSizeText = findViewById(R.id.order_size);
        orderSizeText.setText(String.valueOf(orderSize));

        bacon = findViewById(R.id.checkbox_bacon);
        cheese = findViewById(R.id.checkbox_cheese);
        onionRings = findViewById(R.id.checkbox_onion_rings);

        Button btnAdd = findViewById(R.id.order_add);
        btnAdd.setOnClickListener(v -> {
            orderSize++;
            orderSizeText.setText(String.valueOf(orderSize));
        });

        Button btnRemove = findViewById(R.id.order_remove);
        btnRemove.setOnClickListener(v -> {
            if (orderSize != 1) {
                orderSize--;
                orderSizeText.setText(String.valueOf(orderSize));
            }
        });

        Button btnMakeOrder = findViewById(R.id.make_order_button);
        btnMakeOrder.setOnClickListener(v -> sendOrder());
    }

    private void sendOrder() {
        String personName = usernameInput.getText().toString().trim();
        if (!personName.isEmpty()) {
            String baconIncluded;
            String cheeseIncluded;
            String onionRingsIncluded;
            boolean addedBacon = bacon.isChecked();
            boolean addedCheese = cheese.isChecked();
            boolean addedOnionRings = onionRings.isChecked();

            if (addedBacon) {
                baconIncluded = "Sim";
            } else {
                baconIncluded = "Não";
            }

            if (addedCheese) {
                cheeseIncluded = "Sim";
            } else {
                cheeseIncluded = "Não";
            }

            if (addedOnionRings) {
                onionRingsIncluded = "Sim";
            } else {
                onionRingsIncluded = "Não";
            }

            float initialBurgerPrice = 20;

            finalBurgerPrice = setBurgerPrice(initialBurgerPrice, addedBacon,
                    addedCheese, addedOnionRings);

            Toast.makeText(this, "Abrindo aplicativo de e-mail padrão", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setType("plain/text");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Pedido de " + personName + " - " + "HamburgueriaZ");
            intent.putExtra(Intent.EXTRA_TEXT, printOrderResult(personName, baconIncluded,
                    cheeseIncluded, onionRingsIncluded));
            startActivity(Intent.createChooser(intent, "Enviar pedido"));

            orderPrice.setText(printOrderResult(personName, baconIncluded,
                    cheeseIncluded, onionRingsIncluded));
        } else {
            Toast.makeText(this, "Preencha seu nome", Toast.LENGTH_SHORT).show();
        }
    }

    private float setBurgerPrice(float burgerPrice, boolean addedBacon, boolean addedCheese, boolean addedOnionRings) {
        float finalBurgerPrice = burgerPrice;
        if (addedBacon) {
            finalBurgerPrice += 2;
        }

        if (addedCheese) {
            finalBurgerPrice += 2;
        }

        if (addedOnionRings) {
            finalBurgerPrice += 3;
        }
        return finalBurgerPrice * orderSize;
    }

    private String realPrice(double price) {
        Locale locale = new Locale.Builder().setLanguage("pt").setRegion("BR").build();
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        return numberFormat.format(price);
    }

    private String printOrderResult(String personName, String addedBacon,
                                    String addedCheese, String addedOnionRings) {
        return "Nome do cliente: " + personName + "\n"
                + "Tem Bacon? " + addedBacon + "\n"
                + "Tem Queijo? " + addedCheese + "\n"
                + "Tem Onion Rings? " + addedOnionRings + "\n"
                + "Quantidade: " + orderSize + "\n"
                + "Preço final: " + realPrice(finalBurgerPrice);
    }
}