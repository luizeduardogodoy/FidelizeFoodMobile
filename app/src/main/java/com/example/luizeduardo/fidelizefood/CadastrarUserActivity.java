package com.example.luizeduardo.fidelizefood;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;


public class CadastrarUserActivity extends AppCompatActivity implements onTaskCompletion{

    private EditText nome;
    private EditText pass;
    private EditText cpfCnpj;
    private EditText email;
    private EditText fone;
    private Integer tipo;
    private RadioButton radioCliente;
    private RadioButton radioRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_user);
        radioCliente= findViewById(R.id.radioCliente);
        radioRest = findViewById(R.id.radioRest);

        nome = findViewById(R.id.txtCadNome);
        pass = findViewById(R.id.txtCadSenha);
        cpfCnpj = findViewById(R.id.txtCadCpfCnpj);
        email = findViewById(R.id.txtCadEmail);
        fone = findViewById(R.id.txtCadFone);

        radioCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                radioRest.setChecked(false);

                cpfCnpj.setHint("CPF");

                tipo = 2;
            }
        });

        radioRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            radioCliente.setChecked(false);

            cpfCnpj.setHint("CNPJ");
            tipo = 1;

            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {

                    String e = email.getText().toString();

                    new CreateUserTask().execute(ConnectAPITask.urlAPI, "req=verificaemail&email=" + e);
                }
            }
        });

    }

    public void createUser(View view){

        // tipo 2 = restaurante

        if(nome.getText().toString().equals("") || email.getText().toString().equals("")
                || cpfCnpj.getText().toString().equals("")
                ){

            Toast.makeText(this, "Informar todos os campos!", Toast.LENGTH_LONG).show();
        }
        else {

            String post = "req=cadastrouser&";

            post += "name=" + nome.getText().toString() + "&";
            post += "pass=" + pass.getText().toString() + "&";
            post += "tipo=" + tipo + "&";
            post += "email=" + email.getText().toString() + "&";
            post += "cpf=" + cpfCnpj.getText().toString() + "&";

            new CreateUserTask().execute(ConnectAPITask.urlAPI, post);
        }

    }

    @Override
    public void onTaskCompleted(String value) {

        if(value.equals("sim")) {

            Toast.makeText(getBaseContext(), "EMAIL JÁ EXISTENTE!!", Toast.LENGTH_LONG).show();
            email.requestFocus();
            email.setText("O email informado já existe");
            email.setTextColor(Color.RED);

            AsyncTask<String, Void, String> thread = new AsyncTask<String, Void, String>(){
                @Override
                protected String doInBackground(String... strings) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    return "";
                }
                @Override
                protected void onPostExecute(String s) {
                    email.setTextColor(Color.BLACK);
                    email.setText("");

                }
            };

            thread.execute();



        }
    }




    private class CreateUserTask extends ConnectAPITask{

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s == "API_ERROR"){

                Toast.makeText(getBaseContext(),"Houve um problema com a conexão", Toast.LENGTH_SHORT).show();
            }
            else {
                //Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
                Log.w("userCreated", s);

                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(s);

                    String s1 = jsonObject.get("jaExiste").toString();

                    onTaskCompleted(s1);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }



}
