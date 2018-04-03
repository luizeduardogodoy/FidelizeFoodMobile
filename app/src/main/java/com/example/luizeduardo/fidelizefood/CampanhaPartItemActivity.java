package com.example.luizeduardo.fidelizefood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CampanhaPartItemActivity extends AppCompatActivity {

    ListView lstCampPart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campanha_part_item);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        String nomeRest = bundle.getString("nomeRestaurante");
        int idUsuarioCampanha = bundle.getInt("idUsuarioCampanha");

        TextView txtNomeRest = findViewById(R.id.txtNomeRest);

        txtNomeRest.setText(nomeRest + "- ID: " + idUsuarioCampanha);


        lstCampPart = findViewById(R.id.lstCampPart);


        String post = "req=listarCarimbosPart&idUsuarioCampanha="+idUsuarioCampanha;

        new CampanhaPartItemTask().execute(ConnectAPITask.urlAPI, post);

    }

    class CampanhaPartItemTask extends ConnectAPITask{

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(s);

                JSONArray array = jsonObject.getJSONArray("registros");

                String[] strings = new String[array.length()];

                for (int i=0; i<array.length(); i++) {
                    JSONObject news = array.getJSONObject(i);
                    String carimbo = news.getString("ultima");

                    strings[i] = (i + 1) + " - " + carimbo;
                }

                ArrayAdapter adapter = new ArrayAdapter<String>(
                        getBaseContext(),
                        android.R.layout.simple_list_item_1,
                        strings);

                lstCampPart.setAdapter( adapter);

            } catch (JSONException e) {
                e.printStackTrace();

                Toast.makeText(getBaseContext(), "Houve Um erro - Retorno Server", Toast.LENGTH_LONG).show();
            }

        }
    }
}
