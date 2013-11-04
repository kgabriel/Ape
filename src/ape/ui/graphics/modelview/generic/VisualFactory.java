/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.generic;

import ape.petri.generic.ModelElement;
import ape.petri.generic.net.ArcCollection;
import ape.petri.generic.net.EnumNetElementType;
import ape.petri.generic.net.Place;
import ape.petri.generic.net.Transition;
import ape.ui.graphics.modelview.ModelView;

/**
 *
 * @author Gabriel
 */
public abstract class VisualFactory {
  
  protected ModelView modelView;
  
  public VisualFactory(ModelView modelView) {
    this.modelView = modelView;
  }
  
  public Visual createVisual(ModelElement e) {
    EnumNetElementType elementType = e.getElementType();
    switch(elementType) {
      case Place:
        return createPlaceVisual((Place) e);
      case Transition:
        return createTransitionVisual((Transition) e);
      case ArcCollection:
        return createArcVisual((ArcCollection) e);
      default:
        throw new UnsupportedOperationException("Visual creation for model elements of type '" + elementType + "' not yet implemented.");
    }
  }
  
  public abstract PlaceVisual createPlaceVisual(Place p);
  
  public abstract TransitionVisual createTransitionVisual(Transition t);
  
  public abstract ArcVisual createArcVisual(ArcCollection a);
}
