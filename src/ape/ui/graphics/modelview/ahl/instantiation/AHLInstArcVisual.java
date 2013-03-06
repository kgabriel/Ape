/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.ahl.instantiation;

import ape.petri.generic.net.ArcCollection;
import ape.petri.generic.net.EnumArcDirection;
import ape.ui.graphics.modelview.ahl.AHLArcVisual;
import ape.ui.graphics.modelview.generic.PlaceVisual;
import ape.ui.graphics.modelview.generic.TransitionVisual;
import java.awt.Graphics2D;

/**
 *
 * @author Gabriel
 */
public class AHLInstArcVisual extends AHLArcVisual {

  public AHLInstArcVisual(Graphics2D superGraphics, PlaceVisual pv, TransitionVisual tv, EnumArcDirection dir, ArcCollection arc) {
    super(superGraphics, pv, tv, dir, arc);
  }
}
