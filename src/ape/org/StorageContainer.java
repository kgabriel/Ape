/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.org;

import ape.util.Property;
import ape.util.aml.AMLNode;
import ape.util.aml.AMLWritable;
import java.util.*;

/**
 *
 * @author Gabriel
 */
public abstract class StorageContainer<S extends Storage> extends Storage implements StorageListener, AMLWritable {
  
  protected List<S> storages;
  protected S activeStorage;
  
  public StorageContainer(String name) {
    super(name);
    storages = new ArrayList<>();
    this.activeStorage = null;
  }
  
  public Collection<S> getStorages() {
    return storages;
  }
  
  public void addStorage(S storage) {
    storages.add(storage);
    storage.addStorageListener(this);
    storage.setContainer(this);
    storageHasChanged();
  }
  
  public void addStorages(Collection<S> storages) {
    for(S storage : storages) {
      addStorage(storage);
    }
  }

  public S removeStorage(int index) {
    S removed = storages.remove(index);
    if(removed != null) {
      storageHasChanged();
      removed.setContainer(null);
      removed.removeStorageListener(this);
    }
    return removed;
  }
  
  public S getStorage(int index) {
    return storages.get(index);
  }

  public S getActiveStorage() {
    return activeStorage;
  }

  public void setActiveStorage(S activeStorage) {
    this.activeStorage = activeStorage;
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

  @Override
  public AMLNode getAMLNode() {
    AMLNode node = super.getAMLNode();
    for(S storage : storages)
    node.addChild(storage.getAMLNode());
    return node;
  }
}
