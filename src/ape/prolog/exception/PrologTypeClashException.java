/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.prolog.exception;

import ape.prolog.Atom;

/**
 *
 * @author Gabriel
 */
public class PrologTypeClashException extends PrologException {
  
  private String variable;
  private String type1;
  private String type2;

  public PrologTypeClashException(String variable, String type1, String type2) {
    this.variable = variable;
    this.type1 = type1;
    this.type2 = type2;
  }

  public PrologTypeClashException(Atom variable, Atom type1, Atom type2) {
    this(variable.toString(), type1.toString(), type2.toString());
  }

  public String getType1() {
    return type1;
  }

  public String getType2() {
    return type2;
  }

  public String getVariable() {
    return variable;
  }

  @Override
  public String toString() {
    return "Variable '" + variable + " has inconsistent typing: '" + type1 + "' and " + type2 + "'.";
  }
}
