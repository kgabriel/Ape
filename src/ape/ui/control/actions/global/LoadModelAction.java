/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.actions.global;

import ape.Ape;
import ape.org.ModelStorage;
import ape.petri.generic.EnumModelType;
import ape.ui.control.actions.GlobalAction;
import ape.util.IO;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class LoadModelAction extends GlobalAction {
  private EnumModelType modelType;

  public LoadModelAction(Ape theApe, EnumModelType modelType) {
    super(theApe);
    this.modelType = modelType;
  }

  @Override
  public void invoke() {
    List<ModelStorage> models = IO.loadModels(theApe.ui, modelType);
    if(models.isEmpty()) return;
    theApe.projects.getActiveStorage().addStorages(models);
  }
}
