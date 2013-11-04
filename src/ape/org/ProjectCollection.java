/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.org;

import ape.util.aml.AMLNode;

/**
 *
 * @author Gabriel
 */
public class ProjectCollection extends StorageContainer<ProjectStorage> implements StorageContainerListener {

  public ProjectCollection() {
    super("ProjectCollection");
    addActiveStorage();
  }
  
  private void addActiveStorage() {
    ProjectStorage newProject = new ProjectStorage();
    addStorage(newProject);
    setActiveStorage(newProject);
  }

  @Override
  public void addStorage(ProjectStorage storage) {
    super.addStorage(storage);
    storage.addStorageContainerListener(this);
  }

  @Override
  public ProjectStorage removeActiveStorage() {
    ProjectStorage removed = super.removeActiveStorage();
    if(removed == null) return null;
    removed.removeStorageContainerListener(this);
    return removed;
  }
  
  @Override
  public void readAMLNode(AMLNode node) {
    super.readAMLNode(node);
    clear();
    for(AMLNode child : node.getChildren("ProjectStorage")) {
      ProjectStorage projectStorage = new ProjectStorage();
      projectStorage.readAMLNode(child);
      addStorage(projectStorage);
    }
  }

  @Override
  public String getAMLTagName() {
    return "ProjectCollection";
  }

  @Override
  public void storageSelectionChanged(StorageContainer container) {
    storageSelectionHasChanged(container);
  }

  @Override
  public void storageAdded(Storage addedStorage, StorageContainer container) {
    storageHasBeenAdded(addedStorage, container);
  }

  @Override
  public void storageRemoved(Storage removedStorage, StorageContainer container) {
    storageHasBeenRemoved(removedStorage, container);
  }
}
