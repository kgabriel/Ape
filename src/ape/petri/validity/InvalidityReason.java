/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.validity;

import ape.petri.generic.ModelElement;

/**
 *
 * @author Gabriel
 */
public interface InvalidityReason {
  
  public String getDescription();
  
  public ModelElement getLocation();
}
