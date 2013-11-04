/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.ahl;

import ape.petri.ahl.AHLTransitionData;
import ape.prolog.Prolog;
import ape.ui.graphics.modelview.generic.TransitionVisual;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author Gabriel
 */
public class AHLTransitionVisual extends TransitionVisual {

  public AHLTransitionVisual(Graphics2D superGraphics, AHLTransitionData data, int modelElementId) {
    this(superGraphics, new Point(0,0), data, modelElementId);
  }
  
  public AHLTransitionVisual(Graphics2D superGraphics, Point position, AHLTransitionData data, int modelElementId) {
    super(superGraphics, position, data, modelElementId);
    updateDataVisualContent();
  }
  
  @Override
  protected void updateDataVisualContent() {
    AHLTransitionData ahlData = (AHLTransitionData) data;
    dataVisual.setText(Prolog.toString(ahlData.getConditions()));
    updateSize();
  }

  @Override
  protected void updateExtendedDataVisualContent() {}
  
}
