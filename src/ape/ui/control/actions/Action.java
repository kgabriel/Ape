/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 *
 * @author Gabriel
 */
public abstract class Action {
  
  public abstract void invoke();
  
  public javax.swing.Action getSwingAction() {
    return getSwingAction(null, null);
  }
  
  public javax.swing.Action getSwingAction(String name) {
    return getSwingAction(name, null);
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
