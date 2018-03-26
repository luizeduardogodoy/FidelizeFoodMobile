package com.example.luizeduardo.fidelizefood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FidelizeMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fidelize_main);

        TextView textView = findViewById(R.id.textView6);

      //  SharedPreferences sharedPreferences = this.getSharedPreferences("InfoUser", Context.MODE_PRIVATE);

        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        int tipo = sharedPreferences.getInt("tipo",1);
        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        String nomeUser = bundle.getString("nomeUser");

        textView.setText(nomeUser + " - tipo " + tipo);

        LinearLayout cliente = findViewById(R.id.layoutCliente);
        LinearLayout restaurante = findViewById(R.id.layoutRest);

        restaurante.setVisibility(View.GONE);
        cliente.setVisibility(View.GONE);

        if(tipo == 2){
            restaurante.setVisibility(View.VISIBLE);
        }
        else{
            cliente.setVisibility(View.VISIBLE);

            ListView cartoes = findViewById(R.id.listViewCartoes);
            String[] c = new String[]{"Cardamon","Celma","Jabuti"};

            ArrayAdapter<String> adapter =
                    new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, c);

            cartoes.setAdapter(adapter);
        }


        Toast.makeText(this,nomeUser,Toast.LENGTH_LONG).show();

    }
}
