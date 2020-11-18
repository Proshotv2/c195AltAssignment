/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author jnorr23
 */
public class appointment {
    
    public int apt_id;
    public String ptName;
    public String description;
    public String notes;
    private Timestamp startDateTime;

    public appointment(int apt_id, String ptName, String description, String notes, Timestamp startDateTime) {
        this.apt_id = apt_id;
        this.ptName = ptName;
        this.description = description;
        this.notes = notes;
        this.startDateTime = startDateTime;
    }

    public appointment() {
        
    }

    public int getApt_id() {
        return apt_id;
    }

    public String getPtName() {
        return ptName;
    }

    public String getDescription() {
        return description;
    }

    public String getNotes() {
        return notes;
    }

    public Timestamp getStartDateTime() {
        return startDateTime;
    }

    public void setApt_id(int apt_id) {
        this.apt_id = apt_id;
    }

    public void setPtName(String ptName) {
        this.ptName = ptName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setStartDateTime(Timestamp startDateTime) {
        this.startDateTime = startDateTime;
    }

    
  
}