package com.example.luizeduardo.fidelizefood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FidelizeMain extends Activity {

    ListView cartoes;
    ArrayAdapter<CampanhaParticipante> cartoesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fidelize_main);

        TextView textView = findViewById(R.id.textView6);

        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        int tipo = sharedPreferences.getInt("tipo",1);
        int id = sharedPreferences.getInt("id", 1);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        String nomeUser = bundle.getString("nomeUser");

        textView.setText(nomeUser + " - " + (tipo == 1 ? "Cliente" :  "Restaurante"));

        LinearLayout cliente = findViewById(R.id.layoutCliente);
        LinearLayout restaurante = findViewById(R.id.layoutRest);

        restaurante.setVisibility(View.GONE);
        cliente.setVisibility(View.GONE);

        if(tipo == 2){
            restaurante.setVisibility(View.VISIBLE);
        }
        else{
            cliente.setVisibility(View.VISIBLE);

            this.cartoes = findViewById(R.id.listViewCartoes);

            this.cartoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    CampanhaParticipante campPart = (CampanhaParticipante) cartoes.getItemAtPosition(i);

                    //Log.w("aa",adapterView.getAdapter().getItem(i).toString());

                    Intent campPartItem = new Intent(getBaseContext(), CampanhaPartItemActivity.class);

                    Bundle bCamPart = new Bundle();
                    bCamPart.putString("nomeRestaurante", campPart.getNomeRestaurante());
                    bCamPart.putInt("idUsuarioCampanha", campPart.getIdUsuarioCampanha());

                    campPartItem.putExtras(bCamPart);

                    startActivity(campPartItem);

                }
            });




            String post = "req=listarcampanhaspart&ID_USER="+id;

            new FidelizeMainTask().execute(ConnectAPITask.urlAPI, post);

        }
    }

    class FidelizeMainTask extends ConnectAPITask{

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject json = null;
            try {
                json = new JSONObject(s);

                JSONArray array = json.getJSONArray("registros");

                String[] res = new String[array.length()];


                ArrayList<CampanhaParticipante> campanhaParticipantes = new ArrayList<CampanhaParticipante>();

                for (int i=0; i<array.length(); i++) {
                    JSONObject news = array.getJSONObject(i);
                    String name = news.getString("nomeRestaurante");

                    CampanhaParticipante c = new CampanhaParticipante();
                    c.setIdUsuarioCampanha(news.getInt("idusuariocampanha"));
                    c.setNomeRestaurante(name);

                    Log.w("nomeRestaurante", name);

                    campanhaParticipantes.add(c);

                    res[i] = name;
                }

                cartoesAdapter = new ArrayAdapter<CampanhaParticipante>(getBaseContext(),
                        android.R.layout.simple_list_item_1,
                        campanhaParticipantes);

                cartoes.setAdapter(cartoesAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void testChamadaAPIListarCampanhas(View view){

        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        int tipo = sharedPreferences.getInt("tipo",1);
        int id = sharedPreferences.getInt("id", 1);

        String post = "req=listarcampanhaspart&ID_USER="+id;

        new FidelizeMainTask().execute(ConnectAPITask.urlAPI, post);
    }

}
