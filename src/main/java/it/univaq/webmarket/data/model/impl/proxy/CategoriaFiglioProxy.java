/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.model.impl.proxy;

import it.univaq.webmarket.data.DAO.CategoriaDAO;
import it.univaq.webmarket.data.model.Categoria;
import it.univaq.webmarket.data.model.CategoriaPadre;
import it.univaq.webmarket.data.model.impl.CategoriaFiglioImpl;
import it.univaq.webmarket.framework.data.DataException;
import it.univaq.webmarket.framework.data.DataItemProxy;
import it.univaq.webmarket.framework.data.DataLayer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cdidi
 */

public class CategoriaFiglioProxy extends CategoriaFiglioImpl implements DataItemProxy {
    
    protected boolean modified;
    protected DataLayer dataLayer;
    protected Integer categoriaPadre_key;

    public CategoriaFiglioProxy(DataLayer dataLayer) {
        super();
        this.dataLayer = dataLayer;
        this.modified = false;
        this.categoriaPadre_key = 0;
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
    public void setCategoriaGenitore(CategoriaPadre categoriaPadre) {
        super.setCategoriaGenitore(categoriaPadre);
        this.categoriaPadre_key = categoriaPadre.getKey();
        this.modified = true;
    }

    @Override
    public CategoriaPadre getCategoriaGenitore() {
        if (super.getCategoriaGenitore() == null && categoriaPadre_key > 0) {
            try {
                super.setCategoriaGenitore(((CategoriaDAO) dataLayer.getDAO(Categoria.class)).getCategoriaPadre(categoriaPadre_key));
            } catch (DataException ex) {
                Logger.getLogger(CategoriaFiglioProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return super.getCategoriaGenitore();
    }

    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }

    public void setCategoriaPadre_key(Integer categoriaPadre_key) {
        this.categoriaPadre_key = categoriaPadre_key;
        super.setCategoriaGenitore(null);
    }
}
