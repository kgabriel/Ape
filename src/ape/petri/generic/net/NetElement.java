/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

import ape.petri.generic.ModelElement;
import ape.util.aml.AMLWritable;

/**
 *
 * @author Gabriel
 */
public interface NetElement extends ModelElement, AMLWritable { 

  /**
   * Get the net of this element.
   * @return the net, containing this element
   */
  public Net getNet();
}
