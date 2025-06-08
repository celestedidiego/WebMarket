/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package it.univaq.webmarket.data.model;

/* Vedere se effettivamente utilizzo questi freemarker */
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import it.univaq.webmarket.framework.data.DataItem;
import it.univaq.webmarket.data.model.Ordinante;

import java.time.LocalDate;

/**
 *
 * @author cdidi
 */
public interface Ordine extends DataItem<Integer> {
    
    class StatoConsegna {
        
        public static final String PRESA_IN_CARICO = "Presa in carico";
        public static final String IN_CONSEGNA = "In consegna";
        public static final String CONSEGNATO = "Consegnato";
    }
    
    class Risposta {
        
        public static final String ACCETTATO = "Accettato";
        public static final String RESPINTO = "Respinto";
        public static final String RIFIUTATO = "Rifiutato";
    }
    
    LocalDate getDataConsegna();

    void setDataConsegna(LocalDate dataConsegna);
    
    String getStatoConsegna();

    void setStatoConsegna(String statoConsegna);

    String getRisposta();

    void setRisposta(String risposta);
    
    Tecnico getTecnico();

    void setTecnico(Tecnico tecnico);
    
    PropostaAcquisto getPropostaAcquisto();

    void setPropostaAcquisto(PropostaAcquisto propostaAcquisto);

    Ordinante getOrdinante();

    void setOrdinante(Ordinante ordinante);

    int getOrdinanteKey();
    
    void setOrdinanteKey(int key);
}
