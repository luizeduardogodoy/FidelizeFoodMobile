package com.example.luizeduardo.fidelizefood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
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

        if(!txtCarimbo.getText().toString().equals("")) {

            String post = null;

            post = "req=carimbo";
            post += "&UsuarioID=" + UserSingleton.getInstance().getUser().getId();
            post += "&idusercliente=" + txtCarimbo.getText().toString();

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
