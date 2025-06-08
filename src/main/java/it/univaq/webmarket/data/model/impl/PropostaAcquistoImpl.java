/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.model.impl;

import it.univaq.webmarket.data.model.PropostaAcquisto;
import it.univaq.webmarket.data.model.RichiestaPresaInCarico;
import it.univaq.webmarket.framework.data.DataItemImpl;

/**
 *
 * @author cdidi
 */

public class PropostaAcquistoImpl extends DataItemImpl<Integer> implements PropostaAcquisto {
    
    private String nomeProdotto, codiceProdotto, produttore, note, motivazione, URL, statoProposta;
    private Float prezzo;
    private RichiestaPresaInCarico richiestaPresaInCarico;

    public PropostaAcquistoImpl() {
        this.codiceProdotto = "";
        this.produttore = "";
        this.nomeProdotto = "";
        this.prezzo = 0F;
        this.note = "";
        this.URL = "";
        this.motivazione = "";
        this.statoProposta = "";
        this.richiestaPresaInCarico = null;
    }

    @Override
    public String getCodiceProdotto() {
        return codiceProdotto;
    }

    @Override
    public void setCodiceProdotto(String codiceProdotto) {
        this.codiceProdotto = codiceProdotto;
    }

    @Override
    public String getProduttore() {
        return produttore;
    }

    @Override
    public void setProduttore(String produttore) {
        this.produttore = produttore;
    }
    
    @Override
    public String getNomeProdotto() {
        return nomeProdotto;
    }

    @Override
    public void setNomeProdotto(String nomeProdotto) {
        this.nomeProdotto = nomeProdotto;
    }

    @Override
    public Float getPrezzo() {
        return prezzo;
    }

    @Override
    public void setPrezzo(Float prezzo) {
        this.prezzo = prezzo;
    }
    
    @Override
    public String getNote() {
        return note;
    }

    @Override
    public void setNote(String note) {
        this.note = note;
    }
    
    @Override
    public String getURL() {
        return this.URL;
    }

    @Override
    public void setURL(String URL) {
        this.URL = URL;
    }
    
    @Override
    public String getMotivazione() {
        return motivazione;
    }

    @Override
    public void setMotivazione(String motivazione) {
        this.motivazione = motivazione;
    }

    @Override
    public String getStatoProposta() { return statoProposta;}

    @Override
    public void setStatoProposta(String statoProposta) {
        this.statoProposta = statoProposta;
    }
    
    @Override
    public RichiestaPresaInCarico getRichiestaPresaInCarico() {
        return this.richiestaPresaInCarico;
    }

    @Override
    public void setRichiestaPresaInCarico(RichiestaPresaInCarico richiestaPresaInCarico) {
        this.richiestaPresaInCarico = richiestaPresaInCarico;
    }

    @Override
    public String toString() {
        return "PropostaAcquistoImpl{" +
                "nomeProdotto='" + nomeProdotto + '\'' +
                ", codiceProdotto='" + codiceProdotto + '\'' +
                ", produttore='" + produttore + '\'' +
                ", note='" + note + '\'' +
                ", motivazione='" + motivazione + '\'' +
                ", URL='" + URL + '\'' +
                ", prezzo=" + prezzo +
                ", statoProposta=" + statoProposta +
                ", richiestaPresaInCarico=" + richiestaPresaInCarico +
                '}';
    }
}
