/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.org;

import ape.util.PropertyContainer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Gabriel
 */
public abstract class Storage implements PropertyContainer, Serializable {
  private String name;
  private transient Collection<StorageListener> storageListeners;
  private transient StorageContainer<?> container;
  
  public Storage(String typeName) {
    name = "New " + typeName;
    ensureStorageListenerCollectionExists();
    container = null;
  }

  public String getName() {
    return name;
  }

  public StorageContainer<?> getContainer() {
    return container;
  }

  public void setContainer(StorageContainer<?> container) {
    this.container = container;
  }

  public void setName(String name) {
    this.name = name;
    storageHasChanged();
  }
  
  private void ensureStorageListenerCollectionExists() {
    if(storageListeners != null) return;
    storageListeners = new ArrayList<>();    
  }
  
  public void addStorageListener(StorageListener listener) {
    ensureStorageListenerCollectionExists();
    storageListeners.add(listener);
  }
  
  public boolean removeStorageListener(StorageListener listener) {
    ensureStorageListenerCollectionExists();
    return storageListeners.remove(listener);
  }
  
  public void storageHasChanged() {
    ensureStorageListenerCollectionExists();
    for(StorageListener listener : storageListeners) {
      listener.storageChanged(this);
    }
  }
}
