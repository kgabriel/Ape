/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

import ape.util.aml.AMLNode;

/**
 * An <code>ArcElement</code> is a single arc between a place and a transition.
 * The <code>ArcElement</code> is always contained in an {@link ArcCollection},
 * and operations on <code>ArcElement</code>s should always be handled only
 * by the corresponding {@link ArcCollection} or {@link Net}.
 * @author Gabriel
 */
public class ArcElement extends NetElement implements Arc {
  
  /** the parent collection containing this element */
  protected ArcCollection collection;
  
  /**
   * A new arc element inside the specified collection. The element naturally
   * belongs to the collection's net.
   * @param col the parent collection of this element
   */
  public ArcElement(ArcCollection col, ArcElementData data) {
    super(col.net, data);
    this.collection = col;
  }
  
 /**
   * Returns a simple arc with the same place, transition and direction of this arc element's collection.
   * @return a simple arc, representing this arc
   */
  public SimpleArc toSimpleArc() {
    return new SimpleArc(getPlace(), getTransition(), getDirection());
  }
  

  /**
   * The place of this arc.
   * @return the place of the collection, containing this arc
   */
  @Override
  public Place getPlace() {
    return collection.getPlace();
  }

  /**
   * The transition of this arc.
   * @return the transition of the collection, containing this arc
   */
  @Override
  public Transition getTransition() {
    return collection.getTransition();
  }

  /**
   * Returns the data of this element. 
   * @return the data of this <code>ArcElement</code>
   */
  @Override
  public ArcElementData getData() {
    return (ArcElementData) super.getData();
  }

  /**
   * The source of this arc.
   * @return the source node of the collection, containing this arc
   */
  
  @Override
  public Node getSource() {
    return collection.getSource();
  }

  /**
   * The target of this arc.
   * @return the target node of the collection, containing this arc
   */
  @Override
  public Node getTarget() {
    return collection.getTarget();
  }

  /**
   * Get the direction of this arc. 
   * @return the enumerator object describing the direction of this arc's collection
   */
  @Override
  public EnumArcDirection getDirection() {
    return collection.getDirection();
  }
  
  /**
   * The parent collection, containing this arc.
   * @return the {@link ArcCollection}-object that contains this element
   */
  public ArcCollection getParent() {
    return collection;
  }
  
  /**
   * Set the parent collection of this arc. This method should only be called by the
   * collection itself.
   * @param col the new collection, containing this arc element
   */
  protected void setParent(ArcCollection col) {
    collection = col;
  }

  @Override
  public EnumNetElementType getElementType() {
    return EnumNetElementType.ArcElement;
  }

  @Override
  public String getAMLTagName() {
    return "ArcElement";
  }

  @Override
  public AMLNode getAMLNode() {
    AMLNode node = new AMLNode(getAMLTagName());
    node.addChild(getData().getAMLNode());
    return node;
  }

  @Override
  public void readAMLNode(AMLNode node) {
    AMLNode dataNode = node.getFirstChild("Data");
    getData().readAMLNode(dataNode);
  }
}
