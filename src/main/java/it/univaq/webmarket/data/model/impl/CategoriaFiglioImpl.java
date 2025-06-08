/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.model.impl;

import it.univaq.webmarket.data.model.CategoriaFiglio;
import it.univaq.webmarket.data.model.CategoriaPadre;
import it.univaq.webmarket.framework.data.DataItemImpl;

/**
 *
 * @author cdidi
 */
public class CategoriaFiglioImpl extends DataItemImpl<Integer> implements CategoriaFiglio {
    
    private String nome;
    private CategoriaPadre categoriaGenitore;

    public CategoriaFiglioImpl() {
        super();
        this.categoriaGenitore = null;
        this.nome = "";
    }

    @Override
    public String getNome() { return this.nome; }

    @Override
    public void setNome(String nome) { this.nome = nome; }

    @Override
    public CategoriaPadre getCategoriaGenitore() { return this.categoriaGenitore; }

    @Override
    public void setCategoriaGenitore(CategoriaPadre categoriaPadre) {
        this.categoriaGenitore = categoriaPadre;
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
