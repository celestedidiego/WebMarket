/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.DAO.impl;

import it.univaq.webmarket.data.DAO.RichiestaOrdineDAO;
import it.univaq.webmarket.data.model.Ordinante;
import it.univaq.webmarket.data.model.RichiestaOrdine;
import it.univaq.webmarket.data.model.impl.proxy.RichiestaOrdineProxy;
import it.univaq.webmarket.framework.data.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author cdidi
 */

public class RichiestaOrdineDAO_MySQL extends DAO implements RichiestaOrdineDAO {
    
    private PreparedStatement sRichiestaByID;
    private PreparedStatement iRichiestaOrdine;
    private PreparedStatement uRichiestaOrdine;
    private PreparedStatement dRichiestaOrdine;
    private PreparedStatement sRichiesteByIDOrdinantePage;
    private PreparedStatement sRichiesteNonGestite;
    private PreparedStatement checkCodiceRichiesta;

    public RichiestaOrdineDAO_MySQL(DataLayer d) {
        super(d);
    }

    private final Integer offset = 5;

    @Override
    public void init() throws DataException {
        try {
            super.init();

            iRichiestaOrdine = connection.prepareStatement("INSERT INTO richiestaOrdine (codice_richiesta, note, data, ordinanteID) VALUES(?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            uRichiestaOrdine = connection.prepareStatement("UPDATE richiestaOrdine SET codice_richiesta=?, note=?, data=?, ordinanteID=?, version=? WHERE ID=? AND version=?");
            dRichiestaOrdine = connection.prepareStatement("DELETE FROM richiestaOrdine WHERE ID=?");
            sRichiestaByID = connection.prepareStatement("SELECT * FROM richiestaOrdine WHERE ID=? ORDER BY data DESC");
            sRichiesteByIDOrdinantePage = connection.prepareStatement("SELECT ID FROM richiestaOrdine WHERE ordinanteID=? ORDER BY data DESC LIMIT ?,?");
            sRichiesteNonGestite = connection.prepareStatement("SELECT r.ID FROM richiestaOrdine r LEFT JOIN richiestapresaincarico rp ON r.ID = rp.richiestaOrdineID  WHERE rp.richiestaOrdineID IS NULL ORDER BY r.data LIMIT ?, ?");
            checkCodiceRichiesta = connection.prepareStatement("SELECT 1 FROM RichiestaOrdine WHERE codice_richiesta = ?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing webmarket data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
         try {
             iRichiestaOrdine.close();
             dRichiestaOrdine.close();
             sRichiestaByID.close();
             sRichiesteByIDOrdinantePage.close();
             sRichiesteNonGestite.close();
                checkCodiceRichiesta.close();
        } catch (SQLException ex) {
             throw new DataException("Can't destroy prepared statements", ex);
        }
        super.destroy();
    }

    @Override
    public RichiestaOrdine createRichiestaOrdine() {
        return new RichiestaOrdineProxy(getDataLayer());
    }

    private RichiestaOrdineProxy createRichiestaAcquisto(ResultSet rs) throws DataException {
        RichiestaOrdineProxy ra = (RichiestaOrdineProxy) createRichiestaOrdine();
        try {
            ra.setKey(rs.getInt("ID"));
            ra.setCodiceRichiesta(rs.getString("codice_richiesta"));
            ra.setNote(rs.getString("note"));
            ra.setData(rs.getTimestamp("data").toLocalDateTime().toLocalDate());
            ra.setOrdinante_key(rs.getInt("ordinanteID"));
            ra.setVersion(rs.getLong("version"));

            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT 1 FROM richiestapresaincarico WHERE richiestaOrdineID = ? LIMIT 1")) {
                ps.setInt(1, ra.getKey());
                try (ResultSet prs = ps.executeQuery()) {
                    ra.setPresaInCarico(prs.next());
                }
            }
            
        } catch (SQLException ex) {
            throw new DataException("Unable to create Richiesta from ResultSet", ex);
        }
        return ra;
    }

    @Override
    public RichiestaOrdine getRichiestaOrdine(int richiestaOrdine_key) throws DataException {
        RichiestaOrdine ra = null;
        if (dataLayer.getCache().has(RichiestaOrdine.class, richiestaOrdine_key)) {
            ra = dataLayer.getCache().get(RichiestaOrdine.class, richiestaOrdine_key);
        } else {
            try {
                sRichiestaByID.setInt(1, richiestaOrdine_key);
                try (ResultSet rs = sRichiestaByID.executeQuery()) {
                    if (rs.next()) {
                        ra = createRichiestaAcquisto(rs);
                        //e lo mettiamo anche nella cache
                        dataLayer.getCache().add(RichiestaOrdine.class, ra);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load Richiesta by ID", ex);
            }
        }
        return ra;
    }

    @Override
    public List<RichiestaOrdine> getRichiesteByOrdinante(Ordinante ordinante, Integer page) throws DataException {
        List<RichiestaOrdine> result = new ArrayList<>();
        try {
            sRichiesteByIDOrdinantePage.setInt(1, ordinante.getKey());
            sRichiesteByIDOrdinantePage.setInt(2, page * offset);
            sRichiesteByIDOrdinantePage.setInt(3, offset);
            try (ResultSet rs = sRichiesteByIDOrdinantePage.executeQuery()){
                while (rs.next()){
                    result.add(getRichiestaOrdine(rs.getInt("ID")));
                }
            }
            return result;
        } catch (SQLException ex) {
            throw new DataException("Error loading all Richieste paginate from Ordinante", ex);
        }
    }

    @Override
    public List<RichiestaOrdine> getRichiesteNonGestite(Integer page) throws DataException {
        List<RichiestaOrdine> result = new ArrayList<>();
        try {
            sRichiesteNonGestite.setInt(1, page * offset);
            sRichiesteNonGestite.setInt(2, offset);
            try (ResultSet rs = sRichiesteNonGestite.executeQuery()){
                while (rs.next()){
                    result.add(getRichiestaOrdine(rs.getInt("ID")));
                }
            }
            return result;
        } catch (SQLException ex) {
            throw new DataException("Error loading all Richieste non gestite paginate from Ordinante", ex);
        }
    }

    @Override
    public void storeRichiestaOrdine(RichiestaOrdine richiestaOrdine) throws DataException {
        try {
            if (richiestaOrdine.getKey() != null && richiestaOrdine.getKey() > 0) { //update
                if (richiestaOrdine instanceof DataItemProxy && !((DataItemProxy) richiestaOrdine).isModified()) {
                    return;
                }
                
                uRichiestaOrdine.setString(1, richiestaOrdine.getCodiceRichiesta());
                uRichiestaOrdine.setString(2, richiestaOrdine.getNote());
                uRichiestaOrdine.setTimestamp(3, Timestamp.valueOf(richiestaOrdine.getData().atStartOfDay()));
                uRichiestaOrdine.setInt(4, richiestaOrdine.getOrdinante().getKey());

                long current_version = richiestaOrdine.getVersion();
                long next_version = current_version + 1;

                uRichiestaOrdine.setLong(5, next_version);
                uRichiestaOrdine.setInt(6, richiestaOrdine.getKey());
                uRichiestaOrdine.setLong(7, current_version);

                if (uRichiestaOrdine.executeUpdate() == 0) {
                    throw new OptimisticLockException(richiestaOrdine);
                } else {
                    richiestaOrdine.setVersion(next_version);
                }

            } else { //INSERT
                String codiceRichiesta= getRandomCodiceRichiesta(10);
                while(checkCodiceRichiesta(codiceRichiesta)){
                    codiceRichiesta = getRandomCodiceRichiesta(10);
                }
                iRichiestaOrdine.setString(1, codiceRichiesta);
                if (richiestaOrdine.getNote() != null) {
                    iRichiestaOrdine.setString(2, richiestaOrdine.getNote());
                } else {
                    iRichiestaOrdine.setNull(2, Types.VARCHAR);
                }
                iRichiestaOrdine.setTimestamp(3, Timestamp.valueOf(richiestaOrdine.getData().atStartOfDay()));
                iRichiestaOrdine.setInt(4, richiestaOrdine.getOrdinante().getKey());

                if (iRichiestaOrdine.executeUpdate() == 1) {
                    try (ResultSet keys = iRichiestaOrdine.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            richiestaOrdine.setKey(key);
                            richiestaOrdine.setCodiceRichiesta(codiceRichiesta);
                            dataLayer.getCache().add(RichiestaOrdine.class, richiestaOrdine);
                        }
                    }
                }
            }
            if (richiestaOrdine instanceof DataItemProxy) {
                ((DataItemProxy) richiestaOrdine).setModified(false);
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to store Richiesta", ex);
        }
    }

    @Override
    public void deleteRichiestaOrdine(RichiestaOrdine richiestaOrdine) throws DataException {
        try {

            //Lo cancello prima dalla cache
            dataLayer.getCache().delete(RichiestaOrdine.class, richiestaOrdine);

            dRichiestaOrdine.setInt(1, richiestaOrdine.getKey());
            dRichiestaOrdine.executeUpdate();

        } catch(SQLException e) {
            throw new DataException("Unable to delete Richiesta", e);
        }
    }

    private String getRandomCodiceRichiesta(int n) {
        Random r = new Random();
        StringBuilder code = new StringBuilder();
        for(int i = 0; i < n; i++) {
            code.append(r.nextInt(10));
        }
        return code.toString();
    }

    private boolean checkCodiceRichiesta(String codiceRichiesta) throws DataException {
        try {
            checkCodiceRichiesta.setString(1, codiceRichiesta);
            try (ResultSet resultSet = checkCodiceRichiesta.executeQuery()) {
                return resultSet.next(); // returns true if a row exists
            }
        } catch (SQLException e) {
            throw new DataException("Unable to check codice Richiesta", e);
        }
    }
}
