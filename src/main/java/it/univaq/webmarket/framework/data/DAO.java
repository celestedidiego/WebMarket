/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.framework.data;

import java.sql.Connection;

/**
 *
 * @author cdidi
 */

public class DAO {
    
    protected final DataLayer dataLayer;
    protected final Connection connection;

    public DAO(DataLayer d) {
        this.dataLayer = d;
        this.connection = d.getConnection();
    }

    protected DataLayer getDataLayer() {
        return dataLayer;
    }

    protected Connection getConnection() {
        return connection;
    }

    public void init() throws DataException {

    }

    public void destroy() throws DataException {

    }
}
