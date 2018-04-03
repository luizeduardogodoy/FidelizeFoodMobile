package com.example.luizeduardo.fidelizefood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class CadastrarUserActivity extends AppCompatActivity {

//teste committt2

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

        this.nome = findViewById(R.id.txtCadNome);
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
                    if (email.getText().toString().equals("luiz@actuary.com.br")) {
                        Toast.makeText(view.getContext(), "EMAIL JÁ EXISTENTE!!", Toast.LENGTH_LONG).show();
                        email.setText("");
                        email.setHint("Email");

                        //new ConnectAPITask().execute("http://verificaemail");

                    }
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

    private class CreateUserTask extends ConnectAPITask{

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s == "API_ERROR"){

                Toast.makeText(getBaseContext(),"Houve um problema com a conexão", Toast.LENGTH_SHORT).show();
            }
            else {

                Log.w("userCreated", s);
            }
        }
    }



}
