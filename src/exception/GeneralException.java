/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;


import model.appointment;
import model.counselor;
import model.patient;

/**
 *
 * @author clink
 */
public class GeneralException extends Exception{
    
    public static String appointmentException(String name, String patient, String type, String notes, String start, String end, String errormessage){
    
        if (name == null){
            errormessage = errormessage + "Counselor Name must not be Null";
        }
        if (patient == null){
            errormessage = errormessage + "Patient Name must not be Null";
        }
        if (type == null){
            errormessage = errormessage + "Type of Appointment must not be Null";
        }
        if (notes == null){
            errormessage = errormessage + "Please enter some notes!";
        }
        if (start == null){
            errormessage = errormessage + "You must pick a start time";
        }
        if (end == null){
            errormessage = errormessage + "You must pick an end time";
        }
        
        return errormessage;
    
    }
    
    public static String timeException(int start, int end, String ErrorMessage){
        
        if(start > end){
            ErrorMessage = ErrorMessage + "Start time must come before End Time";
        }
        
        return ErrorMessage;
    }
    
}
