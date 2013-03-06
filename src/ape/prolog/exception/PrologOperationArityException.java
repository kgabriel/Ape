/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.prolog.exception;

/**
 *
 * @author Gabriel
 */
public class PrologOperationArityException extends PrologOperationException {
  
  int arity;
  int arguments;

  public PrologOperationArityException(String operation,int arity, int arguments) {
    super(operation);
    this.arity = arity;
    this.arguments = arguments;
  }

  @Override
  public String toString() {
    return "Operation '" + operation + "' has arity " + arity + ", but is provided with " + arguments + " arguments.";
  }

  public int getArguments() {
    return arguments;
  }

  public int getArity() {
    return arity;
  }
}
