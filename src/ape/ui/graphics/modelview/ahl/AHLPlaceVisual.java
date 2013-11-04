/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.ahl;

import ape.petri.ahl.AHLPlaceData;
import ape.ui.graphics.modelview.generic.PlaceVisual;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author Gabriel
 */
public class AHLPlaceVisual extends PlaceVisual {

  public AHLPlaceVisual(Graphics2D superGraphics, AHLPlaceData data, int modelElementId) {
    this(superGraphics, new Point(0,0), data, modelElementId);
  }

  public AHLPlaceVisual(Graphics2D superGraphics, Point position, AHLPlaceData data, int modelElementId) {
    super(superGraphics, position, data, modelElementId);
    updateLabel();
  }
  
  private void updateLabel() {
    AHLPlaceData ahlData = (AHLPlaceData) data;
    String text = ahlData.getName() + ":" + ahlData.getType();
    label.setText(text);
  }

  @Override
  protected void updateLabelContent() {
    updateLabel();
  }
}
