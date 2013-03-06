/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.ahl.instantiation;

import ape.petri.ahl.instantiation.AHLInstPlaceData;
import ape.petri.generic.net.Place;
import ape.prolog.Atom;
import ape.prolog.ValueTerm;
import ape.ui.graphics.modelview.ahl.AHLPlaceVisual;
import ape.util.EnumPropertyType;
import ape.util.Property;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 *
 * @author Gabriel
 */
public class AHLInstPlaceVisual extends AHLPlaceVisual {

  public AHLInstPlaceVisual(Graphics2D superGraphics, Point position, Place place) {
    super(superGraphics, position, place);
    initProperties();
  }

  public AHLInstPlaceVisual(Graphics2D superGraphics, Place place) {
    this(superGraphics, new Point(0,0), place);
  }
  
  private void initProperties() {
    addProperty(new Property(Property.CATEGORY_VALUES, this, EnumPropertyType.String, "Value", true) {

      @Override
      public Object getValue() {
        AHLInstPlaceData data = (AHLInstPlaceData) place.getData();
        ValueTerm value = data.getValue();
        if(value == null) return ValueTerm.unassignedString;
        return value.toString();
      }

      @Override
      public void setValue(Object value) {
        AHLInstPlaceData data = (AHLInstPlaceData) place.getData();
        if(value.equals(ValueTerm.unassignedString)) {
          data.setValue(null);
        } else {
          data.setValue(new ValueTerm((String) value));
        }
        updateLabelContent();
      }
    });
  }

  @Override
  protected void updateLabelContent() {
    super.updateLabelContent();
    AHLInstPlaceData data = (AHLInstPlaceData) place.getData();
    ValueTerm value = data.getValue();
    if(value != null) {
      String text = label.getText();
      text += "\n=" + value.toString();
      label.setText(text);
    }
  }
  
  
}
