/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package it.univaq.webmarket.data.DAO;

import it.univaq.webmarket.data.model.Ordinante;
import it.univaq.webmarket.data.model.PropostaAcquisto;
import it.univaq.webmarket.data.model.RichiestaPresaInCarico;
import it.univaq.webmarket.data.model.Tecnico;
import it.univaq.webmarket.framework.data.DataException;

import java.util.List;

/**
 *
 * @author cdidi
 */

public interface PropostaAcquistoDAO {
    
    // Crea un oggetto di tipo PropostaAcquisto.
    PropostaAcquisto createPropostaAcquisto();

    // Restituisce l'oggetto PropostaAcquisto con l'id passato come parametro
    PropostaAcquisto getPropostaAcquisto(Integer key) throws DataException;

    // Ritorna una lista di Proposte paginate data una RichiestaPresaInCarico
    List<PropostaAcquisto> getAllProposteByRichiestaPresaInCarico(RichiestaPresaInCarico richiestaPresaInCarico) throws DataException;

    // Ritorna una lista di Proposte paginate suggerite ad un Ordinante e ancora da decidere,
    // cioè l'Ordinante deve ancora decidere se accettare o meno la proposta. Infatti, lo stato
    // di queste proposte è "In attesa"
    List<PropostaAcquisto> getAllProposteDaDecidereByOrdinante(Ordinante ordinante, Integer page) throws DataException;

    // Ritorna una lista di Proposte paginate create da uno specifico Tecnico
    List<PropostaAcquisto> getAllProposteByTecnico(Tecnico tecnico, Integer page) throws DataException;

    // Ritorna una lista di Proposte paginate accettate da un Ordinante
    List<PropostaAcquisto> getAllProposteAccettate(Integer page) throws DataException;

    // Cancella dal database un oggetto di tipo Proposta
    void deletePropostaAcquisto(PropostaAcquisto propostaAcquisto) throws DataException;

    // Salva nel database una nuova Proposta o aggiorna quella esistente
    void storePropostaAcquisto(PropostaAcquisto propostaAcquisto) throws DataException;
}
