/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package it.univaq.webmarket.data.DAO;

import it.univaq.webmarket.data.model.Ordinante;
import it.univaq.webmarket.data.model.RichiestaOrdine;
import it.univaq.webmarket.framework.data.DataException;

import java.util.List;

/**
 *
 * @author cdidi
 */

public interface RichiestaOrdineDAO {
    
    // Crea un oggetto di tipo RichiestaOrdine.
    RichiestaOrdine createRichiestaOrdine();

    // Restituisce l'oggetto RichiestaOrdine con l'id passato come parametro
    RichiestaOrdine getRichiestaOrdine(int richiestaOrdine_key) throws DataException;

    // Restituisce la lista delle Richieste paginate create da un Ordinante
    List<RichiestaOrdine> getRichiesteByOrdinante(Ordinante ordinante, Integer page) throws DataException;

    // Restituisce la lista delle Richieste paginate non gestite da nessun Tecnico, cioè
    // quelle Richieste per la quale non esiste nessuna RichiestaPresaInCarico associata
    List<RichiestaOrdine> getRichiesteNonGestite(Integer page) throws DataException;

    // Salva nel database una nuovo RichiestaOrdine o aggiorna quella esistente
    void storeRichiestaOrdine(RichiestaOrdine richiestaOrdine) throws DataException;

    // Cancella dal database una RichiestaOrdine
    void deleteRichiestaOrdine(RichiestaOrdine richiestaOrdine) throws DataException;
}
