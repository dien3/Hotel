package com.example.mobilehotelgroup2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private KamarAdapter rvAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context = MainActivity.this;

    private static  final int REQUEST_CODE_ADD =1;
    private static  final int REQUEST_CODE_EDIT =2;
    private List<Kamar> kamarList = new ArrayList<Kamar>();
    private ProgressDialog pDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Float button untuk pindah ke halaman input
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InputKamarActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD);

            }
        });

//        set recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        pDialog = new ProgressDialog(this);
        loadDataServerVolley();
    }

//    pemindahan data ke form update
    private void gambarDatakeRecyclerView(){
        rvAdapter = new KamarAdapter(kamarList);
        mRecyclerView.setAdapter(rvAdapter);
        mRecyclerView.addOnItemTouchListener(
              new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                  @Override
                  public void onItemClick(View view, int position) {
                      Kamar kamar = rvAdapter.getItem(position);
                      Intent intent = new Intent(MainActivity.this, KamarEditActivity.class);
                      intent.putExtra("kamar", kamar);
                      startActivityForResult(intent, REQUEST_CODE_EDIT);
                  }
              })
        );
    }

//request data untuk pemanggilan data ke server
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_ADD: {
                if (resultCode == RESULT_OK && null != data) {
                    if (data.getStringExtra("refreshflag").equals("1")) {
                        loadDataServerVolley();
                    }
                }
                break;
            }
            case REQUEST_CODE_EDIT: {
                if (resultCode == RESULT_OK && null != data) {
                    if (data.getStringExtra("refreshflag").equals("1")) {
                        loadDataServerVolley();
                    }
                }
                break;
            }
        }
    }

    //ambil data sever menggunakan volley
    private void loadDataServerVolley(){

        String url = AppConfig.TAMPIL_URL;
        pDialog.setMessage("Retieve Data Kamar...");
        showDialog();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("MainActivity","response:"+response);
                        hideDialog();
                        processResponse(response);
                        gambarDatakeRecyclerView();

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
                //params.put("id","1");
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

//    proses respon json dari server untuk menambahkan kedalam list yang ada di android
    private void processResponse(String response){

        try {
            JSONObject jsonObj = new JSONObject(response);
            JSONArray jsonArray = jsonObj.getJSONArray("data");
            Log.d("TAG", "data length: " + jsonArray.length());
            Kamar objectkamar = null;
            kamarList.clear();
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                objectkamar= new Kamar();
                objectkamar.setId(obj.getString("id"));
                objectkamar.setKodeHotel(obj.getString("kodehotel"));
                objectkamar.setTypeKamar(obj.getString("typekamar"));
                objectkamar.setCheckin(obj.getString("tglcheckin"));
                objectkamar.setCheckOut(obj.getString("tglcheckout"));
                objectkamar.setHargaPerMalam(obj.getString("hargapermalam"));

                kamarList.add(objectkamar);
            }

        } catch (JSONException e) {
            Log.d("MainActivity", "errorJSON");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}