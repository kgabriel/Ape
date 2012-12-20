/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.pt;

import ape.petri.generic.net.EnumNetType;
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
    super(EnumNetType.PTNet, name);
  }

  /**
   * Checks compatibility of this place data with another one.
   * Since transitions in a P/T net do not have any data other than their name that is not relevant
   * for compatibility, the transition data is always compatible.
   * @param td the transition data to check compatibility with
   * @return always <code>true</code>
   */
  @Override
  public boolean isCompatibleWith(TransitionData td) {
    return true;
  }
}
