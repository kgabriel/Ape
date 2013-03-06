/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.ahl;

import ape.petri.ahl.AHLPlaceData;
import ape.petri.generic.net.Place;
import ape.prolog.Atom;
import ape.ui.graphics.modelview.generic.PlaceVisual;
import ape.util.EnumPropertyType;
import ape.util.Property;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author Gabriel
 */
public class AHLPlaceVisual extends PlaceVisual {

  public AHLPlaceVisual(Graphics2D superGraphics, Place place) {
    this(superGraphics, new Point(0,0), place);
  }

  public AHLPlaceVisual(Graphics2D superGraphics, Point position, Place place) {
    super(superGraphics, position, place);
    updateLabel();
    initProperties();
  }
  
  private void initProperties() {
    addProperty(new Property(Property.CATEGORY_VALUES, this, EnumPropertyType.String, "Type", true) {

      @Override
      public Object getValue() {
        AHLPlaceData pd = (AHLPlaceData) place.getData();
        return pd.getType().toString();
      }

      @Override
      public void setValue(Object value) {
        Atom atom = new Atom((String) value);
        AHLPlaceData pd = (AHLPlaceData) place.getData();
        pd.setType(atom);
        updateLabelContent();
      }
    });
  }

  private void updateLabel() {
    AHLPlaceData ahlData = (AHLPlaceData) place.getData();
    String text = ahlData.getName() + ":" + ahlData.getType();
    label.setText(text);
  }

  @Override
  protected void updateLabelContent() {
    updateLabel();
  }
}
