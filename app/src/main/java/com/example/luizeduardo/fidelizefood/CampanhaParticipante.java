package com.example.luizeduardo.fidelizefood;

/**
 * Created by luiz on 28/03/2018.
 */

public class CampanhaParticipante {

    private Integer idUsuarioCampanha;
    private String nomeRestaurante;
    private Integer qtde;
    private Integer carimbo;

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

    public Integer getQtde() {
        return qtde;
    }

    public void setQtde(Integer qtde) {
        this.qtde = qtde;
    }

    public Integer getCarimbo() {
        return carimbo;
    }

    public void setCarimbo(Integer carimbo) {
        this.carimbo = carimbo;
    }

    @Override
    public String toString() {
        return nomeRestaurante + ": " + carimbo + " de " + qtde ;
    }
}
