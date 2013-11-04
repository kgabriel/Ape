/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.commands;

import ape.Ape;
import ape.ui.control.CommandEvent;
import ape.ui.control.EnumCommandReceiverType;
import ape.ui.control.EnumInvocationType;
import ape.ui.control.actions.global.SaveProjectAction;

/**
 *
 * @author Gabriel
 */
public class SaveProjectCommand implements Command {
  private final boolean saveAs;
  
  public SaveProjectCommand(boolean saveAs) {
    this.saveAs = saveAs;
  }

  @Override
  public String getName() {
    return "Save Project" + (saveAs ? " As" : "");
  }

  @Override
  public String getDescription() {
    return "Save the active storage to a file.";
  }

  @Override
  public void invoke(CommandEvent e, Ape ape) {
    new SaveProjectAction(ape, saveAs).invoke();
  }

  @Override
  public boolean invokedBy(EnumInvocationType invocationType) {
    switch(invocationType) {
      case KeyPress: return true;
      default: return false;
    }
  }

  @Override
  public boolean canUserDefineInvocationBinding() {
    return true;
  }

  @Override
  public boolean receivedBy(EnumCommandReceiverType receiverType) {
    switch(receiverType) {
      case Global: return true;
      default: return false;
    }
  }

  @Override
  public boolean isAlwaysActive() {
    return false;
  }
  
}
