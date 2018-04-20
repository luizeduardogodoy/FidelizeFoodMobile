package com.example.luizeduardo.fidelizefood;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FidelizeMain extends Activity {

    ListView cartoes;
    ArrayAdapter<CampanhaParticipante> cartoesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fidelize_main);

        TextView textView = findViewById(R.id.txtUserBemvindo);

        Integer tipo = UserSingleton.getInstance().getUser().getTipo();
        Integer id = UserSingleton.getInstance().getUser().getId();

        textView.setText("Bem vindo, "+ UserSingleton.getInstance().getUser().getNome() );

        LinearLayout cliente = findViewById(R.id.layoutCliente);
        LinearLayout restaurante = findViewById(R.id.layoutRest);

        restaurante.setVisibility(View.GONE);
        cliente.setVisibility(View.GONE);

        if(tipo == 2){
            restaurante.setVisibility(View.VISIBLE);

            Button btnCliente = findViewById(R.id.btnClientes);
            Button btnCarimbo = findViewById(R.id.btnCarimbo);
            Button btnCampanhas = findViewById(R.id.btnCampanhas);
            Button btnRestaurante = findViewById(R.id.btnRestaurante);

            btnRestaurante.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                Intent intent = new Intent(FidelizeMain.this, RestauranteActivity.class);
                startActivity(intent);
                }
            });

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
                    Toast.makeText(getBaseContext(), "Pressionado nao", Toast.LENGTH_SHORT).show();

                    }
                });

                alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    Toast.makeText(getBaseContext(), "Pressionado sim", Toast.LENGTH_SHORT).show();
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
                bCamPart.putInt("qtde", campPart.getQtde());

                campPartItem.putExtras(bCamPart);

                startActivity(campPartItem);
                }
            });

            String post = "req=listarcampanhaspart&ID_USER="+id;

            new FidelizeMainTask().execute(ConnectAPITask.urlAPI, post);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();

        if(UserSingleton.getInstance().getUser().getTipo() == 1) {
            String post = "req=listarcampanhaspart&ID_USER=" + UserSingleton.getInstance().getUser().getId();

            new FidelizeMainTask().execute(ConnectAPITask.urlAPI, post);
        }
    }

/*
    @Override
    protected void onRestart() {
        super.onRestart();


        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
    }*/

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

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                    String dataFinal = news.getString("datafinal");

                    try {
                        c.setDtFinal(sdf.parse(dataFinal));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

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
