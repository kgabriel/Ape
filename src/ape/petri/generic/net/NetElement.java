/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

import ape.petri.generic.ModelElement;

/**
 *
 * @author Gabriel
 */
public interface NetElement extends ModelElement { 

  /**
   * Get the Id of this element given by the net that contains it.
   * @return the (net-wide unique) Id of this element
   * @see Net#freeElementId()
   */
  public int getId();

  /**
   * Get the net of this element.
   * @return the net, containing this element
   */
  public Net getNet();
}
