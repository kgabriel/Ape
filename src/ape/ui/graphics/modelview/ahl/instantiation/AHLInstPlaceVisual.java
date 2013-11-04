/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.ahl.instantiation;

import ape.petri.ahl.instantiation.AHLInstPlaceData;
import ape.prolog.ValueTerm;
import ape.ui.graphics.modelview.ahl.AHLPlaceVisual;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author Gabriel
 */
public class AHLInstPlaceVisual extends AHLPlaceVisual {

  public AHLInstPlaceVisual(Graphics2D superGraphics, Point position, AHLInstPlaceData data, int modelElementId) {
    super(superGraphics, position, data, modelElementId);
  }

  public AHLInstPlaceVisual(Graphics2D superGraphics, AHLInstPlaceData data, int modelElementId) {
    this(superGraphics, new Point(0,0), data, modelElementId);
  }
  
  @Override
  protected void updateLabelContent() {
    super.updateLabelContent();
    AHLInstPlaceData ahlInstData = (AHLInstPlaceData) data;
    ValueTerm value = ahlInstData.getPlaceValue();
    if(value != null) {
      String text = label.getText();
      text += "\n=" + value.toString();
      label.setText(text);
    }
  }
  
  
}
