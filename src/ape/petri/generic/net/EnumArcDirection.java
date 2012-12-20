/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

/**
 * The elements of this enumeration describe the two possible directions of a Petri net arc.
 * That is, an arc can go from a place to a transition ({@link EnumArcDirection#PT}),
 * or from a transition to a place ({@link EnumArcDirection#TP}).
 * @author Gabriel
 */
public enum EnumArcDirection {
  /** direction from a place to a transition */
  PT(true),
  /** direction from a transition to a place */
  TP(false);
  
  /** Internal representation of the direction, as a flag whether 
   * the arc in question goes from a place to a transition.
   */
  private final boolean placeToTransition;
  
  /**
   * Constructor for an arc direction. If the argument is <code>true</code>, the arc
   * goes from a place to a transition. Otherwise, the arc goes from a transition to
   * a place
   * @param pToT a boolean value, whether the arc in question goes from a place to
   * a transition
   */
  private EnumArcDirection(boolean pToT) {
    placeToTransition = pToT;
  }
  
  /**
   * Returns whether the target of an arc with this direction is a transition.
   * @return <code>true</code> if direction is {@link EnumArcDirection#PT},
   * <code>false</code> if direction is {@link EnumArcDirection#TP}
   */
  public boolean goesToTransition() {
    return placeToTransition;
  }
  
  /**
   * Returns whether the target of an arc with this direction is a place.
   * @return <code>true</code> if direction is {@link EnumArcDirection#TP},
   * <code>false</code> if direction is {@link EnumArcDirection#PT}
   */
  public boolean goesToPlace() {
    return ! placeToTransition;
  }
  
  @Override
  public String toString() {
    if(placeToTransition) {
      return "place to transition";
    } else {
      return "transition to place";
    }
  }
}
