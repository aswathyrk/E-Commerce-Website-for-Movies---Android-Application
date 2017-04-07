package com.menon.aswathy.fabflix;

/**
 * Created by Aswathy on 5/17/2016.
 */
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.menon.aswathy.fabflix.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class SingleStar extends AppCompatActivity {

    JSONArray StarDetails;
    int BackPress = 0;
    int TimeDelay = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_star);

        String starname;
        final String StarName = getIntent().getExtras().getString("name");
        final ArrayList<String> MovieList = new ArrayList<>();

        final ArrayAdapter<String> Alist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MovieList);
        final ListView lv = (ListView) findViewById(R.id.ListView);

        assert StarName != null;
        if (StarName.contains(" "))
            starname = StarName.replace(" ", "%20");
        else
            starname = StarName;

        String url = "http://52.26.158.153:8080/fabflix_webapp/mobile/MobileSingleStar?name=" + starname;
        Log.d("URL: ", url);


        JsonArrayRequest sr = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        String id, name, dob, photo_url;
                        TextView tv;
                        JSONObject json;
                        JSONArray movies;

                        Log.i("Response", response.toString());
                        Log.i("Length ", String.valueOf(response.length()));
                        StarDetails = response;

                        Log.i("Stars value ", StarDetails.toString());
                        try {
                            id = StarDetails.getJSONObject(0).getString("id");
                            tv = (TextView) findViewById(R.id.IDtext);
                            assert tv != null;
                            tv.setText("ID: " + id);

                            name = StarDetails.getJSONObject(0).getString("name");
                            tv = (TextView) findViewById(R.id.Nametext);
                            assert tv != null;
                            tv.setText("Name: " + name);

                            dob = StarDetails.getJSONObject(0).getString("dob");
                            tv = (TextView) findViewById(R.id.Dobtext);
                            assert tv != null;
                            tv.setText("Date of Birth: " + dob);

                            photo_url = StarDetails.getJSONObject(0).getString("Photo_url");
                            tv = (TextView) findViewById(R.id.Photo_urltext);
                            assert tv != null;
                            tv.setText("Photo URL: " + photo_url);

                            tv = (TextView) findViewById(R.id.Moviestext);
                            assert tv != null;
                            tv.setText("Movies: ");

                            movies = StarDetails.optJSONObject(0).getJSONArray("movies");

                            for(int i=0;i<movies.length();i++)
                                MovieList.add(movies.getString(i));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        assert lv != null;
                        lv.setAdapter(Alist);

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Response", "ERROR");
                        Toast.makeText(getApplication(), "Unable to connect to server", Toast.LENGTH_LONG).show();

                    }
                });

        Volley.newRequestQueue(getApplicationContext()).add(sr);

        assert lv != null;
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String MovieName = MovieList.get(position);
                String toast = "Details of movie: "+MovieName;
                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG).show();
                Intent i = new Intent(SingleStar.this, SingleMovie.class);
                i.putExtra("title",MovieName);
                startActivity(i);
            }
        });

        FloatingActionButton FloatingSearch = (FloatingActionButton)findViewById(R.id.FloatingSearch);
        assert FloatingSearch != null;
        FloatingSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(SingleStar.this, SearchActivity.class);
                startActivity(i);
            }
        });
    }
}