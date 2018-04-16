package com.example.luizeduardo.fidelizefood;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

               // if(json.getString("status").equals("ok")) {

                if(json.has("nome")) {
                    Utils.alertInfo(CarimboActivity.this,
                            "Fidelize - Carimbo",
                            json.getString("nome") + " - " + json.getString("mensagem"));
                }
                else{
                    Utils.alertInfo(CarimboActivity.this,
                            "Fidelize - Carimbo",
                            "Cliente não encontrado");
                }
                //}


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
