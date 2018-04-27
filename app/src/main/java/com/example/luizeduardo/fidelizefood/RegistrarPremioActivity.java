package com.example.luizeduardo.fidelizefood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

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

            String post = "req=premi0&idusercliente="+ textView.getText().toString();
            post += "&UsuarioID=" + UserSingleton.getInstance().getUser().getId();


            new RegistrarPremio().execute(ConnectAPITask.urlAPI, post);
        }




    }


    class RegistrarPremio extends ConnectAPITask{
        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);

            JSONObject json =  null;

            try {
                json = new JSONObject(s);

                if (json.getString("status").equals("ok")) {


                    Utils.alertInfo(RegistrarPremioActivity.this,
                            "Fidelize - Prêmio",
                            json.getString("nome") + " - " + json.getString("mensagem"));

                    if(json.has("idnotificacao")) {

                        String userId = json.getString("idnotificacao");
                        try {
                            OneSignal.postNotification(new JSONObject(
                                    "{'contents': " +
                                            "{'en':'Você atingiu seu cartão de fidelidade. Restaurante: "  +  UserSingleton.getInstance().getUser().getNome() +  " '}, " +
                                            "'include_player_ids': ['" + userId + "']," +
                                            "'app_id': 'c72fac2d-99ae-4623-a530-68f252d9399d'}"), null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
