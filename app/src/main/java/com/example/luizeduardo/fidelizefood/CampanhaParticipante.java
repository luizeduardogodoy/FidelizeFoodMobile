package com.example.luizeduardo.fidelizefood;

/**
 * Created by luiz on 28/03/2018.
 */

public class CampanhaParticipante {

    private Integer idUsuarioCampanha;
    private String nomeRestaurante;

    public Integer getIdUsuarioCampanha() {
        return idUsuarioCampanha;
    }

    public void setIdUsuarioCampanha(Integer idUsuarioCampanha) {
        this.idUsuarioCampanha = idUsuarioCampanha;
    }

    public String getNomeRestaurante() {
        return nomeRestaurante;
    }

    public void setNomeRestaurante(String nomeRestaurante) {
        this.nomeRestaurante = nomeRestaurante;
    }

    @Override
    public String toString() {
        return nomeRestaurante;
    }
}
