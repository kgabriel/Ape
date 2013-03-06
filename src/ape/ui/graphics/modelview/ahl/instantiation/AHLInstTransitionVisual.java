/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.ahl.instantiation;

import ape.petri.ahl.instantiation.AHLInstTransitionData;
import ape.petri.generic.net.Transition;
import ape.prolog.Prolog;
import ape.ui.graphics.modelview.ahl.AHLTransitionVisual;
import ape.util.EnumPropertyType;
import ape.util.Property;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Collection;
import java.util.Map;
import ape.prolog.Atom;
import ape.prolog.Compound;
import ape.prolog.ValueTerm;
import ape.prolog.exception.PrologParserException;
import java.util.HashMap;

/**
 *
 * @author Gabriel
 */
public class AHLInstTransitionVisual extends AHLTransitionVisual {

  public AHLInstTransitionVisual(Graphics2D superGraphics, Point position, Transition transition) {
    super(superGraphics, position, transition);
    initProperties();
  }

  public AHLInstTransitionVisual(Graphics2D superGraphics, Transition transition) {
    this(superGraphics, new Point(0,0), transition);
  }
  
  private void initProperties() {
    addProperty(new Property(Property.CATEGORY_VALUES, this, EnumPropertyType.String, "Assignment", true) {

      @Override
      public Object getValue() {
        AHLInstTransitionData data = (AHLInstTransitionData) transition.getData();
        Map<Atom,ValueTerm> assignment = data.getAssignment();
        String value = "";
        boolean first = true;
        for(Atom var : assignment.keySet()) {
          if(! first) value += ",\n";
          value += var + "=" + assignment.get(var);
          first = false;
        }
        return value;
      }

      @Override
      public void setValue(Object value) {
        AHLInstTransitionData data = (AHLInstTransitionData) transition.getData();
        try{
        Map<Atom,ValueTerm> assignment = Prolog.parseAssignmentSequence((String) value);

        data.setAssignment(assignment);
        } catch(Exception parserEx) {
          parserEx.printStackTrace();
        }
        updateContent();
      }
    });
  }

  @Override
  protected void updateExtendedDataVisualContent() {
    AHLInstTransitionData data = (AHLInstTransitionData) transition.getData();
    Map<Atom,ValueTerm> assignment = data.getAssignment();
    String text = "";
    boolean first = true;
    for(Atom var : assignment.keySet()) {
      if(! first) text += ", ";
      text += var.toString() + " \u21A6 " + assignment.get(var);
      first = false;
    }
    extendedDataVisual.setText(text);
    updateSize();
  }
  
  
}
