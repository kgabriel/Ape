/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.ahl.instantiation;

import ape.petri.ahl.AHLArcElementData;
import ape.petri.generic.EnumNetType;
import ape.prolog.Atom;

/**
 *
 * @author Gabriel
 */
public class AHLInstArcElementData extends AHLArcElementData {

  public AHLInstArcElementData(Atom inscription) {
    super(inscription);
  }

  public AHLInstArcElementData(String inscription) {
    this(new Atom(inscription));
  }
}
