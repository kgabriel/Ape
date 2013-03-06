/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.prolog;

import jpl.Term;
import jpl.Util;

/**
 *
 * @author Gabriel
 */
public class ValueTerm {

  private String termString;
  public static final String unassignedString = "";
  
  public ValueTerm(Term term) {
    termString = Prolog.termToString(term);
  }
  
  public ValueTerm(String string) {
    termString = Prolog.termToString(Util.textToTerm(string));
  }
  
  public Term jplTerm() {
    return Util.textToTerm(termString);
  }
  
  @Override
  public String toString() {
    return termString;
  }
}
