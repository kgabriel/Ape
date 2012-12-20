/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.exception;

import ape.petri.generic.net.Net;

/**
 * This exception is thrown if someone tries to create a malformed arc, that is, an arc
 * connecting two places or two transitions.
 * @author Gabriel
 */
public class ArcException extends RuntimeException {

  /**
   * Creates a new arc-related exception with the specific message.
   * @param msg a message that should describe the cause of the exception
   */
  public ArcException(String msg) {
    super(msg);
  }
  
  public ArcException(Net net1, Net net2) {
    super("Invalid attempt to connect nodes in different nets with an arc:"
              + Character.LINE_SEPARATOR + " Net 1: " + net1 
              + Character.LINE_SEPARATOR + " Net 2: " + net2);
  }
}
