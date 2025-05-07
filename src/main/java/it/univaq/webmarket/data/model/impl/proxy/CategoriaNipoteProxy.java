/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.model.impl.proxy;

import it.univaq.webmarket.data.DAO.CategoriaDAO;
import it.univaq.webmarket.data.model.Categoria;
import it.univaq.webmarket.data.model.CategoriaFiglio;
import it.univaq.webmarket.data.model.impl.CategoriaNipoteImpl;
import it.univaq.webmarket.framework.data.DataException;
import it.univaq.webmarket.framework.data.DataItemProxy;
import it.univaq.webmarket.framework.data.DataLayer;

import java.util.logging.Logger;

/**
 *
 * @author cdidi
 */

public class CategoriaNipoteProxy extends CategoriaNipoteImpl implements DataItemProxy {
    
    protected boolean modified;
    protected DataLayer dataLayer;
    protected Integer categoriaFiglio_key;

    public CategoriaNipoteProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.modified = false;
        this.categoriaFiglio_key = 0;
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
    public void setCategoriaGenitore(CategoriaFiglio categoriaFiglio) {
        super.setCategoriaGenitore(categoriaFiglio);
        this.categoriaFiglio_key = categoriaFiglio.getKey();
        this.modified = true;
    }

    @Override
    public CategoriaFiglio getCategoriaGenitore() {
        if (super.getCategoriaGenitore() == null && categoriaFiglio_key > 0) {
            try {
                super.setCategoriaGenitore(((CategoriaDAO) dataLayer.getDAO(Categoria.class)).getCategoriaFiglio(categoriaFiglio_key));
            } catch (DataException ex) {
                Logger.getLogger(CategoriaNipoteProxy.class.getName()).severe(ex.getMessage());
            }
        }

        return super.getCategoriaGenitore();
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }

    public void setCategoriaFiglio_key(Integer idCategoriaFiglio) {
        this.categoriaFiglio_key = idCategoriaFiglio;
        super.setCategoriaGenitore(null);
    }
}
