/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.org;

import ape.petri.generic.Model;
import ape.ui.modelview.generic.ModelView;
import java.io.Serializable;

/**
 *
 * @author Gabriel
 */
public class ModelStorage implements Serializable {
  private String name;
  private Model model;
  private ModelView view; 

  public ModelStorage(Model net, ModelView view, String name) {
    this.model = net;
    this.view = view;
    this.name = name;
  }
  
  public Model getModel() {
    return model;
  }

  public ModelView getView() {
    return view;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
