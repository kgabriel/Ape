/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.prolog;

import java.io.Serializable;

/**
 *
 * @author Gabriel
 */
public class Atom implements Serializable {

  private String name;
  public static final Atom unknown_type = new Atom("type_unknown");
  
  public Atom(String string) {
    name = new jpl.Atom(string).name();
  }
  
  public Atom(jpl.Atom atom) {
    this.name = atom.name();
  }
  
  public jpl.Atom jplAtom() {
    return new jpl.Atom(name);
  }
  
  public static jpl.Atom[] jplAtoms(Atom[] atoms) {
    jpl.Atom[] jplAtoms = new jpl.Atom[atoms.length];
    for(int i=0;i<atoms.length;i++) {
      jplAtoms[i] = atoms[i].jplAtom();
    }
    return jplAtoms;
  }
  
  @Override
  public boolean equals(Object other) {
    if(! (other instanceof Atom)) return false;
    Atom o = (Atom) other;
    return this.name.equals(o.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public String toString() {
    return name;
  }
}
