/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.org;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Gabriel
 */
public class ProjectStorage implements Serializable {
  
  Collection<ModelStorage> models;
  
  public ProjectStorage() {
    models = new ArrayList<>();
  }
  
  public void addModel(ModelStorage model) {
    models.add(model);
  }
}
