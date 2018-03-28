package com.example.luizeduardo.fidelizefood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CampanhaPartItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campanha_part_item);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        String nomeRest = bundle.getString("restaurante");


        TextView txtNomeRest = findViewById(R.id.txtNomeRest);

        txtNomeRest.setText(nomeRest);

    }
}
