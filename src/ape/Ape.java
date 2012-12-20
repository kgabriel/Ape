/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape;

import ape.org.ProjectStorage;
import ape.ui.UI;


/**
 *
 * @author Gabriel
 */
public class Ape {

  protected ProjectStorage modelStorages;
  public UI ui;
  
  /**
    * @param args the command line arguments
    */
  public static void main(String[] args) {

    Ape ape = new Ape();
  }
  
  public Ape() {
    init();
    new Test(this).test();
  }
  
  private void init() {
    modelStorages = new ProjectStorage();
    
    // open main frame
    ui = new UI(this);
  }
  
}
