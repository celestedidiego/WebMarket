/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package it.univaq.webmarket.data.model;

/**
 *
 * @author cdidi
 */

public interface CategoriaNipote extends Categoria {
    
    CategoriaFiglio getCategoriaGenitore();

    void setCategoriaGenitore(CategoriaFiglio categoriaFiglio) ;
}
