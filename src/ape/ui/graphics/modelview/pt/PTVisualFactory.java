/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.pt;

import ape.ui.graphics.modelview.generic.*;
import ape.petri.generic.net.*;
import ape.petri.pt.PTArcCollectionData;
import ape.petri.pt.PTPlaceData;
import ape.petri.pt.PTTransitionData;
import ape.ui.graphics.modelview.ModelView;

/**
 *
 * @author Gabriel
 */
public class PTVisualFactory extends VisualFactory {
  
  public PTVisualFactory(ModelView modelView) {
    super(modelView);
  }
  
  @Override
  public PlaceVisual createPlaceVisual(Place p) {
    return new PTPlaceVisual(modelView.getCurrentGraphics(), (PTPlaceData) p.getData(), p.getId());
  }
  
  @Override
  public TransitionVisual createTransitionVisual(Transition t) {
    return new PTTransitionVisual(modelView.getCurrentGraphics(), (PTTransitionData) t.getData(), t.getId());
  }
  
  @Override
  public ArcVisual createArcVisual(ArcCollection a) {
    PlaceVisual pv = (PlaceVisual) modelView.getVisual(a.getPlace());
    TransitionVisual tv = (TransitionVisual) modelView.getVisual(a.getTransition());
    return new PTArcVisual(modelView.getCurrentGraphics(), pv, tv, a.getDirection(), (PTArcCollectionData) a.getData(), a.getId());
  }
}
