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
import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 *
 * @author Gabriel
 */
public class CommandModelViewMouseScroll implements Command {

  @Override
  public String getName() {
    return "Scroll Model View";
  }

  @Override
  public String getDescription() {
    return "Scrolls the current model view by dragging the mouse.";
  }

  @Override
  public void invoke(CommandEvent e, Ape ape) {
    ModelView modelView = ape.ui.getActiveModelView();
    if(modelView == null) return;

    Point lastMouseLocation = e.getLastMouseLocation();
    Point pointInDevice = ((MouseEvent) e.getInputEvent()).getPoint();
    Point delta = new Point(lastMouseLocation.x - pointInDevice.x, lastMouseLocation.y - pointInDevice.y);

    modelView.translateViewCenter(delta);
    modelView.repaint();
  }

  @Override
  public boolean invokedBy(EnumInvocationType invocationType) {
    switch(invocationType) {
      case MouseDrag:
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
    return true;
  }
}
