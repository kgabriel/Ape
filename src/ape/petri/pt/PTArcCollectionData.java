/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.pt;

import ape.petri.generic.net.ArcCollection;
import ape.petri.generic.net.ArcCollectionData;
import ape.petri.generic.net.ArcElement;
import ape.util.EnumPropertyType;
import ape.util.Property;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class PTArcCollectionData extends ArcCollectionData {

  @Override
  public List<Property> getProperties() {
    List<Property> properties = new ArrayList<>();
    properties.add(new Property(Property.CATEGORY_VALUES, this, EnumPropertyType.Integer, "Arc Weight") {

      @Override
      public Object getValue() {
        return size();
      }

      @Override
      public void setValue(Object value) {
        int weight = (int) value;
        ArcCollection arc = (ArcCollection) getModelElement();
        arc.clear();
        for(int i=0;i<weight;i++) {
          arc.addFreshElement(new ArcElement(arc, new PTArcElementData()));
        }
      }
    });
    return properties;
  }

}
