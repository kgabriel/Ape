/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.actions;

import ape.Ape;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 *
 * @author Gabriel
 */
public abstract class Action {
  
  protected Ape theApe;
  private String name;
  
  public Action(Ape theApe, String name) {
    this.theApe = theApe;
    this.name = name;
  }
  
  public String getName() {
    return name;
  }
  
  public void invoke() {
    onInvocation();
  }
  
  protected abstract void onInvocation();
  
  public javax.swing.Action getSwingAction() {
    return getSwingAction(this.name, null);
  }
  
  public javax.swing.Action getSwingAction(String name) {
    return getSwingAction(name, null);
  }
  
  public javax.swing.Action getSwingAction(Icon icon) {
    return getSwingAction(this.name, icon);
  }
  
  public javax.swing.Action getSwingAction(String name, Icon icon) {
    return new AbstractAction(name, icon) {

      @Override
      public void actionPerformed(ActionEvent e) {
        invoke();
      }
    };
  }
}
