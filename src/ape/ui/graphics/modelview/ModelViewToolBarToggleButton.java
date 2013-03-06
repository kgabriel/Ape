/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview;

import javax.swing.JToggleButton;

/**
 *
 * @author Gabriel
 */
public class ModelViewToolBarToggleButton extends JToggleButton {
  
  private EnumModelViewAction modelViewAction;
  
  public ModelViewToolBarToggleButton(EnumModelViewAction action) {
    super();
    
    this.modelViewAction = action;
    
    setToolTipText(action.getDescription());
    setIcon(action.getIcon());
    setText(action.getName());
  }

  public EnumModelViewAction getModelViewAction() {
    return modelViewAction;
  }
}
