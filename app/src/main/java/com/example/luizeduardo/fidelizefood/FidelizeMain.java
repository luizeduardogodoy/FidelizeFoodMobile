package com.example.luizeduardo.fidelizefood;

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

public class FidelizeMain extends AppCompatActivity {

    ListView cartoes;
    ArrayAdapter<String> cartoesAdapter;


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

                    String str = (String) cartoes.getItemAtPosition(i);

                    Log.w("aa",adapterView.getAdapter().getItem(i).toString());


                    Intent campPartItem = new Intent(getBaseContext(), CampanhaPartItemActivity.class);

                    Bundle bundle1 = new Bundle();
                    bundle1.putString("restaurante", str);

                    campPartItem.putExtras(bundle1);

                    startActivity(campPartItem);

                    Toast.makeText(getBaseContext(),str,Toast.LENGTH_SHORT).show();
                }
            });


            //String[] c = new String[]{"Cardamon","Celma","Jabuti"};

            //cartoes.setVisibility(View.VISIBLE);

            //cartoesAdapter =  new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, c);

           // cartoes.setAdapter(cartoesAdapter);

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

                for (int i=0; i<array.length(); i++) {
                    JSONObject news = array.getJSONObject(i);
                    String name = news.getString("nomeRestaurante");

                    Log.w("nomeRestaurante", name);

                    res[i] = name;
                }

                cartoesAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, res);

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
