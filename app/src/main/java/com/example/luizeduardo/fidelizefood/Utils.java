package com.example.luizeduardo.fidelizefood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by luiz on 08/04/2018.
 */

public class Utils {


    public static void alertInfo(Context context, String title, String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);

        builder.setMessage(message);

        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


        builder.show();


    }
}
