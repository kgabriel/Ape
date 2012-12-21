/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape;

import ape.org.ProjectCollection;
import ape.ui.UI;


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

    Ape ape = new Ape();
  }
  
  public Ape() {
    projects = new ProjectCollection();
    init();
//    new Test(this).test();
  }
  
  private void init() {
    
    // open main frame
    ui = new UI(this);
  }
  
}
