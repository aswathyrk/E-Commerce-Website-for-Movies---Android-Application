package com.menon.aswathy.fabflix;

/**
 * Created by Aswathy on 5/17/2016.
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.menon.aswathy.fabflix.R;

import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private EditText search;
    private Button SearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        search = (EditText) findViewById(R.id.search);
        SearchButton = (Button) findViewById(R.id.SearchButton);

        SearchButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                String moviename=null;
                final String title = search.getText().toString();
                if (title.trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter a search query", Toast.LENGTH_LONG).show();
                    return;
                }

                if (title.contains(" "))
                    moviename = title.replace(" ","%20");
                else
                    moviename = title;

                String url = "http://52.26.158.153:8080/fabflix_webapp/mobile/MobileSearch?title="+moviename;

                JsonObjectRequest sr = new JsonObjectRequest(Request.Method.GET, url, null,

                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                Log.i("Response", response.toString());
                                String info = null;
                                int results = 0;

                                try {
                                    info = response.getString("info");
                                    results = response.getInt("results");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (info.equalsIgnoreCase("success") && results!=0) {

                                    String toast = "Search Results: " + results;
                                    Toast.makeText(getApplication(), toast , Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(SearchActivity.this,SearchResults.class);
                                    i.putExtra("title",title);
                                    i.putExtra("results",results);
                                    startActivity(i);

                                }

                                else {
                                    Toast.makeText(getApplication(), "No Results of Query", Toast.LENGTH_LONG).show();
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
            }
        });
    }
}