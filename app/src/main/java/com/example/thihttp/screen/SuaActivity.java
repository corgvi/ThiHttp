package com.example.thihttp.screen;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.thihttp.BaseUrl;
import com.example.thihttp.R;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SuaActivity extends AppCompatActivity {

    private Button btnSua;
    private EditText edName, edPrice, edColor;
    private ImageView img;
    private Moto moto;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua);

        btnSua = findViewById(R.id.btn_sua);
        edColor = findViewById(R.id.ed_color);
        edName = findViewById(R.id.ed_name);
        edPrice = findViewById(R.id.ed_price);
        img = findViewById(R.id.img);
        moto = (Moto) getIntent().getSerializableExtra("moto");
        Log.d("TAG", "onCreate: " + moto.toString());
        edColor.setText(moto.getColor());
        edName.setText(moto.getName());
        edPrice.setText(moto.getPrice()+"");

        Glide.with(this)
                .load(moto.getImage())
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_file_download_off_24)
                .into(img);

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUpdateMoto httpUpdateMoto = new HttpUpdateMoto();
                httpUpdateMoto.execute(BaseUrl.URL_UPDATE_DELETE+moto.getId());
            }
        });


    }
    private class HttpUpdateMoto extends AsyncTask<String, Void, String> {
        String result = "";
        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("PUT");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                JSONObject postMoto = new JSONObject();
                postMoto.put("name", edName.getText().toString());
                postMoto.put("image", moto.getImage());
                postMoto.put("price", edPrice.getText().toString());
                postMoto.put("color", edColor.getText().toString());
                Log.d("TAG", "doInBackground: jsonobject" + postMoto.toString());
                writer.flush();
                writer.append(postMoto.toString());
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
                Log.d("TAG", "doInBackground: result update: " + result);
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
            startActivity(new Intent(SuaActivity.this, MainActivity.class));
        }
    }
}
