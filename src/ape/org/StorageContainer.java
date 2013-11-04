/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.org;

import ape.util.Property;
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
public abstract class StorageContainer<S extends Storage> extends Storage implements StorageListener, AMLWritable {
  
  protected List<S> storages;
  protected S activeStorage;
  private Collection<StorageContainerListener> storageContainerListeners;
  
  public StorageContainer(String name) {
    super(name);
    storages = new ArrayList<>();
    this.activeStorage = null;
    this.storageContainerListeners = new HashSet<>();
  }
  
  public boolean addStorageContainerListener(StorageContainerListener listener) {
    return storageContainerListeners.add(listener);
  }
  
  public boolean removeStorageContainerListener(StorageContainerListener listener) {
    return storageContainerListeners.remove(listener);
  }
  
  protected void storageSelectionHasChanged() {
    storageSelectionHasChanged(this);
  }
  
  protected void storageSelectionHasChanged(StorageContainer container) {
    for(StorageContainerListener listener : storageContainerListeners) {
      listener.storageSelectionChanged(container);
    }
  }
  
  protected void storageHasBeenAdded(Storage storage) {
    storageHasBeenAdded(storage, this);
  }
  
  protected void storageHasBeenAdded(Storage storage, StorageContainer container) {
    for(StorageContainerListener listener : storageContainerListeners) {
      listener.storageAdded(storage, container);
    }
  }
  
  protected void storageHasBeenRemoved(Storage storage) {
    storageHasBeenRemoved(storage, this);
  }

  protected void storageHasBeenRemoved(Storage storage, StorageContainer container) {
    for(StorageContainerListener listener : storageContainerListeners) {
      listener.storageRemoved(storage, container);
    }
  }
  
  public Collection<S> getStorages() {
    return storages;
  }
  
  public void addStorage(S storage) {
    storages.add(storage);
    storage.addStorageListener(this);
    storage.setContainer(this);
    storageHasChanged();
    storageHasBeenAdded(storage);
  }
  
  public void addStorages(Collection<S> storages) {
    for(S storage : storages) {
      addStorage(storage);
    }
  }
  
  public S removeActiveStorage() {
    boolean removed = storages.remove(activeStorage);
    if(! removed) return null;
    
    S removedStorage = activeStorage;
    removedStorage.setContainer(null);
    removedStorage.removeStorageListener(this);
    setFirstAvailableActiveStorage(); 
    storageHasChanged();
    storageHasBeenRemoved(removedStorage);
    storageSelectionHasChanged();
    return removedStorage;
}
  
  public S getStorage(int index) {
    return storages.get(index);
  }

  public S getActiveStorage() {
    return activeStorage;
  }

  public void setActiveStorage(S activeStorage) {
    this.activeStorage = activeStorage;
    storageSelectionHasChanged();
  }
  
  public void setFirstAvailableActiveStorage() {
    if(isEmpty()) {
      setActiveStorage(null);
    } else {
      setActiveStorage(getStorage(0));
    }
  }
  
  @Override
  public void storageChanged(Storage changedStorage) {
    storageHasChanged();
  }

  @Override
  public List<Property> getProperties() {
    return null;
  }
  
  public boolean isEmpty() {
    return storages.isEmpty();
  }
  
  public void clear() {
    storages.clear();
  }
  
  public boolean contains(S storage) {
    return storages.contains(storage);
  }
  
  @Override
  public AMLNode getAMLNode() {
    AMLNode node = super.getAMLNode();
    for(S storage : storages)
    node.addChild(storage.getAMLNode());
    return node;
  }
}
