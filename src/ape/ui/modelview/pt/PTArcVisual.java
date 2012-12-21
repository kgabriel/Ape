/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.modelview.pt;

import ape.util.EnumPropertyType;
import ape.util.Property;
import ape.petri.generic.net.ArcCollection;
import ape.petri.generic.net.ArcElement;
import ape.petri.generic.net.EnumArcDirection;
import ape.petri.pt.PTArcElementData;
import ape.ui.modelview.generic.*;
import java.awt.Graphics2D;

/**
 *
 * @author Gabriel
 */
public class PTArcVisual extends ArcVisual {

  public PTArcVisual(Graphics2D superGraphics, PlaceVisual pv, TransitionVisual tv, EnumArcDirection dir, ArcCollection arc) {
    super(superGraphics, pv, tv, dir, arc);
    initProperties();
  }
  
  private void initProperties() {
    addProperty(new Property(this, EnumPropertyType.Integer, "Arc Weight", true) {

      @Override
      public Object getValue() {
        return arc.getData().size();
      }

      @Override
      public void setValue(Object value) {
        int weight = (int) value;
        arc.clear();
        for(int i=0;i<weight;i++) {
          arc.addFreshElement(new ArcElement(arc, new PTArcElementData()));
        }
        updateLabelContent();
      }
    });
  }

  @Override
  protected void updateLabelContent() {
    TextVisual label = getLabel();
    /* count the weight on the arc */
    int weight = arc.getData().size();
    label.setText("" + weight);
    /* hide the label if the weight is less than 2 */
    label.setHidden(weight < 2);
  }

  @Override
  public String getElementTypeName() {
    return "Arc of a P/T net";
  }  
}
