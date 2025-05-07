/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.model.impl.proxy;

import it.univaq.webmarket.data.DAO.RichiestaPresaInCaricoDAO;
import it.univaq.webmarket.data.model.RichiestaPresaInCarico;
import it.univaq.webmarket.data.model.impl.PropostaAcquistoImpl;
import it.univaq.webmarket.framework.data.DataException;
import it.univaq.webmarket.framework.data.DataItemProxy;
import it.univaq.webmarket.framework.data.DataLayer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cdidi
 */

public class PropostaAcquistoProxy extends PropostaAcquistoImpl implements DataItemProxy {
    
    protected boolean modified;
    protected DataLayer dataLayer;
    protected Integer richiestaPresaInCarico_key;

    public PropostaAcquistoProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.modified = false;
        this.richiestaPresaInCarico_key = 0;
    }

    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
    }

    @Override
    public void setRichiestaPresaInCarico(RichiestaPresaInCarico richiestaPresaInCarico) {
        super.setRichiestaPresaInCarico(richiestaPresaInCarico);
        this.richiestaPresaInCarico_key = richiestaPresaInCarico.getKey();
        this.modified = true;
    }

    @Override
    public RichiestaPresaInCarico getRichiestaPresaInCarico() {
        if (super.getRichiestaPresaInCarico() == null && richiestaPresaInCarico_key > 0) {
            try {
                super.setRichiestaPresaInCarico(((RichiestaPresaInCaricoDAO) dataLayer.getDAO(RichiestaPresaInCarico.class)).getRichiestaPresaInCarico(richiestaPresaInCarico_key));
            } catch (DataException ex) {
                Logger.getLogger(PropostaAcquistoProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getRichiestaPresaInCarico();
    }

    @Override
    public void setCodiceProdotto(String codiceProdotto) {
        super.setCodiceProdotto(codiceProdotto);
        this.modified = true;
    }

    @Override
    public void setProduttore(String produttore) {
        super.setProduttore(produttore);
        this.modified = true;
    }
    
    @Override
    public void setNomeProdotto(String nomeProdotto) {
        super.setNomeProdotto(nomeProdotto);
        this.modified = true;
    }
    
    @Override
    public void setPrezzo(Float prezzo) {
        super.setPrezzo(prezzo);
        this.modified = true;
    }

    @Override
    public void setNote(String note) {
        super.setNote(note);
        this.modified = true;
    }

    @Override
    public void setURL(String URL) {
        super.setURL(URL);
        this.modified = true;
    }

    @Override
    public void setStatoProposta(String statoProposta) {
        super.setStatoProposta(statoProposta);
        this.modified = true;
    }

    @Override
    public void setMotivazione(String motivazione) {
        super.setMotivazione(motivazione);
        this.modified = true;
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }

    public void setRichiestaPresaInCarico_key(Integer richiestaPresaInCarico_key) {
        this.richiestaPresaInCarico_key = richiestaPresaInCarico_key;
        super.setRichiestaPresaInCarico(null);
    }
}
