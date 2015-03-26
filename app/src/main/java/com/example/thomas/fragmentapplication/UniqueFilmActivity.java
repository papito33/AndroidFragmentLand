package com.example.thomas.fragmentapplication;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.thomas.CustomViews.ViewTest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class UniqueFilmActivity extends ActionBarActivity {

    private ViewTest myViewTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unique_film);
        myViewTest = (ViewTest) findViewById(R.id.custView);
        JSONObject obj = new JSONObject();
        try {
            obj = new JSONObject(getIntent().getStringExtra("JSON_FILM"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("film" ,obj.toString());
        try {
            myViewTest.setLabelText(obj.getString("Title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView plotFilm = (TextView) findViewById(R.id.plotFilm);
        try {
            plotFilm.setText(obj.getString("Plot"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_unique_film, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
