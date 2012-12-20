/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.pt;

import ape.petri.generic.net.EnumNetType;
import ape.petri.generic.net.Net;
import ape.petri.validity.InvalidityReason;
import java.util.Collection;

/**
 *
 * @author Gabriel
 */
public class PTNet extends Net {
  
  public PTNet() {
    super(EnumNetType.PTNet);    
  }

  @Override
  public boolean isValid() {
    return true;
  }

  @Override
  public Collection<InvalidityReason> getInvalidityReasons() {
    return null;
  }
}
