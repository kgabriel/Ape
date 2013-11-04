/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.commands;

import ape.Ape;
import ape.org.ModelStorage;
import ape.petri.generic.Model;
import ape.petri.generic.net.*;
import ape.ui.control.CommandEvent;
import ape.ui.control.EnumCommandReceiverType;
import ape.ui.control.EnumInvocationType;
import ape.ui.graphics.modelview.ModelView;
import ape.ui.graphics.modelview.generic.PlaceVisual;
import ape.ui.graphics.modelview.generic.TransitionVisual;
import ape.ui.graphics.modelview.generic.Visual;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

/**
 *
 * @author Gabriel
 */
public class ModelViewMouseMoveCommand implements Command {

  private boolean drag;

  public ModelViewMouseMoveCommand(boolean drag) {
    this.drag = drag;
  }
  
  @Override
  public String getName() {
    return "Model View Mouse " + (drag ? "Drag" : "Move");
  }

  @Override
  public String getDescription() {
    return "Standard actions, when the mouse (usually left button) is used on the model view.";
  }

  @Override
  public void invoke(CommandEvent e, Ape ape) {
    ModelStorage modelStorage = ape.getActiveModel();
    if(modelStorage == null) return;
    ModelView view = modelStorage.getView();

    mouseMoved(ape, view, e.getInputEvent());

    view.repaint();
  }
  
  private void mouseMoved(Ape ape, ModelView view, InputEvent e) {
    Point lastMouseLocation = view.getMouseLocation();
    Point mouseLocation = view.toViewCoordinate(((MouseEvent) e).getPoint());
    view.setMouseLocation(mouseLocation);

    switch(ape.ui.modelViewToolbar.getSelectedAction()) {
      case NetSelection:
        if(view.isMouseDown()) {
          /* if the user is currently selecting an area */
          if(view.selectionStartPointIsSet()) {
            view.setSelectionEndPoint();
            
          /* otherwise */
          } else {
            view.setMouseLocation(mouseLocation);
            int dx = mouseLocation.x - lastMouseLocation.x;
            int dy = mouseLocation.y - lastMouseLocation.y;
            view.translateSelectedVisuals(dx, dy);
          }
        }
        break;
    }
  }

  
  @Override
  public boolean invokedBy(EnumInvocationType invocationType) {
    switch(invocationType) {
      case MouseMove:
        return !drag;
      case MouseDrag:
         return drag;
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
    return drag;
  }
}
