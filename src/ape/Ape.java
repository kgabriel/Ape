/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape;

import ape.org.ModelStorage;
import ape.org.ProjectCollection;
import ape.org.ProjectStorage;
import ape.ui.UI;
import jpl.fli.Prolog;


/**
 *
 * @author Gabriel
 */
public class Ape {

  public ProjectCollection projects;
  public UI ui;
  
  /**
    * @param args the command line arguments
    */
  public static void main(String[] args) {

    Ape theApe = new Ape();
    ape.prolog.Prolog.initialize();
//    PLTest.test();
  }
  
  public Ape() {
    projects = new ProjectCollection();
    init();
  }
  
  public ProjectStorage getActiveProject() {
    return projects.getActiveStorage();
  }
  
  public ModelStorage getActiveModel() {
    ProjectStorage activeProject = getActiveProject();
    if(activeProject == null) return null;
    return activeProject.getActiveStorage();
  }
  
  private void init() {
    
    // open main frame
    ui = new UI(this);
  }
  
}
