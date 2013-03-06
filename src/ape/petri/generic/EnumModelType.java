/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic;

import ape.petri.ahl.AHLNet;
import ape.petri.ahl.instantiation.AHLInstantiation;
import ape.petri.generic.net.Net;
import ape.petri.pt.PTNet;

/**
 *
 * @author Gabriel
 */
public enum EnumModelType {

  Net("Net", "net"),
  Rule("Rule", "rule");

  private String name;
  private String fileExtension;
        
  EnumModelType(String name, String fileExtension) {
    this.name = name;
    this.fileExtension = fileExtension;
  }

  public String getName() {
    return name;
  }

  public String getFileExtension() {
    return fileExtension;
  }
  
  public Model createModel(EnumNetType netType) {
    switch(this) {
      case Net:
        return createNet(netType);
      case Rule:
      default:
        throw new UnsupportedOperationException("Model creation not yet implemented for model type " + this);
    }
  }
  
  public Net createNet(EnumNetType netType) {
    switch(netType) {
      case PTNet:
        return new PTNet();
      case AHLNet:
        return new AHLNet();
      case AHLInstantiation:
        return new AHLInstantiation();
      default:
        throw new UnsupportedOperationException("Net creation not yet implemented for net type " + netType);
    }
  }
  
}
