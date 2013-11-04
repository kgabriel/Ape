/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic;

import ape.petri.generic.net.Data;

/**
 *
 * @author Gabriel
 */
public interface DataChangeListener {
  
  public void dataChanged(Data changedData);
}
