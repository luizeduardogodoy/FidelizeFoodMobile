package com.example.luizeduardo.fidelizefood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        Toast.makeText(this,nomeUser,Toast.LENGTH_LONG).show();

    }
}
