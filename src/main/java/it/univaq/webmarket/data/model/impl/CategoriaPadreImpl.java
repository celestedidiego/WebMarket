/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.model.impl;

import it.univaq.webmarket.data.model.CategoriaPadre;
import it.univaq.webmarket.framework.data.DataItemImpl;

/**
 *
 * @author cdidi
 */

public class CategoriaPadreImpl extends DataItemImpl<Integer> implements CategoriaPadre {
    
    private String nome;

    public CategoriaPadreImpl() {
        super();
        this.nome = "";
    }

    @Override
    public String getNome() { return this.nome;}

    @Override
    public void setNome(String nome) { this.nome = nome; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " {" +
                "key = " + getKey() +
                ", nome = " + getNome() +
                "}";
    }
}
