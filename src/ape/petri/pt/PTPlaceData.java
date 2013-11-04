/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.pt;

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
    super(name);  
  }

}
