/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.actions.ahlinstantiation;

import ape.Ape;
import ape.petri.ahl.instantiation.AHLInstantiation;
import ape.ui.control.actions.GlobalModelAction;

/**
 *
 * @author Gabriel
 */
public class GetSolutionAction extends GlobalModelAction {

  public GetSolutionAction(Ape theApe) {
    super(theApe);
  }

  @Override
  public void invoke() {
    final AHLInstantiation inst = (AHLInstantiation) getModel();

    boolean success = inst.getPrologNet().getSolution();

    if(success) {
      System.out.println("New Realization computed.");
      theApe.ui.modelViewCanvas.repaint();
    } else {
      System.out.println("No solution.");
    }
  }
  
}
