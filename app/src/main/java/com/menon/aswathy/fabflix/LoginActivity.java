package com.menon.aswathy.fabflix;

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


public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        final JSONObject json = new JSONObject();
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        assert mEmailSignInButton != null;

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = mEmailView.getText().toString();
                final String password = mPasswordView.getText().toString();


                try {
                    json.put("email",email);
                    json.put("password",password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (email.length() == 0 || password.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter UserName / Password", Toast.LENGTH_LONG).show();
                    return;
                }
                String url = "http://52.26.158.153:8080/fabflix_webapp/mobile/MobileLogin?email="+email+"&password="+password;
                JsonObjectRequest sr = new JsonObjectRequest(Request.Method.GET, url,json,

                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                Log.i("Response", response.toString());
                                String name=null, info = null;

                                try {
                                    info = response.getString("info");
                                    name = response.getString("name");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (info.equalsIgnoreCase("success")) {
                            Toast.makeText(getApplication(), "Welcome "+name, Toast.LENGTH_LONG).show();
                            Intent MovieSearch = new Intent(LoginActivity.this, SearchActivity.class);
                            startActivity(MovieSearch);

                        } else {
                            Toast.makeText(getApplication(), "Wrong UserName / Password", Toast.LENGTH_LONG).show();
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

