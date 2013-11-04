/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.ahl;

import ape.petri.generic.EnumNetType;
import ape.petri.generic.net.ArcElement;
import ape.petri.generic.net.ArcElementData;
import ape.prolog.Atom;
import ape.util.aml.AMLNode;

/**
 * This class describes the data, an {@link ArcElement} in an AHL net is equipped with. 
 * In our implementation of AHL-nets, an arc can only be inscribed with sums of variables,
 * and therefore, an {@link ArcElement} can be equipped with a single variable.
 * @author Gabriel
 */
public class AHLArcElementData extends ArcElementData {

  private Atom inscription;
    
  /**
   * Creates a new arc data element for an AHL-net with specified inscription.
   * @param inscription the <code>String</code> representation of the inscription
   * variable of this <code>ArcElement</code>
   */
  public AHLArcElementData(String inscription) {
    this(new Atom(inscription));
  }

  /**
   * Creates a new arc data element for an AHL-net with specified inscription.
   * @param inscription the inscription of this arc element given as Prolog atom
   */
  public AHLArcElementData(Atom inscription) {
    super();
    this.inscription = inscription;
  }
  
  
  public Atom getInscription() {
    return inscription;
  }

  public void setInscription(Atom inscription) {
    this.inscription = inscription;
  }

  @Override
  public AMLNode getAMLNode() {
    AMLNode node = super.getAMLNode();
    node.putAttribute("inscription", inscription.toString());
    return node;
  }

  @Override
  public void readAMLNode(AMLNode node) {
    super.readAMLNode(node);
    inscription = new Atom(node.getAttribute("inscription"));
  }
  
  
}