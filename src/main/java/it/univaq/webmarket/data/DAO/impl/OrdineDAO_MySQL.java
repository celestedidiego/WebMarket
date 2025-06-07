/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.DAO.impl;

import it.univaq.webmarket.data.DAO.OrdineDAO;
import it.univaq.webmarket.data.model.Ordinante;
import it.univaq.webmarket.data.model.Ordine;
import it.univaq.webmarket.data.model.Tecnico;
import it.univaq.webmarket.data.model.impl.proxy.OrdineProxy;
import it.univaq.webmarket.framework.data.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cdidi
 */

public class OrdineDAO_MySQL extends DAO implements OrdineDAO {
    
    private final Integer offset = 5;

    private PreparedStatement sOrdineByID;
    private PreparedStatement sOrdineInStoricoByID;
    private PreparedStatement iOrdine;
    private PreparedStatement uOrdine;
    private PreparedStatement sOrdiniByOrdinante;
    private PreparedStatement sOrdiniByTecnico;
    private PreparedStatement sStoricoByOrdinante;

    public OrdineDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();
            sOrdineByID = connection.prepareStatement("SELECT * FROM ordine WHERE ID=?");
            sOrdineInStoricoByID = connection.prepareStatement("SELECT * FROM ordine WHERE ID=?");
            iOrdine = connection.prepareStatement("INSERT INTO ordine(stato_consegna, tecnicoID, propostaAcquistoID, ordinanteID) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            uOrdine = connection.prepareStatement("UPDATE ordine SET stato_consegna=?,risposta=?, tecnicoID=?, propostaAcquistoID=?, ordinanteID=?, version=? WHERE ID=? AND version=?");
            sOrdiniByOrdinante = connection.prepareStatement("SELECT ID FROM ordine WHERE ordinanteID=? LIMIT ?, ?");
            sOrdiniByTecnico = connection.prepareStatement("SELECT * FROM ordine WHERE tecnicoID=? LIMIT ?, ?");
            sStoricoByOrdinante = connection.prepareStatement("SELECT ID FROM ordine WHERE ordinanteID=? LIMIT ?, ?");
        } catch (SQLException e) {
            throw new DataException("Error initializing webmarket data layer", e);
        }
    }

    @Override
    public void destroy() throws DataException {
        try{
            sOrdineByID.close();
            sOrdineInStoricoByID.close();
            sOrdiniByTecnico.close();
            sOrdiniByOrdinante.close();
            sStoricoByOrdinante.close();
            iOrdine.close();
            uOrdine.close();
        } catch (SQLException ex) {
            throw new DataException("Can't destroy prepared statements", ex);
        }
    }

    @Override
    public Ordine createOrdine() {
        return new OrdineProxy(getDataLayer());
    }

    private OrdineProxy createOrdine(ResultSet rs) throws DataException {
        try {
            OrdineProxy o = (OrdineProxy) createOrdine();
            o.setKey(rs.getInt("ID"));
            o.setStatoConsegna(rs.getString("stato_consegna"));
            o.setRisposta(rs.getString("risposta"));
            o.setVersion(rs.getLong("version"));
            o.setDataConsegna(rs.getTimestamp("data_di_consegna") != null ? rs.getTimestamp("data_di_consegna").toLocalDateTime().toLocalDate() : null);
            o.setTecnicoKey(rs.getInt("tecnicoID"));
            o.setPropostaAcquistoKey(rs.getInt("propostaAcquistoID"));
            o.setOrdinanteKey(rs.getInt("ordinanteID"));
            return o;
        } catch (SQLException ex) {
            throw new DataException("Unable to create Ordine object form ResultSet", ex);
        }
    }

    @Override
    public Ordine getOrdine(int key) throws DataException {
        Ordine o = null;
        if (dataLayer.getCache().has(Ordine.class, key)) {
            o = dataLayer.getCache().get(Ordine.class, key);
        } else {
            try {
                sOrdineByID.setInt(1, key);
                try (ResultSet rs = sOrdineByID.executeQuery()) {
                    if (rs.next()) {
                        o = createOrdine(rs);
                        dataLayer.getCache().add(Ordine.class, o);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load Ordine by ID", ex);
            }
        }
        return o;
    }

    @Override
    public Ordine getOrdineInStorico(int key) throws DataException {
        Ordine o = null;
        try {
            sOrdineInStoricoByID.setInt(1, key);
            try (ResultSet rs = sOrdineInStoricoByID.executeQuery()) {
                if (rs.next()) {
                    o = createOrdine(rs);
                    dataLayer.getCache().add(Ordine.class, o);
                }
            }
            return o;
        } catch (SQLException ex) {
            throw new DataException("Unable to load Ordine from Storico by ID", ex);
        }
    }

    @Override
    public List<Ordine> getStorico(Ordinante ordinante, Integer page) throws DataException {
        List<Ordine> ordini = new ArrayList<>();
        try {
            sStoricoByOrdinante.setInt(1, ordinante.getKey());
            sStoricoByOrdinante.setInt(2, page * offset);
            sStoricoByOrdinante.setInt(3, offset);
            try (ResultSet rs = sStoricoByOrdinante.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("ID");
                    ordini.add(getOrdine(id));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load storico Ordini by Ordinante", ex);
        }
        return ordini;
    }

    @Override
    public List<Ordine> getAllOrdiniByOrdinante(Ordinante ordinante, Integer page) throws DataException {
        List<Ordine> ordini = new ArrayList<>();
        try {
            sOrdiniByOrdinante.setInt(1, ordinante.getKey());
            sOrdiniByOrdinante.setInt(2, page * offset);
            sOrdiniByOrdinante.setInt(3, offset);
            try (ResultSet rs = sOrdiniByOrdinante.executeQuery()) {
                while (rs.next()) {
                    ordini.add(getOrdine(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Ordini by Ordinante", ex);
        }
        return ordini;
    }

    @Override
    public List<Ordine> getAllOrdiniByTecnico(Tecnico tecnico, Integer page) throws DataException {
        List<Ordine> ordini = new ArrayList<>();
        try {
            sOrdiniByTecnico.setInt(1, tecnico.getKey());
            sOrdiniByTecnico.setInt(2, page * offset);
            sOrdiniByTecnico.setInt(3, offset);
            try (ResultSet rs = sOrdiniByTecnico.executeQuery()) {
                while (rs.next()) {
                    ordini.add(getOrdine(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Ordini by Tecnico", ex);
        }
        return ordini;
    }

    @Override
    public void storeOrdine(Ordine ordine) throws DataException {
        try {
            if (ordine.getKey() != null && ordine.getKey() > 0) { // update
                if (ordine instanceof DataItemProxy && !((DataItemProxy) ordine).isModified()) {
                    return;
                }

                uOrdine.setString(1, ordine.getStatoConsegna());
                if (ordine.getRisposta() == null) {
                    uOrdine.setNull(2, Types.VARCHAR);
                } else {
                    uOrdine.setString(2, ordine.getRisposta());
                }
                uOrdine.setInt(3, ordine.getTecnico().getKey());
                uOrdine.setInt(4, ordine.getPropostaAcquisto().getKey());
                uOrdine.setInt(5, ordine.getOrdinante().getKey()); 

                long current_version = ordine.getVersion();
                long next_version = current_version + 1;

                uOrdine.setLong(6, next_version);
                uOrdine.setInt(7, ordine.getKey());
                uOrdine.setLong(8, current_version);

                if (uOrdine.executeUpdate() == 0) {
                    throw new OptimisticLockException(ordine);
                } else {
                    ordine.setVersion(next_version);
                }
            } else { // insert

                iOrdine.setString(1, ordine.getStatoConsegna());
                iOrdine.setInt(2, ordine.getTecnico().getKey());
                iOrdine.setInt(3, ordine.getPropostaAcquisto().getKey());
                iOrdine.setInt(4, ordine.getOrdinante().getKey()); 

                if (iOrdine.executeUpdate() == 1) {
                    try (ResultSet keys = iOrdine.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            ordine.setKey(key);
                            dataLayer.getCache().add(Ordine.class, ordine);
                        }
                    }
                }
            }
            if (ordine instanceof DataItemProxy) {
                ((DataItemProxy) ordine).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store Ordine", ex);
        }
    }
}
