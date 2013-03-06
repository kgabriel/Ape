/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.validity;

/**
 *
 * @author Gabriel
 */
public enum EnumInvalidityType {

  InstPlaceUnassigned("Instantiation contains an unassigned place."),
  InstTransitionAssignmentPartial("Instantiation contains transition with only partial assignment."),
  InstVarTypeClash("Instantiation contains variable with inconsistent types."),
  InstOperationNotDeclared("Instantiation contains undeclared operation call."),
  InstOperationArity("Instantiation contains operation call with wrong number of arguments.");
  
  private String description;

  private EnumInvalidityType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
