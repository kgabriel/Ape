/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control;

import java.awt.Component;

/**
 *
 * @author Gabriel
 */
public interface CommandReceiver {

  /** Returns the component of this command receiver. This is used by the command manager
   * to correctly add all listeners associated with this receiver.
   */
  public Component getComponent();
  public EnumCommandReceiverType getCommandReceiverType();
}
