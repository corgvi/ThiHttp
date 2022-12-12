package com.example.thihttp.screen;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.thihttp.BaseUrl;
import com.example.thihttp.OnClickItem;
import com.example.thihttp.R;
import com.example.thihttp.adapter.MotoAdapter;
import com.example.thihttp.model.Moto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.http.Url;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Moto> listMoto = new ArrayList<Moto>();
    private EditText edName, edPrice, edImg, edColor;
    private Button btnAdd, btnLoad, btnBackup;
    private MotoAdapter adapter;
    private RecyclerView rcvMoto;
    private static final String TAG = "MainActivity";
    private OnClickItem onClickItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edName = findViewById(R.id.ed_name);
        edColor = findViewById(R.id.ed_color);
        edImg = findViewById(R.id.ed_img);
        edColor = findViewById(R.id.ed_color);
        edPrice = findViewById(R.id.ed_price);
        btnAdd = findViewById(R.id.btn_add);
        btnLoad = findViewById(R.id.btn_load);
        btnBackup = findViewById(R.id.btn_backup);
        rcvMoto = findViewById(R.id.rcv);
        HttpGetMoto httpGetMoto = new HttpGetMoto();
        httpGetMoto.execute(BaseUrl.URL);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvMoto.setLayoutManager(layoutManager);
        adapter = new MotoAdapter(listMoto, this);
        rcvMoto.setAdapter(adapter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpPostMoto httpPostMoto = new HttpPostMoto();
                httpPostMoto.execute(BaseUrl.URL);
            }
        });
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpGetMoto httpGetMoto = new HttpGetMoto();
                httpGetMoto.execute(BaseUrl.URL);
            }
        });
        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BackupActivity.class));
            }
        });

        onClickItem = new OnClickItem() {
            @Override
            public void onClickDelete(Moto moto) {
                HttpDeleteMoto httpDeleteMoto = new HttpDeleteMoto();
                httpDeleteMoto.execute(BaseUrl.URL_UPDATE_DELETE+moto.getId());
            }
        };
        adapter.onClickItem = onClickItem;
    }

    private class HttpGetMoto extends AsyncTask<String, Void, String>{
        String result = "";
        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String dong;
                while ((dong = reader.readLine()) != null){
                    builder.append(dong)
                            .append("\n");
                }
                reader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                result = builder.toString();
                Log.d(TAG, "doInBackground: result: " + result);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                listMoto.clear();
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Moto moto = new Moto();
                    moto.setColor(jsonObject.getString("color"));
                    moto.setImage(jsonObject.getString("image"));
                    moto.setPrice(Integer.parseInt(jsonObject.getString("price")));
                    moto.setName(jsonObject.getString("name"));
                    moto.setId(jsonObject.getString("id"));
                    moto.setCreatedAt(jsonObject.getString("createdAt"));
                    Log.d(TAG, "onPostExecute: " + moto.toString());
                    listMoto.add(moto);
                    adapter.setData(listMoto);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class HttpPostMoto extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {
                URL url = null;
                url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                JSONObject postMoto = new JSONObject();
                postMoto.put("name", edName.getText().toString());
                postMoto.put("image", edImg.getText().toString());
                postMoto.put("price", edPrice.getText().toString());
                postMoto.put("color", edColor.getText().toString());
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                writer.append(postMoto.toString());
                writer.flush();
                writer.close();
                outputStream.close();
                //doc
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String dong;
                while ((dong = reader.readLine()) != null){
                    builder.append(dong)
                            .append("\n");
                }
                reader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                result = builder.toString();
                Log.d(TAG, "doInBackground: result: " + result);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            HttpGetMoto httpGetMoto = new HttpGetMoto();
            httpGetMoto.execute(BaseUrl.URL);
        }
    }

    private class HttpDeleteMoto extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String result = null;

            URL url = null;
            try {
                url = new URL(strings[0]);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty( "Content-Type", "application/json");
                httpURLConnection.setRequestMethod("DELETE");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                writer.flush();
                writer.close();
                outputStream.close();
                //doc
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String dong;
                while ((dong = reader.readLine()) != null){
                    builder.append(dong)
                            .append("\n");
                }
                reader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                result = builder.toString();
                Log.d(TAG, "doInBackground: result: " + result);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            HttpGetMoto httpGetMoto = new HttpGetMoto();
            httpGetMoto.execute(BaseUrl.URL);
        }
    }

}