/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.pt;

import ape.petri.generic.EnumNetType;
import ape.petri.generic.net.Net;
import ape.petri.validity.Validity;

/**
 *
 * @author Gabriel
 */
public class PTNet extends Net {
  
  public PTNet() {
    super(EnumNetType.PTNet);    
  }

  @Override
  public Validity validate() {
    return new Validity(true);
  }

  
}
