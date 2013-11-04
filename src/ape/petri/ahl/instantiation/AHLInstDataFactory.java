/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.ahl.instantiation;

import ape.petri.generic.net.NetElementDataFactory;
import ape.petri.generic.net.*;
import ape.prolog.Atom;
import ape.prolog.Compound;
import ape.prolog.ValueTerm;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Gabriel
 */
public class AHLInstDataFactory extends NetElementDataFactory {
  
    /**
   * Creates a default place data for the net type of this factory.
   * @return a new data for a place, containing a name <i>"Place i"</i>, where <i>i</i> is a number incrementing
   * on each call of the method
   */
  @Override
  public PlaceData createDefaultPlaceData() {
    return new AHLInstPlaceData(nextPlaceName(), "type_void", null);
  }

  /**
   * Creates a default transition data for the net type of this factory.
   * @return a new transition data, containing a name <i>"Transition i"</i>, where <i>i</i> is a number incrementing
   * on each call of the method
   */
  @Override
  public TransitionData createDefaultTransitionData() {
    return new AHLInstTransitionData(nextTransitionName(), new HashSet<Compound>(), new HashMap<Atom,ValueTerm>());
  }

  /**
   * Creates a default arc element data for the net type of this factory.
   * @return a new arc element data with default content
   */
  @Override
  public ArcElementData createDefaultArcElementData() {
    return new AHLInstArcElementData("x");
  }

  @Override
  public ArcCollectionData createDefaultArcCollectionData() {
    return new AHLInstArcCollectionData();
  }
}
