/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.model.impl;

import it.univaq.webmarket.data.model.CategoriaFiglio;
import it.univaq.webmarket.data.model.CategoriaNipote;
import it.univaq.webmarket.framework.data.DataItemImpl;

/**
 *
 * @author cdidi
 */
public class CategoriaNipoteImpl extends DataItemImpl<Integer> implements CategoriaNipote {
    
    private String nome;
    private CategoriaFiglio categoriaGenitore;
    
    public CategoriaNipoteImpl() {
        super();
        this.categoriaGenitore = null;
        this.nome = "";
    }

    @Override
    public String getNome() { return this.nome; }

    @Override
    public void setNome(String nome) { this.nome = nome; }

    @Override
    public CategoriaFiglio getCategoriaGenitore() { return this.categoriaGenitore; }

    @Override
    public void setCategoriaGenitore(CategoriaFiglio categoriaFiglio) {
        this.categoriaGenitore = categoriaFiglio;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {" +
                "key = " + getKey() +
                ", nome = " + getNome() +
                ", categoriaGenitore = " + getCategoriaGenitore() +
                "}";
    }
}
