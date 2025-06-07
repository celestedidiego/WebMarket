/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.model.impl;

import it.univaq.webmarket.data.model.Ordinante;
import it.univaq.webmarket.data.model.Ordine;
import it.univaq.webmarket.data.model.PropostaAcquisto;
import it.univaq.webmarket.data.model.Tecnico;
import it.univaq.webmarket.framework.data.DataItemImpl;

import java.time.LocalDate;

/**
 *
 * @author cdidi
 */

public class OrdineImpl extends DataItemImpl<Integer> implements Ordine {
    
    private String statoConsegna, risposta;
    private LocalDate dataConsegna;
    private Tecnico tecnico;
    private PropostaAcquisto propostaAcquisto;
    private Ordinante ordinante;
    private int ordinante_key;

    public OrdineImpl() {
        super();
        this.statoConsegna = "";
        this.risposta = "";
        this.tecnico = null;
        this.propostaAcquisto = null;
        this.dataConsegna = null;
    }

    @Override
    public String getStatoConsegna() {
        return statoConsegna;
    }

    @Override
    public void setStatoConsegna(String statoConsegna) {
        this.statoConsegna = statoConsegna;
    }

    @Override
    public String getRisposta() {
        return risposta;
    }

    @Override
    public void setRisposta(String risposta) {
        this.risposta = risposta;
    }

    @Override
    public Tecnico getTecnico() {
        return tecnico;
    }

    @Override
    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
    }

    @Override
    public PropostaAcquisto getPropostaAcquisto() {
        return propostaAcquisto;
    }

    @Override
    public void setPropostaAcquisto (PropostaAcquisto propostaAcquisto) {
        this.propostaAcquisto = propostaAcquisto;
    }

    @Override
    public LocalDate getDataConsegna() {
        return dataConsegna;
    }

    @Override
    public void setDataConsegna(LocalDate dataConsegna) {
        this.dataConsegna = dataConsegna;
    }

    @Override
    public Ordinante getOrdinante() {
        return ordinante;
    }

    @Override
    public void setOrdinante(Ordinante ordinante) {
        this.ordinante = ordinante;
    }

    @Override
    public int getOrdinanteKey() {
        return ordinante_key;
    }

    @Override
    public void setOrdinanteKey(int key) {
        this.ordinante_key = key;
    }
    
    @Override
    public String toString() {
        return "OrdineImpl{" +
                "statoConsegna=" + statoConsegna +
                ", risposta=" + risposta +
                ", dataConsegna=" + dataConsegna +
                ", tecnicoOrdini=" + tecnico +
                ", proposta=" + propostaAcquisto +
                '}';
    }
}
