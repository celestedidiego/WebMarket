/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.model.impl;

import it.univaq.webmarket.data.model.Caratteristica;
import it.univaq.webmarket.data.model.CaratteristicaRichiesta;
import it.univaq.webmarket.framework.data.DataItemImpl;

/**
 *
 * @author cdidi
 */

public class CaratteristicaRichiestaImpl extends DataItemImpl<Integer> implements CaratteristicaRichiesta {
    
    private Caratteristica caratteristica;
    private String valore;

    public CaratteristicaRichiestaImpl() {
        super();
        this.caratteristica = null;
        this.valore = "";
    }

    @Override
    public Caratteristica getCaratteristica() {
        return caratteristica;
    }

    @Override
    public void setCaratteristica(Caratteristica caratteristica) {
        this.caratteristica = caratteristica;
    }

    @Override
    public String getValore() {
        return valore;
    }

    @Override
    public void setValore(String valore) {
        this.valore = valore;
    }

    @Override
    public String toString() {
        return "CaratteristicaRichiestaImpl{" +
                "caratteristica=" + caratteristica +
                ", valore='" + valore + '\'' +
                '}';
    }
}
