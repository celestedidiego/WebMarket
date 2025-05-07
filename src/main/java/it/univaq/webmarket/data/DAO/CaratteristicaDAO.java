/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package it.univaq.webmarket.data.DAO;

import it.univaq.webmarket.data.model.Caratteristica;
import it.univaq.webmarket.data.model.CaratteristicaRichiesta;
import it.univaq.webmarket.data.model.CategoriaNipote;
import it.univaq.webmarket.data.model.RichiestaOrdine;
import it.univaq.webmarket.framework.data.DataException;

import java.util.List;

/**
 *
 * @author cdidi
 */

public interface CaratteristicaDAO {
    
    // Crea un oggetto di tipo Caratteristica
    Caratteristica createCaratteristica();

    // Restituisce l'oggetto Caratteristica con l'id passato come parametro
    Caratteristica getCaratteristica(int key) throws DataException;

    // Restituisce una lista di oggetti Caratteristica da una fonte di dati, suddividendo i risultati in pagine
    List<Caratteristica> getAllCaratteristiche(Integer page) throws DataException;

    // Restituisce tutte le caratteristiche presenti nel database data una certa Categoria Nipote
    List<Caratteristica> getAllCaratteristiche(CategoriaNipote categoriaNipote) throws DataException;

    // Salva nel database una nuova caratteristica o aggiorna quella esistente
    void storeCaratteristica(Caratteristica caratteristica) throws DataException;

    // Cancella dal database una caratteristica
    void deleteCaratteristica(Caratteristica caratteristica) throws DataException;

    // Crea un oggetto di tipo CaratteristicaRichiesta.
    CaratteristicaRichiesta createCaratteristicaRichiesta();

    // Restituisce l'oggetto CaratteristicaRichiesta con l'id passato come parametro
    CaratteristicaRichiesta getCaratteristicaRichiesta(int key) throws DataException;

    // Salva nel database un oggetto di tipo CaratteristicaRichiesta o aggiorna quella esistente
    void storeCaratteristicaRichiesta(CaratteristicaRichiesta caratteristicaRichiesta, Integer richiestaOrdine_key) throws DataException;

    // Cancella dal database un oggetto di tipo CaratteristicaRichiesta
    void deleteCaratteristicaRichiesta(CaratteristicaRichiesta caratteristicaRchiesta) throws DataException;

    // Restituisce tutte le CaratteristicaRichiesta presenti nel database data una certa RichiestaOrdine
    List<CaratteristicaRichiesta> getCaratteristicheRichiesta(RichiestaOrdine richiestaOrdine) throws DataException;
}
