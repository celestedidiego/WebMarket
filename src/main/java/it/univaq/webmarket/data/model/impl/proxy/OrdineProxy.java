/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.model.impl.proxy;

import it.univaq.webmarket.data.DAO.PropostaAcquistoDAO;
import it.univaq.webmarket.data.DAO.TecnicoDAO;
import it.univaq.webmarket.data.model.PropostaAcquisto;
import it.univaq.webmarket.data.model.Tecnico;
import it.univaq.webmarket.data.model.impl.OrdineImpl;
import it.univaq.webmarket.framework.data.DataException;
import it.univaq.webmarket.framework.data.DataItemProxy;
import it.univaq.webmarket.framework.data.DataLayer;
import it.univaq.webmarket.data.DAO.OrdinanteDAO;
import it.univaq.webmarket.data.model.Ordinante;

import java.time.LocalDate;
import java.util.logging.Logger;

/**
 *
 * @author cdidi
 */

public class OrdineProxy extends OrdineImpl implements DataItemProxy {
    
    protected boolean modified;
    protected DataLayer dataLayer;

    protected Integer tecnico_key;
    protected Integer propostaAcquisto_key;

    protected Integer ordinante_key;

    public OrdineProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.modified = false;
        this.tecnico_key = 0;
        this.propostaAcquisto_key = 0;
    }

    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
    }

    @Override
    public void setRisposta(String risposta) {
        super.setRisposta(risposta);
        this.modified = true;
    }

    @Override
    public void setStatoConsegna(String statoConsegna) {
        super.setStatoConsegna(statoConsegna);
        this.modified = true;
    }

    @Override
    public void setDataConsegna(LocalDate dataConsegna) {
        super.setDataConsegna(dataConsegna);
        this.modified = true;
    }

    @Override
    public void setTecnico(Tecnico tecnico) {
        super.setTecnico(tecnico);
        this.tecnico_key = tecnico.getKey();
        this.modified = true;
    }

    @Override
    public void setPropostaAcquisto(PropostaAcquisto propostaAcquisto) {
        super.setPropostaAcquisto(propostaAcquisto);
        this.propostaAcquisto_key = propostaAcquisto.getKey();
        this.modified = true;
    }

    @Override
    public Tecnico getTecnico() {
        if (super.getTecnico() == null && tecnico_key > 0) {
            try {
                super.setTecnico(((TecnicoDAO) dataLayer.getDAO(Tecnico.class)).getTecnico(tecnico_key));
            } catch (DataException ex) {
                Logger.getLogger(OrdineProxy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }

        return super.getTecnico();
    }

    @Override
    public PropostaAcquisto getPropostaAcquisto() {
        if (super.getPropostaAcquisto() == null && propostaAcquisto_key > 0) {
            try {
                super.setPropostaAcquisto(((PropostaAcquistoDAO) dataLayer.getDAO(PropostaAcquisto.class)).getPropostaAcquisto(propostaAcquisto_key));
            } catch (DataException ex) {
                Logger.getLogger(OrdineProxy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }

        return super.getPropostaAcquisto();
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }

    public void setTecnicoKey(Integer key) {
        this.tecnico_key = key;
        super.setTecnico(null);
    }

    public void setPropostaAcquistoKey(Integer key) {
        this.propostaAcquisto_key = key;
        super.setPropostaAcquisto(null);
    }

    @Override
    public int getOrdinanteKey() {
        return ordinante_key != null ? ordinante_key : 0;
    }

    @Override
    public void setOrdinanteKey(int key) {
        this.ordinante_key = key;
        super.setOrdinante(null);
    }

    @Override
    public Ordinante getOrdinante() {
        if (super.getOrdinante() == null && ordinante_key != null && ordinante_key > 0) {
            try {
                super.setOrdinante(((OrdinanteDAO) dataLayer.getDAO(Ordinante.class)).getOrdinante(ordinante_key));
            } catch (DataException ex) {
                Logger.getLogger(OrdineProxy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
        return super.getOrdinante();
    }

    @Override
    public void setOrdinante(Ordinante ordinante) {
        super.setOrdinante(ordinante);
        this.ordinante_key = (ordinante != null) ? ordinante.getKey() : 0;
        this.modified = true;
    }
}
