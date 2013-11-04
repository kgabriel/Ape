/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

import ape.util.EnumPropertyType;
import ape.util.Property;
import ape.util.aml.AMLNode;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public abstract class NodeData extends Data {

  protected String name;
  
  public NodeData(String name) {
    super();
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
    dataHasChanged();
    System.out.println("actually changed");
  }

  @Override
  public List<Property> getProperties() {
    List<Property> properties = super.getProperties();
    properties.add(new Property(Property.CATEGORY_PROPERTIES, this, EnumPropertyType.SingleLineText, "Name") {
      @Override
      public Object getValue() {
        return getName();
      }

      @Override
      public void setValue(Object value) {
        setName((String) value);
      }      
    });
    return properties;
  }
    
  @Override
  public AMLNode getAMLNode() {
    AMLNode node = super.getAMLNode();
    node.putAttribute("name", name);
    return node;
  }

  @Override
  public void readAMLNode(AMLNode node) {
    super.readAMLNode(node);
    this.name = node.getAttribute("name");
  }
  
  
}
