package com.example.luizeduardo.fidelizefood;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by luiz on 28/03/2018.
 */

public class CampanhaParticipanteAdpter extends ArrayAdapter<CampanhaParticipante> {


    public CampanhaParticipanteAdpter(@NonNull Context context, int resource, ArrayList<CampanhaParticipante> campanhaParticipantes) {
        super(context, resource, campanhaParticipantes);
    }





}
