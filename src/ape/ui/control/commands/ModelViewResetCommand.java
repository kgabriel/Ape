/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.commands;

import ape.Ape;
import ape.ui.control.CommandEvent;
import ape.ui.control.EnumCommandReceiverType;
import ape.ui.control.EnumInvocationType;
import ape.ui.graphics.modelview.ModelView;

/**
 *
 * @author Gabriel
 */
public class ModelViewResetCommand implements Command {

  @Override
  public String getName() {
    return "Reset Model View";
  }

  @Override
  public String getDescription() {
    return "Resets the current model view. This means that the view is centered and the scaling"
            + "is reset.";
  }

  @Override
  public void invoke(CommandEvent e, Ape ape) {
    ModelView modelView = ape.ui.getActiveModelView();
    if(modelView == null) return;
    
    modelView.resetView();
    modelView.repaint();
  }

  @Override
  public boolean invokedBy(EnumInvocationType invocationType) {
    switch(invocationType) {
      case KeyPress:
      case MousePress:
        return true;
      default:
        return false;
    }
  }

  @Override
  public boolean receivedBy(EnumCommandReceiverType receiverType) {
    return receiverType == EnumCommandReceiverType.Global;
  }

  @Override
  public boolean isAlwaysActive() {
    return false;
  }

  @Override
  public boolean canUserDefineInvocationBinding() {
    return true;
  }
}
