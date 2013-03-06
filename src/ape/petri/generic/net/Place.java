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
  
  /** the data of this place */
  protected PlaceData data;
  
  /**
   * A new Place in the given net, with the specified data.
   * @param net the net to contain this place
   * @param data the data of this place
   */
  public Place(Net net, PlaceData data) {
    super(net);
    this.data = data;
  }
  
  /**
   * Get the data of this place.
   * @return the place's <code>data</code>-object
   */
  public PlaceData getData() {
    return data;
  }
  
  /**
   * Set the data of this place.
   * @param data the data to be set
   */
  public void setData(PlaceData data) {
    this.data = data;
  }

  @Override
  public EnumElementType getElementType() {
    return EnumElementType.Place;
  }

  @Override
  public String toString() {
    return data.name;
  }

  @Override
  public String getAMLTagName() {
    return "Place";
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
