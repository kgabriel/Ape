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
import ape.ui.control.actions.GlobalAction;
import ape.ui.graphics.modelview.generic.ModelView;

/**
 *
 * @author Gabriel
 */
public class NewModelAction extends GlobalAction {
  private EnumModelType modelType;
  private EnumNetType netType;

  public NewModelAction(Ape theApe, EnumModelType modelType, EnumNetType netType) {
    super(theApe);
    this.modelType = modelType;
    this.netType = netType;
  }

  @Override
  public void invoke() {
    Model model = modelType.createModel(netType);
    ModelStorage modelStorage = new ModelStorage(modelType, model, new ModelView(theApe.ui, model));
    theApe.projects.getActiveStorage().addStorage(modelStorage);
  }
  
  
  
}
