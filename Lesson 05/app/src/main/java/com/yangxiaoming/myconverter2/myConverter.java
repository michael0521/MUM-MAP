package com.yangxiaoming.myconverter2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class myConverter extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_converter);
    }

    public void convert(View source){
        TextView fromText = (TextView) findViewById(R.id.from);
        TextView fromUnit= (TextView) findViewById(R.id.unitK);
        TextView toText = (TextView) findViewById(R.id.to);


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

    public void exchange(View source){
        TextView fromUnit= (TextView) findViewById(R.id.unitK);
        TextView toUnit= (TextView) findViewById(R.id.unitC);
        String fromUnitString = fromUnit.getText().toString();
        String toUnitString = toUnit.getText().toString();
        fromUnit.setText(toUnitString);
        toUnit.setText(fromUnitString);
    }
}
