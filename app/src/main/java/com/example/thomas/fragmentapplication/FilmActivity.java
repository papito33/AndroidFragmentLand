package com.example.thomas.fragmentapplication;

import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.thomas.utils.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FilmActivity extends ActionBarActivity {

    public static final String TAG_FILM_ACTIVITY="film activity en cours";
    private static final String TAG_NAME_FILM_ROW = "rowTextViewTitle";
    private static final String TAG_ID_FILM_ROW = "idFilm";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        JSONObject obj = new JSONObject();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);

        try {
            obj = new JSONObject(getIntent().getStringExtra(MainActivity.JSON_FILM));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.v("obj",obj.toString());
        Bundle bundle = new Bundle();
        bundle.putString("json", obj.toString());
        PlaceholderFragment fragobj = new PlaceholderFragment();
        fragobj.setArguments(bundle);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragobj)
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_film, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        JSONArray jsonFilm = null;
        List<HashMap<String, String>> oslist;
        ListView list;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_film, container, false);
            oslist = new ArrayList<HashMap<String, String>>();
            list= (ListView) rootView.findViewById(R.id.filmListView);
            String obj= getArguments().getString("json");
            Log.v("OBJJJJJJ","toto" +obj);
            try {
                populateListViewForFilm(new JSONObject(obj));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();


        }

        public void populateListViewForFilm(JSONObject json){
            Log.v("populateListViewForFilm", json.toString());
            try {
                // Getting JSON Array from URL
                jsonFilm = json.getJSONArray("Search");
                Log.v("ARRAY JSON", jsonFilm.toString());
                for(int i = 0; i < jsonFilm.length(); i++){
                    JSONObject c = jsonFilm.getJSONObject(i);
                    // Storing  JSON item in a Variable
                    String name = c.getString("Title");
                    String idFilm = c.getString("imdbID");
                    Log.v("NAME FILM", name);
                    Log.v("ID FILM", idFilm);
                    // Adding value HashMap key => value
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_NAME_FILM_ROW, name);
                    map.put(TAG_ID_FILM_ROW, idFilm);
                    oslist.add(map);

                    //Log.v("list" , String.valueOf(list.getId()));
                    ListAdapter adapter = new SimpleAdapter(getActivity(), oslist,
                            R.layout.film_list_item,
                            new String[] { TAG_NAME_FILM_ROW,TAG_ID_FILM_ROW}, new int[] {
                            R.id.rowTextViewTitle,R.id.idFilm});

                    list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Toast.makeText(getActivity(), "You Clicked at " + oslist.get(+position).get(TAG_NAME_FILM_ROW) + "id" +oslist.get(+position).get(TAG_ID_FILM_ROW), Toast.LENGTH_SHORT).show();
                        searchFilmById(oslist.get(+position).get(TAG_ID_FILM_ROW));
                    }
                });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        public void searchFilmById(String idFilm){

            StringBuilder url = new StringBuilder();
            url.append(MainActivity.URLAPIFILM);
            url.append("i=");
            url.append(idFilm);
            url.append("&plot=full");
            url.append("&r=json");
            new JSONParse(url.toString()).execute();
        }
        public class JSONParse extends AsyncTask<String, String, JSONObject> {
            String url;
            String JSON_FILM = "JSON_FILM";
            public JSONParse(String url){
                this.url = url;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();


            }
            @Override
            protected JSONObject doInBackground(String... args) {
                JsonParser jParser = new JsonParser();
                // Getting JSON from URL
                JSONObject json = jParser.getJSONFromUrl(url);
                return json;
            }
            @Override
            protected void onPostExecute(JSONObject json) {

                super.onPostExecute(json);
                Intent intent = new Intent(getActivity(), UniqueFilmActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(JSON_FILM,json.toString());
                startActivity(intent);
            }
        }


    }


}
