/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

import java.util.Objects;

/**
 * A <code>SimpleArc</code> is a simple arc between a place and a transition with a
 * specific direction. Two <code>SimpleArc</code>s are equal, if they have the same
 * place, transition and direction.
 * <br>
 * Note, that a <code>SimpleArc</code> does not depend on a net and therefore
 * <b><code>SimpleArc</code>s of different nets should not get mixed up!</b>
 * @author Gabriel
 */
public class SimpleArc implements Arc {
  
  private final Place place;
  private final Transition transition;
  private final EnumArcDirection direction;
  
  public SimpleArc(Place p, Transition t, EnumArcDirection dir) {
    place = p;
    transition = t;
    direction = dir;
  }

  @Override
  public Place getPlace() {
    return place;
  }

  @Override
  public Transition getTransition() {
    return transition;
  }

  @Override
  public Node getSource() {
    switch(direction){
      case PT: return place;
      case TP: return transition;
      default: return null;
    }
  }

  @Override
  public Node getTarget() {
    switch(direction){
      case TP: return place;
      case PT: return transition;
      default: return null;
    }
  }

  @Override
  public EnumArcDirection getDirection() {
    return direction;
  }
    
  @Override
  public boolean equals(Object o) {
    if(!(o instanceof SimpleArc)) return false;
    SimpleArc other = (SimpleArc) o;
    return this.direction == other.direction && this.place.equals(other.place) && this.transition.equals(other.transition);
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 37 * hash + Objects.hashCode(this.place);
    hash = 37 * hash + Objects.hashCode(this.transition);
    hash = 37 * hash + (this.direction != null ? this.direction.hashCode() : 0);
    return hash;
  }

}
