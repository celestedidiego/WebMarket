/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.model.impl;

import it.univaq.webmarket.data.model.Tecnico;
import it.univaq.webmarket.framework.data.DataItemImpl;

/**
 *
 * @author cdidi
 */

public class TecnicoImpl extends DataItemImpl<Integer> implements Tecnico {
    
    private String email;
    private String password;

    public TecnicoImpl() {
        super();
        this.email = "";
        this.password = "";
    }

    @Override
    public String getEmail() { return this.email; }

    @Override
    public String getPassword() { return this.password; }

    @Override
    public void setEmail(String email) { this.email = email; }

    @Override
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "TecnicoImpl{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
