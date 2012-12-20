/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.modelview.pt;

import ape.petri.generic.net.Transition;
import ape.petri.pt.PTTransitionData;
import ape.ui.modelview.generic.TransitionVisual;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author Gabriel
 */
public class PTTransitionVisual extends TransitionVisual {

  public PTTransitionVisual(Graphics2D superGraphics, Transition transition) {
    super(superGraphics, transition);
  }
  
  public PTTransitionVisual(Graphics2D superGraphics, Point position, Transition transition) {
    super(superGraphics, position, transition);
  }

  @Override
  protected void updateDataVisualContent() {}
  
  @Override
  public String getElementTypeName() {
    return "Transition of a P/T net";
  }  
}
