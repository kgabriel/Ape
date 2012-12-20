/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.org;

import java.util.HashMap;

/**
 *
 * @author Gabriel
 */
public class ProjectStorage extends HashMap<Integer, ModelStorage> {
  
  int freeId;
  
  public ProjectStorage() {
    freeId = 0;
  }
  
  public void addNewStorage(ModelStorage n) {
    put(nextFreeId(), n);
  }
  
  private int nextFreeId() {
    return freeId++;
  }
}
