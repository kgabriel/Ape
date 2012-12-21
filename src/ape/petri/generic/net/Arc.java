/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

import java.io.Serializable;

/**
 * An arc is something that connects a place and a transition and has a direction.
 * @author Gabriel
 */
public interface Arc extends Serializable {

  /**
   * Get the place of this arc.
   * @return the place that is connected to this arc
   */
  public Place getPlace();
  
  /**
   * Get the transition of this arc.
   * @return the transition that is connected to this arc
   */
  public Transition getTransition();
  
  /**
   * Get the source of this arc.
   * @return the source node of this arc
   */
  public Node getSource();
  
  /**
   * Get the target of this arc.
   * @return the target node of this arc
   */
  public Node getTarget();
  
  /**
   * Get the direction of this arc.
   * @return the enumerator object describing the direction of this arc
   */
  public EnumArcDirection getDirection();
}
