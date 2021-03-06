package com.example.luizeduardo.fidelizefood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView ultimoLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCad = findViewById(R.id.btnCadastrar);

        btnCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            Intent cad = new Intent(getBaseContext(), CadastrarUserActivity.class);

            startActivity(cad);

            }
        });

        //ultimoLogado = findViewById(R.id.txtUltimoUsuario);

        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        String result = sharedPreferences.getString("ultimoUserLogado", "");
       // ultimoLogado.setText("Resultado --> " + result);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        String result = sharedPreferences.getString("ultimoUserLogado", "");
        //ultimoLogado.setText("Resultado --> " + result);

        //Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();


    }

    protected void abrirFidelizeMain(String nome, int tipo, int id){

        Intent intent = new Intent(this, FidelizeMain.class);

        Bundle bundle = new Bundle();

        bundle.putString("nomeUser", nome);
        intent.putExtras(bundle);

        //Cria o user que sera utilizado por toda a sessao
        User u = new User();
        u.setId(id);
        u.setNome(nome);
        u.setTipo(tipo);

        UserSingleton.create(u);

        startActivity(intent);
    }


    public void conexaoLogin(View v){

        EditText txtUser =  findViewById(R.id.txtUser);
        EditText txtSenha = findViewById(R.id.txtSenha);

        if(txtSenha.getText().toString().equals("") || txtUser.getText().toString().equals("")){

            Utils.alertInfo(this, "Fidelize - login", "Informar usuário e senha");

        }else {
            String post = "req=login&user=" + txtUser.getText() + "&pass=" + txtSenha.getText();

            new LoginTask().execute(ConnectAPITask.urlAPI, post);
        }
    }

    private class LoginTask extends ConnectAPITask{

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject jsonObject;

            try {
                jsonObject = new JSONObject(s);

                //JSONArray jsonArray = jsonObject.getJSONArray("nome");

               // Log.w("t", jsonObject.get("nome").toString());

                if(!jsonObject.has("nome")){

                    Utils.alertInfo(MainActivity.this,"Fidelize - Login", "Usuário ou senha inválidos" );

                   // Toast.makeText(getBaseContext(),"Usuário ou senha inválidos",Toast.LENGTH_LONG).show();

                }else {

                    //Toast.makeText(getBaseContext(), jsonObject.get("nome").toString(), Toast.LENGTH_LONG).show();

                    abrirFidelizeMain(jsonObject.get("nome").toString(), jsonObject.getInt("tipo"),jsonObject.getInt("id"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }



}

