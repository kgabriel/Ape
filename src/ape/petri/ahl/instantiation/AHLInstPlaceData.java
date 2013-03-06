/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.ahl.instantiation;

import ape.petri.ahl.AHLPlaceData;
import ape.petri.generic.EnumNetType;
import ape.petri.generic.net.Place;
import ape.petri.generic.net.PlaceData;
import ape.petri.validity.EnumInvalidityType;
import ape.petri.validity.InvalidityReason;
import ape.petri.validity.Validity;
import ape.prolog.Atom;
import ape.prolog.ValueTerm;
import ape.util.aml.AMLNode;

/**
 *
 * @author Gabriel
 */
public class AHLInstPlaceData extends AHLPlaceData {

  private ValueTerm value;
  
  public AHLInstPlaceData(String name, Atom type, ValueTerm value) {
    super(name, type);
    setNetType(EnumNetType.AHLInstantiation);
    this.value = value;
  }
  
  public AHLInstPlaceData(String name, String type, String value) {
    this(name, new Atom(type), (value == null) ? null : new ValueTerm(value));
  }

  public ValueTerm getValue() {
    return value;
  }

  public void setValue(ValueTerm value) {
    this.value = value;
    dataHasChanged();
  }

  @Override
  public boolean isCompatibleWith(PlaceData pd) {
    if(! (pd instanceof AHLInstPlaceData)) return false;
    AHLInstPlaceData data = (AHLInstPlaceData) pd;
    if(! this.getType().equals(data.getType())) return false;
    return this.value.equals(data.value);
  }
  
  public Validity validate(Place parent) {
    if(value != null) return new Validity(true);
    Validity invalid = new Validity(false);
    invalid.addInvalidityReason(new InvalidityReason(EnumInvalidityType.InstPlaceUnassigned, "Place '" + parent.toString() + "'"));
    return invalid;
  }

  @Override
  public AMLNode getAMLNode() {
    AMLNode node = super.getAMLNode();
    if(value != null) node.putAttribute("value", value.toString());
    return node;
  }

  @Override
  public void readAMLNode(AMLNode node) {
    super.readAMLNode(node);
    String valueString = node.getAttribute("value");
    if(valueString != null) value = new ValueTerm(valueString);
  }
  
  
}
