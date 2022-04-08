package com.example.justjava;

import static com.example.justjava.R.id.order_button;
import static com.example.justjava.R.id.order_summary_text_view;

import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    int quantity = 2;
    Spinner type;
    int mCoffeeSelected = -1;
    TextView orderSummaryTextView, name;
    MediaPlayer player ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        To start the service
        startService(new Intent(this,backgroundMusicService.class)) ;
        type = findViewById(R.id.spinner);
        name = findViewById(R.id.userName);
//        orderSummaryTextView = findViewById(order_summary_text_view);
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCoffeeSelected = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    /*
     * Extracts the userName
     *
     * @return name of User*/
    private String userName() {
        TextView name = findViewById(R.id.userName);
        Log.v("username", " " + name.getText());
        return name.getText().toString();
    }

    private int[] coffeeSelected() {
        return new int[]{1};
    }

    /*
     * Tells which topping is needed by customer
     *
     * @param checkbox id
     * @return Toppings asked or not
     * */
    private boolean toppingsAsked(View view) {
        CheckBox checkBox = (CheckBox) view;
        return checkBox.isChecked();
    }

    private int priceOfCoffee(int i) {
        /*
         * Latte -> $5*
         * Espresso -> $6*
         * Cappuccino -> $7
         * */

        ArrayList<Integer> coffeePrices = new ArrayList<>();
        coffeePrices.add(5);
        coffeePrices.add(6);
        coffeePrices.add(7);
        return coffeePrices.get(i);
    }


    public void increment(View view) {
        if (quantity == 100) {
            Toast.makeText(this, "You cannot have more than hundred coffee", Toast.LENGTH_SHORT).show();
            return;
        }
        displayQuantity(++quantity);
    }

    public void decrement(View view) {
        if (quantity == 1) {
            Toast.makeText(this, "You cannot have less than one coffee", Toast.LENGTH_SHORT).show();
            return;
        }
        displayQuantity(--quantity);
    }


    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        if (doValidate()) return;
        String message = createOrderSummary();
        Log.d("Working", "" + message);
        displayMessage(message);
    }

    /*
     * This method validates whether user name is entered and type of coffee is selected
     * or not
     *
     * @return boolean
     * True -> Invalid
     * False -> Valid */
    private boolean doValidate() {
        Log.d("Working", "" + name.getText());
        if (name.getText().toString().isEmpty()) {
            name.setError("Enter the username");
            return true;
        }

        if (type.getSelectedItemPosition() == 0) {
            TextView errorText = (TextView) type.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText(R.string.Error_Spinner);//changes the selected item text to this
            return true;
        }
        return false;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int quantity) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + quantity);
    }

    private void displayMessage(String message) {
//        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        Log.d("Working", "True");
        orderSummaryTextView = findViewById(order_summary_text_view);
        orderSummaryTextView.setText(message);
        View orderButton=findViewById(order_button) ;
        orderButton.setVisibility(View.VISIBLE);
    }

    /*
     * Create summary of order
     *
     * @param price of order
     * @return text summary
     * */
    private String createOrderSummary() {
        Log.d("Working", "True");
        boolean hasWhippedCream = false, hasChocolate = false;

        String orderSummary = " Name: " + userName();
//        Type of Coffee
        Spinner spinner = findViewById(R.id.spinner);
        String coffeeType = spinner.getSelectedItem().toString();
        orderSummary += "\n Coffee is " + coffeeType;
//        whether whipped cream is ordered or not
        View toppingWhipped = findViewById(R.id.whippedCream);
        if (toppingsAsked(toppingWhipped)) {
            orderSummary += "\n Add whipped cream? true";
            hasWhippedCream = true;
        } else
            orderSummary += "\n Add whipped cream? false";

//      whether chocolate is ordered or not
        View toppingChocolate = findViewById(R.id.chocolate);
        if (toppingsAsked(toppingChocolate)) {
            orderSummary += "\n Add Chocolate? true";
            hasChocolate = true;
        } else
            orderSummary += "\n Add Chocolate? false";

        orderSummary += "\n Quantity: " + quantity;
        orderSummary += "\n Total: $" + calculateBill(hasChocolate, hasWhippedCream);
        orderSummary += "\n Thank You!";

        return orderSummary;
    }

    /*
     *
     * Calculates the bill
     *
     * @param hasChocolate, hasWhippedCream
     * @return totalAmount*/
    private int calculateBill(boolean hasChocolate, boolean hasWhippedCream) {
        /*
         * base Price or price of coffee without any topping is $5
         * Prices of toppings
         * Whipped Cream -> $1
         * Chocolate -> $2  */

        /* Latte -> $5
         * Espresso -> $6
         * Cappuccino -> $7
         * */
        int basePrice = priceOfCoffee(coffeeSelected()[0]);

        if (hasChocolate)
            basePrice += 2;
        if (hasWhippedCream)
            basePrice += 1;

        return basePrice * quantity;
    }
}