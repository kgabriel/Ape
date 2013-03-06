/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

import ape.petri.generic.EnumNetType;
import ape.util.aml.AMLNode;
import ape.util.aml.AMLWritable;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author Gabriel
 */
public abstract class Data implements Serializable, AMLWritable {
  private EnumElementType elementType;
  private EnumNetType netType;
  private Collection<DataChangeListener> dataChangeListeners;
  
  public Data(EnumNetType netType, EnumElementType elementType) {
    this.netType = netType;
    this.elementType = elementType;
    dataChangeListeners = new HashSet<>();
  }

  public EnumNetType getNetType() {
    return netType;
  }
  
  protected void setNetType(EnumNetType netType) {
    this.netType = netType;
  }

  public EnumElementType getElementType() {
    return elementType;
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
  public AMLNode getAMLNode() {
    AMLNode node = new AMLNode(getAMLTagName());
    node.putAttribute("netType", netType.name());
    node.putAttribute("elementType", elementType.name());
    return node;
  }

  @Override
  public String getAMLTagName() {
    return "Data";
  }

  @Override
  public void readAMLNode(AMLNode node) {
    netType = EnumNetType.valueOf(node.getAttribute("netType"));
    elementType = EnumElementType.valueOf(node.getAttribute("elementType"));
  }
  
  
}
