/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.ahl.instantiation;

import ape.petri.ahl.instantiation.AHLInstTransitionData;
import ape.prolog.Atom;
import ape.prolog.ValueTerm;
import ape.ui.graphics.modelview.ahl.AHLTransitionVisual;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Map;

/**
 *
 * @author Gabriel
 */
public class AHLInstTransitionVisual extends AHLTransitionVisual {

  public AHLInstTransitionVisual(Graphics2D superGraphics, Point position, AHLInstTransitionData data, int modelElementId) {
    super(superGraphics, position, data, modelElementId);
  }

  public AHLInstTransitionVisual(Graphics2D superGraphics, AHLInstTransitionData data, int modelElementId) {
    this(superGraphics, new Point(0,0), data, modelElementId);
  }
  
  @Override
  protected void updateExtendedDataVisualContent() {
    AHLInstTransitionData ahlInstData = (AHLInstTransitionData) data;
    Map<Atom,ValueTerm> assignment = ahlInstData.getAssignment();
    String text = "";
    boolean first = true;
    for(Atom var : assignment.keySet()) {
      if(! first) text += ", ";
      text += var.toString() + " \u21A6 " + assignment.get(var);
      first = false;
    }
    extendedDataVisual.setText(text);
    updateSize();
  }
  
  
}
