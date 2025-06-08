/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.model.impl.proxy;

import it.univaq.webmarket.data.DAO.RichiestaOrdineDAO;
import it.univaq.webmarket.data.DAO.TecnicoDAO;
import it.univaq.webmarket.data.model.RichiestaOrdine;
import it.univaq.webmarket.data.model.Tecnico;
import it.univaq.webmarket.data.model.impl.RichiestaPresaInCaricoImpl;
import it.univaq.webmarket.framework.data.DataException;
import it.univaq.webmarket.framework.data.DataItemProxy;
import it.univaq.webmarket.framework.data.DataLayer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cdidi
 */

public class RichiestaPresaInCaricoProxy extends RichiestaPresaInCaricoImpl implements DataItemProxy {
    
    protected boolean modified;
    protected DataLayer dataLayer;
    protected Integer tecnico_key;
    protected Integer richiestaOrdine_key;

    public RichiestaPresaInCaricoProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.modified = false;
        this.tecnico_key = 0;
        this.richiestaOrdine_key = 0;
    }

    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
    }

    @Override
    public void setRichiestaOrdine(RichiestaOrdine richiestaOrdine) {
        super.setRichiestaOrdine(richiestaOrdine);
        this.richiestaOrdine_key = richiestaOrdine.getKey();
        this.modified = true;
    }


    @Override
    public void setTecnico(Tecnico tecnico) {
        super.setTecnico(tecnico);
        this.tecnico_key = tecnico.getKey();
        this.modified = true;
    }

    @Override
    public RichiestaOrdine getRichiestaOrdine() {
        if (super.getRichiestaOrdine() == null && richiestaOrdine_key > 0) {
            try {
                super.setRichiestaOrdine(((RichiestaOrdineDAO) dataLayer.getDAO(RichiestaOrdine.class)).getRichiestaOrdine(richiestaOrdine_key));
            } catch (DataException ex) {
                Logger.getLogger(RichiestaPresaInCaricoProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return super.getRichiestaOrdine();
    }

    @Override
    public Tecnico getTecnico() {
        if (super.getTecnico() == null && tecnico_key > 0) {
            try {
                super.setTecnico(((TecnicoDAO) dataLayer.getDAO(Tecnico.class)).getTecnico(tecnico_key));
            } catch (DataException ex) {
                Logger.getLogger(RichiestaPresaInCaricoProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getTecnico();
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }

    public void setTecnico_key(Integer tecnico_key) {
        this.tecnico_key = tecnico_key;
        super.setTecnico(null);
    }

    public void setRichiestaOrdine_key(Integer caratteristica_key) {
        this.richiestaOrdine_key = caratteristica_key;
        super.setRichiestaOrdine(null);
    }
}
