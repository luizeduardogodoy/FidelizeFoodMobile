package com.example.luizeduardo.fidelizefood;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    Button btnCreateUser;

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

        btnCreateUser = findViewById(R.id.btnCreateUser);
        btnCreateUser.setEnabled(false);

        radioCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                radioRest.setChecked(false);

                cpfCnpj.setHint("CPF");

                tipo = 1;
            }
        });

        radioRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                radioCliente.setChecked(false);

                cpfCnpj.setHint("CNPJ");
                tipo = 2;

            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {

                    String e = email.getText().toString();

                    if(!e.equals(""))
                        new CreateUserTask().execute(ConnectAPITask.urlAPI, "req=verificaemail&email=" + e);

                }
                else{
                    email.setText("");
                    btnCreateUser.setEnabled(false);
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
        else if(pass.getText().length() <= 5){
            Toast.makeText(this, "A senha deve possuir 6 caracteres no mínimo", Toast.LENGTH_SHORT).show();

        }
        else {

            String post = "req=cadastrouser&";

            post += "name=" + nome.getText().toString() + "&";
            post += "pass=" + pass.getText().toString() + "&";
            post += "tipo=" + tipo + "&";
            post += "email=" + email.getText().toString() + "&";
            post += "cpf=" + cpfCnpj.getText().toString() + "&";
            Log.w("post", post);
            new CreateUserTask().execute(ConnectAPITask.urlAPI, post);
        }

    }

    @Override
    public void onTaskCompleted(String value) {

        if(value.equals("sim")) {

            Toast.makeText(getBaseContext(), "EMAIL JÁ EXISTENTE!!", Toast.LENGTH_LONG).show();
            email.requestFocus();
            email.setText("O email informado já esta sendo utilizado");
            email.setTextColor(Color.RED);
            btnCreateUser.setEnabled(false);

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


            //executa a animação para aletar ao usuário que o email já existe
            thread.execute();
        }
        else{
            btnCreateUser.setEnabled(true);
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
                //Log.w("userCreated", s);

                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(s);

                    if(jsonObject.has("jaExiste")){

                        String s1 = jsonObject.get("jaExiste").toString();

                        onTaskCompleted(s1);
                    }
                    else{
                        if(jsonObject.getString("status").equals("ok")){

                            AlertDialog.Builder builder  = new AlertDialog.Builder(CadastrarUserActivity.this);

                            builder.setTitle("Fidelize - Cadastro de Usuário");
                            builder.setMessage("Usuário Cadastrado com Sucesso");

                            builder.setNeutralButton("ENTRAR", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(CadastrarUserActivity.this, MainActivity.class));
                                }
                            });

                            builder.show();
                        }
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }



}
