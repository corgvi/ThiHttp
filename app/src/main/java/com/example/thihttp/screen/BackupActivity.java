package com.example.thihttp.screen;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.thihttp.BaseUrl;
import com.example.thihttp.R;
import com.example.thihttp.adapter.BackupAdapter;
import com.example.thihttp.model.Moto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BackupActivity extends AppCompatActivity {

    private Button btnBackup;
    private RecyclerView rcv;
    private BackupAdapter adapter;
    private List<Moto> listMoto = new ArrayList<Moto>();
    FirebaseFirestore db ; ;
    String TAG = "zzzzzzz";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        FirebaseApp.initializeApp(this);
        db  = FirebaseFirestore.getInstance();
        btnBackup = findViewById(R.id.btn_backup);
        rcv = findViewById(R.id.rcv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcv.setLayoutManager(layoutManager);
        adapter = new BackupAdapter(listMoto, this);
        rcv.setAdapter(adapter);
        HttpGetMoto httpGetMoto = new HttpGetMoto();
        httpGetMoto.execute(BaseUrl.URL);
        btnBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFirestore(listMoto);
                Log.d(TAG, "onClick: " + listMoto.size());
            }
        });
    }

    private class HttpGetMoto extends AsyncTask<String, Void, String> {
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

    private void addToFirestore(List<Moto> list){
        for (Moto m : list){
            db.collection("MotoHttp")
                    .add( m )
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d("zzzz", "onSuccess: Thêm moto thành công");
                            Log.d(TAG, "onSuccess: " + documentReference.get());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("zzzz", "onFailure: lỗi thêm moto");
                            e.printStackTrace();
                        }
                    });
        }

    }
}
