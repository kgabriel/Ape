/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.actions.global;

import ape.Ape;
import ape.org.ProjectStorage;
import ape.ui.control.actions.Action;

/**
 *
 * @author Gabriel
 */
public class NewProjectAction extends Action {

  public NewProjectAction(Ape theApe) {
    super(theApe, "New Project");
  }

  @Override
  protected void onInvocation() {
    ProjectStorage newProject = new ProjectStorage();
    theApe.projects.addStorage(newProject);
    theApe.projects.setActiveStorage(newProject);
  }
  
}
