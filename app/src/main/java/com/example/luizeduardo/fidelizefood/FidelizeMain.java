package com.example.luizeduardo.fidelizefood;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FidelizeMain extends AppCompatActivity {

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

            ImageView btnCliente = findViewById(R.id.btnClientes);
            ImageView btnCarimbo = findViewById(R.id.btnCarimbo);
            ImageView btnCampanhas = findViewById(R.id.btnCampanhas);
            ImageView btnRestaurante = findViewById(R.id.btnRestaurante);
            ImageView btnPremio = findViewById(R.id.btnPremio);

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
                Intent intent = new Intent(FidelizeMain.this, RelatorioClienteActivity.class);
                startActivity(intent);
                }
            });

            btnPremio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FidelizeMain.this, RegistrarPremioActivity.class);
                    startActivity(intent);
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



            // OneSignal Initialization, cria o user na api
            OneSignal.startInit(this)
                    .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                    .unsubscribeWhenNotificationsAreDisabled(true)
                    .init();

            OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {


                @Override
                public void idsAvailable(String userId, String registrationId) {


                    new FidelizeMainTask().execute(ConnectAPITask.urlAPI, "req=registraOneSignal&userIdOneSignal="+
                            userId + "&UsuarioId="+ UserSingleton.getInstance().getUser().getId()
                            + "");

                    Log.d("debug", "User:" + userId);
                    if (registrationId != null)
                        Log.d("debug", "registrationId:" + registrationId);
                }
            });




            String post = "req=listarcampanhaspart&ID_USER="+id;

            new FidelizeMainTask().execute(ConnectAPITask.urlAPI, post);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(UserSingleton.getInstance().getUser() == null){
            Intent login = new Intent(FidelizeMain.this, MainActivity.class);

            startActivity(login);
        }
        else {

            if (UserSingleton.getInstance().getUser().getTipo() == 1) {
                String post = "req=listarcampanhaspart&ID_USER=" + UserSingleton.getInstance().getUser().getId();

                new FidelizeMainTask().execute(ConnectAPITask.urlAPI, post);
            }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.menuLogout){

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Fidelize - Sair do APP");

            alertDialog.setMessage("deseja realmente sair do FidelizeFood?");

            alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //apaga o usuário da sessão informando null no metodo create
                    UserSingleton.create(null);

                    Intent login = new Intent(FidelizeMain.this, MainActivity.class);

                    startActivity(login);
                }
            });

            alertDialog.show();
        }

        return true;
    }
}
