/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package it.univaq.webmarket.data.model;

import it.univaq.webmarket.framework.data.DataItem;

import java.time.LocalDate;
import java.util.List;
/**
 *
 * @author cdidi
 */
public interface RichiestaOrdine extends DataItem<Integer>{
    
    Ordinante getOrdinante();

    void setOrdinante(Ordinante ordinante);
    
    String getCodiceRichiesta();

    void setCodiceRichiesta(String codiceRichiesta);

    String getNote();

    void setNote(String note);

    LocalDate getData();

    void setData(LocalDate dataEOra);

    List<CaratteristicaRichiesta> getCaratteristicheRichiesta();

    void setCaratteristicheRichiesta(List<CaratteristicaRichiesta> caratteristicheRichiesta);

    boolean isPresaInCarico();
    
    void setPresaInCarico(boolean presaInCarico);
}
