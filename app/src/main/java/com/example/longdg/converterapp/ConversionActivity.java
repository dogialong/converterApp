package com.example.longdg.converterapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ConversionActivity extends Activity {

    private TextView tvUnits;
    private EditText etFrom;
    private TextView tvResult;
    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("conversionData");

        //get layout views:
        tvUnits = (TextView) findViewById(R.id.tvUnits);
        etFrom = (EditText)findViewById(R.id.etFrom);
        tvResult = (TextView)findViewById(R.id.tvResult);

        tvUnits.setText(bundle.getString("units"));
    }
    public void convertClicked(View view) {
        float factor = bundle.getFloat("factor");
        String strFrom = etFrom.getText().toString();
        if (strFrom.isEmpty()) {
            Toast.makeText(this, "Enter units to convert", Toast.LENGTH_SHORT).show();
        } else {
            float from = Float.parseFloat(strFrom);
            tvResult.setText(String.format("%f", from * factor));
        }
    }

    public void deleteClicked(View view) {
        //send the intent back, it already has the
        //id to delete...
        Intent intent = getIntent();
        this.setResult(RESULT_OK, intent);
        finish();
    }
}
