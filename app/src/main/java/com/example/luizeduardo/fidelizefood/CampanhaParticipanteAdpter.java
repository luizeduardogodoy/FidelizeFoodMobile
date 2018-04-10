package com.example.luizeduardo.fidelizefood;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by luiz on 28/03/2018.
 */

public class CampanhaParticipanteAdpter extends ArrayAdapter<CampanhaParticipante> {

    Context context;
    private ArrayList<CampanhaParticipante> campanhaParticipantes;

    public CampanhaParticipanteAdpter(@NonNull Context context,  ArrayList<CampanhaParticipante> campanhaParticipantes) {
        super(context, R.layout.campanha_participante, campanhaParticipantes);
        this.context = context;
        this.campanhaParticipantes = campanhaParticipantes;
    }


    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.campanha_participante, parent, false);

        TextView nomeRest = rowView.findViewById(R.id.campItemNomeRest);
        TextView label = rowView.findViewById(R.id.campItemLabel);

        //converte a data em String no formato desejado
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataFinal = simpleDateFormat.format(campanhaParticipantes.get(pos).getDtFinal());

        nomeRest.setText(campanhaParticipantes.get(pos).getNomeRestaurante());
        label.setText(campanhaParticipantes.get(pos).getCarimbo() + " de " + campanhaParticipantes.get(pos).getQtde()
        + " - Validade: " +  dataFinal);


        return rowView;
    }
}
