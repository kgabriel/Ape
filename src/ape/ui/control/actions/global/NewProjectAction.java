/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.actions.global;

import ape.Ape;
import ape.org.ProjectStorage;
import ape.ui.control.actions.GlobalAction;

/**
 *
 * @author Gabriel
 */
public class NewProjectAction extends GlobalAction {

  public NewProjectAction(Ape theApe) {
    super(theApe);
  }

  @Override
  public void invoke() {
    ProjectStorage newProject = new ProjectStorage();
    theApe.projects.addStorage(newProject);
    theApe.projects.setActiveStorage(newProject);
  }
  
}
