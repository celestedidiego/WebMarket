/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.model.impl;

import it.univaq.webmarket.data.model.CaratteristicaRichiesta;
import it.univaq.webmarket.data.model.Ordinante;
import it.univaq.webmarket.data.model.RichiestaOrdine;
import it.univaq.webmarket.framework.data.DataItemImpl;

import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author cdidi
 */

public class RichiestaOrdineImpl extends DataItemImpl<Integer> implements RichiestaOrdine {
    
    private String note;
    private String codiceRichiesta;
    private LocalDate data;
    private Ordinante ordinante;
    private List<CaratteristicaRichiesta> caratteristicheRichiesta;
    private boolean presaInCarico = false;

    public RichiestaOrdineImpl() {
        super();
        this.note = "";
        this.codiceRichiesta = "";
        this.data = null;
        this.ordinante = null;
        this.caratteristicheRichiesta = null;
    }

    @Override
    public Ordinante getOrdinante() { return this.ordinante; }

    @Override
    public void setOrdinante(Ordinante ordinante) {
        this.ordinante = ordinante;
    }

    @Override
    public String getNote() {
        return this.note;
    }

    @Override
    public void setNote(String note) { this.note = note;}

    @Override
    public String getCodiceRichiesta() {
        return this.codiceRichiesta;
    }

    @Override
    public void setCodiceRichiesta(String codiceRichiesta) { this.codiceRichiesta = codiceRichiesta; }

    @Override
    public LocalDate getData() {
        return this.data;
    }

    @Override
    public void setData(LocalDate data) { this.data = data; }

    @Override
    public List<CaratteristicaRichiesta> getCaratteristicheRichiesta() {
        return this.caratteristicheRichiesta;
    }

    @Override
    public void setCaratteristicheRichiesta(List<CaratteristicaRichiesta> caratteristicheRichiesta) {
        this.caratteristicheRichiesta = caratteristicheRichiesta;
    }

    @Override
    public boolean isPresaInCarico() {
        return presaInCarico;
    }

    @Override
    public void setPresaInCarico(boolean presaInCarico) {
        this.presaInCarico = presaInCarico;
    }

    @Override
    public String toString() {
        return "RichiestaOrdineImpl{" +
                "note='" + note + '\'' +
                ", codiceRichiesta='" + codiceRichiesta + '\'' +
                ", data=" + data +
                ", ordinante=" + ordinante +
                ", caratteristicaRichiesta=" + caratteristicheRichiesta +
                '}';
    }
}
