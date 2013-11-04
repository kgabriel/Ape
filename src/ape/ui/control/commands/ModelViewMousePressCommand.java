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

/**
 *
 * @author Gabriel
 */
public class ModelViewMousePressCommand implements Command {

  @Override
  public String getName() {
    return "Model View Mouse Press";
  }

  @Override
  public String getDescription() {
    return "Standard actions, when the mouse (usually left button) is pressed on the model view.";
  }

  @Override
  public void invoke(CommandEvent e, Ape ape) {
    ModelStorage modelStorage = ape.getActiveModel();
    if(modelStorage == null) return;
    ModelView view = modelStorage.getView();
    int modifier = e.getInputEvent().getModifiersEx();

    mousePressed(ape, view);
    
    view.repaint();
  }
  
  private void mousePressed(Ape ape, ModelView view) {
    view.setMouseDown(true);
    switch(ape.ui.modelViewToolbar.getSelectedAction()) {
      case NetSelection:
        view.setSelectionStartPoint();
        break;
      case NetNewPlace:
        Place p = ((Net) ape.getActiveModel().getModel()).addDefaultPlace();
        Visual pv = view.addVisualFor(p);
        ((PlaceVisual) pv).setCenter(view.getMouseLocation());
        view.setSelectionTo(pv);
        break;
      case NetNewTransition:
        Transition t = ((Net) ape.getActiveModel().getModel()).addDefaultTransition();
        Visual tv = view.addVisualFor(t);
        ((TransitionVisual) tv).setCenter(view.getMouseLocation());
        view.setSelectionTo(tv);
        break;
      case NetNewArc:
        view.addArcNode();
        
        /* start and end point exists -> try to create an arc */
        Node arcStart = view.getArcStartNode();
        Node arcEnd = view.getArcEndNodeId();
        if(arcStart != null && arcEnd != null) {
          tryToCreateNetArc(view, arcStart, arcEnd);
          view.removeArcNodes();
        }
        break;
    }
  }
  
  private void tryToCreateNetArc(ModelView view, Node arcStart, Node arcEnd) {
    Place pOfArc = null;
    Transition tOfArc = null;
    EnumArcDirection dir = null;

    if(arcStart instanceof Place) {
      dir = EnumArcDirection.PT;
      pOfArc = (Place) arcStart;
    } else if(arcEnd instanceof Place) {
      dir = EnumArcDirection.TP;
      pOfArc = (Place) arcEnd;
    }

    if(arcStart instanceof Transition) {
      tOfArc = (Transition) arcStart;
    } else if(arcEnd instanceof Transition) {
      tOfArc = (Transition) arcEnd;
    }

    if(pOfArc != null && tOfArc != null) {
      Model model = view.getModel();
      if(! ((Net) model).hasArc(pOfArc, tOfArc, dir)) {
        ArcCollection arc = ((Net) model).addDefaultArc(pOfArc ,tOfArc , dir);
        Visual arcVisual = view.addVisualFor(arc);
        if(arcVisual != null) {
          view.setSelectionTo(arcVisual);
        }
      }
    }
  }
  
  @Override
  public boolean invokedBy(EnumInvocationType invocationType) {
    switch(invocationType) {
      case MousePress: 
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
