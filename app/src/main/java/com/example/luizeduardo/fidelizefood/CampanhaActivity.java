package com.example.luizeduardo.fidelizefood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

        dtInicio.setText(simpleDateFormat.format(date).toString());

        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        int tipo = sharedPreferences.getInt("tipo",1);
        int id = sharedPreferences.getInt("id", 1);

        //garante que somente usuário tipo 2 poderá verificar se existe os dados da campanha
        if(tipo == 2) {
            new CampanhaTask().execute(ConnectAPITask.urlAPI, "req=consultacampanha&UsuarioID=" + id);
        }
        else{

            Utils.alertInfo(this, "ERRO NA APLICAÇÃO",
                    "CONTATE O SUPORTE - Acesso não permitido ao usuario");

        }

    }

    public void btnSalvar(View v){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {

            if(!dtInicio.getText().toString().equals("") && !dtTermino.getText().toString().equals("")) {

                Date data1 = sdf.parse(dtInicio.getText().toString());
                Date data2 = sdf.parse(dtTermino.getText().toString());

                if (data1.getTime() > data2.getTime()) {
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
    }

    @Override
    public void onTaskCompleted(String value) {

    }

    private class CampanhaTask extends ConnectAPITask{


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

                        for (int i = 0; i < registrosAtivos.length(); i++) {

                            JSONObject reg = registrosAtivos.getJSONObject(i);

                            Log.w("NOME-Part", reg.getString("nome"));


                        }
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
