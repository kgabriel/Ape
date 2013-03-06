/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic;

import ape.petri.generic.net.EnumElementType;
import java.io.Serializable;

/**
 *
 * @author Gabriel
 */
public interface ModelElement extends Serializable {
  
  /**
   * Get the element type of this model element.
   * @return a type defined in {@link EnumElementType} corresponding to this model element
   */
  public EnumElementType getElementType();
  
  /**
   * Get the Id of this element given by the net that contains it.
   * @return the (net-wide unique) Id of this element
   * @see Net#freeElementId()
   */
  public int getId();
}
