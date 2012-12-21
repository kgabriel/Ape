/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.org;

import ape.util.Property;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author Gabriel
 */
public class StorageContainer<S extends Storage> extends Storage implements StorageListener {
  
  protected SortedMap<Integer,S> storages;
  private int freeKey;
  protected S activeStorage;
  
  public StorageContainer(String name) {
    super(name);
    storages = new TreeMap<>();
    this.activeStorage = null;
    this.freeKey = 0;
  }
  
  public Collection<S> getStorages() {
    return storages.values();
  }
  
  public void addStorage(S storage) {
    storages.put(nextFreeKey(), storage);
    storage.addStorageListener(this);
    storage.setContainer(this);
    storageHasChanged();
  }

  public S removeStorage(int key) {
    S removed = storages.remove(key);
    if(removed != null) {
      storageHasChanged();
      removed.setContainer(null);
      removed.removeStorageListener(this);
    }
    return removed;
  }
  
  public S getStorage(int key) {
    return storages.get(key);
  }

  public S getActiveStorage() {
    return activeStorage;
  }

  public void setActiveStorage(S activeStorage) {
    this.activeStorage = activeStorage;
  }
  
  private int nextFreeKey() {
    return freeKey++;
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
}
