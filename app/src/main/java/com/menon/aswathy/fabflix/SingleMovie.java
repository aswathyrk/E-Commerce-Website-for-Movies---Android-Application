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

public class SingleMovie extends AppCompatActivity {

    JSONArray MovieDetails;
    int BackPress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_movie);

        JSONObject moviedetails = null;
        final String title = getIntent().getExtras().getString("title");
        final ArrayList<String> StarsList = new ArrayList<>();
        String moviename = null;

        final ArrayAdapter<String> Alist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, StarsList);
        final ListView lv = (ListView) findViewById(R.id.ListView);

        assert title != null;
        if (title.contains(" "))
            moviename = title.replace(" ","%20");
        else
            moviename = title;

        String url = "http://52.26.158.153:8080/fabflix_webapp/mobile/MobileSingleMovie?title="+moviename;
        Log.d("URL: ",url);

        JsonArrayRequest sr = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        String id, movietitle, year, director, b_url, t_url;
                        TextView tv;

                        Log.i("Response", response.toString());
                        Log.i("Length ",String.valueOf(response.length()));
                        MovieDetails = response;
                        Log.i("Stars value ",MovieDetails.toString());
                        JSONArray Stars;

                        try {
                            id = MovieDetails.getJSONObject(0).getString("id");
                            tv = (TextView) findViewById(R.id.IDtext);
                            assert tv != null;
                            tv.setText("ID: "+id);

                            movietitle = MovieDetails.getJSONObject(0).getString("title");
                            tv = (TextView) findViewById(R.id.Titletext);
                            assert tv != null;
                            tv.setText("Title: "+movietitle);

                            year = MovieDetails.getJSONObject(0).getString("year");
                            tv = (TextView) findViewById(R.id.Yeartext);
                            assert tv != null;
                            tv.setText("Year: "+year);

                            director = MovieDetails.getJSONObject(0).getString("director");
                            tv = (TextView) findViewById(R.id.Directortext);
                            assert tv != null;
                            tv.setText("Director: "+director);

                            b_url = MovieDetails.getJSONObject(0).getString("b_url");
                            tv = (TextView) findViewById(R.id.B_urltext);
                            assert tv != null;
                            tv.setText("Banner: "+b_url);

                            t_url = MovieDetails.getJSONObject(0).getString("t_url");
                            tv = (TextView) findViewById(R.id.T_urltext);
                            assert tv != null;
                            tv.setText("Trailer: "+t_url);

                            Stars = MovieDetails.getJSONObject(0).getJSONArray("stars");

                            for(int i=0;i<Stars.length();i++)
                                StarsList.add(Stars.getString(i));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (StarsList.size() == 0) {

                            tv = (TextView) findViewById(R.id.Starstext);
                            assert tv != null;
                            tv.setText("Stars: Data not available");
                        }

                        else {

                            tv = (TextView) findViewById(R.id.Starstext);
                            assert tv != null;
                            tv.setText("Stars: ");

                            assert lv != null;
                            lv.setAdapter(Alist);
                        }
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

                String StarName = StarsList.get(position);
                String toast = "Details of Star: "+StarName;
                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG).show();
                Intent i = new Intent(SingleMovie.this, SingleStar.class);
                i.putExtra("name",StarName);
                startActivity(i);
            }
        });

        FloatingActionButton FloatingSearch = (FloatingActionButton)findViewById(R.id.FloatingSearch);
        assert FloatingSearch != null;
        FloatingSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(SingleMovie.this, SearchActivity.class);
                startActivity(i);
            }
        });

    }
}
