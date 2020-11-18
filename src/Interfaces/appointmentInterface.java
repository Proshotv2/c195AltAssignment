/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import javafx.collections.ObservableList;
import model.appointment;

/**
 *
 * @author clink
 */
public interface appointmentInterface {
    
    void insertAppointment(appointment a);
    void updateAppointment(appointment a);
    void deleteAppointment(appointment a);
    ObservableList<appointment> getAllMonthly();
    ObservableList<appointment> getAllWeekly();
    ObservableList<appointment> getAllbiWeekly();
    
}
