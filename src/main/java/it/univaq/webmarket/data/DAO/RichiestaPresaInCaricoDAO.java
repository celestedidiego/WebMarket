/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package it.univaq.webmarket.data.DAO;

import it.univaq.webmarket.data.model.RichiestaPresaInCarico;
import it.univaq.webmarket.data.model.Tecnico;
import it.univaq.webmarket.framework.data.DataException;

import java.util.List;

/**
 *
 * @author cdidi
 */

public interface RichiestaPresaInCaricoDAO {
    
    // Crea un oggetto di tipo RichiestaPresaInCarico.
    RichiestaPresaInCarico createRichiestaPresaInCarico();

    // Restituisce l'oggetto RichiestaPresaInCarico con l'id passato come parametro
    RichiestaPresaInCarico getRichiestaPresaInCarico(Integer key) throws DataException;

    // Restituisce la lista dei RichiestaPresaInCarico paginate gestite da un Tecnico
    List<RichiestaPresaInCarico> getAllRichiestePresaInCaricoByTecnico(Tecnico tecnico, Integer page) throws DataException;

    // Salva nel database una nuova RichiestaPresaInCarico o aggiorna quella esistente
    void storeRichiestaPresaInCarico(RichiestaPresaInCarico richiestaPresaInCarico) throws DataException;

    // Cancella dal database una RichiestaPresaInCarico
    void deleteRichiestaPresaInCarico(RichiestaPresaInCarico richiestaPresaInCarico) throws DataException;
}
