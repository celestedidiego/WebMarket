/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.model.impl.proxy;

import it.univaq.webmarket.data.DAO.CategoriaDAO;
import it.univaq.webmarket.data.model.Categoria;
import it.univaq.webmarket.data.model.CategoriaNipote;
import it.univaq.webmarket.data.model.impl.CaratteristicaImpl;
import it.univaq.webmarket.framework.data.DataException;
import it.univaq.webmarket.framework.data.DataItemProxy;
import it.univaq.webmarket.framework.data.DataLayer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cdidi
 */

public class CaratteristicaProxy extends CaratteristicaImpl implements DataItemProxy {
    
    protected boolean modified;
    protected DataLayer dataLayer;
    protected Integer categoriaNipote_key;

    public CaratteristicaProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.modified = false;
        this.categoriaNipote_key = 0;
    }

    @Override
    public void setNome(String nome) {
        super.setNome(nome);
        this.modified = true;
    }

    @Override
    public void setCategoriaNipote(CategoriaNipote categoriaNipote) {
        super.setCategoriaNipote(categoriaNipote);
        this.categoriaNipote_key = categoriaNipote.getKey();
        this.modified = true;
    }

    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
    }

    @Override
    public CategoriaNipote getCategoriaNipote() {
        if (super.getCategoriaNipote() == null && categoriaNipote_key > 0) {
            try {
                super.setCategoriaNipote((((CategoriaDAO) dataLayer.getDAO(Categoria.class)).getCategoriaNipote(categoriaNipote_key)));
            } catch (DataException ex) {
                Logger.getLogger(CaratteristicaProxy.class.getName()).log(Level.SEVERE, "Exception in CaratteristicaProxy", ex);
            }
        }

        return super.getCategoriaNipote();
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }

    public void setCategoriaNipote_key(Integer categoriaNipote_key) {
        this.categoriaNipote_key = categoriaNipote_key;
        super.setCategoriaNipote(null);
    }
}
