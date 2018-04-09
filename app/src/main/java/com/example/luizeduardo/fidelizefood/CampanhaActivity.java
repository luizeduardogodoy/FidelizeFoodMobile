package com.example.luizeduardo.fidelizefood;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CampanhaActivity extends AppCompatActivity {

    private EditText dtInicio;
    private EditText dtTermino;
    private EditText nome;
    private EditText obs;
    private EditText qtde;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campanha);

        dtInicio = findViewById(R.id.editInicioCampanha);
        dtTermino = findViewById(R.id.editTerminoCampanha);
        obs = findViewById(R.id.editObsCampanha);
        nome = findViewById(R.id.editNomeCampanha);
        qtde = findViewById(R.id.editQtdeCamapanha);

        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        dtInicio.setText(simpleDateFormat.format(date).toString());

    }

    public void btnSalvar(View v){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {

            if(!dtInicio.getText().toString().equals("") && !dtTermino.getText().toString().equals("")) {

                Date data1 = simpleDateFormat.parse(dtInicio.getText().toString());
                Date data2 = simpleDateFormat.parse(dtTermino.getText().toString());


                if (data1.getTime() > data2.getTime()) {
                    Utils.alertInfo(this, "Fidelize - regras de tela","Data de início não pode ser maior que a data de término");

                }
            }
            else{

                Utils.alertInfo(this, "Fidelize - regras de tela","Informar data de início e data de término");

            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        String vQtde = qtde.getText().toString();

        if(!vQtde.equals("")) {

            int qtdeInt = Integer.parseInt(vQtde);

            if (qtdeInt <= 0) {

                Utils.alertInfo(CampanhaActivity.this, "A quantidade deve ser maior que zero", "Fidelize - regras de tela");

            }
        }
        else{

            Utils.alertInfo(CampanhaActivity.this, "Informar a quantidade", "Fidelize - regras de tela");


        }




    }



}
