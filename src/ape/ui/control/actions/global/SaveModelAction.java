/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.actions.global;

import ape.Ape;
import ape.org.ModelStorage;
import ape.org.ProjectStorage;
import ape.ui.control.actions.GlobalAction;
import ape.util.IO;

/**
 *
 * @author Gabriel
 */
public class SaveModelAction extends GlobalAction {

  public SaveModelAction(Ape theApe) {
    super(theApe);
  }

  @Override
  public void invoke() {
    ProjectStorage activeProject = theApe.projects.getActiveStorage();
    if(activeProject == null) return;
    ModelStorage activeModel = activeProject.getActiveStorage();
    if(activeModel == null) return;
    IO.saveModel(activeModel, theApe.ui);
  }

  
}
