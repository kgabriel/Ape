/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.pt;

import ape.petri.generic.net.ArcCollectionData;
import ape.petri.generic.net.EnumArcDirection;
import ape.petri.pt.PTArcCollectionData;
import ape.ui.graphics.modelview.generic.ArcVisual;
import ape.ui.graphics.modelview.generic.PlaceVisual;
import ape.ui.graphics.modelview.generic.TextVisual;
import ape.ui.graphics.modelview.generic.TransitionVisual;
import java.awt.Graphics2D;

/**
 *
 * @author Gabriel
 */
public class PTArcVisual extends ArcVisual {

  public PTArcVisual(Graphics2D superGraphics, PlaceVisual pv, TransitionVisual tv, EnumArcDirection dir, PTArcCollectionData data, int modelElementId) {
    super(superGraphics, pv, tv, dir, data, modelElementId);
    updateLabelContent();
  }
  
  @Override
  protected void updateLabelContent() {
    TextVisual label = getLabel();
    /* count the weight on the arc */
    int weight = ((ArcCollectionData) data).size();
    label.setText("" + weight);
    /* hide the label if the weight is less than 2 */
    label.setHidden(weight < 2);
  }
}
