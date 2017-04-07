package com.menon.aswathy.fabflix;

/**
 * Created by Aswathy on 5/17/2016.
 */
import android.content.Intent;
import android.os.Handler;
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

public class SearchResults extends AppCompatActivity {

    private Button PrevPage;
    private Button NextPage;
    JSONArray jsonArray;
    int BackPress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_page);

        final ArrayList<String> list = new ArrayList<>();
        final String pagenum = getIntent().getExtras().getString("pagenum");
        final int page;

        final ArrayAdapter<String> Alist = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);
        final ListView lv = (ListView)findViewById(R.id.ListView);

        if(pagenum == null)
            page=0;
        else
            page = Integer.parseInt(pagenum);

        Log.d("Page num value: ",String.valueOf(page));

        final String title = getIntent().getExtras().getString("title");
        final int results = getIntent().getExtras().getInt("results");
        String moviename;

        final int res;

        if(results%10 != 0)
            res = (results/10)+1;
        else
            res = (results/10);

        TextView tv = (TextView)findViewById(R.id.Pagetext);
        assert tv != null;
        tv.setText("Page: " + (page+1)+ " of "+res);

        assert title != null;
        if (title.contains(" "))
            moviename = title.replace(" ","%20");
        else
            moviename = title;

        String url = "http://52.26.158.153:8080/fabflix_webapp/mobile/MobileSearchResults?title="+moviename+"&page="+page;
        Log.d("URL: ",url);

        PrevPage = (Button)findViewById(R.id.PrevPage);
        NextPage = (Button)findViewById(R.id.NextPage);

        JsonArrayRequest sr = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {

                    Log.i("Response", response.toString());
                    jsonArray = response;

                    try {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = new JSONObject();
                            json = response.getJSONObject(i);
                            String id = json.getString("id");
                            String title = json.getString("title");
                            String year = json.getString("year");
                            String director = json.getString("director");
                            String banner_url = json.getString("b_url");
                            String trailer_url = json.getString("t_url");
                            list.add(title);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (lv != null)
                        lv.setAdapter(Alist);

                    else {
                        Toast.makeText(getApplicationContext(), "Some problem", Toast.LENGTH_LONG).show();
                        return;
                    }

                }},

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

                String movie = list.get(position);
                Toast.makeText(getApplicationContext(), "Details of Movie: "+movie, Toast.LENGTH_LONG).show();
                Intent i = new Intent(SearchResults.this, SingleMovie.class);
                i.putExtra("title",movie);
                startActivity(i);
            }
        });

        PrevPage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                Log.d("Previous page cliked: ",String.valueOf(page-1));
                if (page == 0) {
                    Toast.makeText(getApplicationContext(), "Previous page does not exist", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent i = new Intent(SearchResults.this, SearchResults.class);
                i.putExtra("pagenum",String.valueOf(page-1));
                i.putExtra("title",title);
                i.putExtra("results",results);
                startActivity(i);
            }
        });

        NextPage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                Log.d("Next page cliked: ",String.valueOf(page+1));
                if((page+1) == res) {
                    Toast.makeText(getApplicationContext(), "Next page does not exist", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent i = new Intent(SearchResults.this, SearchResults.class);
                i.putExtra("pagenum",String.valueOf(page+1));
                i.putExtra("title",title);
                i.putExtra("results",results);
                startActivity(i);
            }
        });

        FloatingActionButton FloatingSearch = (FloatingActionButton)findViewById(R.id.FloatingSearch);
        assert FloatingSearch != null;
        FloatingSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(SearchResults.this, SearchActivity.class);
                startActivity(i);
            }
        });

    }
}