/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.ahl.instantiation;

import ape.petri.ahl.AHLPlaceData;
import ape.petri.generic.net.Place;
import ape.petri.validity.EnumInvalidityType;
import ape.petri.validity.InvalidityReason;
import ape.petri.validity.Validity;
import ape.prolog.Atom;
import ape.prolog.ValueTerm;
import ape.util.EnumPropertyType;
import ape.util.Property;
import ape.util.aml.AMLNode;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class AHLInstPlaceData extends AHLPlaceData {

  private ValueTerm placeValue;
  
  public AHLInstPlaceData(String name, Atom type, ValueTerm value) {
    super(name, type);
    this.placeValue = value;
  }
  
  public AHLInstPlaceData(String name, String type, String value) {
    this(name, new Atom(type), (value == null) ? null : new ValueTerm(value));
  }

  public ValueTerm getPlaceValue() {
    return placeValue;
  }

  public void setPlaceValue(ValueTerm value) {
    this.placeValue = value;
    dataHasChanged();
  }

  public Validity validate(Place parent) {
    if(placeValue != null) return new Validity(true);
    Validity invalid = new Validity(false);
    invalid.addInvalidityReason(new InvalidityReason(EnumInvalidityType.InstPlaceUnassigned, "Place '" + parent.toString() + "'"));
    return invalid;
  }

  @Override
  public List<Property> getProperties() {
    List<Property> properties = super.getProperties();
    properties.add(new Property(Property.CATEGORY_VALUES, this, EnumPropertyType.SingleLineText, "Value") {

      @Override
      public Object getValue() {
        ValueTerm value = getPlaceValue();
        if(value == null) return ValueTerm.unassignedString;
        return value.toString();
      }

      @Override
      public void setValue(Object value) {
        if(value.equals(ValueTerm.unassignedString)) {
          setPlaceValue(null);
        } else {
          setPlaceValue(new ValueTerm((String) value));
        }
      }
    });
    return properties;
  }

  @Override
  public AMLNode getAMLNode() {
    AMLNode node = super.getAMLNode();
    if(placeValue != null) node.putAttribute("value", placeValue.toString());
    return node;
  }

  @Override
  public void readAMLNode(AMLNode node) {
    super.readAMLNode(node);
    String valueString = node.getAttribute("value");
    if(valueString != null) placeValue = new ValueTerm(valueString);
  }
  
  
}
