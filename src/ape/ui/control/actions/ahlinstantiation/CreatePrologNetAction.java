/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.actions.ahlinstantiation;

import ape.Ape;
import ape.petri.ahl.instantiation.AHLInstantiation;
import ape.prolog.exception.PrologNetException;
import ape.ui.control.actions.ModelAction;
import javax.swing.JOptionPane;

/**
 *
 * @author Gabriel
 */
public class CreatePrologNetAction extends ModelAction {

  public CreatePrologNetAction(Ape theApe) {
    super(theApe, "Create Prolog Net");
  }

  @Override
  protected void onInvocation() {
    AHLInstantiation inst = (AHLInstantiation) getModel();
    try{
      inst.createPrologNet();
    } catch(PrologNetException prologNetException) {
      JOptionPane.showMessageDialog(theApe.ui.mainFrame, prologNetException.getMessage(),"Creation Failed", JOptionPane.ERROR_MESSAGE);
    }
  }
  
}
