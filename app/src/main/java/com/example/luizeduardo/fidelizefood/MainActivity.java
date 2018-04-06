package com.example.luizeduardo.fidelizefood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
public class MainActivity extends AppCompatActivity {

    TextView ultimoLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toast.makeText(this, "Inicio", Toast.LENGTH_LONG).show();


        Button btnCad = findViewById(R.id.btnCadastrar);

        btnCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cad = new Intent(getBaseContext(), CadastrarUserActivity.class);

                startActivity(cad);

            }
        });

        ultimoLogado = findViewById(R.id.txtUltimoUsuario);

        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        String result = sharedPreferences.getString("ultimoUserLogado", "");
        ultimoLogado.setText("Resultado --> " + result);


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        String result = sharedPreferences.getString("ultimoUserLogado", "");
        ultimoLogado.setText("Resultado --> " + result);

        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();


    }

    protected void abrirFidelizeMain(String nome, int tipo, int id){


        Intent intent = new Intent(this, FidelizeMain.class);


        Bundle bundle = new Bundle();

        bundle.putString("nomeUser", nome);
        intent.putExtras(bundle);

        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ultimoUserLogado", nome);
        editor.putInt("tipo", tipo);
        editor.putInt("id", id);

        editor.apply();

        startActivity(intent);

    }


    public void conexaoLogin(View v){

        EditText txtUser =  findViewById(R.id.txtUser);
        EditText txtSenha = findViewById(R.id.txtSenha);


        if(txtSenha.getText().toString().equals("") || txtUser.getText().toString().equals("")){

            Toast.makeText(this, "Informar usuário e senha", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getBaseContext(),"Usuário ou senha inválidos",Toast.LENGTH_LONG).show();

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

