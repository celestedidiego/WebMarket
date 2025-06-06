/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.DAO.impl;

import it.univaq.webmarket.data.DAO.PropostaAcquistoDAO;
import it.univaq.webmarket.data.model.Ordinante;
import it.univaq.webmarket.data.model.PropostaAcquisto;
import it.univaq.webmarket.data.model.RichiestaPresaInCarico;
import it.univaq.webmarket.data.model.Tecnico;
import it.univaq.webmarket.data.model.impl.proxy.PropostaAcquistoProxy;
import it.univaq.webmarket.framework.data.DAO;
import it.univaq.webmarket.framework.data.DataException;
import it.univaq.webmarket.framework.data.DataLayer;
import it.univaq.webmarket.framework.data.OptimisticLockException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author cdidi
 */

public class PropostaAcquistoDAO_MySQL extends DAO implements PropostaAcquistoDAO {
    
    private Integer offset = 5;

    private PreparedStatement sPropostaAcquistoByID;
    private PreparedStatement sProposteByRichiestaPresaInCarico;
    private PreparedStatement sProposteByTecnico;
    private PreparedStatement sProposteDaDecidereByOrdinante;
    private PreparedStatement sProposteAccettate;
    private PreparedStatement iPropostaAcquisto;
    private PreparedStatement dPropostaAcquisto;
    private PreparedStatement uPropostaAcquisto;
    private PreparedStatement checkCodiceProposta;

    public PropostaAcquistoDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();
            sPropostaAcquistoByID = connection.prepareStatement("SELECT * FROM propostaacquisto WHERE ID=?");
            sProposteByRichiestaPresaInCarico = connection.prepareStatement("SELECT ID FROM propostaacquisto WHERE richiestaPresaInCaricoID = ?");
            sProposteByTecnico = connection.prepareStatement("SELECT p.ID FROM propostaacquisto p JOIN richiestapresaincarico r ON p.richiestaPresaInCaricoID = r.ID WHERE r.tecnicoID = ? LIMIT ?, ?");
            sProposteAccettate = connection.prepareStatement(
                    "SELECT p.ID " +
                    "FROM propostaacquisto p " +
                    "LEFT JOIN Ordine o ON p.ID = o.propostaAcquistoID " +
                    "WHERE p.stato_proposta = 'Accettato' AND o.ID IS NULL " +
                    "LIMIT ?, ?;");
            sProposteDaDecidereByOrdinante = connection.prepareStatement(
                    "SELECT p.ID FROM propostaacquisto p " +
                    "JOIN richiestapresaincarico rp ON p.richiestaPresaInCaricoID = rp.ID " +
                    "JOIN richiestaordine r ON rp.richiestaOrdineID=r.ID " +
                    "WHERE ordinanteID = ? AND p.stato_proposta = 'In attesa' LIMIT ?, ?");
            iPropostaAcquisto = connection.prepareStatement("INSERT INTO propostaacquisto(" +
                    "codice_prodotto, " +
                    "produttore, " +
                    "note, " +
                    "prezzo, " +
                    "nome_prodotto," +
                    "URL, " +
                    "stato_proposta," +
                    "richiestaPresaInCaricoID)" +
                    " VALUES(?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            dPropostaAcquisto = connection.prepareStatement("DELETE FROM propostaacquisto WHERE ID=?");
            uPropostaAcquisto = connection.prepareStatement("UPDATE propostaacquisto SET codice_prodotto=?, produttore=?, note=?, prezzo=?, nome_prodotto=?, URL=?, stato_proposta=?, motivazione=?, richiestaPresaInCaricoID=?, version=? WHERE ID=? AND version=?");
            checkCodiceProposta = connection.prepareStatement("SELECT 1 FROM propostaacquisto WHERE codice_prodotto = ?");
            
        } catch (SQLException ex) {
            throw new DataException("Error initializing webmarket data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            sPropostaAcquistoByID.close();
            sProposteByRichiestaPresaInCarico.close();
            sProposteByTecnico.close();
            sProposteAccettate.close();
            sProposteDaDecidereByOrdinante.close();
            iPropostaAcquisto.close();
            dPropostaAcquisto.close();
            uPropostaAcquisto.close();
            checkCodiceProposta.close();
        } catch (SQLException ex) {
            throw new DataException("Can't destroy prepared statements", ex);
        }
        super.destroy();
    }


    @Override
    public PropostaAcquisto createPropostaAcquisto() {
        return new PropostaAcquistoProxy(getDataLayer());
    }

    private PropostaAcquistoProxy createPropostaAcquisto (ResultSet rs) throws DataException {
        try {
            PropostaAcquistoProxy propostaAcquisto = new PropostaAcquistoProxy(getDataLayer());
            propostaAcquisto.setKey(rs.getInt("ID"));
            propostaAcquisto.setCodiceProdotto(rs.getString("codice_prodotto"));
            propostaAcquisto.setProduttore(rs.getString("produttore"));
            propostaAcquisto.setNote(rs.getString("note"));
            propostaAcquisto.setPrezzo(rs.getFloat("prezzo"));
            propostaAcquisto.setNomeProdotto(rs.getString("nome_prodotto"));
            propostaAcquisto.setURL(rs.getString("URL"));
            propostaAcquisto.setStatoProposta(rs.getString("stato_proposta"));
            propostaAcquisto.setMotivazione(rs.getString("motivazione"));
            propostaAcquisto.setVersion(rs.getLong("version"));
            propostaAcquisto.setRichiestaPresaInCarico_key(rs.getInt("richiestaPresaInCaricoID"));
            propostaAcquisto.setVersion(rs.getLong("version"));
            return propostaAcquisto;

        } catch (SQLException ex) {
            throw new DataException("Unable to create author object form ResultSet", ex);
        }
    }

    @Override
    public PropostaAcquisto getPropostaAcquisto(Integer key) throws DataException{
        PropostaAcquisto propostaAcquisto = null;
        if(dataLayer.getCache().has(PropostaAcquisto.class, key)){
            propostaAcquisto = dataLayer.getCache().get(PropostaAcquisto.class, key);
        }else{
            try {
                sPropostaAcquistoByID.setInt(1,key);
                try (ResultSet rs = sPropostaAcquistoByID.executeQuery()){
                    if(rs.next()){
                        propostaAcquisto = createPropostaAcquisto(rs);
                        dataLayer.getCache().add(PropostaAcquisto.class, propostaAcquisto);
                    }
                }
            }catch (SQLException ex){
                throw new DataException("Unable to get proposta by ID", ex);
            }
        }
        return propostaAcquisto;
    }

    @Override
    public List<PropostaAcquisto> getAllProposteByRichiestaPresaInCarico(RichiestaPresaInCarico richiestaPresaInCarico) throws DataException {
        List<PropostaAcquisto> result = new ArrayList<>();
        try {
            sProposteByRichiestaPresaInCarico.setInt(1, richiestaPresaInCarico.getKey());
            try (ResultSet rs = sProposteByRichiestaPresaInCarico.executeQuery()) {
                while (rs.next()) {
                    result.add(getPropostaAcquisto(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to get all Proposte by RichiestaPresaInCarico", ex);
        }
        return result;
    }

    @Override
    public List<PropostaAcquisto> getAllProposteDaDecidereByOrdinante(Ordinante ordinante, Integer page) throws DataException {
        List<PropostaAcquisto> result = new ArrayList<>();
        try {
            sProposteDaDecidereByOrdinante.setInt(1, ordinante.getKey());
            sProposteDaDecidereByOrdinante.setInt(2, page * offset);
            sProposteDaDecidereByOrdinante.setInt(3, offset);
            try (ResultSet rs = sProposteDaDecidereByOrdinante.executeQuery()) {
                while (rs.next()) {
                    result.add(getPropostaAcquisto(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to get all Proposte by Ordinante", ex);
        }
        return result;
    }

    @Override
    public List<PropostaAcquisto> getAllProposteByTecnico(Tecnico tecnico, Integer page) throws DataException {
        List<PropostaAcquisto> result = new ArrayList<>();
        try {
            sProposteByTecnico.setInt(1, tecnico.getKey());
            sProposteByTecnico.setInt(2, page * offset);
            sProposteByTecnico.setInt(3, offset);
            try (ResultSet rs = sProposteByTecnico.executeQuery()) {
                while (rs.next()) {
                    result.add(getPropostaAcquisto(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to get all Proposte by Tecnico", ex);
        }
        return result;
    }

    @Override
    public List<PropostaAcquisto> getAllProposteAccettate(Integer page) throws DataException {
        List<PropostaAcquisto> result = new ArrayList<>();
        try {
            sProposteAccettate.setInt(1, page * offset);
            sProposteAccettate.setInt(2, offset);
            try (ResultSet rs = sProposteAccettate.executeQuery()) {
                while (rs.next()) {
                    result.add(getPropostaAcquisto(rs.getInt("ID")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to get all Proposte accettate", ex);
        }
        return result;
    }


    @Override
    public void deletePropostaAcquisto(PropostaAcquisto propostaAcquisto) throws DataException {
        try {
            dataLayer.getCache().delete(PropostaAcquisto.class, propostaAcquisto);
            dPropostaAcquisto.setInt(1,propostaAcquisto.getKey());
            dPropostaAcquisto.executeUpdate();
        }catch (SQLException ex){
            throw new DataException("Unable to delete Proposta", ex);
        }

    }

    @Override
    public void storePropostaAcquisto(PropostaAcquisto propostaAcquisto) throws DataException {
        try{
            if(propostaAcquisto.getKey()!= null && propostaAcquisto.getKey()>0){
               if(propostaAcquisto instanceof PropostaAcquistoProxy && !((PropostaAcquistoProxy) propostaAcquisto).isModified()){
                   return;
               }
               
                uPropostaAcquisto.setString(1, propostaAcquisto.getCodiceProdotto());
                uPropostaAcquisto.setString(2, propostaAcquisto.getProduttore());
                uPropostaAcquisto.setString(3, propostaAcquisto.getNote());
                uPropostaAcquisto.setFloat(4, propostaAcquisto.getPrezzo());
                uPropostaAcquisto.setString(5, propostaAcquisto.getNomeProdotto());
                uPropostaAcquisto.setString(6, propostaAcquisto.getURL());
                uPropostaAcquisto.setString(7, propostaAcquisto.getStatoProposta());
                uPropostaAcquisto.setString(8, propostaAcquisto.getMotivazione());
                uPropostaAcquisto.setInt(9, propostaAcquisto.getRichiestaPresaInCarico().getKey());

                long current_version = propostaAcquisto.getVersion();
                long next_version = current_version + 1;

                uPropostaAcquisto.setLong(10, next_version);
                uPropostaAcquisto.setInt(11, propostaAcquisto.getKey());
                uPropostaAcquisto.setLong(12, current_version);

                if(uPropostaAcquisto.executeUpdate() == 0){
                    throw new OptimisticLockException(propostaAcquisto);
                }else {
                    propostaAcquisto.setVersion(next_version);
                }
            }else {
                
                String codiceProposta = getRandomCodiceRichiesta(10);
                while(checkCodiceProposta(codiceProposta)){
                    codiceProposta = getRandomCodiceRichiesta(10);
                }
                iPropostaAcquisto.setString(1, codiceProposta);
                iPropostaAcquisto.setString(2, propostaAcquisto.getProduttore());
                iPropostaAcquisto.setString(3, propostaAcquisto.getNote());
                iPropostaAcquisto.setFloat(4, propostaAcquisto.getPrezzo());
                iPropostaAcquisto.setString(5, propostaAcquisto.getNomeProdotto());
                iPropostaAcquisto.setString(6, propostaAcquisto.getURL());
                iPropostaAcquisto.setString(7, propostaAcquisto.getStatoProposta());
                iPropostaAcquisto.setInt(8, propostaAcquisto.getRichiestaPresaInCarico().getKey());

                if(iPropostaAcquisto.executeUpdate() == 1){
                    try(ResultSet keys = iPropostaAcquisto.getGeneratedKeys()){
                        if(keys.next()){
                            int key = keys.getInt(1);
                            propostaAcquisto.setKey(key);
                            propostaAcquisto.setCodiceProdotto(codiceProposta);
                            dataLayer.getCache().add(PropostaAcquisto.class, propostaAcquisto);
                        }
                    }
                }
            }
        }catch (SQLException ex){
            throw new DataException("Unable to store Proposta", ex);
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

    private boolean checkCodiceProposta(String codiceProposta) throws DataException {
        try {
            checkCodiceProposta.setString(1, codiceProposta);
            try (ResultSet resultSet = checkCodiceProposta.executeQuery()) {
                return resultSet.next(); // returns true if a row exists
            }
        } catch (SQLException e) {
            throw new DataException("Unable to check codice Proposta", e);
        }
    }
}
