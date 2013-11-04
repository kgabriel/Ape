/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.actions.global;

import ape.Ape;
import ape.org.ModelStorage;
import ape.petri.generic.EnumModelType;
import ape.petri.generic.EnumNetType;
import ape.petri.generic.Model;
import ape.ui.control.actions.Action;
import ape.ui.graphics.modelview.ModelView;

/**
 *
 * @author Gabriel
 */
public class NewModelAction extends Action {
  private EnumModelType modelType;
  private EnumNetType netType;

  public NewModelAction(Ape theApe, EnumModelType modelType, EnumNetType netType) {
    super(theApe, "New Model");
    this.modelType = modelType;
    this.netType = netType;
  }

  @Override
  protected void onInvocation() {
    Model model = modelType.createModel(netType);
    ModelStorage modelStorage = new ModelStorage(model, theApe.ui);
    theApe.projects.getActiveStorage().addStorage(modelStorage);
  }
  
  
  
}
