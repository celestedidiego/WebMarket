/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// Viene utilizzato per ritardare il caricamento dell'oggetto Caratteristica fino a quando non è effettivamente necessario, migliorando così le prestazioni e riducendo il caricamento di dati non necessari. Inoltre, il flag modified tiene traccia delle modifiche apportate all'oggetto, utile per operazioni di salvataggio o aggiornamento dei dati.

package it.univaq.webmarket.data.model.impl.proxy;

import it.univaq.webmarket.data.DAO.CaratteristicaDAO;
import it.univaq.webmarket.data.model.Caratteristica;
import it.univaq.webmarket.data.model.impl.CaratteristicaRichiestaImpl;
import it.univaq.webmarket.framework.data.DataException;
import it.univaq.webmarket.framework.data.DataItemProxy;
import it.univaq.webmarket.framework.data.DataLayer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cdidi
 */

public class CaratteristicaRichiestaProxy extends CaratteristicaRichiestaImpl implements DataItemProxy {
    
    protected boolean modified;
    protected DataLayer dataLayer;
    protected Integer caratteristica_key;

    public CaratteristicaRichiestaProxy(DataLayer dataLayer) {
        super();
        this.dataLayer = dataLayer;
        this.modified = false;
        this.caratteristica_key = 0;
    }

    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
    }

    @Override
    public void setCaratteristica(Caratteristica caratteristica) {
        super.setCaratteristica(caratteristica);
        this.caratteristica_key = caratteristica.getKey();
        this.modified = true;
    }

    @Override
    public void setValore(String valore) {
        super.setValore(valore);
        this.modified = true;
    }

    @Override
    public Caratteristica getCaratteristica() {
        if (super.getCaratteristica() == null && caratteristica_key > 0) {
            try {
                super.setCaratteristica(((CaratteristicaDAO) dataLayer.getDAO(Caratteristica.class)).getCaratteristica(caratteristica_key));
            } catch (DataException ex) {
                Logger.getLogger(CaratteristicaRichiestaProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getCaratteristica();
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }

    public void setCaratteristica_key(Integer caratteristica_key) {
        this.caratteristica_key = caratteristica_key;
        super.setCaratteristica(null);
    }
}
