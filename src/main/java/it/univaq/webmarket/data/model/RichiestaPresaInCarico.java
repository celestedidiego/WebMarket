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

public interface RichiestaPresaInCarico extends DataItem<Integer> {
    
    RichiestaOrdine getRichiestaOrdine();

    void setRichiestaOrdine(RichiestaOrdine richiestaOrdine);

    Tecnico getTecnico();

    void setTecnico(Tecnico tecnico);
}
