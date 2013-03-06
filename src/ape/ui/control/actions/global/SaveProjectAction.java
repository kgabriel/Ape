/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.actions.global;

import ape.Ape;
import ape.org.ProjectStorage;
import ape.ui.control.actions.GlobalAction;
import ape.util.IO;

/**
 *
 * @author Gabriel
 */
public class SaveProjectAction extends GlobalAction {

  public SaveProjectAction(Ape theApe) {
    super(theApe);
  }

  @Override
  public void invoke() {
    ProjectStorage activeProject = theApe.projects.getActiveStorage();
    if(activeProject == null) return;
    IO.saveProject(activeProject, theApe.ui);
  }
}
