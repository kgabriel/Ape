/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.pt;

import ape.petri.generic.net.ArcCollection;
import ape.petri.generic.net.ArcElement;
import ape.petri.generic.net.ArcElementData;
import ape.util.Property;
import java.util.ArrayList;
import java.util.List;

/**
 * This class describes the data, an {@link ArcElement} in a P/T net is equipped with. 
 * Since P/T nets do not have any arc inscriptions other than an arc weight, this class
 * is empty. The weight of an arc is modeled by the number of {@link ArcElement}s
 * contained in an {@link ArcCollection}.
 * @author Gabriel
 */
public class PTArcElementData extends ArcElementData {

  @Override
  public List<Property> getProperties() {
    return new ArrayList<>();
  }

}