/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape;

import java.util.Hashtable;
import jpl.Query;
import jpl.Term;
import jpl.Util;
import jpl.Variable;

/**
 *
 * @author Gabriel
 */
public class PLTest {

  
  public static void test() {
    Query q = new Query("op_declaration", new Term[] { new jpl.Atom("next"), new Variable("X") });
    if(! q.hasMoreElements()) {
      System.out.println("'next' is not defined properly...");
    } else {
      Hashtable s = (Hashtable) q.nextElement();
      Term[] x = Util.listToTermArray((Term) s.get("X"));
      System.out.println("X = " + x);
    }
  }
}
