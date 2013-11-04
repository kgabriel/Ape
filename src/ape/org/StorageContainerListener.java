/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.org;

/**
 *
 * @author Gabriel
 */
public interface StorageContainerListener {
  
  public void storageSelectionChanged(StorageContainer container);
  
  public void storageAdded(Storage addedStorage, StorageContainer container);
  
  public void storageRemoved(Storage removedStorage, StorageContainer container);
}
