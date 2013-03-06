/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.ahl;

import ape.petri.generic.EnumNetType;
import ape.petri.generic.net.Net;
import ape.petri.validity.Validity;

/**
 *
 * @author Gabriel
 */
public class AHLNet extends Net {
  
  public AHLNet() {
    super(EnumNetType.AHLNet);    
  }

  @Override
  public Validity validate() {
    return new Validity(true);
  }
}
