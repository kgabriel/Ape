/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.ahl;

import ape.petri.generic.net.PlaceData;
import ape.prolog.Atom;
import ape.util.EnumPropertyType;
import ape.util.Property;
import ape.util.aml.AMLNode;
import java.util.List;

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
    super(name);  
    this.type = type;
  }
  
  public Atom getType() {
    return type;
  }
  
  public void setType(Atom type) {
    this.type = type;
    dataHasChanged();
  }

  @Override
  public List<Property> getProperties() {
    List<Property> properties = super.getProperties();
    properties.add(new Property(Property.CATEGORY_VALUES, this, EnumPropertyType.SingleLineText, "Type") {

      @Override
      public Object getValue() {
        return type.toString();
      }

      @Override
      public void setValue(Object value) {
        Atom atom = new Atom((String) value);
        setType(atom);
      }
    });
    return properties;
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
