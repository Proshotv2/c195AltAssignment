/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author jnorr23
 */
public class AddressExceptionInvalid extends Exception {
    public AddressExceptionInvalid(String message) {
        super(message);
    }
}
