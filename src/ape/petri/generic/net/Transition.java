/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

/**
 *
 * @author Gabriel
 */
public class Transition extends Node {
  /** the data of this transition */
  protected TransitionData data;

  public Transition(Net net, TransitionData data) {
    super(net);
    this.data = data;
  }
  
  public TransitionData getData() {
    return data;
  }

  public void setData(TransitionData data) {
    this.data = data;
  }

  @Override
  public EnumElementType getElementType() {
    return EnumElementType.Transition;
  }
}
