/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

import ape.util.aml.AMLNode;

/**
 *
 * @author Gabriel
 */
public class Transition extends Node {
  /** the data of this transition */
  protected TransitionData data;

  public Transition(Net net, TransitionData data) {
    super(net);
    this.data = data;
  }
  
  public TransitionData getData() {
    return data;
  }

  public void setData(TransitionData data) {
    this.data = data;
  }

  @Override
  public EnumElementType getElementType() {
    return EnumElementType.Transition;
  }

  @Override
  public String toString() {
    return data.name;
  }

  @Override
  public String getAMLTagName() {
    return "Transition";
  }

  @Override
  public AMLNode getAMLNode() {
    AMLNode node = super.getAMLNode();
    node.addChild(data.getAMLNode());
    return node;
  }

  @Override
  public void readAMLNode(AMLNode node) {
    super.readAMLNode(node);
    AMLNode dataNode = node.getFirstChild("Data");
    data.readAMLNode(dataNode);
  }
}
