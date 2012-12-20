/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

/**
 * This interface is used to define the data carried by a place.
 * @author Gabriel
 */
public abstract class PlaceData extends NodeData {

  public PlaceData(EnumNetType netType, String name) {
    super(netType, EnumElementType.Place, name);
  }
  
  /**
   * Checks if this place data is compatible with another one.
   * @param pd the place data to be compared with
   * @return <code>true</code> if this arc data is compatible with <code>pd</code>
   */
  public abstract boolean isCompatibleWith(PlaceData pd);
  
  
}
