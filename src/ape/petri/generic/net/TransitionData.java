/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

import ape.petri.generic.EnumNetType;

/**
 * This interface is used to define the data carried by a transition.
 * @author Gabriel
 */
public abstract class TransitionData extends NodeData {

  public TransitionData(EnumNetType netType, String name) {
    super(netType, EnumElementType.Transition, name);
  }
  
  /**
   * Checks if this transition data is compatible with another one.
   * @param td the transition data to be compared with
   * @return <code>true</code> if this arc data is compatible with <code>td</code>
   */
  public abstract boolean isCompatibleWith(TransitionData td);
}
