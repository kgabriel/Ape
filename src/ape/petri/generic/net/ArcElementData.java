/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

import ape.petri.generic.EnumNetType;

/**
 * This interface is used to define the data carried by an {@link ArcElement}.
 * @author Gabriel
 * @see ArcCollectionData
 */
public abstract class ArcElementData extends Data {
  
  public ArcElementData(EnumNetType netType) {
    super(netType, EnumElementType.ArcElement);
  }
  
  /**
   * Checks if this arc data is compatible with another one.
   * @param ad the arc data to be compared with
   * @return <code>true</code> if this arc data is compatible with <code>ad</code>
   */
  public abstract boolean isCompatibleWith(ArcElementData ad);
}
