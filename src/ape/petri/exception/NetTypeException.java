/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.exception;

import ape.petri.generic.net.EnumNetType;

/**
 * This exception is thrown if someone tries to create a malformed arc, that is, an arc
 * connecting two places or two transitions.
 * @author Gabriel
 */
public class NetTypeException extends RuntimeException {

  /**
   * Creates a new arc-related exception with the specific message.
   * @param msg a message that should describe the cause of the exception
   */
  public NetTypeException(String msg) {
    super("Wrong net type! " + msg);
  }
  
  public NetTypeException(EnumNetType expected, EnumNetType found) {
    this("Expected " + expected + ". Found " + found + ".");
  }
}
