/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

import ape.petri.generic.DataChangeListener;
import ape.petri.generic.EnumNetType;
import ape.petri.generic.ModelElement;
import ape.util.EnumPropertyType;
import ape.util.Property;
import ape.util.PropertyConstant;
import ape.util.PropertyContainer;
import ape.util.aml.AMLNode;
import ape.util.aml.AMLWritable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public abstract class Data implements AMLWritable, PropertyContainer {
  private Collection<DataChangeListener> dataChangeListeners;
  private ModelElement modelElement;
  
  public Data() {
    dataChangeListeners = new HashSet<>();
  }
  
  protected ModelElement getModelElement() {
    return modelElement;
  }
  
  protected void setModelElement(ModelElement modelElement) {
    this.modelElement = modelElement;
  }

  public EnumNetType getNetType() {
    return modelElement.getNetType();
  }
  
  public EnumNetElementType getElementType() {
    return modelElement.getElementType();
  }
  
  public void addDataChangeListener(DataChangeListener listener) {
    dataChangeListeners.add(listener);
  }
  
  public boolean removeDataChangeListener(DataChangeListener listener) {
    return dataChangeListeners.remove(listener);
  }
  
  protected final void dataHasChanged() {
    for(DataChangeListener listener : dataChangeListeners) {
      listener.dataChanged(this);
    }
  }

  @Override
  public List<Property> getProperties() {
    List<Property> properties = new ArrayList<>();
    properties.add(new PropertyConstant(Property.CATEGORY_PROPERTIES, this, EnumPropertyType.Integer, "Element ID", getModelElement().getId()));
    return properties;
  }
  
  

  @Override
  public AMLNode getAMLNode() {
    AMLNode node = new AMLNode(getAMLTagName());
    return node;
  }

  @Override
  public String getAMLTagName() {
    return "Data";
  }

  @Override
  public void readAMLNode(AMLNode node) {
  }
}
