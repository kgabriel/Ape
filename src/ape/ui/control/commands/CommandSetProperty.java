/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.commands;

import ape.Ape;
import ape.ui.graphics.PropertyTable;
import ape.ui.control.CommandEvent;
import ape.ui.control.EnumCommandReceiverType;
import ape.ui.control.EnumInvocationType;

/**
 *
 * @author Gabriel
 */
public class CommandSetProperty implements Command {
  
  String propertyName;
  
  public CommandSetProperty(String propertyName) {
    this.propertyName = propertyName;
  }

  @Override
  public String getName() {
    return "Set '" + propertyName + "' Property";
  }

  @Override
  public String getDescription() {
    return "Set the property with the key '" + propertyName + "'.";
  }

  @Override
  public void invoke(CommandEvent e, Ape ape) {
    PropertyTable propertyTable = ape.ui.getPropertyTable();
    int propertyRow = propertyTable.getPropertyRow(propertyName);
    if(propertyRow == -1) return;
    propertyTable.editPropertyAt(propertyRow);
  }

  @Override
  public boolean invokedBy(EnumInvocationType invocationType) {
    switch(invocationType) {
      case KeyPress:
        return true;
      default:
        return false;
    }
  }

  @Override
  public boolean receivedBy(EnumCommandReceiverType receiverType) {
    return receiverType == EnumCommandReceiverType.Global;
  }

  @Override
  public boolean isAlwaysActive() {
    return true;
  }
}
