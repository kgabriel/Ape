/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.pt;

import ape.petri.pt.PTTransitionData;
import ape.ui.graphics.modelview.generic.TransitionVisual;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author Gabriel
 */
public class PTTransitionVisual extends TransitionVisual {

  public PTTransitionVisual(Graphics2D superGraphics, PTTransitionData data, int modelElementId) {
    super(superGraphics, data, modelElementId);
  }
  
  public PTTransitionVisual(Graphics2D superGraphics, Point position, PTTransitionData data, int modelElementId) {
    super(superGraphics, position, data, modelElementId);
  }

  @Override
  protected void updateDataVisualContent() {}
  
  @Override
  protected void updateExtendedDataVisualContent() {}
}
