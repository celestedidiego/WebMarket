/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.DAO.impl;

import it.univaq.webmarket.application.WebmarketDataLayer;
import it.univaq.webmarket.data.DAO.RichiestaPresaInCaricoDAO;
import it.univaq.webmarket.data.model.RichiestaPresaInCarico;
import it.univaq.webmarket.data.model.Tecnico;
import it.univaq.webmarket.data.model.impl.proxy.RichiestaPresaInCaricoProxy;
import it.univaq.webmarket.framework.data.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cdidi
 */

public class RichiestaPresaInCaricoDAO_MySQL extends DAO implements RichiestaPresaInCaricoDAO {
    
    private final Integer offset = 5;

    private PreparedStatement sRichiestaPresaInCaricoByID;
    private PreparedStatement sRichiestePresaInCaricoByTecnico;
    private PreparedStatement iRichiestaPresaInCarico;
    private PreparedStatement uRichiestaPresaInCarico;
    private PreparedStatement dRichiestaPresaInCarico;

    public RichiestaPresaInCaricoDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();
            sRichiestaPresaInCaricoByID = connection.prepareStatement("SELECT * FROM richiestapresaincarico WHERE ID=?");

            /*
            * SPIEGAZIONE: Dobbiamo prendere quelle RichiestePreseInCarico che non hanno una Proposta associata
            * oppure che hanno una Proposta associata ma è stata rifiutata e non ci sono altre Proposte valide
            * */
            sRichiestePresaInCaricoByTecnico = connection.prepareStatement(
                    "SELECT ID FROM (" +
                            "SELECT r.ID, r.tecnicoID FROM RichiestaPresaInCarico r " +
                            "JOIN PropostaAcquisto p ON r.ID = p.richiestaPresaInCaricoID " +
                            "AND p.stato_proposta = 'Rifiutato' " +
                            "WHERE NOT EXISTS (" +
                            "   SELECT 1 " +
                            "   FROM PropostaAcquisto p2 " +
                            "   WHERE p2.richiestaPresaInCaricoID = r.ID " +
                            "   AND p2.ID != p.ID " +
                            "   AND p2.stato_proposta != 'Rifiutato' " +
                            ")" +
                            "UNION " +
                            "SELECT r.ID, r.tecnicoID FROM RichiestaPresaInCarico r " +
                            "LEFT JOIN PropostaAcquisto p ON r.ID = p.richiestaPresaInCaricoID " +
                            "WHERE p.ID IS NULL" +
                        ") AS richieste_non_proposte_valide " +
                        "WHERE tecnicoID=? " +
                        "LIMIT ?, ?;");
            iRichiestaPresaInCarico = connection.prepareStatement("INSERT INTO richiestapresaincarico(tecnicoID, richiestaOrdineID) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            uRichiestaPresaInCarico = connection.prepareStatement("UPDATE richiestapresaincarico SET tecnicoID=?, richiestaOrdineID=?, version=? WHERE ID=? AND version=?");
            dRichiestaPresaInCarico = connection.prepareStatement("DELETE FROM richiestapresaincarico WHERE ID=?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing webmarket data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            sRichiestaPresaInCaricoByID.close();
            iRichiestaPresaInCarico.close();
            sRichiestePresaInCaricoByTecnico.close();
            uRichiestaPresaInCarico.close();
            dRichiestaPresaInCarico.close();
        } catch (SQLException ex) {
            throw new DataException("Can't destroy prepared statements", ex);
        }
        super.destroy();
    }

    @Override
    public RichiestaPresaInCarico createRichiestaPresaInCarico() {
        return new RichiestaPresaInCaricoProxy(getDataLayer());
    }

    private RichiestaPresaInCaricoProxy createRichiestaPresaInCarico(ResultSet rs) throws DataException{
        try {
            RichiestaPresaInCaricoProxy rpic = (RichiestaPresaInCaricoProxy) createRichiestaPresaInCarico();
            rpic.setKey(rs.getInt("ID"));
            rpic.setRichiestaOrdine_key(rs.getInt("richiestaOrdineID"));
            rpic.setTecnico_key(rs.getInt("tecnicoID"));
            rpic.setVersion(rs.getLong("version"));
            // Carica e setta la richiesta ordine
            rpic.setRichiestaOrdine(
                ((WebmarketDataLayer) getDataLayer()).getRichiestaOrdineDAO().getRichiestaOrdine(rs.getInt("richiestaOrdineID"))
            );
            return rpic;
        } catch (SQLException ex) {
            throw new DataException("Unable to create RichiestaPresaInCarico object form ResultSet", ex);
        }
    }

    @Override
    public RichiestaPresaInCarico getRichiestaPresaInCarico(Integer key) throws DataException {
        RichiestaPresaInCarico rpic = null;
        if (dataLayer.getCache().has(RichiestaPresaInCarico.class, key)) {
            rpic = dataLayer.getCache().get(RichiestaPresaInCarico.class, key);
        } else {
            try {
                sRichiestaPresaInCaricoByID.setInt(1, key);
                try (ResultSet rs = sRichiestaPresaInCaricoByID.executeQuery()) {
                    if (rs.next()) {
                        rpic = createRichiestaPresaInCarico(rs);
                        dataLayer.getCache().add(RichiestaPresaInCarico.class, rpic);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load RichiestaPresaInCarico by ID", ex);
            }
        }
        return rpic;
    }

    @Override
    public List<RichiestaPresaInCarico> getAllRichiestePresaInCaricoByTecnico(Tecnico tecnico, Integer  page) throws DataException {
        List<RichiestaPresaInCarico> result = new ArrayList<>();
        try {
            
            sRichiestePresaInCaricoByTecnico.setInt(1,tecnico.getKey());
            sRichiestePresaInCaricoByTecnico.setInt(2, page * offset);
            sRichiestePresaInCaricoByTecnico.setInt(3, offset);
            try (ResultSet rs = sRichiestePresaInCaricoByTecnico.executeQuery()) {
                while (rs.next()) {
                    result.add(getRichiestaPresaInCarico(rs.getInt("ID")));
                }
            }
            return result;
        } catch (SQLException ex) {
            throw new DataException("Unable to load RichiestePreseInCarico by TecnicoPreventivi", ex);
        }
    }

    @Override
    public void storeRichiestaPresaInCarico(RichiestaPresaInCarico richiestaPresaInCarico) throws DataException {
        try {
            if (richiestaPresaInCarico.getKey() != null && richiestaPresaInCarico.getKey() > 0) { //update
                if (richiestaPresaInCarico instanceof DataItemProxy && !((DataItemProxy) richiestaPresaInCarico).isModified()) {
                    return;
                }

                uRichiestaPresaInCarico.setInt(1, richiestaPresaInCarico.getTecnico().getKey());
                uRichiestaPresaInCarico.setInt(2, richiestaPresaInCarico.getRichiestaOrdine().getKey());

                long current_version = richiestaPresaInCarico.getVersion();
                long next_version = current_version + 1;

                uRichiestaPresaInCarico.setLong(3, next_version);
                uRichiestaPresaInCarico.setInt(4, richiestaPresaInCarico.getKey());
                uRichiestaPresaInCarico.setLong(5, current_version);

                if (uRichiestaPresaInCarico.executeUpdate() == 0) {
                    throw new OptimisticLockException(richiestaPresaInCarico);
                } else {
                    richiestaPresaInCarico.setVersion(next_version);
                }
            } else { //insert

                iRichiestaPresaInCarico.setInt(1, richiestaPresaInCarico.getTecnico().getKey());
                iRichiestaPresaInCarico.setInt(2, richiestaPresaInCarico.getRichiestaOrdine().getKey());

                if (iRichiestaPresaInCarico.executeUpdate() == 1) {
                    try (ResultSet keys = iRichiestaPresaInCarico.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            richiestaPresaInCarico.setKey(key);
                            dataLayer.getCache().add(RichiestaPresaInCarico.class, richiestaPresaInCarico);
                        }
                    }
                }
            }
            if (richiestaPresaInCarico instanceof DataItemProxy) {
                ((DataItemProxy) richiestaPresaInCarico).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store RichiestaPresaInCarico", ex);
        }
    }

    @Override
    public void deleteRichiestaPresaInCarico(RichiestaPresaInCarico richiestaPresaInCarico) throws DataException {
        try {
            
            dataLayer.getCache().delete(RichiestaPresaInCarico.class, richiestaPresaInCarico);

            dRichiestaPresaInCarico.setInt(1, richiestaPresaInCarico.getKey());
            dRichiestaPresaInCarico.executeUpdate();

        } catch (SQLException e) {
            throw new DataException("Unable to delete RichiestaPresaInCarico", e);
        }
    }
}
