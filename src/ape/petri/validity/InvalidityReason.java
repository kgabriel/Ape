/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.validity;

/**
 *
 * @author Gabriel
 */
public class InvalidityReason {
  
  EnumInvalidityType type;
  String location;
  
  public InvalidityReason(EnumInvalidityType type, String location) {
    this.type = type;
    this.location = location;
  }
  
  public EnumInvalidityType getType() {
    return type;
  }
  
  public String getLocation() {
    return location;
  }
  
  public String toString() {
    String string = " Problem: " + type.getDescription() + "\n";
    string += " Location: " + location + "\n";
    return string;
  }
}
