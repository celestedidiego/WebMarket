/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.model.impl.proxy;

import it.univaq.webmarket.data.model.impl.CategoriaPadreImpl;
import it.univaq.webmarket.framework.data.DataItemProxy;
import it.univaq.webmarket.framework.data.DataLayer;

/**
 *
 * @author cdidi
 */

public class CategoriaPadreProxy extends CategoriaPadreImpl implements DataItemProxy {
    
    protected boolean modified;
    protected DataLayer dataLayer;

    public CategoriaPadreProxy(DataLayer dataLayer) {
        super();
        this.dataLayer = dataLayer;
        this.modified = false;
    }

    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
    }

    @Override
    public void setNome(String nome) {
        super.setNome(nome);
        this.modified = true;
    }

    @Override
    public boolean isModified() { return modified; }

    @Override
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }
}
