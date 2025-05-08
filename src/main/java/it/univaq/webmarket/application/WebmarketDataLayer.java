/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.application;

import it.univaq.webmarket.data.DAO.*;
import it.univaq.webmarket.data.DAO.impl.*;
import it.univaq.webmarket.data.model.*;
import it.univaq.webmarket.framework.data.DataException;
import it.univaq.webmarket.framework.data.DataLayer;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 *
 * @author cdidi
 */

public class WebmarketDataLayer extends DataLayer {
    
    public WebmarketDataLayer(DataSource datasource) throws SQLException {
        super(datasource);
    }
    
    @Override
    public void init() throws DataException {
        registerDAO(RichiestaOrdine.class, new RichiestaOrdineDAO_MySQL(this));
        registerDAO(Ordinante.class, new OrdinanteDAO_MySQL(this));
        registerDAO(Amministratore.class, new AmministratoreDAO_MySQL(this));
        registerDAO(Tecnico.class, new TecnicoDAO_MySQL(this));
        registerDAO(Categoria.class, new CategoriaDAO_MySQL(this));
        registerDAO(Caratteristica.class, new CaratteristicaDAO_MySQL(this));
        registerDAO(RichiestaPresaInCarico.class, new RichiestaPresaInCaricoDAO_MySQL(this));
        registerDAO(PropostaAcquisto.class, new PropostaAcquistoDAO_MySQL(this));
        registerDAO(Ordine.class, new OrdineDAO_MySQL(this));
    }
    
    public RichiestaOrdineDAO getRichiestaOrdineDAO() {
        return (RichiestaOrdineDAO) getDAO(RichiestaOrdine.class);
    }

    public OrdinanteDAO getOrdinanteDAO() {
        return (OrdinanteDAO) getDAO(Ordinante.class);
    }

    public AmministratoreDAO getAmministratoreDAO() {
        return (AmministratoreDAO) getDAO(Amministratore.class);
    }

    public TecnicoDAO getTecnicoDAO() {
        return (TecnicoDAO) getDAO(Tecnico.class);
    }

    public CategoriaDAO getCategoriaDAO() { 
        return (CategoriaDAO) getDAO(Categoria.class); 
    }

    public CaratteristicaDAO getCaratteristicaDAO() {
        return (CaratteristicaDAO) getDAO(Caratteristica.class);
    }

    public RichiestaPresaInCaricoDAO getRichiestaPresaInCaricoDAO() {
        return (RichiestaPresaInCaricoDAO) getDAO(RichiestaPresaInCarico.class);
    }

    public PropostaAcquistoDAO getPropostaAcquistoDAO() {
        return (PropostaAcquistoDAO) getDAO(PropostaAcquisto.class);
    }

    public OrdineDAO getOrdineDAO() {
        return (OrdineDAO) getDAO(Ordine.class);
    }
}
