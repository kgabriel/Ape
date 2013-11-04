/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.actions.global;

import ape.Ape;
import ape.org.ProjectStorage;
import ape.ui.control.actions.Action;
import ape.util.IO;

/**
 *
 * @author Gabriel
 */
public class SaveProjectAction extends Action {
  
  private boolean saveAs;

  public SaveProjectAction(Ape theApe, boolean saveAs) {
    super(theApe, "Save Project" + (saveAs ? " As" : ""));
    this.saveAs = saveAs;
  }

  @Override
  protected void onInvocation() {
    ProjectStorage activeProject = theApe.projects.getActiveStorage();
    if(activeProject == null) return;
    IO.saveProject(activeProject, theApe.ui, saveAs);
  }
}
