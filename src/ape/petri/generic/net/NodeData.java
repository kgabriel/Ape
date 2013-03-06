/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

import ape.petri.generic.EnumNetType;
import ape.util.aml.AMLNode;

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

  @Override
  public AMLNode getAMLNode() {
    AMLNode node = super.getAMLNode();
    node.putAttribute("name", name);
    return node;
  }

  @Override
  public void readAMLNode(AMLNode node) {
    super.readAMLNode(node);
    this.name = node.getAttribute("name");
  }
  
  
}
