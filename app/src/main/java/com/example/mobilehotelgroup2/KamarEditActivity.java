package com.example.mobilehotelgroup2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class KamarEditActivity extends AppCompatActivity {

    private EditText EditKodeHotel,EditTypeKamar,  EditTglCheckIn, EditTglCheckOut, EditHargaPerMalam;
    private Kamar kamar;
    private SimpleDateFormat dateFormatter, dateFormatter2;

    private String [] TypeKamarSpin = {"Pilih Tipe Kamar","Standar", "Classic", "Superior", "Presidential"};
    private String action_flag="add";
    private String refreshFlag="0";
    private static final String TAG="KamarEditActivity";
    private EditText tvDateResult, tvDateResult2;
    private ProgressDialog pDialog;
    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamar_edit);

        FloatingActionButton Fab = findViewById(R.id.fabSave);
        Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataVolley();
            }
        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        untuk menampilkan popup kalender  cekout
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", localeID);
        tvDateResult = (EditText) findViewById(R.id.EditTglCheckIn);
        ImageButton btnCekin = findViewById(R.id.imageButtonCekin);
        btnCekin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showDateDialog();
            }
        });
//        untuk menampilkan popup kalender  cekout
        dateFormatter2 = new SimpleDateFormat("yyyy-MM-dd", localeID);
        tvDateResult2 = (EditText) findViewById(R.id.EditTglCheckOut);
        ImageButton btnCekout = findViewById(R.id.imageButtonCekout);
        btnCekout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showDateDialog2();
            }
        });

//        inisiasi variabel
        kamar = new Kamar();
        initUI();
        //initEvent();
        Intent intent = getIntent();
        if (intent.hasExtra("kamar")) {
            kamar = (Kamar) intent.getSerializableExtra("kamar");
            Log.d(TAG, "Kamar : " + kamar.toString());
            setData(kamar);
            action_flag = "edit";
            EditKodeHotel.setEnabled(false);
            EditTypeKamar.setEnabled(false);
            EditHargaPerMalam.setEnabled(false);
            EditTglCheckIn.setEnabled(false);
            EditTglCheckOut.setEnabled(false);
        }else{
            kamar = new Kamar();
        }

        FloatingActionButton fabDelete = findViewById(R.id.fab2);

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusData();
            }
        });

//        set otomatis spinner
        Spinner spinner = findViewById(R.id.Spinner);
        EditHargaPerMalam = findViewById(R.id.EditHargaPerMalam);
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,TypeKamarSpin);
        spinner.setAdapter(adapterSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                switch (i) {
                    case 0:

                        break;
                    case 1:
                        EditHargaPerMalam.setText("450000");
                        EditTypeKamar.setText("Standar");
                        Toast.makeText(getApplicationContext(), "Terpilih Standar Harga Per Malam Rp.450.000", Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        EditHargaPerMalam.setText("800000");
                        EditTypeKamar.setText("Classic");
                        Toast.makeText(getApplicationContext(), "Terpilih Classic Harga Per Malam Rp.800.000", Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        EditHargaPerMalam.setText("1850000");
                        EditTypeKamar.setText("Superior");
                        Toast.makeText(getApplicationContext(), "Terpilih Superior Harga Per Malam Rp.1.850.000", Toast.LENGTH_LONG).show();
                        break;
                    case 4:
                        EditHargaPerMalam.setText("2750000");
                        EditTypeKamar.setText("Presidential");
                        Toast.makeText(getApplicationContext(), "Terpilih Presidential Harga Per Malam Rp.2.750.000", Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
            });
    }

    //Show popup kalender cekin
    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                tvDateResult.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    //Show popup kalender cekout
    private void showDateDialog2(){
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                tvDateResult2.setText(dateFormatter2.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

//    untuk set data kedalam form update
    private void setData(Kamar kamar) {
        EditKodeHotel.setText(kamar.getKodeHotel());
        EditTypeKamar.setText(kamar.getTypeKamar());
        EditTglCheckIn.setText(kamar.getCheckin());
        EditTglCheckOut.setText(kamar.getCheckOut());
        EditHargaPerMalam.setText(Double.toString(kamar.getHargaPerMalam()));
    }

//    inisiasi variabel edittext
    private void initUI() {
        pDialog = new ProgressDialog(KamarEditActivity.this);

        EditKodeHotel   = (EditText) findViewById(R.id.EditKodeHotel);
        EditTypeKamar   = (EditText) findViewById(R.id.EditTypeKamar);
        EditTglCheckIn  = (EditText) findViewById(R.id.EditTglCheckIn);
        EditTglCheckOut = (EditText) findViewById(R.id.EditTglCheckOut);
        EditHargaPerMalam = (EditText) findViewById(R.id.EditHargaPerMalam);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.buttonSave) {
            saveDataVolley();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    perintah button dan dialog sekaligus pemangilan method delete data ke server
    private void hapusData() {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Data Penginapan")
                .setMessage("Hapus Data " + kamar.getKodeHotel() + " ?")
                .setIcon(android.R.drawable.ic_delete)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        hapusDataServer();
                        refreshFlag = "1";
                        //finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

//    method finisih untuk merefresh page yang selesai melakukan aktifitas
    @Override
    public void finish() {
        System.gc();
        Intent data = new Intent();
        data.putExtra("refreshflag", refreshFlag);
        //  data.putExtra("kamar", kamar);
        setResult(RESULT_OK, data);
        super.finish();
    }

//    update data ke server
    private void saveDataVolley(){
        refreshFlag="1";
        final String KodeHotel = EditKodeHotel.getText().toString();
        final String TypeKamar = EditTypeKamar.getText().toString();
        final String TglCheckIn = EditTglCheckIn.getText().toString();
        final String TglCheckOut = EditTglCheckOut.getText().toString();
        final String HargaPerMalam = EditHargaPerMalam.getText().toString();

        String url = AppConfig.SIMPAN_URL;
        pDialog.setMessage("Save Data Penginapan...");
        showDialog();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Log.d("KamarEditActivity", "response :" + response);
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

                params.put("kodehotel",KodeHotel);
                params.put("typekamar",TypeKamar);
                params.put("tglcheckin",TglCheckIn);
                params.put("tglcheckout",TglCheckOut);
                params.put("hargapermalam",HargaPerMalam);
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

//    Response kepada server
    private void processResponse(String paction, String response){

        try {
            JSONObject jsonObj = new JSONObject(response);
            String errormsg = jsonObj.getString("errormsg");
            Toast.makeText(getBaseContext(),paction+" "+errormsg,Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            Log.d("KamarEditActivity", "errorJSON");
        }

    }

//    method enghapus data server
    private void hapusDataServer(){
        String url = AppConfig.HAPUS_URL;
        refreshFlag="1";
        pDialog.setMessage("Hapus Data Penginapan...");
        showDialog();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Log.d("KamarEditActivity", "response :" + response);
                        processResponse("Hapus Data", response);
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
                params.put("id",kamar.getId());

                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);

    }

//    show dialog pdialog
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}