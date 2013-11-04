/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.actions.global;

import ape.Ape;
import ape.org.ModelStorage;
import ape.petri.generic.EnumModelType;
import ape.ui.control.actions.Action;
import ape.util.IO;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class LoadModelAction extends Action {
  private EnumModelType modelType;

  public LoadModelAction(Ape theApe, EnumModelType modelType) {
    super(theApe, "Load Model");
    this.modelType = modelType;
  }

  @Override
  protected void onInvocation() {
    List<ModelStorage> models = IO.loadModels(theApe.ui, modelType);
    if(models.isEmpty()) return;
    theApe.projects.getActiveStorage().addStorages(models);
  }
}
