package com.example.luizeduardo.fidelizefood;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by luiz on 22/03/2018.
 */

public class ConnectAPITask extends AsyncTask<String,Void, String>{

    public static String urlAPI = "http://fidelizefood.azurewebsites.net/public_html/index.php";
    //public static String urlAPI = "http://10.0.3.2/index.php";
                                 //  http://softprevirpps/fidelizefood-backend/public_html/

    @Override
    protected String doInBackground(String... strings) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL url = new URL(strings[0]);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoOutput(true);

            //connection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
           // connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(strings[1]);
            writer.flush();
            writer.close();
            //connection.connect();
            int statusCode = connection.getResponseCode();

            if(statusCode == 200){

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    stringBuilder.append(line + " ");

                    Log.w("retornoAPI", line);
                }
            }
            else{
                return "API_ERROR";
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return stringBuilder.toString();
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);



        //Log.e("s",s);
        // JSONObject object = null;

        // JSONArray array = object


    }
}
