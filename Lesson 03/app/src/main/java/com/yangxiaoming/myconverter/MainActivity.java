package com.yangxiaoming.myconverter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout outerLayout;
    private LinearLayout buttonLayout;
    private LinearLayout innerLayout1;
    private LinearLayout innerLayout2;

    private EditText fromText;
    private EditText toText;
    private Button convertButton;
    private Button switchButton;

    //private TextView answerText;
    private TextView bannerText;
    private TextView fromLabel;
    private TextView fromUnit;
    private TextView toLabel;
    private TextView toUnit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        outerLayout = new LinearLayout(this);
        innerLayout1 = new LinearLayout(this);
        innerLayout2 = new LinearLayout(this);

        buttonLayout = new LinearLayout(this);
        fromText = new EditText(this);
        toText = new EditText(this);
        convertButton = new Button(this);
        switchButton = new Button(this);
        
        //answerText = new TextView(this);
        bannerText = new TextView(this);
        fromLabel = new TextView(this);
        toLabel = new TextView(this);
        fromUnit = new TextView(this);
        toUnit = new TextView(this);


        outerLayout.setOrientation(LinearLayout.VERTICAL);
        outerLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        outerLayout.setBackgroundColor(Color.BLUE);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        innerLayout1.setOrientation(LinearLayout.HORIZONTAL);
        innerLayout1.setGravity(Gravity.CENTER_HORIZONTAL);
        innerLayout2.setOrientation(LinearLayout.HORIZONTAL);
        innerLayout2.setGravity(Gravity.CENTER_HORIZONTAL);

        bannerText.setWidth(300);
        bannerText.setGravity(Gravity.CENTER_HORIZONTAL);
        bannerText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
        bannerText.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        bannerText.setText("Temperature Converter");
        bannerText.setTextColor(Color.YELLOW);
        bannerText.setPadding(0, 0, 0, 10);

        fromText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        toText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);


        fromLabel.setText("From");
        fromLabel.setWidth(50);
        fromUnit.setText("K (Fahrenheit)");
        fromUnit.setTextColor(Color.WHITE);
        fromUnit.setWidth(90);
        toLabel.setText("To");
        toLabel.setWidth(50);
        toUnit.setText("C (Celsius)");
        toUnit.setWidth(90);
        toUnit.setTextColor(Color.WHITE);

        convertButton.setText("Convert");
        convertButton.setWidth(90);
        switchButton.setText("Switch");
        switchButton.setWidth(90);


      /*  answerText.setText("0");
        answerText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        answerText.setPadding(0, 20, 0, 20);
        answerText.setWidth(150);
        answerText.setGravity(Gravity.CENTER_HORIZONTAL);
        answerText.setTextColor(Color.WHITE);*/

        convertButton.setOnClickListener(convertClicked);
        switchButton.setOnClickListener(switchClicked);

        outerLayout.addView(bannerText);
        innerLayout1.addView(fromLabel);
        innerLayout1.addView(fromText);
        innerLayout1.addView(fromUnit);
        innerLayout2.addView(toLabel);
        innerLayout2.addView(toText);
        innerLayout2.addView(toUnit);
        buttonLayout.addView(convertButton);
        buttonLayout.addView(switchButton);
        outerLayout.addView(innerLayout1);
        outerLayout.addView(innerLayout2);

        outerLayout.addView(buttonLayout);



        setContentView(outerLayout);

        fromText.setLayoutParams(new LinearLayout.LayoutParams(150, 50));
        toText.setLayoutParams(new LinearLayout.LayoutParams(150, 50));
        convertButton.setLayoutParams(new LinearLayout.LayoutParams(100, 50));

    }

    private OnClickListener convertClicked = new OnClickListener() {

        @Override
        public void onClick(View v) {
            String fromString = fromText.getText().toString();
            String unit = fromUnit.getText().toString();
            if(!fromString.equals("") && unit.equals("K (Fahrenheit)")) {
                double fromtNumber = Double.parseDouble(fromString);
                double toNumber = (fromtNumber - 32) * 5 / 9;
                String resultString = String.format("%.2f", toNumber);
                toText.setText(resultString);
            }
            if(!fromString.equals("") && unit.equals("C (Celsius)")) {
                double fromtNumber = Double.parseDouble(fromString);
                double toNumber = (fromtNumber + 32) * 9 / 5;
                String resultString = String.format("%.2f", toNumber);
                toText.setText(resultString);
            }
        }
    };

    private OnClickListener switchClicked = new OnClickListener() {

        @Override
        public void onClick(View v) {

            String fromUnitString = fromUnit.getText().toString();
            String toUnitString = toUnit.getText().toString();
            fromUnit.setText(toUnitString);
            toUnit.setText(fromUnitString);
        }
    };

}

