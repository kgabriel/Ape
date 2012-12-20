/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.pt;

import ape.petri.generic.net.EnumNetType;
import ape.petri.generic.net.PlaceData;

/**
 *
 * @author Gabriel
 */
public class PTPlaceData extends PlaceData {
  
  /**
   * Creates a new place data element for a P/T net, containing the specified name.
   * @param name the name of the place, stored in this data
   */
  public PTPlaceData(String name) {
    super(EnumNetType.PTNet,name);  
  }

  /**
   * Checks compatibility of this place data with another one.
   * Since places in a P/T net do not have any data other than their name that is not relevant
   * for compatibility, the place data is always compatible.
   * @param pd the place data to check compatibility with
   * @return always <code>true</code>
   */
  @Override
  public boolean isCompatibleWith(PlaceData pd) {
    return true;
  }  
}
