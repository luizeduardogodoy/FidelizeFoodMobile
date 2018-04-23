package com.example.luizeduardo.fidelizefood;

import android.content.Intent;
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

        //garante que somente usuário tipo 2 poderá verificar se existe os dados da campanha
        if(UserSingleton.getInstance().getUser().getTipo() == 2) {
            new CarregaCampanhaTask().execute(ConnectAPITask.urlAPI, "req=consultacampanha&UsuarioID=" + UserSingleton.getInstance().getUser().getId());
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


            String vQtde = qtde.getText().toString();

            if(!dtInicio.getText().toString().equals("") && !dtTermino.getText().toString().equals("")) {

                dataIni = sdf.parse(dtInicio.getText().toString());
                dataFim = sdf.parse(dtTermino.getText().toString());

                if (dataIni.getTime() > dataFim.getTime()) {
                    Utils.alertInfo(this, "Fidelize - regras de tela","Data de início não pode ser maior que a data de término");
                }
            }
            else if(dtInicio.getText().toString().equals("") || dtTermino.getText().toString().equals("")){

                Utils.alertInfo(this, "Fidelize - regras de tela","Informar data de início e data de término");
            }

            else if(!vQtde.equals("")) {

                int qtdeInt = Integer.parseInt(vQtde);

                if (qtdeInt <= 0) {

                    Utils.alertInfo(CampanhaActivity.this, "Fidelize - regras de tela", "A quantidade deve ser maior que zero");
                }
                else{

                    if(qtdeInt > 50){
                        Utils.alertInfo(CampanhaActivity.this, "Fidelize - regras de tela", "A quantidade maxima deve ser 50");
                    }

                }
            }
            else if(vQtde.equals("")){

                Utils.alertInfo(CampanhaActivity.this, "Fidelize - regras de tela", "Informar a quantidade");
            }

            else {
                //formata as datas para o formato americano
                SimpleDateFormat formatBD = new SimpleDateFormat("yyyy-MM-dd");
                String dataIniBD = formatBD.format(dataIni);
                String dataFIMBD = formatBD.format(dataFim);

                String post = "req=cadastrocampanha&UsuarioID=" + UserSingleton.getInstance().getUser().getId();
                post += "&nomeCampanha=" + nome.getText().toString();
                post += "&dtInicio=" + dataIniBD;
                post += "&dtFim=" + dataFIMBD;
                post += "&qtde=" + qtde.getText().toString();
                post += "&obs=" + obs.getText().toString();

                new CadastraCampanhaTask().execute(ConnectAPITask.urlAPI, post);
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onTaskCompleted(String value) {

    }

    private class CadastraCampanhaTask extends ConnectAPITask{
        @Override
        protected void onPostExecute(String s) {

        }
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
                    nome.setEnabled(false);

                    obs.setText(json.getString("observacao"));
                    qtde.setText(json.getString("qtde"));
                    qtde.setEnabled(false);

                    dtInicio.setText(json.getString("datainicial"));
                    dtInicio.setEnabled(false);

                    dtTermino.setText(json.getString("datafinal"));

                    String rel = "";

                    String part = "";

                    if(json.has("registrosativos")) {

                        JSONArray registrosAtivos = json.getJSONArray("registrosativos");


                        for (int i = 0; i < registrosAtivos.length(); i++) {

                            JSONObject reg = registrosAtivos.getJSONObject(i);

                            part += reg.getString("nome") + " - Qtde: " +
                                    reg.getString("refeicoes") +

                                    "\n";
                        }
                    }

                    if(!part.equals("")){

                        rel = "\nFidelize - Participantes \n\n";

                        rel += part;
                    }


                    String premi = "";

                    if(json.has("registrospremiados")) {

                        JSONArray registrosPrem = json.getJSONArray("registrospremiados");

                        for (int i = 0; i < registrosPrem.length(); i++) {

                            JSONObject reg = registrosPrem.getJSONObject(i);

                            premi += reg.getString("nome") + " - Data: " +
                                    reg.getString("utilizado") + "\n";
                        }

                    }

                    if(!premi.equals("")){
                        rel += "\nFidelize - Premiados \n\n";

                        rel += premi;

                    }

                    if(!rel.equals("")){

                        Utils.alertInfo(CampanhaActivity.this, "Fidelize - Informações da Campanha", rel);
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
