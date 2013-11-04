/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.exception;

import ape.petri.generic.net.EnumNetElementType;

/**
 * This exception is thrown if someone tries to create a malformed arc, that is, an arc
 * connecting two places or two transitions.
 * @author Gabriel
 */
public class ElementTypeException extends RuntimeException {

  /**
   * Creates a new arc-related exception with the specific message.
   * @param msg a message that should describe the cause of the exception
   */
  public ElementTypeException(String msg) {
    super("Wrong element type! " + msg);
  }
  
  public ElementTypeException(EnumNetElementType expected, EnumNetElementType found) {
    this("Expected " + expected + ". Found " + found + ".");
  }
}
