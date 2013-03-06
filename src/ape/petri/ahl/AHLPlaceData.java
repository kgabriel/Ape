/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.ahl;

import ape.petri.generic.EnumNetType;
import ape.petri.generic.net.PlaceData;
import ape.prolog.Atom;
import ape.util.aml.AMLNode;

/**
 *
 * @author Gabriel
 */
public class AHLPlaceData extends PlaceData {
  
  /** the type of the place given as a Prolog atom */
  private Atom type;
  
  /**
   * Creates a new place data element for a AHL-net, containing the specified name.
   * @param name the name of the place, stored in this data
   * @param type a <code>String</code> representation of the type of the place
   */
  public AHLPlaceData(String name, String type) {
    this(name, new Atom(type));
  }

  /**
   * Creates a new place data element for a AHL-net, containing the specified name.
   * @param name the name of the place, stored in this data
   * @param type the type of this place given as Prolog atom
   */
  public AHLPlaceData(String name, Atom type) {
    super(EnumNetType.AHLNet,name);  
    this.type = type;
  }
  
  public Atom getType() {
    return type;
  }
  
  public void setType(Atom type) {
    this.type = type;
    dataHasChanged();
  }

  /**
   * Checks compatibility of this place data with another one.
   * @param pd the place data to check compatibility with
   * @return <code>true</code> if the type of the other place equals the type of this place
   */
  @Override
  public boolean isCompatibleWith(PlaceData pd) {
    if(! (pd instanceof AHLPlaceData)) return false;
    return type.equals(((AHLPlaceData) pd).type);
  }

  @Override
  public AMLNode getAMLNode() {
    AMLNode node = super.getAMLNode();
    node.putAttribute("type", type.toString());
    return node;
  }

  @Override
  public void readAMLNode(AMLNode node) {
    super.readAMLNode(node);
    type = new Atom(node.getAttribute("type"));
  }
  
  
}
