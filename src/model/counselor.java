/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ComboBox;

/**
 *
 * @author jnorr23
 */
public class counselor {
    
    
    /**
     * Initialize Counselor Name
     */
    public String cName;
    
        /**
     * Initialize Counselor Password
     */
    public String cPass;
    
    /**
     * Initialize Counselor Pin
     */
    public int cPin;

    public String getcName() {
        return cName;
    }

    public String getcPass() {
        return cPass;
    }

    public int getcPin() {
        return cPin;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public void setcPass(String cPass) {
        this.cPass = cPass;
    }

    public void setcPin(int cPin) {
        this.cPin = cPin;
    }

    public counselor(String cName, String cPass, int cPin) {
        this.cName = cName;
        this.cPass = cPass;
        this.cPin = cPin;
    }


    public counselor(){}

    
    
    
}
