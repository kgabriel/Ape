/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

import ape.petri.exception.ArcException;
import ape.petri.generic.EnumModelType;
import ape.petri.generic.Model;
import ape.petri.generic.ModelElement;
import java.util.*;

/**
 * This class is an abstract implementation of a Petri net. Every net is typed over a specific
 * net type, and every {@link NetElement} shares the net type of the
 * net containing it.
 * <br />
 * A Petri net has a set of {@link Place}s and a set of {@link Transition}s. Moreover,
 * it consists of arcs, that are organized in a two-level architecture. The first level
 * is a bipartite SimpleGraph of {@link ArcCollection}s, where a SimpleGraph is a graph with at most
 * one arc from one node to another one. Of course, the two partitions of the bipartite SimpleGraph
 * are the sets of places and transitions.
 * <br />
 * The second level of the organization of arcs are {@link ArcElement}s, stored in the
 * {@link ArcCollection}s. Every <code>ArcElement</code> is stored in an <code>ArcCollection</code>,
 * and an <code>ArcCollection</code> that does not contain any <code>ArcElement</code>s is
 * automatically getting removed from the net.
 * <br />
 * In order to use this class, first, one has to define a
 * corresponding subclasses of {@link PlaceData}, {@link TransitionData} and {@link ArcElementData},
 * specifying the data of places, transitions and arcs, respectively, in that net type.
 * Second, one has to implement a subclass of {@link NetElementFactory} for that net type,
 * that provides the corresponding net elements.
 * <br />
 * Finally, a subclass of <code>Net</code> has to be implemented.
 * @author Gabriel
 * @see EnumNetType
 */
public abstract class Net implements Model {

  /** the next free Id in this net */
  private int freeId;
  
  /** the places of this net */
  protected Set<Place> places;
  
  /** the transitions of this net */
  protected Set<Transition> transitions;
  
  /** the arcs of this net, stored in a map, where the keys are {@link SimpleArc}s */
  protected Map<SimpleArc,ArcCollection> arcs;
  
  /** the factory providing net elements for this net's net type */
  protected NetElementFactory factory;
  
  private EnumNetType netType;
  
    
  /**
   * Constructor that should be called by every concrete net implementation. 
   * @param netType the net type of this net
   */
  public Net(EnumNetType netType) {
    places = new HashSet<>();
    transitions = new HashSet<>();
    arcs = new HashMap<>();
    freeId = 0;
    this.netType = netType;
    initFactory();
  }
  
  private void initFactory() {
    factory = new NetElementFactory(this);
  }

  @Override
  public EnumNetType getNetType() {
    return netType;
  }

  @Override
  public Collection<ModelElement> getAllElements() {
    int size = places.size() + transitions.size() + arcs.values().size();
    Collection<ModelElement> elements = new ArrayList<>(size);
    elements.addAll(places);
    elements.addAll(transitions);
    elements.addAll(arcs.values());
    return elements;
  }
  
  
   /**
    * Returns a collection containing all the nodes (places and transitions) in this net. 
    * It is recommended to use {@link Net#getPlaces()} and {@link Net#getTransitions()} instead.
    * @return a collection containing all nodes in the net
    * @see Net#getPlaces() 
    * @see Net#getTransitions() 
    */
  public Collection<Node> getNodes() {
    Collection<Node> nodes = new ArrayList<>(places.size() + transitions.size());
    nodes.addAll(places);
    nodes.addAll(transitions);
    return nodes;
  }

  /**
    * Returns the places in this net.
    * @return a collection containing all places in the net
    */
  public Collection<Place> getPlaces() {
    return new ArrayList<>(places);
  }

  /**
    * Returns the transitions in this net.
    * @return a collection containing all transitions in the net
    */
  public Collection<Transition> getTransitions() {
    return new ArrayList<>(transitions);
  }
  
  /**
  * Returns the arcs in this net.
  * @return a collection containing all arcs in the net
  */
  public Collection<ArcCollection> getArcs() {
    return arcs.values();
  }
  
  public boolean hasArc(Place p, Transition t, EnumArcDirection dir) {
    return arcs.containsKey(new SimpleArc(p, t, dir));
  }
  
  /**
   * Adds a new place to this net with the specified data.
   * @param data the data of the place
   * @return the newly created place
   */
  public Place addPlace(PlaceData data) {
    return addPlace(factory.createPlace(data));
  }
  
  /**
   * Adds a new place to this net with default data for the type of this net.
   * @return the newly created place
   * @see Net#addPlace(ape.petri.generic.PlaceData) 
   * @see NetElementFactory#createDefaultPlaceData() 
   */
  public Place addDefaultPlace() {
    return addPlace(factory.createDefaultPlace());
  }
  
  private Place addPlace(Place p) {
    places.add(p);
    return p;
  }
    
  /**
   * Adds a new transition to this net with the specified data.
   * @param data the data of the transition
   * @return the newly created transition
   */
  public Transition addTransition(TransitionData data) {
    return addTransition(factory.createTransition(data));
  }
  
  /**
   * Adds a new transition to this net with default data for the type of this net.
   * @return the newly created transition
   * @see Net#addTransition(ape.petri.generic.TransitionData) 
   * @see NetElementFactory#createDefaultTransition() 
   */
  public Transition addDefaultTransition() {
    return addTransition(factory.createDefaultTransition());
  }
  
  private Transition addTransition(Transition t) {
    transitions.add(t);
    return t;
  }
    
  /**
   * Returns an existing {@link ArcCollection} between a place and transition with given
   * direction in this net or creates a new one, if it does not exist.
   * @param p the place of the arc in request
   * @param t the transition of the arc in request
   * @param dir the direction of the arc in request
   * @return 
   */
  private ArcCollection getOrCreateArcCollection(Place p, Transition t, EnumArcDirection dir) {
    SimpleArc simArc = new SimpleArc(p,t,dir);
    ArcCollection collection = arcs.get(simArc);
    if(collection == null) {
      collection = new ArcCollection(this,p,t,dir);
      arcs.put(simArc,collection);
    }
    return collection;
  }
  
  /**
   * Adds a new {@link ArcElement} to this net.
   * If there already is an existing {@link ArcCollection} between the specified
   * nodes, the element is added to that collection. Otherwise, a new collection
   * is created.
   * @param p the place of the arc
   * @param t the transition of the arc
   * @param direction the direction of the arc
   * @param data the data of the arc
   * @return the collection, containing the newly created arc in this net
   */
  public ArcCollection addArc(Place p, Transition t, EnumArcDirection direction, ArcElementData data) {
    checkArcNet(p,t);
    ArcCollection collection = getOrCreateArcCollection(p,t,direction);
    return addArc(collection, factory.createArcElement(collection, data));
  }
  
  /**
   * Adds a new {@link ArcElement} to this net with default data for the type of the net.
   * @param p the place of the arc
   * @param t the transition of the arc
   * @return the collection, containing the the newly created arc in this net
   * @see Net#addArc(ape.petri.generic.Place, ape.petri.generic.Transition, ape.petri.generic.EnumArcDirection, ape.petri.generic.ArcElementData) 
   * @see NetElementFactory#createDefaultArcElementData(ape.petri.generic.ArcCollection) 
   */
  public ArcCollection addDefaultArc(Place p, Transition t, EnumArcDirection direction) {
    checkArcNet(p,t);
    ArcCollection collection = getOrCreateArcCollection(p,t,direction);
    return addArc(collection, factory.createDefaultArcElement(collection));
  }
  
  /** 
   * Checks whether the given place and transition are in this net, and throws an
   * {@link ArcException} if at least one is not.
   */
  private void checkArcNet(Place p, Transition t) {
    if(p.getNet() != this || t.getNet() != this) {
      throw new ArcException(p.getNet(), t.getNet());
    }
  }
  
  private ArcCollection addArc(ArcCollection collection, ArcElement arc) {
    collection.addFreshElement(arc);
    return collection;
    
  }

  /**
   * Removes an {@link ArcCollection} from this net. This involves removing the arc
   * from this net's {@link Net#arcs} map, as well as the destruction of the arc. That is,
   * the place and transition of the <code>ArcCollection</code> are set to null,
   * with the effect that it is de-registered from the previously set nodes.
   * @param arc the <code>ArcCollection</code> to be removed
   * @return <code>true</code> if the <code>ArcCollection</code> actually was a part of this net
   */
  public boolean removeArcCollection(ArcCollection arc) {
    ArcCollection removed = arcs.remove(arc.toSimpleArc());
    if(removed == null) return false;
    removed.setPlace(null);
    removed.setTransition(null);
    return true;    
  }
  
  /**
   * Removes an {@link ArcElement} from this net. This involves removing it from its
   * collection, and, if this results in an empty {@link ArcCollection}, also its
   * <code>ArcCollection</code> is removed from the net.
   * @param arc the <code>ArcElement</code> to be removed
   * @return <code>true</code> if <code>arc</code> actually was a part of this net
   */
  public boolean removeArcElement(ArcElement arc) {
    ArcCollection collection = arcs.get(arc.toSimpleArc());
    if(collection == null) return false;
    boolean removed = collection.remove(arc);
    if(! removed) return false;
    if(collection.isEmpty()) {
      removeArcCollection(collection);
    }
    return true;
  }
  
  /**
   * Removes a {@link Place} from this net. This involves removing it from this
   * net's {@link Net#places} set, as well as removing all arcs that are connected to it
   * using the {@link Net#removeArcCollection(ape.petri.generic.ArcCollection)} method.
   * @param p the place to be removed
   * @return <code>true</code> if <code>p</code> actually was a part of this net
   */
  public boolean removePlace(Place p) {
    if(! places.contains(p)) return false;
    for(ArcCollection incoming : p.getIncomingArcs()) {
      removeArcCollection(incoming);
    }
    for(ArcCollection outgoing : p.getOutgoingArcs()) {
      removeArcCollection(outgoing);
    }
    return places.remove(p);
  }
  
  /**
   * Removes a {@link Transition} from this net. This involves removing it from this
   * net's {@link Net#transitions} set, as well as removing all arcs that are connected to it
   * using the {@link Net#removeArcCollection(ape.petri.generic.ArcCollection)} method.
   * @param t the transition to be removed
   * @return <code>true</code> if <code>t</code> actually was a part of this net
   */
  public boolean removeTransition(Transition t) {
    if(! transitions.contains(t)) return false;
    for(ArcCollection incoming : t.getIncomingArcs()) {
      removeArcCollection(incoming);
    }
    for(ArcCollection outgoing : t.getOutgoingArcs()) {
      removeArcCollection(outgoing);
    }
    return transitions.remove(t);
  }
  
    
  /** 
   * This method gives an unused Id for a new element. It returns the current value
   * of an integer variable, increasing on every call.
   * @return a fresh Id
   */
  public int freeElementId() {
    return freeId++;
  }

  /**
   * Returns the model type of this model.
   * @return {@link EnumModelType#Net}
   */
  @Override
  public EnumModelType getModelType() {
    return EnumModelType.Net;
  }
  
}
