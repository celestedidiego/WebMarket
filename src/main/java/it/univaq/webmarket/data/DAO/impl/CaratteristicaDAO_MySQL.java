/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.DAO.impl;

import it.univaq.webmarket.data.DAO.CaratteristicaDAO;
import it.univaq.webmarket.data.model.*;
import it.univaq.webmarket.data.model.impl.proxy.CaratteristicaRichiestaProxy;
import it.univaq.webmarket.data.model.impl.proxy.CaratteristicaProxy;
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

public class CaratteristicaDAO_MySQL extends DAO implements CaratteristicaDAO {
    
    private Integer offset = 5;

    private PreparedStatement iCaratteristica;
    private PreparedStatement sCaratteristicaByID;
    private PreparedStatement sAllCaratteristiche;
    private PreparedStatement uCaratteristica;
    private PreparedStatement dCaratteristica;
    private PreparedStatement sCaratteristicheByCategoriaNipote;

    private PreparedStatement sCaratteristicaRichiestaByID;
    private PreparedStatement sCaratteristicheRichiesta;
    private PreparedStatement iCaratteristicaRichiesta;
    private PreparedStatement uCaratteristicaRichiesta;
    private PreparedStatement dCaratteristicaRichiesta;

    public CaratteristicaDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();
            iCaratteristica = connection.prepareStatement("INSERT INTO caratteristica(nome, categoriaNipoteID) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            sCaratteristicaByID = connection.prepareStatement("SELECT * FROM caratteristica WHERE ID=?");
            sAllCaratteristiche = connection.prepareStatement("SELECT ID FROM caratteristica LIMIT ?, ?");
            uCaratteristica = connection.prepareStatement("UPDATE caratteristica SET nome=?, categoriaNipoteID=?, version=? WHERE ID=? AND version=?");
            dCaratteristica = connection.prepareStatement("DELETE FROM caratteristica WHERE ID=?");
            sCaratteristicheByCategoriaNipote = connection.prepareStatement("SELECT * FROM caratteristica WHERE categoriaNipoteID = ?");

            sCaratteristicaRichiestaByID = connection.prepareStatement("SELECT * FROM caratteristicaRichiesta WHERE ID=?");
            sCaratteristicheRichiesta = connection.prepareStatement("SELECT * FROM caratteristicaRichiesta WHERE richiestaOrdineID = ?");
            iCaratteristicaRichiesta = connection.prepareStatement("INSERT INTO caratteristicaRichiesta(caratteristicaID, richiestaOrdineID, valore) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            uCaratteristicaRichiesta = connection.prepareStatement("UPDATE caratteristicaRichiesta SET caratteristicaID=?, richiestaOrdineID=?, valore=?, version=? WHERE ID=? AND version=?");
            dCaratteristicaRichiesta = connection.prepareStatement("DELETE FROM caratteristicaRichiesta WHERE ID=?");
        } catch (SQLException ex) {
            throw new DataException("Errore inizializzazione data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            iCaratteristica.close();
            sCaratteristicaByID.close();
            sAllCaratteristiche.close();
            uCaratteristica.close();
            dCaratteristica.close();
            sCaratteristicheByCategoriaNipote.close();

            sCaratteristicaRichiestaByID.close();
            sCaratteristicheRichiesta.close();
            iCaratteristicaRichiesta.close();
            uCaratteristicaRichiesta.close();
            dCaratteristicaRichiesta.close();
        } catch (SQLException ex) {
            throw new DataException("Can't destroy prepared statements", ex);
        }
        super.destroy();
    }

    @Override
    public Caratteristica createCaratteristica() {
        return new CaratteristicaProxy(getDataLayer());
    }

    @Override
    public CaratteristicaRichiesta createCaratteristicaRichiesta() {
        return new CaratteristicaRichiestaProxy(getDataLayer());
    }


    public CaratteristicaProxy createCaratteristica(ResultSet rs) throws DataException {
        try {
            CaratteristicaProxy caratteristica = new CaratteristicaProxy(getDataLayer());
            caratteristica.setKey(rs.getInt("ID"));
            caratteristica.setNome(rs.getString("nome"));
            caratteristica.setCategoriaNipote_key(rs.getInt("categoriaNipoteID"));
            caratteristica.setVersion(rs.getLong("version"));

            return caratteristica;
        } catch (SQLException ex) {
            throw new DataException("Unable to create Caratteristica object form ResultSet", ex);
        }
    }

    public CaratteristicaRichiestaProxy createCaratteristicaRichiesta(ResultSet rs) throws DataException {
        try {
            CaratteristicaRichiestaProxy caratteristicaRichiesta = new CaratteristicaRichiestaProxy(getDataLayer());
            caratteristicaRichiesta.setKey(rs.getInt("ID"));
            caratteristicaRichiesta.setValore(rs.getString("valore"));
            caratteristicaRichiesta.setCaratteristica_key(rs.getInt("caratteristicaID"));
            caratteristicaRichiesta.setVersion(rs.getLong("version"));
            return caratteristicaRichiesta;
        } catch (SQLException ex) {
            throw new DataException("Unable to create CaratteristicaRichiesta object form ResultSet", ex);
        }
    }


    @Override
    public Caratteristica getCaratteristica(int key) throws DataException {
        Caratteristica caratteristica = null;
        if (dataLayer.getCache().has(Caratteristica.class, key)) {
            caratteristica = dataLayer.getCache().get(Caratteristica.class, key);
        } else {
            try {
                sCaratteristicaByID.setInt(1, key);
                try (ResultSet rs = sCaratteristicaByID.executeQuery()) {
                    if (rs.next()) {
                        caratteristica = createCaratteristica(rs);
                        dataLayer.getCache().add(Caratteristica.class, caratteristica);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to get Caratteristica by ID", ex);
            }
        }
        return caratteristica;
    }


    @Override
    public List<Caratteristica> getAllCaratteristiche(Integer page) throws DataException {
        List<Caratteristica> result = new ArrayList<>();
        try {
            sAllCaratteristiche.setInt(1, page * offset);
            sAllCaratteristiche.setInt(2, offset);
            try (ResultSet rs = sAllCaratteristiche.executeQuery()) {
                while (rs.next()) {
                    result.add(getCaratteristica(rs.getInt("ID")));
                }
            }
            return result;
        } catch (SQLException ex) {
            throw new DataException("Error loading all Caratteristica", ex);
        }
    }

    @Override
    public List<Caratteristica> getAllCaratteristiche(CategoriaNipote categoriaNipote) throws DataException {
        List<Caratteristica> result = new ArrayList<>();
        try {
            sCaratteristicheByCategoriaNipote.setInt(1, categoriaNipote.getKey());
            try (ResultSet rs = sCaratteristicheByCategoriaNipote.executeQuery()) {
                while (rs.next()) {
                    result.add(getCaratteristica(rs.getInt("ID")));
                }
            }
            return result;
        } catch (SQLException ex) {
            throw new DataException("Error loading all Caratteristica from CategoriaNipote", ex);
        }
    }

    @Override
    public void storeCaratteristica(Caratteristica caratteristica) throws DataException {
        try {
            if (caratteristica.getKey() != null && caratteristica.getKey() > 0) {
                if (caratteristica instanceof CaratteristicaProxy && !((CaratteristicaProxy) caratteristica).isModified()) {
                    return;
                }
                uCaratteristica.setString(1, caratteristica.getNome());
                uCaratteristica.setInt(2, caratteristica.getCategoriaNipote().getKey());

                long current_version = caratteristica.getVersion();
                long next_version = current_version + 1;

                uCaratteristica.setLong(3, next_version);
                uCaratteristica.setInt(4, caratteristica.getKey());
                uCaratteristica.setLong(5, current_version);

                if (uCaratteristica.executeUpdate() == 0) {
                    throw new OptimisticLockException(caratteristica);
                } else {
                    caratteristica.setVersion(next_version);
                }


            } else {
                iCaratteristica.setString(1, caratteristica.getNome());
                iCaratteristica.setInt(2, caratteristica.getCategoriaNipote().getKey());

                if (iCaratteristica.executeUpdate() == 1) {
                    try (ResultSet keys = iCaratteristica.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            caratteristica.setKey(key);
                            dataLayer.getCache().add(Caratteristica.class, caratteristica);
                        }
                    }
                }

            }
        } catch (SQLException ex) {
            throw new DataException("Unable to store Caratteristica", ex);
        }
    }

    @Override
    public void deleteCaratteristica(Caratteristica caratteristica) throws DataException {
        try {

            dataLayer.getCache().delete(Caratteristica.class, caratteristica);
            dCaratteristica.setInt(1, caratteristica.getKey());
            dCaratteristica.executeUpdate();

        } catch (SQLException e) {
            throw new DataException("Unable to delete Caratteristica", e);
        }
    }

    @Override
    public CaratteristicaRichiesta getCaratteristicaRichiesta(int key) throws DataException {
        CaratteristicaRichiesta caratteristicaRichiesta = null;
        if (dataLayer.getCache().has(CaratteristicaRichiesta.class, key)) {
            caratteristicaRichiesta = dataLayer.getCache().get(CaratteristicaRichiesta.class, key);
        } else {
            try {
                sCaratteristicaRichiestaByID.setInt(1, key);
                try (ResultSet rs = sCaratteristicaRichiestaByID.executeQuery()) {
                    if (rs.next()) {
                        caratteristicaRichiesta = createCaratteristicaRichiesta(rs);
                        dataLayer.getCache().add(CaratteristicaRichiesta.class, caratteristicaRichiesta);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to get CaratteristicaRichiesta by ID", ex);
            }
        }
        return caratteristicaRichiesta;
    }

    @Override
    public void storeCaratteristicaRichiesta(CaratteristicaRichiesta caratteristicaRichiesta, Integer richiestaOrdine_key) throws DataException {
        try {
            if (caratteristicaRichiesta.getKey() != null && caratteristicaRichiesta.getKey() > 0) {
                if (caratteristicaRichiesta instanceof CaratteristicaRichiestaProxy && !((CaratteristicaRichiestaProxy) caratteristicaRichiesta).isModified()) {
                    return;
                }
                
                uCaratteristicaRichiesta.setInt(1, caratteristicaRichiesta.getCaratteristica().getKey());
                uCaratteristicaRichiesta.setInt(2, richiestaOrdine_key);
                uCaratteristicaRichiesta.setString(3, caratteristicaRichiesta.getValore());

                long current_version = caratteristicaRichiesta.getVersion();
                long next_version = current_version + 1;

                uCaratteristicaRichiesta.setLong(4, next_version);
                uCaratteristicaRichiesta.setInt(5, caratteristicaRichiesta.getKey());
                uCaratteristicaRichiesta.setLong(6, current_version);

                if (uCaratteristicaRichiesta.executeUpdate() == 0) {
                    throw new OptimisticLockException(caratteristicaRichiesta);
                } else {
                    caratteristicaRichiesta.setVersion(next_version);
                }


            } else {

                iCaratteristicaRichiesta.setInt(1, caratteristicaRichiesta.getCaratteristica().getKey());
                iCaratteristicaRichiesta.setInt(2, richiestaOrdine_key);
                iCaratteristicaRichiesta.setString(3, caratteristicaRichiesta.getValore());

                if (iCaratteristicaRichiesta.executeUpdate() == 1) {
                    try (ResultSet keys = iCaratteristicaRichiesta.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            caratteristicaRichiesta.setKey(key);
                            dataLayer.getCache().add(CaratteristicaRichiesta.class, caratteristicaRichiesta);
                        }
                    }
                }

            }
            if (caratteristicaRichiesta instanceof DataItemProxy) {
                ((DataItemProxy) caratteristicaRichiesta).setModified(false);
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to store CaratteristicaRichiesta", ex);
        }
    }

    @Override
    public void deleteCaratteristicaRichiesta(CaratteristicaRichiesta caratteristicaRichiesta) throws DataException {
        try {
            dataLayer.getCache().delete(CaratteristicaRichiesta.class, caratteristicaRichiesta);
            dCaratteristicaRichiesta.setInt(1, caratteristicaRichiesta.getKey());
            dCaratteristicaRichiesta.executeUpdate();
        } catch (SQLException e) {
            throw new DataException("Unable to delete CaratteristicaRichiesta", e);
        }
    }


    @Override
    public List<CaratteristicaRichiesta> getCaratteristicheRichiesta(RichiestaOrdine richiestaOrdine) throws DataException {
        List<CaratteristicaRichiesta> caratteristicheRichiesta = new ArrayList<>();

        try {
            sCaratteristicheRichiesta.setInt(1, richiestaOrdine.getKey());
            try (ResultSet rs = sCaratteristicheRichiesta.executeQuery()) {
                while (rs.next()) {
                    CaratteristicaRichiesta caratteristicaRichiesta = createCaratteristicaRichiesta();
                    caratteristicaRichiesta.setKey(rs.getInt("ID"));
                    caratteristicaRichiesta.setVersion(rs.getLong("version"));
                    caratteristicaRichiesta.setCaratteristica(getCaratteristica(rs.getInt("caratteristicaID")));
                    caratteristicaRichiesta.setValore(rs.getString("valore"));
                    caratteristicheRichiesta.add(caratteristicaRichiesta);
                }
            }
            return caratteristicheRichiesta;
        } catch (SQLException ex) {
            throw new DataException("Error loading all CaratteristicaRichiesta from Richiesta", ex);
        }
    }
}
