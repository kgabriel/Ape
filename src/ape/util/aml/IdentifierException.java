/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.util.aml;

/**
 *
 * @author Gabriel
 */
public class IdentifierException extends RuntimeException {

  public IdentifierException(String name) {
    super("Illegal identifier '" + name + "'.");
  }
  
  
}
