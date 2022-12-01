package com.example.mobilehotelgroup2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import in.dd4you.appsconfig.DD4YouConfig;

public class RegistActivity extends AppCompatActivity {

    private EditText EditUsername, EditPassword;
    private Kamar kamar;

    private String action_flag="add";
    private String refreshFlag="0";
    private static final String TAG="RegistActivity";
    private ProgressDialog pDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);


//        button untuk save data ke server
        Button buttonRegist = findViewById(R.id.buttonRegist);
        buttonRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataVolley();
            }
        });

//        set inisiasi initUI
        kamar = new Kamar();
        initUI();
        //initEvent();
        Intent intent = getIntent();
        if (intent.hasExtra("kamar")) {
            kamar = (Kamar) intent.getSerializableExtra("kamar");
            Log.d(TAG, "Kamar : " + kamar.toString());
            setData(kamar);
            action_flag = "edit";
        }else{
            kamar = new Kamar();
        }

        TextView textLogin = findViewById(R.id.textLogin);
        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    //setdata update atau edit
    private void setData(Kamar kamar) {
        EditUsername.setText(kamar.getKodeHotel());
        EditPassword.setText(kamar.getCheckin());
    }

    //    set atau inisiasi variabel untuk inisiasi form
    private void initUI() {
        pDialog = new ProgressDialog(RegistActivity.this);

        EditUsername   = (EditText) findViewById(R.id.EditUsername);
        EditPassword  = (EditText) findViewById(R.id.EditPassword);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.buttonRegist) {
            saveDataVolley();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        System.gc();
        Intent data = new Intent();
        data.putExtra("refreshflag", refreshFlag);
        //  data.putExtra("kamar", kamar);
        setResult(RESULT_OK, data);
        super.finish();
    }

    //    metode untuk save ke server menggunakan volley
    private void saveDataVolley(){
        refreshFlag="1";
        final String Username = EditUsername.getText().toString();
        final String Password = EditPassword.getText().toString();

        String url = AppConfig.REGIST_URL;
        pDialog.setMessage("Save Data Penginapan...");
        showDialog();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Log.d("RegistActivity", "response :" + response);
                        // Toast.makeText(getBaseContext(),"response: "+response, Toast.LENGTH_SHORT).show();
                        processResponse("Save Data",response);
                        finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:

                params.put("username",Username);
                params.put("password",Password);
                if (action_flag.equals("add")){
                    params.put("id","0");
                }else{
                    params.put("id",kamar.getId());
                }
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);

    }

    private void processResponse(String paction, String response){

        try {
            JSONObject jsonObj = new JSONObject(response);
            String errormsg = jsonObj.getString("errormsg");
            Toast.makeText(getBaseContext(),paction+" "+errormsg,Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            Log.d("RegistActivity", "errorJSON");
        }

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