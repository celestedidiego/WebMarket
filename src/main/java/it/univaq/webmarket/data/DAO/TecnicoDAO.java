/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package it.univaq.webmarket.data.DAO;

import it.univaq.webmarket.data.model.Tecnico;
import it.univaq.webmarket.framework.data.DataException;

import java.util.List;

/**
 *
 * @author cdidi
 */

public interface TecnicoDAO {
    
    // Crea un oggetto di tipo Tecnico.
    Tecnico createTecnico();

    // Restituisce l'oggetto Tecnico con l'id passato come parametro
    Tecnico getTecnico(int tecnico_key) throws DataException;

    // Restituisce la lista dei Tecnico paginati presenti nel database
    List<Tecnico> getAllTecnico(Integer page) throws DataException;

    // Restituisce l'oggetto Tecnico con l'email passata come parametro
    Tecnico getTecnicoByEmail(String email) throws DataException;

    // Salva nel database un nuovo Tecnico o aggiorna quello esistente
    void storeTecnico(Tecnico tecnico) throws DataException;

    // Cancella dal database un Tecnico
    void deleteTecnico(Tecnico tecnico) throws DataException;
}
