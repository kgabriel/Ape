/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.ahl;

import ape.petri.ahl.AHLArcElementData;
import ape.prolog.exception.PrologParserException;
import ape.petri.generic.net.ArcCollection;
import ape.petri.generic.net.ArcElement;
import ape.petri.generic.net.ArcElementData;
import ape.petri.generic.net.EnumArcDirection;
import ape.prolog.Prolog;
import ape.ui.graphics.modelview.generic.ArcVisual;
import ape.ui.graphics.modelview.generic.PlaceVisual;
import ape.ui.graphics.modelview.generic.TextVisual;
import ape.ui.graphics.modelview.generic.TransitionVisual;
import ape.util.EnumPropertyType;
import ape.util.Property;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import ape.prolog.Atom;

/**
 *
 * @author Gabriel
 */
public class AHLArcVisual extends ArcVisual {

  public AHLArcVisual(Graphics2D superGraphics, PlaceVisual pv, TransitionVisual tv, EnumArcDirection dir, ArcCollection arc) {
    super(superGraphics, pv, tv, dir, arc);
    initProperties();
  }
  
  private void initProperties() {
    addProperty(new Property(Property.CATEGORY_VALUES, this, EnumPropertyType.String, "Inscriptions", true) {

      @Override
      public Object getValue() {
        Collection<ArcElementData> dataCollection = arc.getData().getDataElements();
        Collection<Atom> atoms = new ArrayList<>(dataCollection.size());
        for(ArcElementData data : dataCollection) {
          atoms.add(((AHLArcElementData) data).getInscription());
        }
        return Prolog.toString(atoms);
      }

      @Override
      public void setValue(Object value) {
        String string = (String) value;
        Atom[] atoms = null;
        try {
          atoms = Prolog.parseAtomSequence(string);
        } catch(PrologParserException e) {
          System.err.println(e);
        }

        arc.clear();
        if(atoms != null) {
          for(Atom a : atoms) {
            AHLArcElementData data = (AHLArcElementData) arc.getNet().createDefaultArcElementData();
            data.setInscription(a);
            arc.addFreshElement(new ArcElement(arc, data));
          }
        }
        updateLabelContent();
      }
    });
  }

  @Override
  protected void updateLabelContent() {
    TextVisual label = getLabel();

    Collection<ArcElementData> dataCollection = arc.getData().getDataElements();
    Collection<Atom> atoms = new ArrayList<>(dataCollection.size());
    for(ArcElementData data : dataCollection) {
      atoms.add(((AHLArcElementData) data).getInscription());
    }
    String sequence = Prolog.toString(atoms);
    
    label.setText(sequence);
  }
}
