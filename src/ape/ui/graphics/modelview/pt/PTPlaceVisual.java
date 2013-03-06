/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.pt;

import ape.petri.generic.net.Place;
import ape.petri.pt.PTPlaceData;
import ape.ui.graphics.modelview.generic.PlaceVisual;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author Gabriel
 */
public class PTPlaceVisual extends PlaceVisual {

  public PTPlaceVisual(Graphics2D superGraphics, Place place) {
    this(superGraphics, new Point(0,0), place);
  }

  public PTPlaceVisual(Graphics2D superGraphics, Point position, Place place) {
    super(superGraphics, position, place);
    updateLabel();
  }

  private void updateLabel() {
    PTPlaceData ptData = (PTPlaceData) place.getData();
    label.setText(ptData.getName());
  }

  @Override
  protected void updateLabelContent() {
    updateLabel();
  }
}
