/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.actions;

import ape.Ape;
import ape.org.ModelStorage;
import ape.petri.generic.Model;
import ape.ui.graphics.modelview.generic.ModelView;

/**
 *
 * @author Gabriel
 */
public abstract class GlobalModelAction extends GlobalAction {
  
  public GlobalModelAction(Ape theApe) {
    super(theApe);
  }
  
  protected Model getModel() {
    ModelStorage modelStorage = theApe.getActiveModel();
    return modelStorage.getModel();
  }
  
  protected ModelView getView() {
    ModelStorage modelStorage = theApe.getActiveModel();
    return modelStorage.getView();
  }
}
