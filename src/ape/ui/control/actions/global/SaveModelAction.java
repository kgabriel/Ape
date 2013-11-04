/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.actions.global;

import ape.Ape;
import ape.org.ModelStorage;
import ape.org.ProjectStorage;
import ape.ui.control.actions.Action;
import ape.util.IO;

/**
 *
 * @author Gabriel
 */
public class SaveModelAction extends Action {

  public SaveModelAction(Ape theApe) {
    super(theApe, "Save Model");
  }

  @Override
  protected void onInvocation() {
    ProjectStorage activeProject = theApe.projects.getActiveStorage();
    if(activeProject == null) return;
    ModelStorage activeModel = activeProject.getActiveStorage();
    if(activeModel == null) return;
    IO.saveModel(activeModel, theApe.ui);
  }

  
}
