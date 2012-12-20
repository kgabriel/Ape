/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control;

import java.awt.Point;
import java.awt.event.InputEvent;

/**
 *
 * @author Gabriel
 */
public class CommandEvent {
  
  protected CommandBinding binding;
  private final CommandReceiver receiver;
  protected InputEvent inputEvent;
  protected Point lastMouseLocation;

  public CommandEvent(CommandBinding binding, CommandReceiver receiver, InputEvent event, Point lastMouseLocation) {
    this.binding = binding;
    this.receiver = receiver;
    this.inputEvent = event;
    this.lastMouseLocation = lastMouseLocation;
  }

  public CommandBinding getBinding() {
    return binding;
  }

  public InputEvent getInputEvent() {
    return inputEvent;
  }
  
  public CommandReceiver getReceiver() {
    return receiver;
  }

  public Point getLastMouseLocation() {
    return lastMouseLocation.getLocation();
  }
}
