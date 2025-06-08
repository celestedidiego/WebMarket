/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package it.univaq.webmarket.data.DAO;


import it.univaq.webmarket.data.model.Ordinante;
import it.univaq.webmarket.data.model.Ordine;
import it.univaq.webmarket.data.model.Tecnico;
import it.univaq.webmarket.framework.data.DataException;

import java.util.List;

/**
 *
 * @author cdidi
 */

public interface OrdineDAO {
    
    // Crea un oggetto di tipo Ordine.
    Ordine createOrdine();

    // Restituisce l'oggetto Ordine con l'id passato come parametro
    Ordine getOrdine(int key) throws DataException;

    // Restituisce l'oggetto Ordine con l'id passato come parametro all'interno dello storico
    // di un Ordinante
    Ordine getOrdineInStorico(int key) throws DataException;

    // Restituisce la lista degli Ordini paginati  presenti all'interno dello storico di un Ordinante
    List<Ordine> getStorico(Ordinante ordinante, Integer page) throws DataException;

    // Restituisce una lista di Ordini paginati effettuati da un Ordinante
    List<Ordine> getAllOrdiniByOrdinante(Ordinante ordinante, Integer page) throws DataException;

    // Ritorna una lista di Ordini paginati gestiti da uno specifico tecnico degli ordini
    List<Ordine> getAllOrdiniByTecnico(Tecnico tecnico, Integer page) throws DataException;

    // Salva nel database una nuovo Ordine o aggiorna quella esistente
    void storeOrdine(Ordine ordine) throws DataException;
}
