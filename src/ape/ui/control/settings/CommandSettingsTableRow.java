/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.settings;

import ape.ui.control.CommandBinding;
import ape.ui.control.CommandBindingModifier;
import ape.ui.control.EnumCommandReceiverType;
import ape.ui.control.EnumInvocationType;
import ape.ui.control.commands.Command;

/**
 *
 * @author Gabriel
 */
public class CommandSettingsTableRow {
  
  private Command command;
  private CommandBinding binding;
  private CommandBindingModifier modifiers;

  public CommandSettingsTableRow(Command command, CommandBinding binding, CommandBindingModifier modifiers) {
    this.command = command;
    this.binding = binding;
    this.modifiers = modifiers;
  }
  
  public String[] toStringsRow() {
    String[] row = new String[6];
    row[0] = EnumCommandReceiverType.userFriendlyString(command);
    row[1] = command.getName();
    row[2] = command.getDescription();
    row[3] = EnumInvocationType.userFriendlyString(command);
    row[4] = binding.toUserFriendlyString(modifiers);
    row[5] = Boolean.valueOf(! command.isAlwaysActive()).toString();
    return row;
  }
}
