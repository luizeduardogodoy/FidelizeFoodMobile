package com.example.luizeduardo.fidelizefood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CarimboActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carimbo);


        /*try {*/




           /* String jsonResponse;

            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic NGEwMGZmMjItY2NkNy0xMWUzLTk5ZDUtMDAwYzI5NDBlNjJj");
            con.setRequestMethod("POST");

            String strJsonBody = "{"
                    +   "\"app_id\": \"c72fac2d-99ae-4623-a530-68f252d9399d\","
                    +   "\"include_player_ids\": [\"1839aa31-ac7e-43c1-8a67-7dae42a19661\"],"
                    +   "\"data\": {\"foo\": \"bar\"},"
                    +   "\"contents\": {\"en\": \"English Message\"}"
                    + "}";


            System.out.println("strJsonBody:\n" + strJsonBody);

            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            System.out.println("httpResponse: " + httpResponse);

            if (  httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            System.out.println("jsonResponse:\n" + jsonResponse);

        } catch(Throwable t) {
            t.printStackTrace();
        }*/


    }

    public void carimbar(View v){

        EditText txtCarimbo = findViewById(R.id.editCPFCarimbo);
        CheckBox permiteMais = findViewById(R.id.checkPermiteMaisCarimbo);

        if(!txtCarimbo.getText().toString().equals("")) {

            String post = null;

            post = "req=carimbo";
            post += "&UsuarioID=" + UserSingleton.getInstance().getUser().getId();
            post += "&idusercliente=" + txtCarimbo.getText().toString();
            post += "&permiteMaisDeUm=" + (permiteMais.isChecked() ? "S" : "N");

            new CarimboTask().execute(ConnectAPITask.urlAPI, post);
        }
        else{
            Utils.alertInfo(this,"Fidelize - Aviso", "Informar o CPF");
        }

    }

    class CarimboTask extends ConnectAPITask{

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject json =  null;

            try {
                json = new JSONObject(s);

                if(json.has("nome") && json.getString("status").equals("ok")) {

                    Utils.alertInfo(CarimboActivity.this,
                            "Fidelize - Carimbo",
                            json.getString("nome") + " - " + json.getString("mensagem"));


                    if(json.has("idnotificacao")) {

                        String userId = json.getString("idnotificacao");
                        try {
                            OneSignal.postNotification(new JSONObject(
                                    "{'contents': " +
                                            "{'en':'Carimbo registrado pelo Restaurante: "  +  UserSingleton.getInstance().getUser().getNome() +  " '}, " +
                                            "'include_player_ids': ['" + userId + "']," +
                                    "'app_id': 'c72fac2d-99ae-4623-a530-68f252d9399d'}"), null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
                else if (json.getString("status").equals("!restaurante")){
                    Utils.alertInfo(CarimboActivity.this,
                            "Fidelize - Carimbo",
                            "Dados do Restaurante não foram cadastrados");
                }
                else if (json.getString("status").equals("!temcampanha")){
                    Utils.alertInfo(CarimboActivity.this,
                            "Fidelize - Carimbo",
                            "Não existe campanha ativa");
                }

                else{
                    Utils.alertInfo(CarimboActivity.this,
                            "Fidelize - Carimbo",
                            "Cliente não encontrado");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
