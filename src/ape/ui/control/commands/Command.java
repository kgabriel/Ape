/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.commands;

import ape.Ape;
import ape.ui.control.CommandEvent;
import ape.ui.control.EnumCommandReceiverType;
import ape.ui.control.EnumInvocationType;

/**
 *
 * @author Gabriel
 */
public interface Command {
  
  public String getName();
  
  public String getDescription();
  
  public void invoke(CommandEvent e, Ape ape);

  public boolean invokedBy(EnumInvocationType invocationType);
  
  public boolean canUserDefineInvocationBinding();

  public boolean receivedBy(EnumCommandReceiverType receiverType);
  
  public boolean isAlwaysActive();
}
