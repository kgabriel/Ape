/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.commands;

import ape.ui.control.actions.global.SaveProjectAction;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Gabriel
 */
public class CommandProvider {
  
  private Map<String, Command> commands;
  
  public CommandProvider() {
    commands = new HashMap<>();
    initCommands();
  }

  private void initCommands() {
    addCommand(new ModelViewMouseClickCommand());
    addCommand(new ModelViewMousePressCommand());
    addCommand(new ModelViewMouseReleaseCommand());
    addCommand(new ModelViewMouseMoveCommand(false));
    addCommand(new ModelViewMouseMoveCommand(true));
    addCommand(new ModelViewResetCommand());
    addCommand(new ModelViewScaleCommand());
    addCommand(new ModelViewMouseScrollCommand());
    addCommand(new SetPropertyCommand("Name"));
    addCommand(new SetPropertyCommand("Type"));
    addCommand(new SetPropertyCommand("Conditions"));
    addCommand(new SetPropertyCommand("Inscriptions"));
    addCommand(new SaveProjectCommand(false));
    addCommand(new SaveProjectCommand(true));
  }
  
  private void addCommand(Command com) {
    commands.put(com.getName(), com);
  }
  
  public Command getCommand(String name) {
    return commands.get(name);
  }
}
