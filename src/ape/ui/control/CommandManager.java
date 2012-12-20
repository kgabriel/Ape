/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control;

import ape.Ape;
import ape.ui.control.commands.Command;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 *
 * @author Gabriel
 */
public class CommandManager {
  
  private Ape theApe;
  
  /**
   * A map, associating command receiver types with currently active receivers of that type.
   */
  private Map<EnumCommandReceiverType, Collection<CommandReceiver>> receivers;
  
  /**
   * A map, associating command invocation types with commands that are available for that type,
   * and can be activated by associating it with a specific binding.
   */
  private Map<EnumInvocationType,Collection<Command>> availableCommands;
  
  /** A map, associating invocation bindings with commands in two layers: Every binding
   *  maps to another map that in turn maps binding modifiers to associated commands.
   */
  private Map<CommandBinding,Map<CommandBindingModifier,Collection<Command>>> activeCommands;
  
  private Map<CommandReceiver,CommandListener> commandListeners;
  
  public CommandManager(Ape ape) {
    theApe = ape;
    init();
  }
  
  /**
   * Initializes the command manager. This method should be called by the constructor.
   * It initializes the data structures owned by the manager.
   */
  private void init() {
    receivers = new HashMap<>(EnumCommandReceiverType.numberOfTypes());
    for(EnumCommandReceiverType type : EnumCommandReceiverType.values()) {
      receivers.put(type, new HashSet<CommandReceiver>());
    }
    availableCommands = new HashMap<>(EnumInvocationType.numberOfTypes());
    for(EnumInvocationType type : EnumInvocationType.values()) {
      availableCommands.put(type, new HashSet<Command>());
    }
    activeCommands = new HashMap<>();
    commandListeners = new HashMap<>();
  }
  
  /**
   * Adds a receiver for commands. The manager adds itself as listener for all
   * kinds of events that can possibly invoke a command.
   * @param receiver the object that is set to receive commands
   */
  public void addReceiver(CommandReceiver receiver) {
    receivers.get(receiver.getCommandReceiverType()).add(receiver);
    commandListeners.put(receiver, new CommandListener(this,receiver));
  }
  
  /**
   * Removes a receiver for commands. The manager stops listening to the receiver.
   * @param receiver the receiver that stops receiving commands
   * @return <code>true</code> if the the receiver was actually added as receiver before
   */
  public boolean removeReceiver(CommandReceiver receiver) {
    boolean removed = receivers.get(receiver.getCommandReceiverType()).remove(receiver);
    if(! removed) return false;
    CommandListener listener = commandListeners.remove(receiver);
    if(listener == null) return false;
    listener.stop();
    return true;
  }
  
  /**
   * Registers a command to be available. Available commands can be activated by associating
   * it to a specific invocation binding.
   * @param com the command to be available for activation
   */
  public void addCommand(Command com) {
    for(EnumInvocationType type : EnumInvocationType.values()) {
      if(com.invokedBy(type)) {
        availableCommands.get(type).add(com);
      }
    }
  }
  
  /**
   * Activates a command by associating it with a specific invocation binding.
   * @param binding the invocation binding, the command is to be invoked on
   * @param com the command to be activated
   */
  public void activateCommand(Command com, CommandBinding binding, CommandBindingModifier modifier) {
    Collection<Command> associatedCommands = getOrCreateAssociatedCommands(binding, modifier);
    associatedCommands.add(com);
    System.out.println(binding.toUserFriendlyString(modifier) + " " + com);
  }
  
  /**
   * Changes the binding of a command that is currently active. If the command was not active
   * before, it is activated afterwards with the specified new binding. 
   * If the command was active before, its specified old
   * binding is removed, before it is activated with the specified new binding.
   * @param com the command to change the binding
   * @param oldBinding the previously set invocation binding of the command
   * @param oldModifier the previously set invocation binding modifiers of the command 
   * @param newBinding the new invocation binding to set for the command
   * @param newModifier the new invocation binding modifiers to set for the command
   */
  public void changeBinding(Command com, CommandBinding oldBinding, CommandBindingModifier oldModifier,
          CommandBinding newBinding, CommandBindingModifier newModifier) {
    deactivateCommand(com, oldBinding, oldModifier, true);
    activateCommand(com, newBinding, newModifier);
  }
  
  /**
   * Returns the set of commands associated with the specified binding. If there was
   * no set associated with the binding, it is created.
   * @param binding the set of commands, currently associated with the binding
   * @return 
   */
  private Collection<Command> getOrCreateAssociatedCommands(CommandBinding binding, CommandBindingModifier modifier) {
    Map<CommandBindingModifier,Collection<Command>> associatedModifiers = activeCommands.get(binding);
    if(associatedModifiers == null) {
      associatedModifiers = new HashMap<>();
      activeCommands.put(binding,associatedModifiers);
    }
    Collection<Command> associatedCommands = associatedModifiers.get(modifier);
    if(associatedCommands == null) {
      associatedCommands = new HashSet<>();
      associatedModifiers.put(modifier, associatedCommands);
    }
    return associatedCommands;
  }
  
  /**
   * Deactivates a command, if it is active, and it is possible to remove the command.
   * It is only possible to completely remove a command, if the command can be set by the user, that is,
   * if the {@link Command#isAlwaysActive()} method of the command returns <code>false</code>.
   * always on).
   * @param com the command to deactivate in the context of the specified invocation type
   * @param binding the binding, with which the command was activated, and from which it is now deactivated
   * @param modifier the modifiers, with which the command was activated, and from which it is now deactivated
   * @param changing <code>true</code> means that the command is not removed completely, but
   * it is only removed to change the binding, that is, it is activated with another binding afterwards;
   * <code>false</code> means that it is removed completely
   * @return <code>true</code> if the command has actually been removed; the method returns
   * <code>false</code> if either the command can not be set by the user, or if it was not 
   * active in the context of <code>type</code> before
   * @see CommandManager#deactivateCommand(ape.ui.control.commands.Command, ape.ui.control.CommandBinding, ape.ui.control.CommandBindingModifier) 
   */
  public boolean deactivateCommand(Command com, CommandBinding binding, CommandBindingModifier modifier, boolean changing) {
    if(! changing && com.isAlwaysActive()) return false;

    Map<CommandBindingModifier,Collection<Command>> associatedModifiers = activeCommands.get(binding);
    if(associatedModifiers == null) return false;

    Collection<Command> associatedCommands = associatedModifiers.get(modifier);
    if(associatedCommands == null) return false;

    return associatedCommands.remove(com);
  }

  /**
   * Deactivates a command, if it is active, and it is possible to remove the command.
   * It is only possible to completely remove a command, if the command can be set by the user, that is,
   * if the {@link Command#isAlwaysActive()} method of the command returns <code>false</code>.
   * always on). A call of this method is equal to a call of
   * {@link CommandManager#deactivateCommand(ape.ui.control.commands.Command, ape.ui.control.CommandBinding, ape.ui.control.CommandBindingModifier, boolean) }
   * with the parameter <code>changing</code> set to <code>false</code>.
   * @param com the command to deactivate in the context of the specified invocation type
   * @param binding the binding, with which the command was activated, and from which it is now deactivated
   * @param modifier the modifiers, with which the command was activated, and from which it is now deactivated
   * @return <code>true</code> if the command has actually been removed; the method returns
   * <code>false</code> if either the command can not be set by the user, or if it was not 
   * active in the context of <code>type</code> before
   * @see CommandManager#deactivateCommand(ape.ui.control.commands.Command, ape.ui.control.CommandBinding, ape.ui.control.CommandBindingModifier, boolean) 
   */
  public boolean deactivateCommand(Command com, CommandBinding binding, CommandBindingModifier modifier) {
    return deactivateCommand(com, binding, modifier, false);
  }

  
  public void invokeCommands(CommandEvent e) {
    CommandBinding binding = e.getBinding();
    int eventModifiers = e.getInputEvent().getModifiersEx();

    Map<CommandBindingModifier,Collection<Command>> associatedCommands = activeCommands.get(binding);
    if(associatedCommands == null) return;

    
    for(CommandBindingModifier comModifiers : associatedCommands.keySet()) {
      if(comModifiers.fitsToModifiers(eventModifiers)) {
        for(Command com : associatedCommands.get(comModifiers)) {
          com.invoke(e, theApe);
        }
      }
    }
  }
}
