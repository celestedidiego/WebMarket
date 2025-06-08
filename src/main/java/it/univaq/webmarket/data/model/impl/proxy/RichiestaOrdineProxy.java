/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.model.impl.proxy;

import it.univaq.webmarket.data.DAO.CaratteristicaDAO;
import it.univaq.webmarket.data.DAO.OrdinanteDAO;
import it.univaq.webmarket.data.model.*;
import it.univaq.webmarket.data.model.impl.RichiestaOrdineImpl;
import it.univaq.webmarket.framework.data.DataException;
import it.univaq.webmarket.framework.data.DataItemProxy;
import it.univaq.webmarket.framework.data.DataLayer;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cdidi
 */

public class RichiestaOrdineProxy extends RichiestaOrdineImpl implements DataItemProxy {
    
    protected boolean modified;
    protected DataLayer dataLayer;
    protected Integer ordinante_key;

    public RichiestaOrdineProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.modified = false;
        this.ordinante_key = 0;
    }

    
    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
    }

    @Override
    public Ordinante getOrdinante() {
        if (super.getOrdinante() == null && ordinante_key > 0) {
            try {
                Ordinante ordinante = ((OrdinanteDAO) dataLayer.getDAO(Ordinante.class)).getOrdinante(ordinante_key);
                super.setOrdinante(ordinante);
            } catch (DataException e) {
                Logger.getLogger(RichiestaOrdineProxy.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
            }
        }

        return super.getOrdinante();
    }

    @Override
    public void setOrdinante(Ordinante ordinante) {
        super.setOrdinante(ordinante);
        this.ordinante_key = ordinante.getKey();
        this.modified = true;
    }

    @Override
    public void setNote(String note) {
        super.setNote(note);
        this.modified = true;
    }

    @Override
    public void setCodiceRichiesta(String codiceRichiesta) {
        super.setCodiceRichiesta(codiceRichiesta);
        this.modified = true;
    }

    @Override
    public void setData(LocalDate data) {
        super.setData(data);
        this.modified = true;
    }

    @Override
    public void setCaratteristicheRichiesta(List<CaratteristicaRichiesta> caratteristicheRichiesta) {
        super.setCaratteristicheRichiesta(caratteristicheRichiesta);
    }

    @Override
    public List<CaratteristicaRichiesta> getCaratteristicheRichiesta() {
        if (super.getCaratteristicheRichiesta() == null) {
            try {
                List<CaratteristicaRichiesta> caratteristicheRichiesta = ((CaratteristicaDAO) dataLayer.getDAO(Caratteristica.class))
                        .getCaratteristicheRichiesta(this);
                super.setCaratteristicheRichiesta(caratteristicheRichiesta);
            } catch (DataException ex) {
                Logger.getLogger(RichiestaOrdineProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getCaratteristicheRichiesta();
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }

    public void setOrdinante_key(Integer ordinante_key) {
        this.ordinante_key = ordinante_key;
        super.setOrdinante(null);
    }

        @Override
    public boolean isPresaInCarico() {
        return super.isPresaInCarico();
    }

    @Override
    public void setPresaInCarico(boolean presaInCarico) {
        super.setPresaInCarico(presaInCarico);
        this.modified = true;
    }
}
