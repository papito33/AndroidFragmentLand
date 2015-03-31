package com.example.thomas.fragmentapplication;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thomas.CustomViews.ViewTest;
import com.example.thomas.utils.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class UniqueFilmActivity extends ActionBarActivity {

    private ViewTest myViewTest;
    // Animation
    public Animation animSlideDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unique_film);
        myViewTest = (ViewTest) findViewById(R.id.custView);
        //Button back menu
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

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

        try {
            new BitmapUrl(obj.getString("Poster")).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public void showThePlot(View view){
        TextView text = (TextView) findViewById(R.id.plotFilm);
        text.setVisibility(View.VISIBLE);
        animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        text.startAnimation(animSlideDown);
        text.setMovementMethod(new ScrollingMovementMethod());
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

    private class BitmapUrl extends AsyncTask<String, Void, Bitmap> {
        String url;
        public BitmapUrl(String url){
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Bitmap doInBackground(String... args) {
            JsonParser jParser = new JsonParser();
            // Getting JSON from URL
            Bitmap bitmap = DownloadImage(url);
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {

            super.onPostExecute(bitmap);
            ImageView imageView = (ImageView) findViewById(R.id.posterFilm);
            imageView.setImageBitmap(bitmap);
        }

        private InputStream OpenHttpConnection(String urlString) throws IOException {
            InputStream in = null;
            int response = -1;

            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();

            if (!(conn instanceof HttpURLConnection))
                throw new IOException("Not an HTTP connection");

            try {
                HttpURLConnection httpConn = (HttpURLConnection) conn;
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                response = httpConn.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    in = httpConn.getInputStream();
                }
            } catch (Exception ex) {
                throw new IOException("Error connecting");
            }
            return in;
        }

        private Bitmap DownloadImage(String URL) {
            Bitmap bitmap = null;
            InputStream in = null;
            try {
                in = OpenHttpConnection(URL);
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            return bitmap;
        }



    }


}
