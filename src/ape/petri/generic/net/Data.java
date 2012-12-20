/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author Gabriel
 */
public abstract class Data implements Serializable {
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
}
