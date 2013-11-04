/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.pt;

import ape.petri.generic.net.TransitionData;

/**
 *
 * @author Gabriel
 */
public class PTTransitionData extends TransitionData {
  
  /**
   * Creates a new transition data element for a P/T net, containing the specified name.
   * @param name the name of the transition, stored this data
   */
  public PTTransitionData(String name) {
    super(name);
  }
}
