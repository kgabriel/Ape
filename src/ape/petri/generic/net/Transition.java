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

  public Transition(Net net, TransitionData data) {
    super(net, data);
  }
  
  @Override
  public TransitionData getData() {
    return (TransitionData) super.getData();
  }

  @Override
  public EnumNetElementType getElementType() {
    return EnumNetElementType.Transition;
  }

  @Override
  public String toString() {
    return getData().name;
  }

  @Override
  public String getAMLTagName() {
    return "Transition";
  }

  @Override
  public AMLNode getAMLNode() {
    AMLNode node = super.getAMLNode();
    node.addChild(getData().getAMLNode());
    return node;
  }

  @Override
  public void readAMLNode(AMLNode node) {
    super.readAMLNode(node);
    AMLNode dataNode = node.getFirstChild("Data");
    getData().readAMLNode(dataNode);
  }
}
