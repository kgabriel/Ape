/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.prolog.exception;

/**
 *
 * @author Gabriel
 */
public class PrologOperationNotDeclaredException extends PrologOperationException {
  
  public PrologOperationNotDeclaredException(String operation) {
    super(operation);
  }

  @Override
  public String toString() {
    return "Missing declaration for operation '" + operation + "'.";
  }
}
