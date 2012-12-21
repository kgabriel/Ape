/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.org;

/**
 *
 * @author Gabriel
 */
public class ProjectCollection extends StorageContainer<ProjectStorage> {

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
  public ProjectStorage removeStorage(int key) {
    ProjectStorage removed = super.removeStorage(key);
    if(isEmpty()) {
      addActiveStorage();
    }
    if(removed.equals(getActiveStorage())) {
      setActiveStorage(storages.get(storages.firstKey()));
    }
    return removed;
  }
  
  
}
