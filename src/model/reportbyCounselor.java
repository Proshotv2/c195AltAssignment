/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author clink
 */
public class reportbyCounselor {
    
    public String counselor;
    public int total;

    public String getCounselor() {
        return counselor;
    }

    public int getTotal() {
        return total;
    }

    public void setCounselor(String counselor) {
        this.counselor = counselor;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public reportbyCounselor(String counselor, int total) {
        this.counselor = counselor;
        this.total = total;
    }
    
    public reportbyCounselor(){}
    
}
