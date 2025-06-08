/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package it.univaq.webmarket.data.model;

import it.univaq.webmarket.framework.data.DataItem;

/**
 *
 * @author cdidi
 */

public interface Utente extends DataItem<Integer> {

    String getEmail();

    String getPassword();

    void setEmail(String email);

    void setPassword(String password);
}
