/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.modelview.generic;

import ape.petri.generic.EnumModelType;
import ape.petri.generic.ModelElement;
import ape.petri.generic.net.*;
import ape.ui.modelview.pt.PTArcVisual;
import ape.ui.modelview.pt.PTPlaceVisual;
import ape.ui.modelview.pt.PTTransitionVisual;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gabriel
 */
public class VisualFactory implements Serializable {
  
  protected ModelView modelView;
  
  public VisualFactory(ModelView modelView) {
    this.modelView = modelView;
  }
  
  public Map<ModelElement,Visual> createVisuals(Collection<ModelElement> elements) {
    Map<ModelElement,Visual> visuals = new HashMap<>();
    for(ModelElement e : elements) {
      Visual v = createVisual(e);
      /* it may be necessary for some elements like arcs to postpone them and
       * try again later when the visuals are created upon which they depend;
       * this could be done in a generic way by returning null on the first try and collecting all
       * elements returning null for a second run
       */
      visuals.put(e, v);
    }
    return visuals;
  }
  
  public Visual createVisual(ModelElement e) {
    switch(modelType()) {
      case Net:
        return createVisual((NetElement) e);
      default:
        throw new UnsupportedOperationException("Generic visual creation not yet implemented for model type " + modelType() + ".");
    }
  }
  
  public Visual createVisual(NetElement e) {
    switch(e.getElementType()) {
      case Place:
        return createPlaceVisual((Place) e);
      case Transition:
        return createTransitionVisual((Transition) e);
      case ArcCollection:
        return createArcVisual((ArcCollection) e);
      default:
        throw new UnsupportedOperationException("Generic visual creation not yet implemented for element type " + e.getElementType() + ".");
    }
  }

  private PlaceVisual createPlaceVisual(Place p) {
    switch(netType()) {
      case PTNet:
        return new PTPlaceVisual(modelView.getCurrentGraphics(), p);
      default:
        throw new UnsupportedOperationException("Place visual creation not yet implemented for net type " + netType() + ".");
    }
  }
  
  private TransitionVisual createTransitionVisual(Transition t) {
    switch(netType()) {
      case PTNet:
        return new PTTransitionVisual(modelView.getCurrentGraphics(), t);
      default:
        throw new UnsupportedOperationException("Transition visual creation not yet implemented for net type " + netType() + ".");
    }
  }
  
  private ArcVisual createArcVisual(ArcCollection a) {
    PlaceVisual pv = (PlaceVisual) modelView.getVisual(a.getPlace());
    TransitionVisual tv = (TransitionVisual) modelView.getVisual(a.getTransition());
    EnumArcDirection dir = a.getDirection();
    switch(netType()) {
      case PTNet:
        return new PTArcVisual(modelView.getCurrentGraphics(), pv, tv, dir, a);
      default:
        throw new UnsupportedOperationException("Transition visual creation not yet implemented for net type " + netType() + ".");
    }
  }

  private EnumModelType modelType() {
    return modelView.getModelType();
  }
  
  private EnumNetType netType() {
    Net net = (Net) modelView.getModel();
    return net.getNetType();
  }
}
