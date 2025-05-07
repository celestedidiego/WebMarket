/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package it.univaq.webmarket.data.DAO;


import it.univaq.webmarket.data.model.Amministratore;
import it.univaq.webmarket.framework.data.DataException;

/**
 *
 * @author cdidi
 */

public interface AmministratoreDAO {
    
    // Crea un oggetto di tipo Amministratore.
    Amministratore createAmministratore();

    // Restituisce l'oggetto Amministratore con l'email passata come parametro
    Amministratore getAmministratoreByEmail(String email) throws DataException;
    
    // Restituisce l'oggetto Amministratore con l'id passato come parametro
    Amministratore getAmministratoreByID(Integer id) throws DataException;
}
