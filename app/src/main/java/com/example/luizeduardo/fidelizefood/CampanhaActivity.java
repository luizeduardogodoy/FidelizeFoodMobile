package com.example.luizeduardo.fidelizefood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CampanhaActivity extends AppCompatActivity implements onTaskCompletion {

    private EditText dtInicio;
    private EditText dtTermino;
    private EditText nome;
    private EditText obs;
    private EditText qtde;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campanha);

        dtInicio = findViewById(R.id.editInicioCampanha);
        dtTermino = findViewById(R.id.editTerminoCampanha);
        obs = findViewById(R.id.editObsCampanha);
        nome = findViewById(R.id.editNomeCampanha);
        qtde = findViewById(R.id.editQtdeCamapanha);

        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //define a data de hoje no campo inicio da campanha
        dtInicio.setText(simpleDateFormat.format(date).toString());

        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        int tipo = sharedPreferences.getInt("tipo",1);
        int id = sharedPreferences.getInt("id", 1);

        //garante que somente usuário tipo 2 poderá verificar se existe os dados da campanha
        if(tipo == 2) {
            new CarregaCampanhaTask().execute(ConnectAPITask.urlAPI, "req=consultacampanha&UsuarioID=" + id);
        }
        else{

            Utils.alertInfo(this, "ERRO NA APLICAÇÃO",
                    "CONTATE O SUPORTE - Acesso não permitido ao usuário");

        }

    }

    public void btnSalvar(View v){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date dataIni = null;
        Date dataFim = null;

        try {

            if(!dtInicio.getText().toString().equals("") && !dtTermino.getText().toString().equals("")) {

                dataIni = sdf.parse(dtInicio.getText().toString());
                dataFim = sdf.parse(dtTermino.getText().toString());

                if (dataIni.getTime() > dataFim.getTime()) {
                    Utils.alertInfo(this, "Fidelize - regras de tela","Data de início não pode ser maior que a data de término");
                }
            }
            else{

                Utils.alertInfo(this, "Fidelize - regras de tela","Informar data de início e data de término");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String vQtde = qtde.getText().toString();

        if(!vQtde.equals("")) {

            int qtdeInt = Integer.parseInt(vQtde);

            if (qtdeInt <= 0) {

                Utils.alertInfo(CampanhaActivity.this, "Fidelize - regras de tela", "A quantidade deve ser maior que zero");
            }
        }
        else{

            Utils.alertInfo(CampanhaActivity.this, "Fidelize - regras de tela", "Informar a quantidade");
        }
        //formata as datas para o formato americano
        SimpleDateFormat formatBD = new SimpleDateFormat("yyyy-MM-dd");
        String dataIniBD = formatBD.format(dataIni);
        String dataFIMBD = formatBD.format(dataFim);

        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);

        int id = sharedPreferences.getInt("id", 1);

        String post = "req=cadastrocampanha&UsuarioID=" + id;
        post += "&nomeCampanha=" + nome.getText().toString();
        post += "&dtInicio=" + dataIniBD;
        post += "&dtFim=" + dataFIMBD;
        post += "&qtde=" + qtde.getText().toString();
        post += "&obs=" + obs.getText().toString();

        new CadastraCampanhaTask().execute(ConnectAPITask.urlAPI, post);

    }

    @Override
    public void onTaskCompleted(String value) {

    }

    private class CadastraCampanhaTask extends ConnectAPITask{
/*
        @Override
        protected void onPostExecute(String s) {

        }*/
    }

    private class CarregaCampanhaTask extends ConnectAPITask{

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject json = null;

            try {
                json = new JSONObject(s);

                if(json.getString("status").equals("ok")) {

                    int idCamapanha = json.getInt("idcampanha");
                    String nomeCampanha = json.getString("nomecampanha");

                    Log.w("idCamapanha", idCamapanha + "");
                    Log.w("nomeCampanha", nomeCampanha + "");

                    nome.setText(nomeCampanha);
                    obs.setText(json.getString("observacao"));
                    qtde.setText(json.getString("qtde"));
                    dtInicio.setText(json.getString("datainicial"));
                    dtTermino.setText(json.getString("datafinal"));

                    if(json.has("registrosativos")) {

                        JSONArray registrosAtivos = json.getJSONArray("registrosativos");

                        String part = "";

                        for (int i = 0; i < registrosAtivos.length(); i++) {

                            JSONObject reg = registrosAtivos.getJSONObject(i);

                            //Log.w("NOME-Part", reg.getString("nome"));

                            part += reg.getString("nome") + " - Qtde: " +
                                    reg.getString("refeicoes") +

                                    "\n";
                        }

                        Utils.alertInfo(CampanhaActivity.this,"Fidelize - Participantes",part);
                    }


                    if(json.has("registrospremiados")) {

                        JSONArray registrosPrem = json.getJSONArray("registrospremiados");

                        String part = "";

                        for (int i = 0; i < registrosPrem.length(); i++) {

                            JSONObject reg = registrosPrem.getJSONObject(i);

                            //Log.w("NOME-Part", reg.getString("nome"));

                            part += reg.getString("nome") + " - Data: " +
                                    reg.getString("utilizado") +

                                    "\n";
                        }

                        Utils.alertInfo(CampanhaActivity.this,"Fidelize - Premiados",part);
                    }
                }
                else if(json.getString("status").equals("!restaurante")){
                    Utils.alertInfo(CampanhaActivity.this,"Fidelize - Aviso","Cadastrar os dados do Restaurante");

                    Intent intent = new Intent(CampanhaActivity.this, RestauranteActivity.class);
                    startActivity(intent);
                }
                else{

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



}
