/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic;

/**
 *
 * @author Gabriel
 */
public enum EnumModelType {

  Net("Net"),
  Rule("Rule");

  private String name;
        
  EnumModelType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
