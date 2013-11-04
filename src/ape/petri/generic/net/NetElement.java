/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

import ape.petri.generic.EnumNetType;
import ape.petri.generic.ModelElement;
import ape.util.aml.AMLNode;
import java.util.Objects;

/**
 * An <code>NetElement</code> is an abstract top-level element in a net, like
 * a {@link Place}, a {@link Transition} or an {@link ArcCollection}.
 * @author Gabriel
 */
public abstract class NetElement implements ModelElement {
  private int id;
  protected Net net;
  private Data data;

  /**
   * Constructor for an abstract element in a given net.
   * The new element knows to which net it belongs. Also, each element
   * has a unique Id from the net's {@link Net#freeElementId()}-method. 
   * @param net the net, containing this element
   */
  protected NetElement(Net net, Data data) {
    this.net = net;
    id = net.freeElementId();
    this.data = data;
    data.setModelElement(this);
  }

  @Override
  public EnumNetType getNetType() {
    return net.getNetType();
  }
  
  @Override
  public Data getData() {
    return data;
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
  public Net getNet() {
    return net;
  }
  
  protected void setNet(Net net) {
    this.net = net;
  }
  
  /**
   * Compares this element to another object, and returns whether they are equal. An
   * <code>NetElement</code> is never equal to another element that is not
   * an <code>NetElement</code>. Two <code>NetElement</code>s are
   * equal if and only if they have the same Id, and they belong to the same net.
   * @param o the object to be compared if equal
   * @return <code>true</code> if <code>o</code> is an <code>NetElement</code>
   * that has the same Id and net as this element
   */
  @Override
  public boolean equals(Object o) {
    if(! (o instanceof NetElement)) return false;
    NetElement other = (NetElement) o;
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
