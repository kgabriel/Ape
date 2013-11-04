/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.ahl;

import ape.petri.generic.net.*;
import ape.prolog.Atom;
import ape.prolog.Prolog;
import ape.prolog.exception.PrologParserException;
import ape.util.EnumPropertyType;
import ape.util.Property;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class AHLArcCollectionData extends ArcCollectionData {

  @Override
  public List<Property> getProperties() {
    List<Property> properties = super.getProperties();
    
    properties.add(new Property(Property.CATEGORY_VALUES, this, EnumPropertyType.MultiLineText, "Inscriptions") {

      @Override
      public Object getValue() {
        Collection<Atom> atoms = new ArrayList<>(size());
        for(ArcElementData data : getDataElements()) {
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

        ArcCollection arc = (ArcCollection) getModelElement();
        arc.clear();
        if(atoms != null) {
          for(Atom a : atoms) {
            AHLArcElementData data = new AHLArcElementData(a);
            arc.addFreshElement(new ArcElement(arc, data));
          }
        }
      }
    });
    return properties;
  }

  
}
