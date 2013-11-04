/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.prolog;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author Gabriel
 */
public class Compound {

  Atom name;
  Atom[] args;
  
  public Compound(String name, Atom[] args) {
    this.name = new Atom(name);
    this.args = new Atom[args.length];
    for(int i=0;i<args.length;i++) {
      this.args[i] = args[i];
    }
  }

  public Compound(jpl.Compound compound) {
    this.name = new Atom(compound.name());
    args = new Atom[compound.arity()];
    for(int i=1;i<=compound.arity();i++) {
      args[i-1] = new Atom(compound.arg(i-1).name());
    }
  }
  
  public jpl.Compound jplCompound() {
    return new jpl.Compound(name.toString(), Atom.jplAtoms(args));  
  }
  
  public int arity() {
    return args.length;
  }
  
  public String name() {
    return name.toString();
  }
  
  public Atom arg(int i) {
    if(i == 0) return name;
    return args[i-1];
  }
  
  public Atom[] args() {
    return args;
  }
  
  @Override
  public boolean equals(Object other) {
    if(! (other instanceof Compound)) return false;
    Compound o = (Compound) other;
    if(! o.name.equals(this.name)) return false;
    if(! (o.arity() == this.arity())) return false;
    for(int i=0;i<args.length;i++) {
      if(! o.args[i].equals(this.args[i])) return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 89 * hash + Objects.hashCode(this.name);
    hash = 89 * hash + Arrays.deepHashCode(this.args);
    return hash;
  }
  
  @Override
  public String toString() {
    String string = name.toString() + "(";
    boolean first = true;
    for(Atom arg : args) {
      if(! first) string += ", ";
      string += arg.toString();
      first = false;
    }
    return string + ")";
  }
}
