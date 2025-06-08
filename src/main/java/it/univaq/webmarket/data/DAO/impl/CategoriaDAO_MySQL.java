/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.DAO.impl;

import it.univaq.webmarket.data.DAO.CategoriaDAO;
import it.univaq.webmarket.data.model.*;
import it.univaq.webmarket.data.model.impl.proxy.CategoriaFiglioProxy;
import it.univaq.webmarket.data.model.impl.proxy.CategoriaPadreProxy;
import it.univaq.webmarket.data.model.impl.proxy.CategoriaNipoteProxy;
import it.univaq.webmarket.framework.data.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cdidi
 */

public class CategoriaDAO_MySQL extends DAO implements CategoriaDAO {
    
    private Integer offset = 5;

    private PreparedStatement sCategoriaPadreByID;
    private PreparedStatement sCategoriaFiglioByID;
    private PreparedStatement sCategoriaNipoteByID;

    private PreparedStatement sCategoriePadre;
    private PreparedStatement sCategoriePadrePaginata;
    private PreparedStatement sCategorieFiglio;
    private PreparedStatement sCategorieNipote;

    private PreparedStatement iCategoriaPadre;
    private PreparedStatement iCategoriaFiglio;
    private PreparedStatement iCategoriaNipote;

    private PreparedStatement uCategoriaPadre;
    private PreparedStatement uCategoriaFiglio;
    private PreparedStatement uCategoriaNipote;

    private PreparedStatement sCategorieFiglioFromPadre;
    private PreparedStatement sCategorieNipoteFromFiglio;

    private PreparedStatement dCategoriaPadre;
    private PreparedStatement dCategoriaFiglio;
    private PreparedStatement dCategoriaNipote;

    public CategoriaDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();
            sCategoriaPadreByID = connection.prepareStatement("SELECT * FROM categoriaPadre WHERE ID=?");
            sCategoriaFiglioByID = connection.prepareStatement("SELECT * FROM categoriaFiglio WHERE ID=?");
            sCategoriaNipoteByID = connection.prepareStatement("SELECT * FROM categoriaNipote WHERE ID=?");

            sCategoriePadre = connection.prepareStatement("SELECT ID FROM categoriaPadre");
            sCategoriePadrePaginata = connection.prepareStatement("SELECT ID FROM categoriaPadre LIMIT ?, ?");
            sCategorieFiglio = connection.prepareStatement("SELECT ID FROM categoriaFiglio");
            sCategorieNipote = connection.prepareStatement("SELECT ID FROM categoriaNipote");

            iCategoriaPadre = connection.prepareStatement("INSERT INTO categoriapadre(nome) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS);
            iCategoriaFiglio = connection.prepareStatement("INSERT INTO categoriafiglio(nome, categoriaPadreID) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            iCategoriaNipote = connection.prepareStatement("INSERT INTO categorianipote(nome, categoriaFiglioID) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);

            uCategoriaPadre = connection.prepareStatement("UPDATE categoriapadre SET nome=?, version=? WHERE ID=? AND version=?");
            uCategoriaFiglio = connection.prepareStatement("UPDATE categoriafiglio SET nome=?, categoriaPadreID=?, version=? WHERE ID=? AND version=?");
            uCategoriaNipote = connection.prepareStatement("UPDATE categorianipote SET nome=?, categoriaFiglioID=?, version=? WHERE ID=? AND version=?");

            sCategorieFiglioFromPadre = connection.prepareStatement("SELECT * FROM categoriafiglio WHERE categoriaPadreID = ?");
            sCategorieNipoteFromFiglio = connection.prepareStatement("SELECT * FROM categorianipote WHERE categoriaFiglioID = ?");

            dCategoriaPadre = connection.prepareStatement("DELETE FROM categoriapadre WHERE ID=?");
            dCategoriaFiglio = connection.prepareStatement("DELETE FROM categoriafiglio WHERE ID=?");
            dCategoriaNipote = connection.prepareStatement("DELETE FROM categorianipote WHERE ID=?");

        } catch (SQLException e) {
            throw new DataException("Error initializing webmarket data layer", e);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            sCategoriaPadreByID.close();
            sCategoriaFiglioByID.close();
            sCategoriaNipoteByID.close();

            sCategoriePadrePaginata.close();
            sCategorieFiglio.close();
            sCategorieNipote.close();

            iCategoriaPadre.close();
            iCategoriaFiglio.close();
            iCategoriaNipote.close();

            uCategoriaPadre.close();
            uCategoriaFiglio.close();
            uCategoriaNipote.close();

            sCategorieFiglioFromPadre.close();
            sCategorieNipoteFromFiglio.close();
        } catch (SQLException ex) {
            throw new DataException("Can't destroy prepared statements", ex);
        }
        super.destroy();
    }

    @Override
    public CategoriaPadre createCategoriaPadre() {
        return new CategoriaPadreProxy(getDataLayer());
    }

    @Override
    public CategoriaFiglio createCategoriaFiglio() {
        return new CategoriaFiglioProxy(getDataLayer());
    }

    @Override
    public CategoriaNipote createCategoriaNipote() {
        return new CategoriaNipoteProxy(getDataLayer());
    }

    private CategoriaPadre createCategoriaPadre(ResultSet rs) throws DataException {
        try {
            CategoriaPadreProxy cp = (CategoriaPadreProxy) createCategoriaPadre();
            cp.setKey(rs.getInt("ID"));
            cp.setNome(rs.getString("nome"));
            cp.setVersion(rs.getLong("version"));
            return cp;
        } catch (SQLException ex) {
            throw new DataException("Unable to create CategoriaPadre object form ResultSet", ex);
        }
    }

    private CategoriaFiglio createCategoriaFiglio(ResultSet rs) throws DataException {
        try {
            CategoriaFiglioProxy cf = (CategoriaFiglioProxy) createCategoriaFiglio();
            cf.setKey(rs.getInt("ID"));
            cf.setNome(rs.getString("nome"));
            cf.setCategoriaPadre_key(rs.getInt("categoriaPadreID"));
            cf.setVersion(rs.getLong("version"));
            return cf;
        } catch (SQLException ex) {
            throw new DataException("Unable to create CategoriaFiglio object form ResultSet", ex);
        }
    }

    private CategoriaNipote createCategoriaNipote(ResultSet rs) throws DataException {
        try {
            CategoriaNipoteProxy cn = (CategoriaNipoteProxy) createCategoriaNipote();
            cn.setKey(rs.getInt("ID"));
            cn.setNome(rs.getString("nome"));
            cn.setCategoriaFiglio_key(rs.getInt("categoriaFiglioID"));
            cn.setVersion(rs.getLong("version"));
            return cn;
        } catch (SQLException ex) {
            throw new DataException("Unable to create CategoriaNipote object form ResultSet", ex);
        }
    }

    @Override
    public CategoriaPadre getCategoriaPadre(int categoriaPadre_key) throws DataException {
        CategoriaPadre cp = null;
        if (dataLayer.getCache().has(CategoriaPadre.class, categoriaPadre_key)) {
            cp = dataLayer.getCache().get(CategoriaPadre.class, categoriaPadre_key);
        } else {
            try {
                sCategoriaPadreByID.setInt(1, categoriaPadre_key);
                try (ResultSet rs = sCategoriaPadreByID.executeQuery()) {
                    if (rs.next()) {
                        cp = createCategoriaPadre(rs);
                        dataLayer.getCache().add(CategoriaPadre.class, cp);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load CategoriaPadre by ID", ex);
            }
        }
        return cp;
    }

    @Override
    public CategoriaFiglio getCategoriaFiglio(int categoriaFiglio_key) throws DataException {
        CategoriaFiglio cf = null;
        if (dataLayer.getCache().has(CategoriaFiglio.class, categoriaFiglio_key)) {
            cf = dataLayer.getCache().get(CategoriaFiglio.class, categoriaFiglio_key);
        } else {
            try {
                sCategoriaFiglioByID.setInt(1, categoriaFiglio_key);
                try (ResultSet rs = sCategoriaFiglioByID.executeQuery()) {
                    if (rs.next()) {
                        cf = createCategoriaFiglio(rs);
                        dataLayer.getCache().add(CategoriaFiglio.class, cf);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load CategoriaFiglio by ID", ex);
            }
        }
        return cf;
    }

    @Override
    public CategoriaNipote getCategoriaNipote(int categoriaNipote_key) throws DataException {
        CategoriaNipote cn = null;
        if (dataLayer.getCache().has(CategoriaNipote.class, categoriaNipote_key)) {
            cn = dataLayer.getCache().get(CategoriaNipote.class, categoriaNipote_key);
        } else {
            try {
                sCategoriaNipoteByID.setInt(1, categoriaNipote_key);
                try (ResultSet rs = sCategoriaNipoteByID.executeQuery()) {
                    if (rs.next()) {
                        cn = createCategoriaNipote(rs);
                        dataLayer.getCache().add(CategoriaNipote.class, cn);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load CategoriaNipote by ID", ex);
            }
        }
        return cn;
    }

    @Override
    public List<CategoriaPadre> getAllCategoriePadre() throws DataException {
        List<CategoriaPadre> result = new ArrayList<>();
        try {
            try (ResultSet rs = sCategoriePadre.executeQuery()) {
                while (rs.next()) {
                    result.add(getCategoriaPadre(rs.getInt("ID")));
                }
            }
            return result;
        } catch (SQLException ex) {
            throw new DataException("Error loading all CategoriaPadre", ex);
        }
    }

    @Override
    public List<CategoriaPadre> getAllCategoriePadre(Integer page) throws DataException {
        List<CategoriaPadre> result = new ArrayList<>();
        try {
            sCategoriePadrePaginata.setInt(1, page * offset);
            sCategoriePadrePaginata.setInt(2, offset);
            try (ResultSet rs = sCategoriePadrePaginata.executeQuery()) {
                while (rs.next()) {
                    result.add(getCategoriaPadre(rs.getInt("ID")));
                }
            }
            return result;
        } catch (SQLException ex) {
            throw new DataException("Error loading all CategoriaPadre", ex);
        }
    }

    @Override
    public List<CategoriaFiglio> getAllCategorieFiglio() throws DataException {
        List<CategoriaFiglio> result = new ArrayList<>();
        try {
            try (ResultSet rs = sCategorieFiglio.executeQuery()) {
                while (rs.next()) {
                    result.add(getCategoriaFiglio(rs.getInt("ID")));
                }
            }
            return result;
        } catch (SQLException ex) {
            throw new DataException("Error loading all CategoriaFiglio", ex);
        }
    }

    @Override
    public List<CategoriaNipote> getAllCategorieNipote() throws DataException {
        List<CategoriaNipote> result = new ArrayList<>();
        try {
            try (ResultSet rs = sCategorieNipote.executeQuery()) {
                while (rs.next()) {
                    result.add(getCategoriaNipote(rs.getInt("ID")));
                }
            }
            return result;
        } catch (SQLException ex) {
            throw new DataException("Error loading all CategoriaNipote", ex);
        }
    }

    @Override
    public List<CategoriaFiglio> getCategorieFiglioByPadre(CategoriaPadre categoriaPadre) throws DataException {
        List<CategoriaFiglio> result = new ArrayList<>();
        try {
            sCategorieFiglioFromPadre.setInt(1, categoriaPadre.getKey());
            try (ResultSet rs = sCategorieFiglioFromPadre.executeQuery()) {
                while (rs.next()) {
                    result.add(getCategoriaFiglio(rs.getInt("ID")));
                }
            }
            return result;
        } catch (SQLException ex) {
            throw new DataException("Error loading CategorieFiglio from CategoriaPadre", ex);
        }
    }

    @Override
    public List<CategoriaNipote> getCategorieNipoteByFiglio(CategoriaFiglio categoriaFiglio) throws DataException {
        List<CategoriaNipote> result = new ArrayList<>();
        try {
            sCategorieNipoteFromFiglio.setInt(1, categoriaFiglio.getKey());
            try (ResultSet rs = sCategorieNipoteFromFiglio.executeQuery()) {
                while (rs.next()) {
                    result.add(getCategoriaNipote(rs.getInt("ID")));
                }
            }
            return result;
        } catch (SQLException ex) {
            throw new DataException("Error loading CategorieNipote from CategoriaFiglio", ex);
        }
    }

    @Override
    public void storeCategoriaPadre(CategoriaPadre categoriaPadre) throws DataException {
        try {
            if (categoriaPadre.getKey() != null && categoriaPadre.getKey() > 0) { //update
                if (categoriaPadre instanceof DataItemProxy && !((DataItemProxy) categoriaPadre).isModified()) {
                    return;
                }
                uCategoriaPadre.setString(1, categoriaPadre.getNome());
                long current_version = categoriaPadre.getVersion();
                long next_version = current_version + 1;

                uCategoriaPadre.setLong(2, next_version);
                uCategoriaPadre.setInt(3, categoriaPadre.getKey());
                uCategoriaPadre.setLong(4, current_version);

                if (uCategoriaPadre.executeUpdate() == 0) {
                    throw new OptimisticLockException(categoriaPadre);
                } else {
                    categoriaPadre.setVersion(next_version);
                }
            } else { //insert
                iCategoriaPadre.setString(1, categoriaPadre.getNome());
                if (iCategoriaPadre.executeUpdate() == 1) {
                    try (ResultSet keys = iCategoriaPadre.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            categoriaPadre.setKey(key);
                            dataLayer.getCache().add(CategoriaPadre.class, categoriaPadre);
                        }
                    }
                }
            }
            if (categoriaPadre instanceof DataItemProxy) {
                ((DataItemProxy) categoriaPadre).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store CategoriaPadre", ex);
        }
    }

    @Override
    public void storeCategoriaFiglio(CategoriaFiglio categoriaFiglio) throws DataException {
        try {
            if (categoriaFiglio.getKey() != null && categoriaFiglio.getKey() > 0) {
                if (categoriaFiglio instanceof DataItemProxy && !((DataItemProxy) categoriaFiglio).isModified()) {
                    return;
                }
                uCategoriaFiglio.setString(1, categoriaFiglio.getNome());
                long current_version = categoriaFiglio.getVersion();
                long next_version = current_version + 1;

                uCategoriaFiglio.setInt(2, categoriaFiglio.getCategoriaGenitore().getKey());
                uCategoriaFiglio.setLong(3, next_version);
                uCategoriaFiglio.setInt(4, categoriaFiglio.getKey());
                uCategoriaFiglio.setLong(5, current_version);

                if (uCategoriaFiglio.executeUpdate() == 0) {
                    throw new OptimisticLockException(categoriaFiglio);
                } else {
                    categoriaFiglio.setVersion(next_version);
                }
            } else {
                iCategoriaFiglio.setString(1, categoriaFiglio.getNome());
                iCategoriaFiglio.setInt(2, categoriaFiglio.getCategoriaGenitore().getKey());
                if (iCategoriaFiglio.executeUpdate() == 1) {
                    try (ResultSet keys = iCategoriaFiglio.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            categoriaFiglio.setKey(key);
                            dataLayer.getCache().add(CategoriaFiglio.class, categoriaFiglio);
                        }
                    }
                }
            }
            if (categoriaFiglio instanceof DataItemProxy) {
                ((DataItemProxy) categoriaFiglio).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store CategoriaFiglio", ex);
        }
    }

    @Override
    public void storeCategoriaNipote(CategoriaNipote categoriaNipote) throws DataException {
        try {
            if (categoriaNipote.getKey() != null && categoriaNipote.getKey() > 0) {
                if (categoriaNipote instanceof DataItemProxy && !((DataItemProxy) categoriaNipote).isModified()) {
                    return;
                }
                uCategoriaNipote.setString(1, categoriaNipote.getNome());
                long current_version = categoriaNipote.getVersion();
                long next_version = current_version + 1;

                uCategoriaNipote.setInt(2, categoriaNipote.getCategoriaGenitore().getKey());
                uCategoriaNipote.setLong(3, next_version);
                uCategoriaNipote.setInt(4, categoriaNipote.getKey());
                uCategoriaNipote.setLong(4, current_version);

                if (uCategoriaNipote.executeUpdate() == 0) {
                    throw new OptimisticLockException(categoriaNipote);
                } else {
                    categoriaNipote.setVersion(next_version);
                }
            } else {
                iCategoriaNipote.setString(1, categoriaNipote.getNome());
                iCategoriaNipote.setInt(2, categoriaNipote.getCategoriaGenitore().getKey());
                if (iCategoriaNipote.executeUpdate() == 1) {
                    try (ResultSet keys = iCategoriaNipote.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            categoriaNipote.setKey(key);
                            dataLayer.getCache().add(CategoriaNipote.class, categoriaNipote);
                        }
                    }
                }
            }
            if (categoriaNipote instanceof DataItemProxy) {
                ((DataItemProxy) categoriaNipote).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store CategoriaNipote", ex);
        }
    }

    @Override
    public void deleteCategoriaPadre(CategoriaPadre categoriaPadre) throws DataException {
        try {
            dataLayer.getCache().delete(CategoriaPadre.class, categoriaPadre);
            dCategoriaPadre.setInt(1, categoriaPadre.getKey());
            dCategoriaPadre.executeUpdate();

        } catch (SQLException e) {
            throw new DataException("Unable to delete CategoriaPadre", e);
        }
    }

    @Override
    public void deleteCategoriaFiglio(CategoriaFiglio categoriaFiglio) throws DataException {
        try {
            dataLayer.getCache().delete(CategoriaFiglio.class, categoriaFiglio);
            dCategoriaFiglio.setInt(1, categoriaFiglio.getKey());
            dCategoriaFiglio.executeUpdate();

        } catch (SQLException e) {
            throw new DataException("Unable to delete CategoriaFiglio", e);
        }
    }

    @Override
    public void deleteCategoriaNipote(CategoriaNipote categoriaNipote) throws DataException {
        try {
            dataLayer.getCache().delete(CategoriaNipote.class, categoriaNipote);
            dCategoriaNipote.setInt(1, categoriaNipote.getKey());
            dCategoriaNipote.executeUpdate();

        } catch (SQLException e) {
            throw new DataException("Unable to delete CategoriaNipote", e);
        }
    }
}
