/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.prolog.exception;

/**
 *
 * @author Gabriel
 */
public class PrologOperationException extends PrologException {
  
  String operation;

  public PrologOperationException(String operation) {
    this.operation = operation;
  }

  public String getOperation() {
    return operation;
  }
}
