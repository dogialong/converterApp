package com.example.longdg.converterapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewConversionActivity extends Activity {

    private EditText etFromUnits;
    private EditText etToUnits;
    private EditText etFactor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_conversion);

        etFromUnits = (EditText) findViewById(R.id.fromText);
        etToUnits = (EditText) findViewById(R.id.toText);
        etFactor = (EditText) findViewById(R.id.factorText);
    }

    public void addClicked(View view) {
        //get the texts:
        String fromUnits = etFromUnits.getText().toString();
        String toUnits = etToUnits.getText().toString();
        String factor = etFactor.getText().toString();
        //make sure none are empty:
        if (fromUnits.isEmpty() || toUnits.isEmpty() || factor.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        } else {
            //convert factor to float:
            float multiplier = Float.parseFloat(factor);
            //make the conversion string:
            String conversion = fromUnits + " to " + toUnits;
            //get the intent, and add data:
            Intent data = getIntent();
            Bundle bundle = new Bundle();
            bundle.putString("conversion", conversion);
            bundle.putFloat("multiplier", multiplier);
            data.putExtra("conversionData", bundle);
            //send the result back:
            this.setResult(Activity.RESULT_OK, data);
            //close the activity:
            finish();
        }
    }
}
