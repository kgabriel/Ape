/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

import ape.petri.exception.ElementTypeException;
import ape.petri.exception.NetTypeException;
import ape.petri.generic.EnumNetType;
import java.io.Serializable;

/**
 * A <code>NetElementFactory</code> is used to provide net elements for a net of a specific
 * net type.
 * @author Gabriel
 */
public class NetElementFactory implements Serializable {
  
  /** the net for which this createDataFactory produces elements */
  private Net net;
  private EnumNetType netType;
  private DataFactory dataFactory;
  
  public NetElementFactory(Net net) {
    this.net = net;
    this.netType = net.getNetType();
    dataFactory = netType.createDataFactory(net);
  }
  
  private void checkNetType(Data data) {
    if(data.getNetType() != netType) {
      throw new NetTypeException(netType, data.getNetType());
    }
  }
  
  /**
   * Creates a new place that can be added to this createDataFactory's net.
   * @param data the data, the new place should be equipped with
   * @return a new place belonging to this createDataFactory's net
   */
  public Place createPlace(PlaceData data) {
   checkNetType(data);
   return new Place(net, data);
  }
  
  /**
   * Creates a new default place that can be added to this createDataFactory's net.
   * @return a new place belonging to this createDataFactory's net
   */
  public Place createDefaultPlace() {
    return createPlace(dataFactory.createDefaultPlaceData());
  }

  /**
   * Creates a new transition that can be added to this createDataFactory's net.
   * @param data the data, the new transition should be equipped with
   * @return a new transition belonging to this createDataFactory's net
   */
  public Transition createTransition(TransitionData data) {
   checkNetType(data);
   return new Transition(net, data);
  }
  
  /**
   * Creates a new default transition that can be added to this createDataFactory's net.
   * @return a new transition belonging to this createDataFactory's net
   */
  public Transition createDefaultTransition() {
    return createTransition(dataFactory.createDefaultTransitionData());
  }
  
  /**
   * Creates a new arc element that can be added to this 
   * @param data the data, the new arc should be equipped with
   * @return a new arc belonging to this createDataFactory's net
   */
  public ArcElement createArcElement(ArcCollection collection, ArcElementData data) {
    checkNetType(data);
    return new ArcElement(collection, data);
  }
  
  public ArcElement createDefaultArcElement(ArcCollection collection) {
    return createArcElement(collection, dataFactory.createDefaultArcElementData());
  }
  
  /**
   * Creates a new node for the specified data. The type of the node is determined
   * by the {@link EnumElementType} returned by the data.
   * @param data the data for which a node is to be created
   * @return a new node carrying the specified data
   */
  public Node createNode(Data data) {
    switch(data.getElementType()) {
      case Place:
        return createPlace((PlaceData) data);
      case Transition:
        return createTransition((TransitionData) data);
      default:
        throw new ElementTypeException("Expected " + EnumElementType.Place + " or " + EnumElementType.Transition + ". Found " + data.getElementType());
    }
  }


  /**
   * Returns the net for which this createDataFactory produces elements.
   * @return this createDataFactory's net
   */
  public Net getNet() {
    return net;
  }

  public TransitionData createDefaultTransitionData() {
    return dataFactory.createDefaultTransitionData();
  }

  public PlaceData createDefaultPlaceData() {
    return dataFactory.createDefaultPlaceData();
  }

  public ArcElementData createDefaultArcElementData() {
    return dataFactory.createDefaultArcElementData();
  }
}
