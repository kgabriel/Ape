/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.actions.ahlinstantiation;

import ape.Ape;
import ape.petri.ahl.instantiation.AHLInstPlaceData;
import ape.petri.ahl.instantiation.AHLInstTransitionData;
import ape.petri.ahl.instantiation.AHLInstantiation;
import ape.petri.generic.net.Place;
import ape.petri.generic.net.Transition;
import ape.prolog.Atom;
import ape.prolog.ValueTerm;
import ape.ui.control.actions.ModelAction;
import java.util.HashMap;

/**
 *
 * @author Gabriel
 */
public class ClearInstantiationAction extends ModelAction {

  public ClearInstantiationAction(Ape theApe) {
    super(theApe, "Clear Instantiation");
  }

  @Override
  protected void onInvocation() {
    AHLInstantiation inst = (AHLInstantiation) getModel();
    for(Place p : inst.getPlaces()) {
      AHLInstPlaceData data = (AHLInstPlaceData) p.getData();
      data.setPlaceValue(null);
    }
    for(Transition t : inst.getTransitions()) {
      AHLInstTransitionData data = (AHLInstTransitionData) t.getData();
      data.setAssignment(new HashMap<Atom,ValueTerm>());
    }
    theApe.ui.modelViewCanvas.repaint();
  }
  
}
