package com.pabi.pizzaordering;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends ActionBarActivity {

    int numberOfPizzas = 0;
    double price = 0;
    boolean hasExtraCheese;
    boolean hasBacon;
    boolean hasAvo;
    boolean hasFiveSausages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText nameField = (EditText) findViewById(R.id.name_field);
        nameField.setCursorVisible(true);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                ((EditText) v).setCursorVisible(true);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    ((EditText) v).setCursorVisible(false);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
    /**
     * This method is called when the + button is clicked.
     */
    public  void increment(View view) {
        if (numberOfPizzas <= 49) {
            numberOfPizzas = numberOfPizzas + 1;
            display(numberOfPizzas);
            displayPrice(numberOfPizzas,hasExtraCheese,hasBacon,hasAvo,hasFiveSausages);
        } else {
            Toast.makeText(this, getString(R.string.fifty_pizzas), Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * This method is called when the - button is clicked.
     */
    public void decrement(View view) {
        if (numberOfPizzas >= 1) {
            numberOfPizzas = numberOfPizzas - 1;
            display(numberOfPizzas);
            displayPrice(numberOfPizzas,hasExtraCheese,hasBacon,hasAvo,hasFiveSausages);
        } else {
            Toast.makeText(this, getString(R.string.negative_pizzas), Toast.LENGTH_SHORT).show();
        }
    }

    public void addExtraCheese(View view) {
        CheckBox extraCheeseBox = (CheckBox) findViewById(R.id.extra_cheese);
        hasExtraCheese = extraCheeseBox.isChecked();
        displayPrice(numberOfPizzas, hasExtraCheese, hasBacon, hasAvo, hasFiveSausages);
    }

    public void addBacon(View view) {
        CheckBox baconCheckBox = (CheckBox) findViewById(R.id.bacon);
        hasBacon = baconCheckBox.isChecked();
        displayPrice(numberOfPizzas, hasExtraCheese, hasBacon, hasAvo, hasFiveSausages);
    }

    public void addAvo(View view) {
        CheckBox avoCheckBox = (CheckBox) findViewById(R.id.avo);
        hasAvo = avoCheckBox.isChecked();
        displayPrice(numberOfPizzas, hasExtraCheese, hasBacon, hasAvo, hasFiveSausages);
    }

    public void addFiveSausages(View view) {
        CheckBox fiveSausagesCheckBox = (CheckBox) findViewById(R.id.five_sausages);
        hasFiveSausages = fiveSausagesCheckBox.isChecked();
        displayPrice(numberOfPizzas, hasExtraCheese, hasBacon, hasAvo, hasFiveSausages);
    }

    public void submitOrder(String message, EditText nameField, CheckBox extraCheeseCheckBok, CheckBox baconCheckBox, CheckBox avoCheckBox, CheckBox fiveSausagesCheckBox) {
        TextView confirmTextView = (TextView) findViewById(R.id.confirmation);
        confirmTextView.setText(message);
        Button emailButton = (Button) findViewById(R.id.email_confirm);
        emailButton.setVisibility(View.VISIBLE);
        Button orderButton = (Button) findViewById(R.id.order);
        orderButton.setText(getString(R.string.done));
        orderButton.setTextSize(20);
        orderButton.setTextColor(android.graphics.Color.WHITE);
        orderButton.setBackgroundColor(Color.TRANSPARENT);
        orderButton.setClickable(false);
        Button incrementButton = (Button) findViewById(R.id.increment);
        incrementButton.setClickable(false);
        Button decrementButton = (Button) findViewById(R.id.decrement);
        decrementButton.setClickable(false);
        nameField.setFocusable(false);
        nameField.setFocusableInTouchMode(false);
        extraCheeseCheckBok.setClickable(false);
        baconCheckBox.setClickable(false);
        avoCheckBox.setClickable(false);
        fiveSausagesCheckBox.setClickable(false);
        TextView priceTextView = (TextView)findViewById(R.id.price_text_view);
        priceTextView.setVisibility(View.INVISIBLE);
    }

    public void composeEmail(String subject, String text) {
        Intent emailSummary = new Intent(Intent.ACTION_SENDTO);
        emailSummary.setData(Uri.parse("mailto:"));
        emailSummary.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailSummary.putExtra(Intent.EXTRA_TEXT, text);
        if (emailSummary.resolveActivity(getPackageManager()) != null) {
            startActivity(emailSummary);
        }
    }

    public void confirmEmail(View view) {
        EditText nameField = (EditText)findViewById(R.id.name_field);
        String name = nameField.getText().toString();
        TextView confirmField = (TextView)findViewById(R.id.confirmation);
        String confirmMessage = confirmField.getText().toString();
        composeEmail(getString(R.string.email_subject) + name, confirmMessage);
    }

    public void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    public void displayPrice(int quantity,boolean hasExtraCheese, boolean hasBacon, boolean hasAvo, boolean hasFiveSausages) {
        double priceBacon = 0.7;
        double priceExtraCheese = 0.5;
        double priceAvo = 0.3;
        double priceFiveSausages = 0.15;
        price = quantity*3;
        if (hasExtraCheese) {
            price += priceExtraCheese * quantity;
        }
        if (hasBacon) {
            price += priceBacon* quantity;
        }
        if(hasAvo) {
            price += priceAvo * quantity;
        }
        if(hasFiveSausages) {
            price += priceFiveSausages * quantity;
        }
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(getString(R.string.price) +": " + NumberFormat.getCurrencyInstance().format(price));
    }

    public void displayOrderSummary(View view) {
        createOrderSummary(numberOfPizzas, price);
    }
    public void createOrderSummary(int quantity, double total){
        EditText nameField = (EditText)findViewById(R.id.name_field);
        String name = nameField.getText().toString();
        CheckBox extraCheeseCheckBox = (CheckBox) findViewById(R.id.extra_cheese);
        CheckBox baconCheckBox = (CheckBox) findViewById(R.id.bacon);
        CheckBox avoCheckBox = (CheckBox)findViewById(R.id.avo);
        CheckBox fiveSausagesCheckBox = (CheckBox)findViewById(R.id.five_sausages);
        boolean hasExtraCheese = extraCheeseCheckBox.isChecked();
        boolean hasBacon = baconCheckBox.isChecked();
        boolean hasAvo = avoCheckBox.isChecked();
        boolean hasFiveSausages = fiveSausagesCheckBox.isChecked();
        String confirmMessage;
        if (name.trim().length() == 0) {
            Toast.makeText(this, getString(R.string.name_required), Toast.LENGTH_SHORT).show();
        }
        else{
            if (numberOfPizzas == 0) {
                Toast.makeText(this, getString(R.string.make_selection), Toast.LENGTH_SHORT).show();

            } else {
                confirmMessage = getString(R.string.name) + ": " + name + "\n" +getString(R.string.quantity)  + numberOfPizzas;
                if (hasExtraCheese) {
                    confirmMessage += "\n" + getString(R.string.with_extra_cheese);
                }
                if (hasBacon) {
                    confirmMessage += "\n" + getString(R.string.with_bacon);
                }
                if (hasAvo) {
                    confirmMessage += "\n" + getString(R.string.with_avo);
                }
                if (hasFiveSausages) {
                    confirmMessage += "\n" + getString(R.string.with_five_sausages);
                }
                confirmMessage += "\n" + getString(R.string.total) + ": " + NumberFormat.getCurrencyInstance().format(price) + "\n" + getString(R.string.thank_you);
                submitOrder(confirmMessage, nameField, extraCheeseCheckBox, baconCheckBox, avoCheckBox, fiveSausagesCheckBox);
            }
        }
    }
}
