package com.example.luizeduardo.fidelizefood;

import android.content.Intent;
import android.sax.RootElement;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CampanhaPartItemActivity extends AppCompatActivity {

    ListView lstCampPart;

    Bundle bundle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campanha_part_item);

        Intent intent = getIntent();

        this.bundle = intent.getExtras();

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

                ArrayList<RowCartao> carimbos = new ArrayList<RowCartao>();

                int qtdeCarimbos = array.length();

                Log.w("qtde", qtdeCarimbos + "");
                int c = 0;
                int l = 0;

                int qtdePorLinha = 4;

                RowCartao rc = new RowCartao();
                rc.carimbos = new HashMap<Integer, String>();

                while(bundle.getInt("qtde") > c){

                    if(l == qtdePorLinha){

                        carimbos.add(rc);

                        l = 0;

                        rc = new RowCartao();

                        rc.carimbos = new HashMap<Integer, String>();
                    }

                    if(c < qtdeCarimbos) {

                        JSONObject jObj = array.getJSONObject(c);

                        rc.carimbos.put(l, jObj.getString("ultima"));
                    }
                    else
                        rc.carimbos.put(l, "vazio");

                    l++;

                    c++;
                }

                carimbos.add(rc);

                Log.w("quantidade c", c + "");



                for(RowCartao rcc : carimbos){

                    Log.w("debugHashMap",rcc.carimbos.toString());

                }


/*
                for (int i=0; i<array.length(); i++) {
                    JSONObject news = array.getJSONObject(i);
                    String carimbo = news.getString("ultima");

                    carimbos[i] = (i + 1) + " - " + carimbo;
                }
*/

                ArrayAdapter<RowCartao> rowCartaoArrayAdapter = new ClienteCarimboAdapter(CampanhaPartItemActivity.this, carimbos);

                lstCampPart.setAdapter( rowCartaoArrayAdapter);

            } catch (JSONException e) {
                e.printStackTrace();

                Toast.makeText(getBaseContext(), "Houve Um erro - Retorno Server", Toast.LENGTH_LONG).show();
            }

        }
    }
}
