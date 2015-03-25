package com.example.thomas.fragmentapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;
import android.widget.TextView;

import com.example.thomas.utils.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {

    private static final String URLAPIFILM ="http://www.omdbapi.com/?";
    private static final String TAG_URL="url api film omdbapi";
    public static final String TAG_JSON= "json returned";
    public static final String JSON_FILM="com.example.fragmentapplication.MainActivity.JSON_FILM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent(this, PrefFragmentActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

     /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    public void changeFragment(View view){
        Intent intent = new Intent(this, SettingsFragmentActivity.class);
        startActivity(intent);
    }

    public void broadcastCustomIntent(View view)
    {
        Intent intent = new Intent("MyCustomIntent");

        EditText et = (EditText)findViewById(R.id.textIntentBroadCast);
        // add data to the Intent
        intent.putExtra("message", (CharSequence)et.getText().toString());
        intent.setAction("com.example.thomas.fragmentapplication.CUSTOM_INTENT");
        sendBroadcast(intent);
    }

    public void goToMap(View view){
        // Map point based on address
        Uri location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
        // Or map point based on latitude/longitude
        // Uri location = Uri.parse("geo:37.422219,-122.08364?z=14"); // z param is zoom level
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        startActivity(mapIntent);
    }

    public void searchFilm(View view){
        new JSONParse().execute();
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        StringBuilder url = new StringBuilder();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            EditText editTextFilm = (EditText)findViewById(R.id.searchFilm);
            url.append(URLAPIFILM);
            url.append("s="+editTextFilm.getText());
            url.append("&r=json");

            Log.v(TAG_URL,url.toString());
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            JsonParser jParser = new JsonParser();
            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url.toString());
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
                Log.v(TAG_JSON, json.toString());

                super.onPostExecute(json);
                Intent intent = new Intent(MainActivity.this, FilmActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(JSON_FILM,json.toString());
                getApplicationContext().startActivity(intent);
        }
    }

}
