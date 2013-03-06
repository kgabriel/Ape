/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.actions;

import ape.Ape;

/**
 *
 * @author Gabriel
 */
public abstract class GlobalAction extends Action {
  protected Ape theApe;
  
  public GlobalAction(Ape theApe) {
    this.theApe = theApe;
  }
}
