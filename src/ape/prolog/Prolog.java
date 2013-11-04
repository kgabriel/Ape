/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.prolog;

import ape.Ape;
import ape.prolog.exception.PrologParserException;
import ape.util.ResourceIO;
import java.net.URL;
import java.util.*;
import jpl.Query;
import jpl.Term;
import jpl.Util;

/**
 *
 * @author Gabriel
 */
public class Prolog {
    
  static {
    consultResource("prolog/modules/net.pl");
    consultResource("prolog/modules/data.pl");
    consultResource("prolog/modules/wave.pl");
  }
  
  public static boolean consultFile(String fileName) {
    Query q = new Query("consult", new Term[] { new jpl.Atom(fileName)});
    return q.hasSolution();
  }
  
  public static boolean consultResource(String res) {
    return consultFile(ResourceIO.getResourcePath(res));
  }
    
  public static Atom[] parseAtomSequence(String input) {
    String[] elements = input.split(",");
    Atom[] atoms = new Atom[elements.length];
    for(int i=0;i<elements.length;i++) {
      atoms[i] = new Atom(elements[i].trim());
    }
    return atoms;
  }
  
  public static Collection<Compound> parseCompoundSequence(String input, boolean tolerant) {
    String string = input.trim();
    ArrayList<Compound> terms = new ArrayList<>();
    while(string.length() > 0) {
      int openPar = string.indexOf('(');
      int comma = string.indexOf(',');
      if(comma != -1 && comma < openPar) {
        try {
          terms.add(parseCompound(string.substring(0,comma)));
        } catch(PrologParserException e1) {
          System.err.println(e1);
          if(! tolerant) throw e1;
        }
        string = string.substring(comma + 1).trim();
      } else {
        int closePar = string.indexOf(')');
        if(openPar != -1 && closePar == -1) {
          System.err.println("stop.");
          throw new PrologParserException(string, openPar, "Input ended before ')'");
        }
        comma = string.indexOf(',',closePar);
        if(comma != -1) {
          try {
            terms.add(parseCompound(string.substring(0,comma)));
          } catch(PrologParserException e2) {
          System.err.println(e2);
            if(! tolerant) throw e2;
          }
          string = string.substring(comma + 1).trim();
        } else {
          try {
            terms.add(parseCompound(string));
          } catch(PrologParserException e3) {
            System.err.println(e3);
            if(! tolerant) throw e3;
          }

          string = "";
        }
      }
    }
    return terms;
  }
  
  public static Map<Atom,ValueTerm> parseAssignmentSequence(String input) {
    Map<Atom,ValueTerm> assignment = new HashMap<>();
    Term[] eqns = Util.listToTermArray(Util.textToTerm("[" + input + "]"));
    for(int i=0;i<eqns.length;i++) {
      assignment.put(new Atom(eqns[i].arg(1).toString()), new ValueTerm(eqns[i].arg(2)));
    }
    return assignment;
  }
  
  public static Compound parseCompound(String input) {
    String string = input.trim();
    int firstPar = string.indexOf('(');
    if(firstPar < 0) {
      return parseEquation(string);
    } else if(firstPar < 1) {
      throw new PrologParserException(string, firstPar, "Identifier expected.");
    } 
    int lastPar = string.indexOf(')');
    if(lastPar < 0) {
      throw new PrologParserException(string, firstPar, "Input ended before ')'.");
    } else if(lastPar < firstPar) {
      throw new PrologParserException(string, lastPar, "Unexpected ')'.");
    } else if(lastPar < string.length()-1) {
      throw new PrologParserException(string, lastPar, "Reached end of term before end of input.");
    }
    String pred = string.substring(0, firstPar);

    String argString = string.substring(firstPar + 1, lastPar);
    String[] argStrings = argString.split(",");
    if(argStrings.length == 0) {
      throw new PrologParserException(input, firstPar + 1, "Expected at least one argument for '" + pred + "'.");
    }
    
    Atom[] args = new Atom[argStrings.length];
    for(int i=0;i<argStrings.length;i++) {
      args[i] = new Atom(argStrings[i].trim());
    }
    return new Compound(pred, args);
  }
  
  private static Compound parseEquation(String input) {
    int eqPos = input.indexOf('=');
    if(eqPos < 0) {
      throw new PrologParserException(input, input.length(), "Expected predicate =/2.");
    }
    int commaPos = input.indexOf(',');
    if(commaPos == -1) commaPos = input.length();
    Atom lhs = new Atom(input.substring(0, eqPos).trim());
    Atom rhs = new Atom(input.substring(eqPos + 1, commaPos).trim());
    return new Compound("=", new Atom[] {lhs, rhs});
  }
  
  public static String toString(Collection objects) {
    String sequence = "";
    for(Object o : objects) {
      if(sequence.length() > 0) sequence += ",\n";
      sequence += o.toString();
    }
    return sequence;
  }
  
  public static String termSequenceToString(Term[] terms) {
    String sequence = "";
    for(Term t : terms) {
      if(sequence.length() > 0) sequence += ", ";
      sequence += termToString(t);
    }
    return sequence;
  }
  
  public static String termToString(Term t) {
    if(t.isCompound() && t.arity() > 0) {
      jpl.Compound compound = (jpl.Compound) t;
      if(compound.name().equals(".")) {
        List<String> elements = new ArrayList<>();
        String end = parseListTerm(compound, elements);
        String string = "[";
        boolean first = true;
        for(String e : elements) {
          if(! first) string += ", ";
          string += e;
          first = false;
        }
        if(end != null) string += "| " + end;
        string += "]";
        return string;
      } else {
        return compound.toString();//compound.name() + "(" + termSequenceToString(compound.args()) + ")";
      }
    } else {
      return t.toString();
    }
  }
  
  private static String parseListTerm(Term t, List<String> elements) {
    elements.add(termToString(t.arg(1)));
    Term rest = t.arg(2);
    if(rest.isCompound() && rest.name().equals(".")) {
      return parseListTerm(rest, elements);
    }
    if(rest.toString().equals("[]")) return null;
    return termToString(rest);
  }
}
