/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.ahl;

import ape.petri.ahl.AHLArcCollectionData;
import ape.petri.ahl.AHLArcElementData;
import ape.petri.generic.net.ArcCollectionData;
import ape.petri.generic.net.ArcElementData;
import ape.petri.generic.net.EnumArcDirection;
import ape.prolog.Prolog;
import ape.ui.graphics.modelview.generic.ArcVisual;
import ape.ui.graphics.modelview.generic.PlaceVisual;
import ape.ui.graphics.modelview.generic.TextVisual;
import ape.ui.graphics.modelview.generic.TransitionVisual;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import ape.prolog.Atom;

/**
 *
 * @author Gabriel
 */
public class AHLArcVisual extends ArcVisual {

  public AHLArcVisual(Graphics2D superGraphics, PlaceVisual pv, TransitionVisual tv, EnumArcDirection dir, AHLArcCollectionData data, int modelElementId) {
    super(superGraphics, pv, tv, dir, data, modelElementId);
    updateLabelContent();
  }
  
  @Override
  protected void updateLabelContent() {
    TextVisual label = getLabel();

    Collection<ArcElementData> dataCollection = ((ArcCollectionData) data).getDataElements();
    Collection<Atom> atoms = new ArrayList<>(dataCollection.size());
    for(ArcElementData elementData : dataCollection) {
      atoms.add(((AHLArcElementData) elementData).getInscription());
    }
    String sequence = Prolog.toString(atoms);
    
    label.setText(sequence);
  }
}
