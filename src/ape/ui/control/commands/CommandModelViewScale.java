/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.commands;

import ape.Ape;
import ape.ui.control.CommandEvent;
import ape.ui.control.EnumCommandReceiverType;
import ape.ui.control.EnumInvocationType;
import ape.ui.graphics.modelview.generic.ModelView;
import java.awt.event.MouseWheelEvent;

/**
 *
 * @author Gabriel
 */
public class CommandModelViewScale implements Command {

  @Override
  public String getName() {
    return "Scale Model View";
  }

  @Override
  public String getDescription() {
    return "Changes the scale of the current model view using the mouse wheel.";
  }

  @Override
  public void invoke(CommandEvent e, Ape ape) {
    ModelView modelView = ape.ui.getActiveModelView();
    if(modelView == null) return;
    
    MouseWheelEvent m = (MouseWheelEvent) e.getInputEvent();
    double amount = m.getPreciseWheelRotation();
    double scaleChange = 1.0 - (amount / 10.0);
    modelView.scaleViewFromDevice(scaleChange, m.getPoint());
    modelView.repaint();
}

  @Override
  public boolean invokedBy(EnumInvocationType invocationType) {
    switch(invocationType) {
      case MouseWheel:
        return true;
      default:
        return false;
    }
  }

  @Override
  public boolean receivedBy(EnumCommandReceiverType receiverType) {
    return receiverType == EnumCommandReceiverType.ModelView;
  }

  @Override
  public boolean isAlwaysActive() {
    return false;
  }
}
