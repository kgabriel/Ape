/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic;

import ape.petri.generic.net.Data;
import ape.petri.generic.net.EnumNetElementType;
import ape.util.aml.AMLWritable;

/**
 *
 * @author Gabriel
 */
public interface ModelElement extends AMLWritable {
  
  /**
   * Get the element type of this model element.
   * @return a type defined in {@link EnumNetElementType} corresponding to this model element
   */
  public EnumNetElementType getElementType();
  
  public EnumNetType getNetType();
  
  /**
   * Get the Id of this element given by the <code>Model</code> that contains it.
   * @return the (model-wide unique) Id of this element
   * @see Model#freeElementId()
   */
  public int getId();
  
  /**
   * Get the data of this model element.
   * @return the data element, containing all (additional) data of this element
   */
  public Data getData();
}
