/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package it.univaq.webmarket.data.DAO;

import it.univaq.webmarket.data.model.Ordinante;
import it.univaq.webmarket.framework.data.DataException;

import java.util.List;

/**
 *
 * @author cdidi
 */

public interface OrdinanteDAO {
    
    // Crea un oggetto di tipo Ordinante.
    Ordinante createOrdinante();

    // Restituisce l'oggetto Ordinante con l'id passato come parametro
    Ordinante getOrdinante(int ordinante_key) throws DataException;

    // Restituisce tutte le Ordinanti presenti nel database paginate con un certo offset
    //(l'offset è dichiarato nell implementazione di questa interfaccia)
    List<Ordinante> getAllOrdinanti(Integer page) throws DataException;

    // Restituisce l'oggetto Ordinante con l'email passata come parametro
    Ordinante getOrdinanteByEmail(String email) throws DataException;

    // Salva nel database un nuovo Ordinante o aggiorna quello esistente
    void storeOrdinante(Ordinante ordinante) throws DataException;
    
    // Cancella dal database un Ordinante
    void deleteOrdinante(Ordinante ordinante) throws DataException;
}
