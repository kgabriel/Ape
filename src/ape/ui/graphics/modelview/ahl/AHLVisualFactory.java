/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.ahl;

import ape.petri.ahl.AHLArcCollectionData;
import ape.petri.ahl.AHLPlaceData;
import ape.petri.ahl.AHLTransitionData;
import ape.ui.graphics.modelview.generic.*;
import ape.petri.generic.net.*;
import ape.ui.graphics.modelview.ModelView;

/**
 *
 * @author Gabriel
 */
public class AHLVisualFactory extends VisualFactory {
  
  public AHLVisualFactory(ModelView modelView) {
    super(modelView);
  }
  
  @Override
  public PlaceVisual createPlaceVisual(Place p) {
    return new AHLPlaceVisual(modelView.getCurrentGraphics(), (AHLPlaceData) p.getData(), p.getId());
  }
  
  @Override
  public TransitionVisual createTransitionVisual(Transition t) {
    return new AHLTransitionVisual(modelView.getCurrentGraphics(), (AHLTransitionData) t.getData(), t.getId());
  }
  
  @Override
  public ArcVisual createArcVisual(ArcCollection a) {
    PlaceVisual pv = (PlaceVisual) modelView.getVisual(a.getPlace());
    TransitionVisual tv = (TransitionVisual) modelView.getVisual(a.getTransition());
    return new AHLArcVisual(modelView.getCurrentGraphics(), pv, tv, a.getDirection(), (AHLArcCollectionData) a.getData(), a.getId());
  }
}
