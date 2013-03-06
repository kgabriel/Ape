/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.actions.global;

import ape.Ape;
import ape.org.ProjectStorage;
import ape.ui.control.actions.GlobalAction;
import ape.util.IO;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class LoadProjectAction extends GlobalAction {

  public LoadProjectAction(Ape theApe) {
    super(theApe);
  }

  @Override
  public void invoke() {
    List<ProjectStorage> projects = IO.loadProjects(theApe.ui);
    if(projects.isEmpty()) return;
    theApe.projects.addStorages(projects);
  }
}
