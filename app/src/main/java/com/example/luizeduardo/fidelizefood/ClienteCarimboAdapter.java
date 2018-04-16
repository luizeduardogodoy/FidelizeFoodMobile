package com.example.luizeduardo.fidelizefood;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by luiz on 15/04/2018.
 */

public class ClienteCarimboAdapter extends ArrayAdapter<RowCartao> {

    Context context;
    private ArrayList<RowCartao> rowsCartao;

    public ClienteCarimboAdapter(@NonNull Context context, @NonNull ArrayList<RowCartao> objects) {
        super(context,R.layout.cliente_carimbos, objects);

        this.context = context;
        this.rowsCartao = objects;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.cliente_carimbos, parent, false);


        RowCartao rc = rowsCartao.get(position);
        TextView coluna01 = rowView.findViewById(R.id.txtCarimboColuna01);
        TextView coluna02 = rowView.findViewById(R.id.txtCarimboColuna02);
        TextView coluna03 = rowView.findViewById(R.id.txtCarimboColuna03);
        TextView coluna04 = rowView.findViewById(R.id.txtCarimboColuna04);


        ImageView img01 = rowView.findViewById(R.id.imgCarimboColuna01);
        ImageView img02 = rowView.findViewById(R.id.imgCarimboColuna02);
        ImageView img03 = rowView.findViewById(R.id.imgCarimboColuna03);
        ImageView img04 = rowView.findViewById(R.id.imgCarimboColuna04);

        Log.w("Adpater", rc.carimbos.toString());

        if(rc.carimbos.get(0) != null){
            coluna01.setText(rc.carimbos.get(0).toString());

            img01.setImageResource(R.drawable.carimbos);


            if(rc.carimbos.get(0).equals("vazio")){
                img01.setImageResource(R.drawable.prato);

            }


        }


        if(rc.carimbos.get(1) != null){
            coluna02.setText(rc.carimbos.get(1).toString());

            img02.setImageResource(R.drawable.carimbos);

            if(rc.carimbos.get(1).equals("vazio")){
                img02.setImageResource(R.drawable.prato);

            }

        }


        if(rc.carimbos.get(2) != null){
            coluna03.setText(rc.carimbos.get(2).toString());

            img03.setImageResource(R.drawable.carimbos);

            if(rc.carimbos.get(2).equals("vazio")){
                img03.setImageResource(R.drawable.prato);
            }
        }


        if(rc.carimbos.get(3) != null){
            coluna04.setText(rc.carimbos.get(3).toString());

            img04.setImageResource(R.drawable.carimbos);

            if(rc.carimbos.get(3).equals("vazio")){
                img04.setImageResource(R.drawable.prato);
            }
        }


        return rowView;

    }
}
