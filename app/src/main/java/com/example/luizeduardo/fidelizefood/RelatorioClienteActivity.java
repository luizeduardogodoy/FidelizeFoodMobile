package com.example.luizeduardo.fidelizefood;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RelatorioClienteActivity extends AppCompatActivity {

    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_cliente);

        tableLayout = findViewById(R.id.tableC);

        String post = "req=relClientes";
        post += "&UsuarioID=" + UserSingleton.getInstance().getUser().getId();

        new ListarUsers().execute(ConnectAPITask.urlAPI, post);

    }

    private class ListarUsers extends ConnectAPITask{

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject users = new JSONObject(s);

                JSONArray array = users.getJSONArray("users");

                for(int i = 0; i < array.length(); i++){

                    JSONObject object = array.getJSONObject(i);

                    TableRow tableRow = new TableRow(RelatorioClienteActivity.this);
                    tableRow.setPadding(0,2,0,10);

                    TextView textView = new TextView(getBaseContext());

                    textView.setText(object.getString("nomecliente"));
                    textView.setTextColor(Color.BLACK);

                    TextView textView2 = new TextView(getBaseContext());
                    textView2.setText(object.getString("email"));
                    textView2.setTextColor(Color.BLACK);

                    TextView textView3 = new TextView(getBaseContext());
                    textView3.setText(object.getString("ultima"));
                    textView3.setTextColor(Color.BLACK);

                    tableRow.addView(textView);

                    tableRow.addView(textView2);

                    tableRow.addView(textView3);

                    tableLayout.addView(tableRow);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
