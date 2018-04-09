package com.example.luizeduardo.fidelizefood;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FidelizeMain extends AppCompatActivity {

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

        final Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        String nomeUser = bundle.getString("nomeUser");
        textView.setText(nomeUser + " - " + (tipo == 1 ? "Cliente" :  "Restaurante"));

        LinearLayout cliente = findViewById(R.id.layoutCliente);
        LinearLayout restaurante = findViewById(R.id.layoutRest);

        restaurante.setVisibility(View.GONE);
        cliente.setVisibility(View.GONE);

        if(tipo == 2){
            restaurante.setVisibility(View.VISIBLE);

            Button btnCliente = findViewById(R.id.btnClientes);
            Button btnCarimbo = findViewById(R.id.btnCarimbo);
            Button btnCampanhas = findViewById(R.id.btnCampanhas);

            btnCarimbo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent1 = new Intent(FidelizeMain.this, CarimboActivity.class);
                    startActivity(intent1);

                }
            });

            btnCampanhas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(FidelizeMain.this, CampanhaActivity.class);
                    startActivity(intent);

                }
            });


            btnCliente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(FidelizeMain.this );


                    alertDialog.setTitle("Teste");

                    alertDialog.setMessage("Olá");

                    alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(FidelizeMain.this, "Pressionado nao", Toast.LENGTH_SHORT).show();


                        }
                    });

                    alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Toast.makeText(FidelizeMain.this, "Pressionado sim", Toast.LENGTH_SHORT).show();
                        }
                    });

                    alertDialog.show();





                }
            });


        }
        else{
            cliente.setVisibility(View.VISIBLE);

            this.cartoes = findViewById(R.id.listViewCartoes);

            this.cartoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CampanhaParticipante campPart = (CampanhaParticipante) cartoes.getItemAtPosition(i);

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

                    CampanhaParticipante c = new CampanhaParticipante();
                    c.setIdUsuarioCampanha(news.getInt("idusuariocampanha"));
                    c.setNomeRestaurante( news.getString("nomeRestaurante"));
                    c.setCarimbo(news.getInt("refeicoes"));
                    c.setQtde(news.getInt("qtde"));

                    campanhaParticipantes.add(c);
                }

                cartoesAdapter = new CampanhaParticipanteAdpter(getBaseContext(), campanhaParticipantes);

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
