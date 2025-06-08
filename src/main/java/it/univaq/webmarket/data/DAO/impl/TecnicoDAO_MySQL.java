/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.DAO.impl;

import it.univaq.webmarket.data.DAO.TecnicoDAO;
import it.univaq.webmarket.data.model.Tecnico;
import it.univaq.webmarket.data.model.impl.proxy.TecnicoProxy;
import it.univaq.webmarket.framework.data.*;
import it.univaq.webmarket.framework.security.SecurityHelpers;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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

public class TecnicoDAO_MySQL extends DAO implements TecnicoDAO {
    
    private Integer offset = 5;

    private PreparedStatement sTecnicoByID;
    private PreparedStatement sTecnicoByEmail;
    private PreparedStatement iTecnico;
    private PreparedStatement sTecnico;
    private PreparedStatement uTecnico;
    private PreparedStatement dTecnico;

    public TecnicoDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();
            sTecnicoByID = connection.prepareStatement("SELECT * FROM tecnico WHERE ID=?");
            sTecnicoByEmail = connection.prepareStatement("SELECT * FROM tecnico WHERE email=?");
            iTecnico = connection.prepareStatement("INSERT INTO tecnico(email, password) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            sTecnico = connection.prepareStatement("SELECT ID FROM tecnico LIMIT ?, ?");
            uTecnico = connection.prepareStatement("UPDATE tecnico SET email=?,password=?,version=? WHERE ID=? and version=?");
            dTecnico = connection.prepareStatement("DELETE FROM tecnico WHERE ID=?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing webmarket data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            sTecnicoByID.close();
            sTecnicoByEmail.close();
            iTecnico.close();
            sTecnico.close();
            uTecnico.close();
            dTecnico.close();
        } catch (SQLException ex) {
            throw new DataException("Can't destroy prepared statements", ex);
        }
        super.destroy();
    }

    @Override
    public Tecnico createTecnico() {
        return new TecnicoProxy(getDataLayer());
    }

    private TecnicoProxy createTecnico(ResultSet rs) throws DataException {
        try {
            TecnicoProxy t = (TecnicoProxy) createTecnico();
            t.setKey(rs.getInt("ID"));
            t.setEmail(rs.getString("email"));
            t.setPassword(rs.getString("password"));
            t.setVersion(rs.getLong("version"));
            return t;
        } catch (SQLException ex) {
            throw new DataException("Unable to create Tecnico object form ResultSet", ex);
        }
    }

    @Override
    public Tecnico getTecnico(int tecnico_key) throws DataException {
        Tecnico t = null;
        if (dataLayer.getCache().has(Tecnico.class, tecnico_key)) {
            t = dataLayer.getCache().get(Tecnico.class, tecnico_key);
        } else {
            try {
                sTecnicoByID.setInt(1, tecnico_key);
                try (ResultSet rs = sTecnicoByID.executeQuery()) {
                    if (rs.next()) {
                        t = createTecnico(rs);
                        dataLayer.getCache().add(Tecnico.class, t);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load Tecnico by ID", ex);
            }
        }
        return t;
    }

    @Override
    public List<Tecnico> getAllTecnico(Integer page) throws DataException {
        List<Tecnico> result = new ArrayList<>();
        try {
            sTecnico.setInt(1, page  * offset);
            sTecnico.setInt(2, offset);
            try (ResultSet rs = sTecnico.executeQuery()) {
                while (rs.next()) {
                    result.add(getTecnico(rs.getInt("ID")));
                }
            }
            return result;
        } catch (SQLException ex) {
            throw new DataException("Unable to load Tecnico", ex);
        }
    }

    @Override
    public Tecnico getTecnicoByEmail(String email) throws DataException {
        try {
            sTecnicoByEmail.setString(1, email);
            try (ResultSet rs = sTecnicoByEmail.executeQuery()) {
                if (rs.next()) {
                    return getTecnico(rs.getInt("ID"));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to find Tecnico", ex);
        }
        return null;
    }

    @Override
    public void storeTecnico(Tecnico tecnico) throws DataException {
        try {
            if (tecnico.getKey() != null && tecnico.getKey() > 0) {
                if (tecnico instanceof DataItemProxy && !((DataItemProxy) tecnico).isModified()) {
                    //Se non è stato modificato semplicemente non faccio nulla
                    return;
                }
                uTecnico.setString(1, tecnico.getEmail());
                uTecnico.setString(2, SecurityHelpers.getPasswordHashPBKDF2(tecnico.getPassword()));

                long current_version = tecnico.getVersion();
                long next_version = current_version + 1;

                uTecnico.setLong(3, next_version);
                uTecnico.setInt(4, tecnico.getKey());
                uTecnico.setLong(5, current_version);

                if (uTecnico.executeUpdate() == 0) {
                    throw new OptimisticLockException(tecnico);
                } else {
                    tecnico.setVersion(next_version);
                }
            } else {
                iTecnico.setString(1, tecnico.getEmail());
                iTecnico.setString(2, SecurityHelpers.getPasswordHashPBKDF2(tecnico.getPassword()));

                if (iTecnico.executeUpdate() == 1) {
                    try (ResultSet keys = iTecnico.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            tecnico.setKey(key);
                            dataLayer.getCache().add(Tecnico.class, tecnico);
                        }
                    }
                }
            }
            if (tecnico instanceof DataItemProxy) {
                ((DataItemProxy) tecnico).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store Tecnico", ex);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTecnico(Tecnico tecnico) throws DataException {
        try {

            //Lo cancello prima dalla cache
            dataLayer.getCache().delete(Tecnico.class, tecnico);

            dTecnico.setInt(1, tecnico.getKey());
            dTecnico.executeUpdate();

        } catch(SQLException e) {
            throw new DataException("Unable to delete Tecnico", e);
        }

    }
}
