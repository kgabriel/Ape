/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.pt;

import ape.petri.generic.net.ArcCollection;
import ape.petri.generic.net.ArcElement;
import ape.petri.generic.net.ArcElementData;
import ape.petri.generic.net.EnumNetType;

/**
 * This class describes the data, an {@link ArcElement} in a P/T net is equipped with. 
 * Since P/T nets do not have any arc inscriptions other than an arc weight, this class
 * is empty. The weight of an arc is modeled by the number of {@link ArcElement}s
 * contained in an {@link ArcCollection}.
 * @author Gabriel
 */
public class PTArcElementData extends ArcElementData {

  /**
   * Creates a new empty arc data element for a P/T net.
   */
  public PTArcElementData() {
    super(EnumNetType.PTNet);
  }

  /**
   * Checks compatibility of this arc data with another one.
   * Since arcs in a P/T net do not have any data, the arc data is always compatible.
   * @param ad the arc data to check compatibility with
   * @return always <code>true</code>
   */
  @Override
  public boolean isCompatibleWith(ArcElementData ad) {
    return true;
  }  
}