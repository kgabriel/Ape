/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

import ape.petri.exception.ArcException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * An <code>ArcCollection</code> is an {@link AbstractNetElement}, representing a
 * bundle of arcs with the same source and target.
 * This class should always be used by a net to manage its single {@link ArcElement ArcElements}.
 * An <code>ArcCollection</code> is a {@link java.util.Collection Collection} of 
 * {@link ArcElement ArcElements} as well as an {@link Arc}.
 * @author Gabriel
 */
public class ArcCollection extends AbstractNetElement implements Arc, Collection<ArcElement> {

  /** a collection holding the single elements of this <code>ArcCollection</code> */
  private Collection<ArcElement> arcs;
  
  /** the place of this arc */ 
  private Place place; 
  
  /** the transition of this arc */
  private Transition transition;
  
  /** the direction of this arc */
  private EnumArcDirection direction;
  
  /** the data of this arc collection */
  private ArcCollectionData data;

  /**
   * A new arc between a place and a transition with the given direction.
   * @param net the net, containing this arc
   * @param p the target place
   * @param t the source transition
   * @param dir the direction of this arc
   * @see EnumArcDirection
   */
  public ArcCollection(Net net, Place p, Transition t, EnumArcDirection dir) {
    super(net);
    direction = dir;
    setAndRegisterPlace(p);
    setAndRegisterTransition(t);
    arcs = new ArrayList<>();
    data = new ArcCollectionData(net.getNetType());
  }
  
  
  /**
   * A new arc from a given source to target
   * @param net the net, containing this arc
   * @param source the new source node
   * @param target the target node
   * @param dir the direction of this arc (place to transition or transition to place)
   * @throws ArcException the type of source or target nodes does not match the given direction
   */
  public ArcCollection(Net net, Node source, Node target, EnumArcDirection dir) throws ArcException {
    super(net);
    direction = dir;
    setAndRegisterNode(source,true);
    setAndRegisterNode(target,false);
    arcs = new ArrayList<>();
    data = new ArcCollectionData(net.getNetType());
  }
  
  /**
   * Returns a simple arc with the same place, transition and direction of this arc collection.
   * @return a simple arc, representing this collection
   */
  public SimpleArc toSimpleArc() {
    return new SimpleArc(place, transition, direction);
  }

  /**
   * Get the place of this arc.
   * @return the place
   */
  @Override
  public Place getPlace() {
    return place;
  }
  
  private void setAndRegisterPlace(Place p) {
    this.place = p;
    if(this.place != null) {
      this.place.deregisterArc(this);
    }
    if(p != null) {
      p.registerArc(this, direction.goesToPlace());
    }
  }

  /**
   * Set the place of this arc. Properly registers this arc at the place, and, if necessary,
   * de-registers it from the previous place
   * @param p the new place for this arc
   */
  public void setPlace(Place p) {
    setAndRegisterPlace(p);
  }

  /**
   * Get the transition of this arc.
   * @return the transition
   */
  @Override
  public Transition getTransition() {
    return transition;
  }

  private void setAndRegisterTransition(Transition t) {
    this.transition = t;
    if(this.transition != null) {
      this.transition.deregisterArc(this);
    }
    if(t != null) {
      t.registerArc(this, direction.goesToTransition());
    }
  }

  /**
   * Set the transition of this arc. Properly registers this arc at the transition, and, if necessary,
   * de-registers it from the previous transition
   * @param t the new transition for this arc
   */
  public void setTransition(Transition t) {
    setAndRegisterTransition(t);
  }

  /**
   * Get the source of this arc.
   * @return the source node
   */  
  @Override
  public Node getSource() {
    switch(direction){
      case PT: return place;
      case TP: return transition;
      default: return null;
    }
  }

  /**
   * Get the target of this arc.
   * @return the target node
   */
  @Override
  public Node getTarget() {
    switch(direction){
      case PT: return transition;
      case TP: return place;
      default: return null;
    }
  }
  
  private void setAndRegisterNode(Node node, boolean source) throws ArcException {
    // set source of TP or target of PT -> set transition
    if((source == direction.goesToPlace())) {
      try { 
        setAndRegisterTransition((Transition) node);
      } catch (ClassCastException e) {
        throw new ArcException("Invalid attempt to connect two places with an arc.");
      }
    // otherwise -> set place
    } else {
        try { 
          setAndRegisterPlace((Place) node);
        } catch (ClassCastException e) {
          throw new ArcException("Invalid attempt to connect two transitions with an arc.");
        }
    }
  }

  /**
   * Sets a node of this arc. 
   * @param node the node that has to be set
   * @param source true if the source node has to be set, false for the target node
   * @throws ArcException the type of the node does not match this arc's direction
   */
  public void setNode(Node node, boolean source) throws ArcException {
    setAndRegisterNode(node, source);
  }

  /**
   * Sets the source node of this arc. 
   * @param node the node that has to be set as the new source
   * @throws ArcException the type of the node does not match this arc's direction
   */
  public void setSource(Node node) throws ArcException {
    setNode(node, true);
  }

  /**
   * Sets the target node of this arc. 
   * @param node the node that has to be set as the new target
   * @throws ArcException the type of the node does not match this arc's direction
   */
  public void setTarget(Node node) throws ArcException {
      setNode(node, false);
  }
    
  /**
   * Get the direction of this arc.
   * @return the enumerator object describing the direction of this arc
   */
  @Override
  public EnumArcDirection getDirection() {
    return direction;
  }

  /** 
   * Adds the data of the specified element to the data of this element.
   * @param e the element which is to be added to this arc collection
   * @see ArcCollectionData
   * @see ArcElementData
   */
  private void addDataOf(ArcElement e) {
    data.addDataElement(e.getData());
  }
  
  /** 
   * Removes the data of the specified element from the data of this element.
   * @param e the element which is to be removed from this arc collection
   * @see ArcCollectionData
   * @see ArcElementData
   */
  private void removeDataOf(ArcElement e) {
    data.removeDataElement(e.getData());
  }

  /**
   * Adds an {@link ArcElement} to this collection. If the given element already
   * is contained in another collection, it is first removed from that collection.
   * If the element already is contained in this collection, nothing happens.
   * This method is more expensive than 
   * {@link ArcCollection#addFreshElement(ape.petri.generic.ArcElement)}, and it
   * is recommended to use that method instead, if the element is surely not used elsewhere.
   * @param e the arc element to be added
   * @return <code>true</code> if this collection has been changed by this call
   * @see ArcCollection#addFreshElement(ape.petri.generic.ArcElement) 
   */
  @Override
  public boolean add(ArcElement e) {
    ArcCollection p = e.getParent();
    if(p.equals(this)) return false;
    if(p != null) p.remove(e);
    e.setParent(this);
    addDataOf(e);
    return arcs.add(e);
  }
  
  /**
   * Adds an {@link ArcElement} to this collection. 
   * If the element already is contained in this collection, nothing happens.
   * This method should only be used with freshly created elements, such that
   * it is sure that these elements are not used elsewhere.
   * @param e the arc element to be added
   * @return <code>true</code> if this collection has been changed by this call
   */
  public boolean addFreshElement(ArcElement e) {
    e.setParent(this);
    addDataOf(e);
    return arcs.add(e);
  }

  
  /**
   * Calls the {@link ArcCollection#add(ape.petri.generic.ArcElement)}-method for each
   * element in the given collection. This method should not be used.
   * @param c a collection, containing arc elements to be added
   * @return <code>true</code> if this collection has been changed by this call
   */
  @Override
  public boolean addAll(Collection<? extends ArcElement> c) {
    boolean changed = false;
    for(ArcElement e : c) {
      changed |= add(e);
    }
    return changed;
  }

  /**
   * Removes all elements from this collection. The parent of each element is set
   * to <code>null</code>.
   */
  @Override
  public void clear() {
    for(ArcElement e : arcs) {
      e.setParent(null);
      removeDataOf(e);
    }
    arcs.clear();
  }

  /**
   * Returns <code>true</code> if the specified element is contained in this collection.
   * @param o element whose presence in this collection is to be tested
   * @return <code>true</code> if this collection contains the specified element
   */
  @Override
  public boolean contains(Object o) {
    return arcs.contains(o);
  }

  
  /**
   * Returns <code>true</code> if all elements in the specified collection are 
   * contained in this collection.
   * @param c a collection of elements whose presence in this collection is to be tested
   * @return <code>true</code> if this collection contains all of the specified elements
   */
  @Override
  public boolean containsAll(Collection<?> c) {
    return arcs.containsAll(c);
  }

  /**
   * Returns <code>true</code> if this collection is empty.
   * @return <code>true</code> if this collection is empty
   */
  @Override
  public boolean isEmpty() {
    return arcs.isEmpty();
  }

  /**
   * Returns an iterator for this collection.
   * @return an iterator for this collection
   */
  @Override
  public Iterator<ArcElement> iterator() {
    return arcs.iterator();
  }

  /**
   * Removes an element from this collection if it is contained. Also, in case of
   * actually removing the object, the parent of the specified element is set to <code>null</code>.
   * @param o the element that has to be removed from this collection
   * @return <code>true</code> if the call of this method changed this collection
   */
  @Override
  public boolean remove(Object o) {
    if(! (o instanceof ArcElement)) return false;
    
    ArcElement e = (ArcElement) o;
    if(contains(e)) {
      e.setParent(null);
      removeDataOf(e);
    }
    return arcs.remove(o);
  }

  /**
   * Removes all elements in this collection that are also contained in the specified collection.
   * @param c collection containing elements to be removed
   * @return <code>true</code> if the content of this collection has been changed
   * @see ArcCollection#remove(java.lang.Object) 
   */
  @Override
  @SuppressWarnings("element-type-mismatch")
  public boolean removeAll(Collection<?> c) {
    boolean changed = false;
    for(Object e : c) {
      changed |= remove(e);
    }
    return changed;
  }

  /**
   * Intersects this collection with the specified one. Elements of this collection are retained
   * only if they are also contained in the specified collection.
   * @param c the collection to intersect with this collection
   * @return <code>true</code> if the content of this collection has been changed
   */
  @Override
  public boolean retainAll(Collection<?> c) {
    boolean changed = false;
    for(ArcElement e : arcs) {
      if(! c.contains(e)) {
        changed |= remove(e);
      }
    }
    return changed;
  }

  /**
   * Returns the number of arc elements in this collection.
   * @return the size of this collection
   */
  @Override
  public int size() {
    return arcs.size();
  }

  /**
   * Turns this collection into an array.
   * @return an array, containing the elements of this collection
   */
  @Override
  public Object[] toArray() {
    return arcs.toArray();
  }

  /**
   * Returns an array containing all of the elements in this collection; 
   * the runtime type of the returned array is that of the specified array. 
   * If the collection fits in the specified array, it is returned therein. 
   * Otherwise, a new array is allocated with the runtime type 
   * of the specified array and the size of this collection.
   * 
   * If this collection fits in the specified array with room to spare 
   * (i.e., the array has more elements than this collection), 
   * the element in the array immediately following the end of the collection is set to null. 
   * (This is useful in determining the length of this collection only if the caller 
   * knows that this collection does not contain any null elements.)    
   * @param a  the array into which the elements of this collection are to be stored, 
   * if it is big enough; 
   * otherwise, a new array of the same runtime type is allocated for this purpose.
   * @param <T> the type of the returned array
   * @return an array, containing the elements of this collection
   */
  @Override
  public <T> T[] toArray(T[] a) {
    return arcs.toArray(a);
  }

  @Override
  public EnumElementType getElementType() {
    return EnumElementType.ArcCollection;
  }

  /**
   * Returns the data of this arc collection.
   * @return the {@link ArcCollectionData} containing all data of the elements in this
   * collection
   */
  public ArcCollectionData getData() {
    return data;
  }
}
