/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package it.univaq.webmarket.data.DAO;

import it.univaq.webmarket.data.model.CategoriaFiglio;
import it.univaq.webmarket.data.model.CategoriaNipote;
import it.univaq.webmarket.data.model.CategoriaPadre;
import it.univaq.webmarket.framework.data.DataException;

import java.util.List;

/**
 *
 * @author cdidi
 */

public interface CategoriaDAO {
    
    // Crea un oggetto di tipo CategoriaPadre
    CategoriaPadre createCategoriaPadre();

    // Crea un oggetto di tipo CategoriaFiglio
    CategoriaFiglio createCategoriaFiglio();

    // Crea un oggetto di tipo CategoriaNipote
    CategoriaNipote createCategoriaNipote();

    // Restituisce l'oggetto CategoriaPadre con l'id passato come parametro
    CategoriaPadre getCategoriaPadre(int categoriaPadre_key) throws DataException;

    // Restituisce l'oggetto CategoriaFiglio con l'id passato come parametro
    CategoriaFiglio getCategoriaFiglio(int categoriaFiglio_key) throws DataException;

    // Restituisce l'oggetto CategoriaNipote con l'id passato come parametro
    CategoriaNipote getCategoriaNipote(int categoriaNipote_key) throws DataException;

    // Restituisce tutte le categorie padre presenti nel database
    List<CategoriaPadre> getAllCategoriePadre() throws DataException;

    // Restituisce tutte le CategoriePadre presenti nel database paginate con un certo offset
    List<CategoriaPadre> getAllCategoriePadre(Integer page) throws DataException;

    // Restituisce tutte le CategorieFiglio presenti nel database
    List<CategoriaFiglio> getAllCategorieFiglio() throws DataException;

    // Restituisce tutte le CategorieNipote presenti nel database
    List<CategoriaNipote> getAllCategorieNipote() throws DataException;

    // Restituisce tutte le CategorieFiglio presenti nel database data una certa CategoriaPadre
    List<CategoriaFiglio> getCategorieFiglioByPadre(CategoriaPadre categoriaPadre) throws DataException;

    // Restituisce tutte le CategorieNipote presenti nel database data una certa CategoriaFiglio
    List<CategoriaNipote> getCategorieNipoteByFiglio(CategoriaFiglio categoriaFiglio) throws DataException;

    // Salva nel database una nuova CategoriaPadre o aggiorna quella esistente
    void storeCategoriaPadre(CategoriaPadre categoriaPadre) throws DataException;

    // Salva nel database una nuova CategoriaFiglio o aggiorna quella esistente
    void storeCategoriaFiglio(CategoriaFiglio categoriaFiglio) throws DataException;

    // Salva nel database una nuova CategoriaNipote o aggiorna quella esistente
    void storeCategoriaNipote(CategoriaNipote categoriaNipote) throws DataException;

    // Cancella dal database una CategoriaPadre
    void deleteCategoriaPadre(CategoriaPadre categoriaPadre) throws DataException;

    // Cancella dal database una CategoriaFiglio
    void deleteCategoriaFiglio(CategoriaFiglio categoriaFiglio) throws DataException;

    // Cancella dal database una CategoriaNipote
    void deleteCategoriaNipote(CategoriaNipote categoriaNipote) throws DataException;
}
