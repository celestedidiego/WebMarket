/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.model.impl;

import it.univaq.webmarket.data.model.Caratteristica;
import it.univaq.webmarket.data.model.CategoriaNipote;
import it.univaq.webmarket.framework.data.DataItemImpl;

/**
 *
 * @author cdidi
 */

public class CaratteristicaImpl extends DataItemImpl<Integer> implements Caratteristica {
    
    private String nome;
    private CategoriaNipote categoriaNipote;

    public CaratteristicaImpl() {
        super();
        this.nome = "";
        this.categoriaNipote = null;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public CategoriaNipote getCategoriaNipote() { return categoriaNipote; }

    @Override
    public void setNome(String nome) { this.nome = nome;}

    @Override
    public void setCategoriaNipote(CategoriaNipote categoriaNipote) {
        this.categoriaNipote = categoriaNipote;
    }

    @Override
    public String toString() {
        return "CaratteristicaImpl{" +
                "nome='" + nome + '\'' +
                ", categoriaNipote=" + categoriaNipote +
                '}';
    }
}
