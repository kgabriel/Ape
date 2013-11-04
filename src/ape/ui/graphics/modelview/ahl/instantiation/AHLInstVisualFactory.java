/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.ahl.instantiation;

import ape.petri.ahl.instantiation.AHLInstArcCollectionData;
import ape.petri.ahl.instantiation.AHLInstPlaceData;
import ape.petri.ahl.instantiation.AHLInstTransitionData;
import ape.petri.generic.net.ArcCollection;
import ape.petri.generic.net.Place;
import ape.petri.generic.net.Transition;
import ape.ui.graphics.modelview.ModelView;
import ape.ui.graphics.modelview.generic.ArcVisual;
import ape.ui.graphics.modelview.generic.PlaceVisual;
import ape.ui.graphics.modelview.generic.TransitionVisual;
import ape.ui.graphics.modelview.generic.VisualFactory;

/**
 *
 * @author Gabriel
 */
public class AHLInstVisualFactory extends VisualFactory {
  
  public AHLInstVisualFactory(ModelView modelView) {
    super(modelView);
  }
  
  @Override
  public PlaceVisual createPlaceVisual(Place p) {
    return new AHLInstPlaceVisual(modelView.getCurrentGraphics(), (AHLInstPlaceData) p.getData(), p.getId());
  }
  
  @Override
  public TransitionVisual createTransitionVisual(Transition t) {
    return new AHLInstTransitionVisual(modelView.getCurrentGraphics(), (AHLInstTransitionData) t.getData(), t.getId());
  }
  
  @Override
  public ArcVisual createArcVisual(ArcCollection a) {
    PlaceVisual pv = (PlaceVisual) modelView.getVisual(a.getPlace());
    TransitionVisual tv = (TransitionVisual) modelView.getVisual(a.getTransition());
    return new AHLInstArcVisual(modelView.getCurrentGraphics(), pv, tv, a.getDirection(), (AHLInstArcCollectionData) a.getData(), a.getId());
  }
}
