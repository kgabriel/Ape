/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.actions.ahlinstantiation;

import ape.Ape;
import ape.petri.ahl.instantiation.AHLInstantiation;
import ape.petri.validity.EnumInvalidityType;
import ape.petri.validity.InvalidityReason;
import ape.petri.validity.Validity;
import ape.ui.control.actions.ModelAction;
import javax.swing.JOptionPane;

/**
 *
 * @author Gabriel
 */
public class ValidateInstantiationAction extends ModelAction {

  public ValidateInstantiationAction(Ape theApe) {
    super(theApe, "Validate Instantiation");
  }

  @Override
  protected void onInvocation() {
    AHLInstantiation inst = (AHLInstantiation) getModel();
    Validity validity = inst.validate();
    if(validity.isValid()) {
      JOptionPane.showMessageDialog(theApe.ui.mainFrame, validity.toString(), "Valid Instantiation", JOptionPane.INFORMATION_MESSAGE);
    } else {
      String msg = "";
      for(InvalidityReason reason : validity.getInvalidityReasons()) {
        if(reason.getType() != EnumInvalidityType.InstPlaceUnassigned && reason.getType() != EnumInvalidityType.InstTransitionAssignmentPartial) {
          msg += reason.toString();
        }
      }
      JOptionPane.showMessageDialog(theApe.ui.mainFrame, msg, "Invalid Instantiation", JOptionPane.ERROR_MESSAGE);
    }
  }
  
}
