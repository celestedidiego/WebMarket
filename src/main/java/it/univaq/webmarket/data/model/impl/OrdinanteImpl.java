/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.model.impl;

import it.univaq.webmarket.data.model.Ordinante;
import it.univaq.webmarket.framework.data.DataItemImpl;

/**
 *
 * @author cdidi
 */

public class OrdinanteImpl extends DataItemImpl<Integer> implements Ordinante {
    
    private String email;
    private String password;

    public OrdinanteImpl() {
        super();
        this.email = "";
        this.password = "";
    }

    @Override
    public String getEmail() { return this.email; }

    @Override
    public String getPassword() { return this.password; }

    @Override
    public void setEmail(String email) { this.email = email;}

    @Override
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "OrdinanteImpl{" +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
