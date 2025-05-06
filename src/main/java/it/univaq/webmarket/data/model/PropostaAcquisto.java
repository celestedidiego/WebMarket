/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package it.univaq.webmarket.data.model;

import it.univaq.webmarket.framework.data.DataItem;

/**
 *
 * @author cdidi
 */

public interface PropostaAcquisto extends DataItem<Integer> {
    
    class StatoProposta {

        public static final String IN_ATTESA = "In attesa";
        public static final String ACCETTATO = "Accettato";
        public static final String RIFIUTATO = "Rifiutato";

    }

    String getCodiceProdotto();

    void setCodiceProdotto(String codiceProdotto);

    String getProduttore();

    void setProduttore(String produttore);
    
    String getNomeProdotto();

    void setNomeProdotto(String nomeProdotto);
    
    Float getPrezzo();

    void setPrezzo(Float prezzo);
    
    String getNote();

    void setNote(String note);

    String getURL();

    void setURL(String URL);
    
    String getMotivazione();

    void setMotivazione(String motivazione);

    String getStatoProposta();

    void setStatoProposta(String statoProposta);

    RichiestaPresaInCarico getRichiestaPresaInCarico();

    void setRichiestaPresaInCarico(RichiestaPresaInCarico richiestaPresaInCarico);
}
