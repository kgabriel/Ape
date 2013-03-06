/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic;

import ape.petri.ahl.AHLDataFactory;
import ape.petri.ahl.instantiation.AHLInstDataFactory;
import ape.petri.generic.net.DataFactory;
import ape.petri.generic.net.Net;
import ape.petri.pt.PTDataFactory;

/**
 *
 * @author Gabriel
 */
public enum EnumNetType {
  
  PTNet("P/T"),
  AHLNet("AHL"),
  AHLInstantiation("Instantiated AHL");
  
  private String name;

  EnumNetType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }
  
  public DataFactory createDataFactory(Net net) {
    switch(this) {
      case PTNet:
        return new PTDataFactory(net);
      case AHLNet:
        return new AHLDataFactory(net);
      case AHLInstantiation:
        return new AHLInstDataFactory(net);
      default:
        throw new UnsupportedOperationException("Data Factory not yet implemented for net type" + this);
    }
  }
}
