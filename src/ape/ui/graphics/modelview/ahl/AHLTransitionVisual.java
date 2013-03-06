/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.ahl;

import ape.petri.ahl.AHLTransitionData;
import ape.petri.generic.net.Transition;
import ape.prolog.Compound;
import ape.prolog.Prolog;
import ape.ui.graphics.modelview.generic.TransitionVisual;
import ape.util.EnumPropertyType;
import ape.util.Property;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Gabriel
 */
public class AHLTransitionVisual extends TransitionVisual {

  public AHLTransitionVisual(Graphics2D superGraphics, Transition transition) {
    this(superGraphics, new Point(0,0), transition);
  }
  
  public AHLTransitionVisual(Graphics2D superGraphics, Point position, Transition transition) {
    super(superGraphics, position, transition);
    init();
  }
  
  private void init() {
    updateDataVisualContent();
    initProperties();
  }
  
  private void initProperties() {
    addProperty(new Property(Property.CATEGORY_VALUES, this, EnumPropertyType.String, "Conditions", true) {

      @Override
      public Object getValue() {
        AHLTransitionData data = (AHLTransitionData) transition.getData();
        return Prolog.toString(data.getConditions());
      }

      @Override
      public void setValue(Object value) {
        AHLTransitionData data = (AHLTransitionData) transition.getData();
        Collection<Compound> terms = Prolog.parseCompoundSequence((String) value, true);
        Set<Compound> cond = new HashSet<>(terms.size());
        cond.addAll(terms);
        data.setConditions(cond);
        updateContent();
      }
    });
  }

  @Override
  protected void updateDataVisualContent() {
    AHLTransitionData data = (AHLTransitionData) transition.getData();
    dataVisual.setText(Prolog.toString(data.getConditions()));
    updateSize();
  }

  @Override
  protected void updateExtendedDataVisualContent() {}
  
}
