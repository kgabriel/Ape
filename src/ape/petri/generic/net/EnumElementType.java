/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

/**
 *
 * @author Gabriel
 */
public enum EnumElementType {

  ArcElement("ArcElement"),
  ArcCollection("ArcCollection"),
  Place("Place"),
  Transition("Transition");
  
  String name;
  
  EnumElementType(String name) {
    this.name = name;
  }
  
  @Override
  public String toString() {
    return name;
  }
}
