/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

import java.util.Collection;
import java.util.HashSet;

/**
 * A node is an abstract superclass of {@link Place} and {@link Transition}.
 * The class already contains the registration and de-registration of incoming and
 * outgoing arcs. Every proper {@link Arc} in a net registers itself to the nodes it is connected
 * to on creation, and de-registers itself when removed. This ensures that every <code>Node</code>
 * knows, which <code>Arc</code>s are connected to it.
 * @author Gabriel
 */
public abstract class Node extends AbstractNetElement {

  /** A collection of all arcs that have this node as their target node. */
  protected Collection<ArcCollection> incomingArcs;
  
  /** A collection of all arcs that have this node as their source node. */
  protected Collection<ArcCollection> outgoingArcs;

  /**
   * A new node in the given net.
   * @param net the net to contain this node
   */
  public Node(Net net) {
    super(net);
    incomingArcs = new HashSet<>();
    outgoingArcs = new HashSet<>();
  }
  

  /**
   * Registers an arc to this node. The arc is stored in {@link Node#incomingArcs} or
   * {@link Node#outgoingArcs}.
   * @param arc the arc to be registered
   * @param incoming <code>true</code> if the arc is incoming, 
   * <code>false</code> if it is outgoing
   */
  public void registerArc(ArcCollection arc, boolean incoming) {
    if(incoming) {
      incomingArcs.add(arc);
    } else {
      outgoingArcs.add(arc);
    }
  }

  /**
   * De-registers an arc from this node. This means, that it is removed from
   * {@link Node#incomingArcs} or {@link Node#outgoingArcs}, if it is present
   * in one of these collections.
   * @param arc the arc to be de-registered
   * @return <code>true</code> if the arc was registered to this node as incoming or outgoing arc
   * 
   */
  public boolean deregisterArc(ArcCollection arc) {
    return incomingArcs.remove(arc) | outgoingArcs.remove(arc);
  }

  /**
   * Returns a collection of all the arcs that have this node as their target node.
   * @return a collection of all incoming arcs, registered to this node
   */
  public Collection<ArcCollection> getIncomingArcs() {
    return incomingArcs;
  }

  /**
   * Returns a collection of all the arcs that have this node as their source node.
   * @return a collection of all outgoing arcs, registered to this node
   */
  public Collection<ArcCollection> getOutgoingArcs() {
    return outgoingArcs;
  }
}
