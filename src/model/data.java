/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.collections.ObservableList;

/**
 *
 * @author clink
 */
public class data {
    
    public String state;
    public int total;

    public String getState() {
        return state;
    }

    public int getTotal() {
        return total;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public data(String state, int total) {
        this.state = state;
        this.total = total;
    }
    
    public data(){
        
    }
    
}
