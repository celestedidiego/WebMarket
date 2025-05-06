/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.model.impl;

import it.univaq.webmarket.data.model.RichiestaOrdine;
import it.univaq.webmarket.data.model.RichiestaPresaInCarico;
import it.univaq.webmarket.data.model.Tecnico;
import it.univaq.webmarket.framework.data.DataItemImpl;

/**
 *
 * @author cdidi
 */

public class RichiestaPresaInCaricoImpl extends DataItemImpl<Integer> implements RichiestaPresaInCarico {
    
    private RichiestaOrdine richiestaOrdine;
    private Tecnico tecnico;

    public RichiestaPresaInCaricoImpl() {
        super();
        this.richiestaOrdine = null;
        this.tecnico = null;
    }

    @Override
    public RichiestaOrdine getRichiestaOrdine() {
        return richiestaOrdine;
    }

    @Override
    public void setRichiestaOrdine(RichiestaOrdine richiestaOrdine) {
        this.richiestaOrdine = richiestaOrdine;
    }

    @Override
    public Tecnico getTecnico() {
        return tecnico;
    }

    @Override
    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
    }
}
