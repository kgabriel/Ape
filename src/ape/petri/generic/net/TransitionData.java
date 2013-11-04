/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

/**
 * This interface is used to define the data carried by a transition.
 * @author Gabriel
 */
public abstract class TransitionData extends NodeData {

  public TransitionData(String name) {
    super(name);
  }
}
