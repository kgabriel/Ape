/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.pt;

import ape.petri.pt.PTPlaceData;
import ape.ui.graphics.modelview.generic.PlaceVisual;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author Gabriel
 */
public class PTPlaceVisual extends PlaceVisual {

  public PTPlaceVisual(Graphics2D superGraphics, PTPlaceData data, int modelElementId) {
    this(superGraphics, new Point(0,0), data, modelElementId);
  }

  public PTPlaceVisual(Graphics2D superGraphics, Point position, PTPlaceData data, int modelElementId) {
    super(superGraphics, position, data, modelElementId);
    updateLabel();
  }

  private void updateLabel() {
    PTPlaceData ptData = (PTPlaceData) data;
    label.setText(ptData.getName());
  }

  @Override
  protected void updateLabelContent() {
    updateLabel();
  }
}
