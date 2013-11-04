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
public class CloseActiveStorageAction extends Action {

  /** <code>true</code> if this action closes projects,
   * <code>false</code> if this action closes models.
   */
  private boolean closeProject;
  
  public CloseActiveStorageAction(Ape theApe, boolean closeProject) {
    super(theApe, "Close Active " + (closeProject ? "Project" : "Model"));
    this.closeProject = closeProject;
  }
  
  @Override
  protected void onInvocation() {
    if(closeProject) {
      ProjectStorage activeStorage = theApe.projects.getActiveStorage();
      if(activeStorage == null) return;
      theApe.projects.removeActiveStorage();
    } else {
      ProjectStorage activeProject = theApe.projects.getActiveStorage();
      if(activeProject != null) {
        activeProject.removeActiveStorage();
      }
    }
  }
}
