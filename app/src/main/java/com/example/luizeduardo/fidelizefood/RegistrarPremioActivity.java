package com.example.luizeduardo.fidelizefood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegistrarPremioActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_premio);
    }


    public void registrar(View view){


        TextView textView = findViewById(R.id.editCpfPremio);

        if(textView.getText().toString().equals("")){


            Utils.alertInfo(this, "Fidelize - Aviso", "Informe o cpf do seu cliente");
        }
        else{

            String post = "req=premi0";



            new RegistrarPremio().execute(ConnectAPITask.urlAPI, post);
        }




    }


    class RegistrarPremio extends ConnectAPITask{

    }
}
