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
public class patient {
    
    /**
     * Initialize pt_id
     */
    public int pt_id;
    
    
    /**
     * Initialize ptName
     */
    public String ptName;
    
    
    /**
     * Initialize inspr
     */
    public String inspr;
    
    /**
     * Initialize addressId FK
     */
    public int addressId;
    
    
    //Initialize address1 field
    public String address1;
    
    //Initialize address2 field
    public String address2;
    
    //Initialize city field
    public String city;
    
    //Initialize state field
    public String state;
    
    //Initialize postal code
    public String postalCode;
    
    //Initialize Phone
    public String phone;
    
    
    //Add delete and update patient records

    public int getPt_id() {
        return pt_id;
    }

    public void setPt_id(int pt_id) {
        this.pt_id = pt_id;
    }

    public String getPtName() {
        return ptName;
    }

    public void setPtName(String ptName) {
        this.ptName = ptName;
    }

    public String getInspr() {
        return inspr;
    }

    public void setInspr(String inspr) {
        this.inspr = inspr;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    

    public patient(int pt_id, int addressId, String ptName, String inspr,String address1, String city, String state, String postalCode, String phone) {
        this.pt_id = pt_id;
        this.addressId = addressId;
        this.ptName = ptName;
        this.inspr = inspr;
        this.address1 = address1;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.phone = phone;
    }
    
    
}
