/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.framework.data;

/**
 *
 * @author cdidi
 */

public class DataException extends Exception {
    
    public DataException(String message) {
        super(message);
    }

    public DataException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage() + (getCause() != null ? " (" + getCause().getMessage() + ")" : "");
    }
}
