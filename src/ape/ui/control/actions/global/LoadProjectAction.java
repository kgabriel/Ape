/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.actions.global;

import ape.Ape;
import ape.org.ProjectStorage;
import ape.ui.control.actions.Action;
import ape.util.IO;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class LoadProjectAction extends Action {

  public LoadProjectAction(Ape theApe) {
    super(theApe, "Load Project");
  }

  @Override
  protected void onInvocation() {
    List<ProjectStorage> projects = IO.loadProjects(theApe.ui);
    if(projects.isEmpty()) return;
    theApe.projects.addStorages(projects);
  }
}
