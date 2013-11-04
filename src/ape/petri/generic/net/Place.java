/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

import ape.util.aml.AMLNode;


/**
 * <code>Place</code> is the abstract super class of all place types. 
 * It is a {@link Node} with a {@link PlaceData} element.
 * For each implemented net type there should be a subclass of this class,
 * defining and handling the concrete <code>PlaceData</code> of that type.
 * @author Gabriel
 */
public class Place extends Node {
  
  /**
   * A new Place in the given net, with the specified data.
   * @param net the net to contain this place
   * @param data the data of this place
   */
  public Place(Net net, PlaceData data) {
    super(net, data);
  }
  
  /**
   * Get the data of this place.
   * @return the place's <code>data</code>-object
   */
  @Override
  public PlaceData getData() {
    return (PlaceData) super.getData();
  }
  
  @Override
  public EnumNetElementType getElementType() {
    return EnumNetElementType.Place;
  }

  @Override
  public String toString() {
    return getData().name;
  }

  @Override
  public String getAMLTagName() {
    return "Place";
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
