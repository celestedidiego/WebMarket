/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.data.DAO.impl;

import it.univaq.webmarket.data.DAO.AmministratoreDAO;
import it.univaq.webmarket.data.model.Amministratore;
import it.univaq.webmarket.data.model.impl.proxy.AmministratoreProxy;
import it.univaq.webmarket.framework.data.DAO;
import it.univaq.webmarket.framework.data.DataException;
import it.univaq.webmarket.framework.data.DataLayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author cdidi
 */

public class AmministratoreDAO_MySQL extends DAO implements AmministratoreDAO {
    
    private PreparedStatement sAmministratoreByEmail;
    private PreparedStatement sAmministratoreByID;

    public AmministratoreDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();
            sAmministratoreByID = connection.prepareStatement("SELECT * FROM amministratore WHERE ID=?");
            sAmministratoreByEmail = connection.prepareStatement("SELECT * FROM amministratore WHERE email=?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing webmarket data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            sAmministratoreByEmail.close();
            sAmministratoreByID.close();
        } catch (SQLException ex) {
            throw new DataException("Can't destroy prepared statements", ex);
        }
        super.destroy();
    }

    @Override
    public Amministratore createAmministratore() {
        return new AmministratoreProxy(getDataLayer());
    }

    private AmministratoreProxy createAmministratore(ResultSet rs) throws DataException {

        try {
            AmministratoreProxy a = new AmministratoreProxy(getDataLayer());
            a.setKey(rs.getInt("ID"));
            a.setEmail(rs.getString("email"));
            a.setPassword(rs.getString("password"));
            return a;
        } catch (SQLException ex) {
            throw new DataException("Unable to create user object form ResultSet", ex);
        }
    }

    @Override
    public Amministratore getAmministratoreByEmail(String email) throws DataException {
        try {
            sAmministratoreByEmail.setString(1, email);
            try ( ResultSet rs = sAmministratoreByEmail.executeQuery()) {
                if (rs.next()) {
                    return createAmministratore(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to find user", ex);
        }
        return null;
    }

    @Override
    public Amministratore getAmministratoreByID(Integer id) throws DataException {
        Amministratore a = null;
        if (dataLayer.getCache().has(Amministratore.class, id)) {
            a = dataLayer.getCache().get(Amministratore.class, id);
        } else {
            try {
                sAmministratoreByID.setInt(1, id);
                try (ResultSet rs = sAmministratoreByID.executeQuery()) {
                    if (rs.next()) {
                        a = createAmministratore(rs);
                        dataLayer.getCache().add(Amministratore.class, a);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load Amministratore by ID", ex);
            }
        }
        return a;
    }
}
