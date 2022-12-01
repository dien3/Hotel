package com.example.mobilehotelgroup2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    ProgressDialog pDialog;
    Button buttonLogin;
    Context context;
    private static  final int REQUEST_CODE_ADD =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;

        //inisialisasi tampilan
        pDialog = new ProgressDialog(context);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.buttonLogin);

//button sistem login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginSistem();
            }
        });

//        text view untuk pindah ke activity regist
        TextView textRegist = findViewById(R.id.textRegist);
        textRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(intent);
            }
        });


    }

//login sistem
    public void LoginSistem(){
        final String Url = "http://10.0.2.2/e-HotelMobile/login.php";
        final String NamaUser = username.getText().toString().trim();
        final String Pass = password.getText().toString().trim();
        pDialog.setMessage("Login Process...");
        showDialog();
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //If we are getting success from server
                        if (response.contains("success")) {
                            hideDialog();
                            Toast.makeText(context, "Login Berhasil Masuk!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivityForResult(intent, REQUEST_CODE_ADD);

                        } else {
                            hideDialog();
                            //Displaying an error message on toast
                            Toast.makeText(context, "Nama User atau Password SALAH!", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                        hideDialog();
                        Toast.makeText(context, "The server unreachable", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put("NamaUser", NamaUser);
                params.put("Pass", Pass);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue

        Volley.newRequestQueue(this).add(stringRequest);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}