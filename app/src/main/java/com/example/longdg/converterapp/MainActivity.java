package com.example.longdg.converterapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {


    private static ConversionDBHelper conversionHelper = null;
    private static SQLiteDatabase conversionDB = null;
    private static SimpleCursorAdapter cursorAdapter = null;
    private static ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get helper and db:
        conversionHelper = new ConversionDBHelper(this);
        conversionDB = conversionHelper.getWritableDatabase();
        listView = (ListView) findViewById(R.id.list);

        // cursor

        Cursor cursor = conversionDB.rawQuery("select _id, units from conversions", null);
        String[] columns = {cursor.getColumnName(1)};
        int[] displayViews = {R.id.listText};
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, columns, displayViews, 0);
        listView.setAdapter(cursorAdapter);
        listView.setOnItemClickListener(mOnItemClick);

    }

    AdapterView.OnItemClickListener mOnItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(MainActivity.this, ConversionActivity.class);
            TextView tv = (TextView) view;
            String units = tv.getText().toString();
            String select =
                    String.format("SELECT * FROM conversions WHERE units = '%s'", units);
            Cursor cursor = conversionDB.rawQuery(select, null);
            if (cursor.moveToFirst()) {
                int rowID = cursor.getInt(0);                   //_id
                String conversionUnits = cursor.getString(1);   //units
                Float conversionFactor = cursor.getFloat(2);    //factor

                //Log.i("conv", rowID + " " + conversionUnits + " " + conversionFactor);

                //bundle for data:
                Bundle bundle = new Bundle();
                bundle.putInt("id", rowID);
                bundle.putString("units", conversionUnits);
                bundle.putFloat("factor", conversionFactor);
                //add data to intent:
                intent.putExtra("conversionData", bundle);

                //start the conversion activity with an request code of 2:
                startActivityForResult(intent, 2);
            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //make sure result is OK:
        if (resultCode == Activity.RESULT_OK) {
            //which request code?
            switch (requestCode) {
                case 1: //request to add new conversion
                    //get the returned data:
                    Bundle dataBundle = data.getBundleExtra("conversionData");
                    String conversion = dataBundle.getString("conversion");
                    Float multiplier = dataBundle.getFloat("multiplier");
                    //post the new conversion to the database:
                    String insert =
                            "INSERT INTO conversions (units, factor) " +
                                    "VALUES ('" + conversion + "', '" +
                                    multiplier + "')";
                    conversionDB.execSQL(insert);
                    //update the list:
                    updateCursorAdaptor();
                    break;
                case 2:
                    Bundle bundle = data.getBundleExtra("conversionData");
                    int deleteID = bundle.getInt("id");
                    String delete =
                            "DELETE FROM conversions WHERE _id = " + deleteID;
                    conversionDB.execSQL(delete);
                    updateCursorAdaptor();
                    break;
            }
        }
    }
    private void addConversion() {
        //send intent to NewConversionActivity:
        Intent intent = new Intent(this, NewConversionActivity.class);
        startActivityForResult(intent, 1);
    }
    private void updateCursorAdaptor() {
        Cursor cursor = conversionDB.rawQuery("SELECT _id, units FROM conversions", null);
        String[] columns = {cursor.getColumnName(1)};
        int[] displayViews = {R.id.listText};
        cursorAdapter.changeCursor(cursor);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_add) {
            addConversion();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

