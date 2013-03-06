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
public class ArcElement implements NetElement, Arc {
  
  /** the parent collection containing this element */
  protected ArcCollection collection;
  
  /** the data of this arc element */
  protected ArcElementData data;
  
  /**
   * A new arc element inside the specified collection. The element naturally
   * belongs to the collection's net.
   * @param col the parent collection of this element
   */
  public ArcElement(ArcCollection col, ArcElementData data) {
    collection = col;
    this.data = data;
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
  public ArcElementData getData() {
    return data;
  }

  /**
   * Sets the data of this element to a new value.
   * @param data the data to be set
   */
  public void setData(ArcElementData data) {
    this.data = data;
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

  /**
   * Returns the id of the collection, containing this element. An
   * <code>ArcElement</code> does not have an own Id.
   * @return the value returned by the {@link NetElement#getId()} method of this element's collection
   */
  @Override
  public int getId() {
    return collection.getId();
  }

  /**
   * Returns the net containing this arc element.
   * @return the net, containing the collection that contains this arc element
   */
  @Override
  public Net getNet() {
    return collection.getNet();
  }

  @Override
  public EnumElementType getElementType() {
    return EnumElementType.ArcElement;
  }

  @Override
  public String getAMLTagName() {
    return "ArcElement";
  }

  @Override
  public AMLNode getAMLNode() {
    AMLNode node = new AMLNode(getAMLTagName());
    node.addChild(data.getAMLNode());
    return node;
  }

  @Override
  public void readAMLNode(AMLNode node) {
    AMLNode dataNode = node.getFirstChild("Data");
    data.readAMLNode(dataNode);
  }
}
