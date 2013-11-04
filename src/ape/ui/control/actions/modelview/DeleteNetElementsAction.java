/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.actions.modelview;

import ape.Ape;
import ape.petri.generic.ModelElement;
import ape.petri.generic.net.*;
import ape.ui.control.actions.ModelAction;
import ape.ui.graphics.modelview.ModelView;
import java.util.Set;

/**
 *
 * @author Gabriel
 */
public class DeleteNetElementsAction extends ModelAction {

  public DeleteNetElementsAction(Ape theApe) {
    super(theApe, "Delete Selection");
  }

  @Override
  protected void onInvocation() {
    ModelView modelView = getView();
    Net net = (Net) modelView.getModel();
    Set<ModelElement> selection = modelView.getSelection();
    for(ModelElement element : selection) {
      if(element instanceof Place) {
        removeDanglingArcVisuals((Node) element);
        net.removePlace((Place) element);
      } else if(element instanceof Transition) {
        removeDanglingArcVisuals((Node) element);
        net.removeTransition((Transition) element);
      } else if(element instanceof ArcCollection) {
        net.removeArcCollection((ArcCollection) element);
      }
      modelView.removeVisualFor(element);
    }
    modelView.repaint();
  }
  
  private void removeDanglingArcVisuals(Node n) {
    ModelView modelView = getView();
    for(ArcCollection c: n.getIncomingArcs()) {
      modelView.removeVisualFor(c);
    }
    for(ArcCollection c: n.getOutgoingArcs()) {
      modelView.removeVisualFor(c);
    }
  }
}
