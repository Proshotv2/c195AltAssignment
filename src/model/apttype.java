/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author jnorr23
 */
public class apttype {
    
    /**
     * Initialize APPT Type ID
     */
    public int APTtypeID; 
    
    /**
     * Initialize Description
     */
    public String description;
    
    public int count;

    public apttype(int APTtypeID, String description, int count) {
        this.APTtypeID = APTtypeID;
        this.description = description;
        this.count = count;
    }

    public apttype() {
       
    }
    

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public int getAPTtypeID() {
        return APTtypeID;
    }

    public String getDescription() {
        return description;
    }

    public void setAPTtypeID(int APTtypeID) {
        this.APTtypeID = APTtypeID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    
    
    
}
