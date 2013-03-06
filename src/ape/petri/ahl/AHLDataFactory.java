/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.ahl;

import ape.petri.generic.net.*;

/**
 *
 * @author Gabriel
 */
public class AHLDataFactory extends DataFactory {

  public AHLDataFactory(Net net) {
    super(net);
  }
  
    /**
   * Creates a default place data for the net type of this factory.
   * @return a new data for a place, containing a name <i>"Place i"</i>, where <i>i</i> is a number incrementing
   * on each call of the method
   */
  @Override
  public PlaceData createDefaultPlaceData() {
    return new AHLPlaceData(nextPlaceName(),"type_void");
  }

  /**
   * Creates a default transition data for the net type of this factory.
   * @return a new transition data, containing a name <i>"Transition i"</i>, where <i>i</i> is a number incrementing
   * on each call of the method
   */
  @Override
  public TransitionData createDefaultTransitionData() {
    return new AHLTransitionData(nextTransitionName());
  }

  /**
   * Creates a default arc element data for the net type of this factory.
   * @return a new arc element data with default content
   */
  @Override
  public ArcElementData createDefaultArcElementData() {
    return new AHLArcElementData("x");
  }

}
