/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.prolog.exception;

/**
 *
 * @author Gabriel
 */
public class PrologTypeNotDeclaredException extends PrologException {

  String type;

  public PrologTypeNotDeclaredException(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return "Type '" + type + "' is not declared.";
  }
  
  
}
