/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

import java.io.Serializable;

/**
 *
 * @author Gabriel
 */
public abstract class DataFactory implements Serializable {
  
  private Net net;
  private int nextPlaceNumber;
  private int nextTransitionNumber;
  
  public DataFactory(Net net) {
    this.net = net;
    nextPlaceNumber = 1;
    nextTransitionNumber = 1;
  }
 
  /**
   * Creates a default place data for the net type of this factory.
   * @return a new data for a place
   * on each call of the method
   */
  public abstract PlaceData createDefaultPlaceData();
  
  /**
   * Creates a default transition data for the net type of this factory.
   * @return a new transition data
   * on each call of the method
   */
  public abstract TransitionData createDefaultTransitionData();

  /**
   * Creates a default arc element data for the net type of this factory.
   * @return a new arc element data with default content
   */
  public abstract ArcElementData createDefaultArcElementData(ArcCollection collection);
  
  protected String nextPlaceName() {
    return "Place " + nextPlaceNumber++;
  }
  
  protected String nextTransitionName() {
    return "Transition " + nextTransitionNumber++;
  }
}
