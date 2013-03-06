/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

import ape.util.aml.AMLNode;
import java.util.Objects;

/**
 * An <code>AbstractNetElement</code> is an abstract top-level element in a net, like
 * a {@link Place}, a {@link Transition} or an {@link ArcCollection}.
 * @author Gabriel
 */
public abstract class AbstractNetElement implements NetElement {
  private int id;
  protected Net net;

  /**
   * Constructor for an abstract element in a given net.
   * The new element knows to which net it belongs. Also, each element
   * has a unique Id from the net's {@link Net#freeElementId()}-method. 
   * @param net the net, containing this element
   */
  protected AbstractNetElement(Net net) {
    this.net = net;
    id = net.freeElementId();
  }
  
  /**
   * Get the Id of this element given by the net that contains it.
   * @return the (net-wide unique) Id of this element
   * @see Net#freeElementId()
   */
  @Override
  public int getId() {
    return id;
  }

  /**
   * Get the net of this element.
   * @return the net, containing this element
   */
  @Override
  public Net getNet() {
    return net;
  }
  
  protected void setNet(Net net) {
    this.net = net;
  }
  
  /**
   * Compares this element to another object, and returns whether they are equal. An
   * <code>AbstractNetElement</code> is never equal to another element that is not
   * an <code>AbstractNetElement</code>. Two <code>AbstractNetElement</code>s are
   * equal if and only if they have the same Id, and they belong to the same net.
   * @param o the object to be compared if equal
   * @return <code>true</code> if <code>o</code> is an <code>AbstractNetElement</code>
   * that has the same Id and net as this element
   */
  @Override
  public boolean equals(Object o) {
    if(! (o instanceof AbstractNetElement)) return false;
    AbstractNetElement other = (AbstractNetElement) o;
    return this.id == other.id && this.net.equals(other.net);
  }

  /**
   * Returns a hash code for this element. The hash code is calculated from its
   * Id and net.
   * @return a hash code for this element
   */
  @Override
  public int hashCode() {
    int hash = 5;
    hash = 67 * hash + this.id;
    hash = 67 * hash + Objects.hashCode(this.net);
    return hash;
  }

  @Override
  public AMLNode getAMLNode() {
    AMLNode node = new AMLNode(getAMLTagName());
    node.putAttribute("id", id);
    return node;
  }

  @Override
  public void readAMLNode(AMLNode node) {
    id = node.getAttributeInt("id");
  }
  
  
}
