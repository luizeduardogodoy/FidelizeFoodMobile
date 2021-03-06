package com.example.luizeduardo.fidelizefood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import com.onesignal.OneSignal;
import org.json.JSONException;
import org.json.JSONObject;

public class CarimboActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carimbo);
    }

    public void carimbar(View v){

        EditText txtCarimbo = findViewById(R.id.editCPFCarimbo);
        CheckBox permiteMais = findViewById(R.id.checkPermiteMaisCarimbo);

        if(!txtCarimbo.getText().toString().equals("")) {

            String post = null;

            post = "req=carimbo";
            post += "&UsuarioID=" + UserSingleton.getInstance().getUser().getId();
            post += "&idusercliente=" + txtCarimbo.getText().toString();
            post += "&permiteMaisDeUm=" + (permiteMais.isChecked() ? "S" : "N");

            new CarimboTask().execute(ConnectAPITask.urlAPI, post);
        }
        else{
            Utils.alertInfo(this,"Fidelize - Aviso", "Informar o CPF");
        }

    }

    class CarimboTask extends ConnectAPITask{

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject json =  null;

            try {
                json = new JSONObject(s);

                if(json.has("nome") && json.getString("status").equals("ok")) {

                    Utils.alertInfo(CarimboActivity.this,
                            "Fidelize - Carimbo",
                            json.getString("nome") + " - " + json.getString("mensagem"));

                    if(json.has("idnotificacao")) {

                        String userId = json.getString("idnotificacao");
                        try {
                            OneSignal.postNotification(new JSONObject(
                                    "{'contents': " +
                                            "{'en':'Carimbo registrado pelo Restaurante: "  +  UserSingleton.getInstance().getUser().getNome() +  " '}, " +
                                            "'include_player_ids': ['" + userId + "']," +
                                    "'app_id': 'c72fac2d-99ae-4623-a530-68f252d9399d'}"), null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else if (json.getString("status").equals("!restaurante")){
                    Utils.alertInfo(CarimboActivity.this,
                            "Fidelize - Carimbo",
                            "Dados do Restaurante não foram cadastrados");
                }
                else if (json.getString("status").equals("!temcampanha")){
                    Utils.alertInfo(CarimboActivity.this,
                            "Fidelize - Carimbo",
                            "Não existe campanha ativa");
                }

                else{
                    Utils.alertInfo(CarimboActivity.this,
                            "Fidelize - Carimbo",
                            "Cliente não encontrado");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
