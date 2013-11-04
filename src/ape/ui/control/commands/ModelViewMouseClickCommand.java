/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.commands;

import ape.Ape;
import ape.org.ModelStorage;
import ape.ui.control.CommandEvent;
import ape.ui.control.EnumCommandReceiverType;
import ape.ui.control.EnumInvocationType;
import ape.ui.graphics.modelview.ModelView;
import java.awt.event.MouseEvent;

/**
 *
 * @author Gabriel
 */
public class ModelViewMouseClickCommand implements Command {

  @Override
  public String getName() {
    return "Model View Mouse Click";
  }

  @Override
  public String getDescription() {
    return "Standard actions, when the mouse (usually left button) is clicked on model view.";
  }

  @Override
  public void invoke(CommandEvent e, Ape ape) {
    ModelStorage modelStorage = ape.getActiveModel();
    if(modelStorage == null) return;
    ModelView view = modelStorage.getView();
    int modifier = e.getInputEvent().getModifiersEx();

    mouseClicked(ape, view, modifier);
    
    view.repaint();
  }
  
  private void mouseClicked(Ape ape, ModelView view, int modifier) {
    switch(ape.ui.modelViewToolbar.getSelectedAction()) {
      case NetSelection:
        /* update selection, taking into account a 1 pixel square at the mouse location;
        * - if CTRL is pressed: add elements in the area that were not in the selection before and remove
        * elements that were there before
        * - if SHIFT is pressed: add all elements in the area, even if they were there before
        */
        boolean xor = ((modifier & MouseEvent.CTRL_DOWN_MASK) != 0);
        boolean add = ((modifier & MouseEvent.SHIFT_DOWN_MASK) != 0) | xor;
        view.updateSingleSelection(add, xor);
        break;
    }
  }
 
  @Override
  public boolean invokedBy(EnumInvocationType invocationType) {
    switch(invocationType) {
      case MouseClick: 
         return true;
      default: return false;
    }
  }

  @Override
  public boolean receivedBy(EnumCommandReceiverType receiverType) {
    switch(receiverType) {
      case ModelView: return true;
      default: return false;
    }
  }

  @Override
  public boolean isAlwaysActive() {
    return true;
  }

  @Override
  public boolean canUserDefineInvocationBinding() {
    return true;
  }
}
