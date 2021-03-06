package com.example.luizeduardo.fidelizefood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class RestauranteActivity extends AppCompatActivity {

    private EditText nome;
    private EditText cidade;
    private EditText estado;
    private EditText endereco;
    private EditText fone;

    private Integer UsuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante);

        nome = findViewById(R.id.editNomeRest);
        estado = findViewById(R.id.editEstadoRest);
        cidade = findViewById(R.id.editCidadeRest);
        endereco = findViewById(R.id.editEndRest);
        fone = findViewById(R.id.editFoneRest);

        UsuarioID = UserSingleton.getInstance().getUser().getId();

        new RestauranteTask().execute(ConnectAPITask.urlAPI,"req=consultarestaurante&UsuarioID="+ UsuarioID);
    }


    public void btnSalvar(View v){

        String post = "req=cadastrorestaurant";

        if(nome.getText().toString().equals("") ){
            Utils.alertInfo(this,"Fidelize - Regras de tela","Informar o nome do Restaurante");
        }

        post += "&name=" + nome.getText().toString();
        post += "&city=" + cidade.getText().toString();
        post += "&state=" + estado.getText().toString();
        post += "&address=" + endereco.getText().toString();
        post += "&phone=" + fone.getText().toString();
        post += "&idusuario=" + this.UsuarioID;
        Log.w("post", post);
        new RestauranteTask().execute(ConnectAPITask.urlAPI, post);
    }

    private class RestauranteTask extends ConnectAPITask{


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject json = null;

            try {
                json = new JSONObject(s);

                if(json.getString("acao").equals("consulta")) {

                    nome.setText(json.getString("nome"));
                    estado.setText(json.getString("estado"));
                    endereco.setText(json.getString("endereco"));
                    cidade.setText(json.getString("cidade"));
                    fone.setText(json.getString("telefone"));
                }

                if(json.getString("acao").equals("cadastro")){

                    if(json.getString("status").equals("ok")){
                        Utils.alertInfo(RestauranteActivity.this,
                                "Fidelize - Aviso",
                                "Dados do Restaurante foram salvos com sucesso");
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
