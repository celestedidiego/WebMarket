/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package it.univaq.webmarket.framework.data;

/**
 *
 * @author cdidi
 */

public class OptimisticLockException extends DataException {
    
    private DataItem item;

    public OptimisticLockException(DataItem item) {
        super("Version mismatch (optimistic locking) for instance " + item.getKey() + " of class " + item.getClass().getCanonicalName());
        this.item = item;
    }

    /**
     * @return the item
     */
    public DataItem getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(DataItem item) {
        this.item = item;
    }
}
