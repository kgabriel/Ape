/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

import ape.petri.pt.PTDataFactory;
import ape.petri.pt.PTNet;

/**
 *
 * @author Gabriel
 */
public enum EnumNetType {
  
  PTNet("P/T net"),
  AHLNet("AHL net");
  
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
      default:
        throw new UnsupportedOperationException("Data Factory not yet implemented for net type" + this);
    }
  }
}
