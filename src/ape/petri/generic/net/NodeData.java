/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

/**
 *
 * @author Gabriel
 */
public abstract class NodeData extends Data {

  protected String name;
  
  public NodeData(EnumNetType netType, EnumElementType elementType, String name) {
    super(netType, elementType);
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
    dataHasChanged();
  }
}
