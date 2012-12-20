/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control;

import java.awt.Point;
import java.awt.event.*;

/**
 *
 * @author Gabriel
 */
public class CommandListener implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

  /** The command manager, this listener is listening for. */
  private CommandManager manager;
  
  /** The receiver of the commands invoked by events this listener listens to. */
  private CommandReceiver receiver;
  
  
  /** The position of the mouse on the last/previous occurrence of a mouse event. */
  private Point lastMouseLocation;
  
  /** The button of the mouse that was used on the last/previously press or release
   *  event of the mouse.
   */
  private int lastMouseButton;

  /** A new listener that listens to events in the component of the specified receiver
   * and passes them along to the specified manager.
   * @param manager the command manager that invokes the commands on events passed
   * along by this listener
   * @param receiver the receiver with a component, this listener is listening to
   */
  public CommandListener(CommandManager manager, CommandReceiver receiver) {
    this.manager = manager;
    this.receiver = receiver;
    this.lastMouseLocation = new Point();
    this.lastMouseButton = 0;
    init();
  }
  
  /** Start listening to the receiver's component. */
  private void init() {
    listenToReceiver(true);
  }
  
  /** Stop listening to the receiver's component. */
  public void stop() {
    listenToReceiver(false);
  }
  
  /**
   * If <code>add</code> is <code>true</code>, this method sets the manager as listener 
   * in the receiver's component for all device types that could invoke a command.
   * Otherwise, the manager removes itself as listener from the receiver's component.
   * @param add <code>true</code> for adding the manager as listener to the receiver's
   * component, <code>false</code> for removing it
   */
  protected void listenToReceiver(boolean add) {
    for(EnumInputDeviceType type : EnumInputDeviceType.values()) {
      listenToReceiver(type, add);
    }
  }
  
  /**
   * If <code>add</code> is <code>true</code>, this method sets the manager as listener 
   * in the receiver's component for the specified device type that could invoke a command.
   * Otherwise, the manager removes itself as listener from the receiver's component for
   * the specified device type.
   * @param type the device type, the manager will start or stop listening to
   * @param add <code>true</code> for adding the manager as listener to the receiver's
   * component, <code>false</code> for removing it
   */
  protected void listenToReceiver(EnumInputDeviceType type, boolean add) {
    switch(type) {
      case Keyboard:
        if(add) {
          receiver.getComponent().addKeyListener(this);
        } else {
          receiver.getComponent().removeKeyListener(this);
        }
        break;
      case Mouse:
        if(add) {
          receiver.getComponent().addMouseListener(this);
          receiver.getComponent().addMouseMotionListener(this);
          receiver.getComponent().addMouseWheelListener(this);
        } else {
          receiver.getComponent().removeMouseListener(this);
          receiver.getComponent().removeMouseMotionListener(this);
          receiver.getComponent().removeMouseWheelListener(this);
        }
        break;
    }
  }
  
  /**
   * This method is called, whenever a keyboard event occurred. It creates a
   * corresponding {@link CommandEvent} and passes it along to the {@link CommandManager} 
   * @param e the event that occurred
   */
  public void keyEvent(KeyEvent e) {
    EnumCommandReceiverType recType = receiver.getCommandReceiverType();
    int keyCode = e.getKeyCode();
    CommandBinding binding = 
      new CommandBinding(EnumInvocationType.getType(e), recType, keyCode);
    CommandEvent comEvent = new CommandEvent(binding, receiver, e, lastMouseLocation);
    manager.invokeCommands(comEvent);
  }
  
  /**
   * This method is called, whenever a mouse event occurred. It creates a
   * corresponding {@link CommandEvent} and passes it along to the {@link CommandManager} 
   * @param e the event that occurred
   */
  public void mouseEvent(MouseEvent e) {
    EnumCommandReceiverType recType = receiver.getCommandReceiverType();
    EnumInvocationType invocationType = EnumInvocationType.getType(e);
    int button = e.getButton();
    if(invocationType == EnumInvocationType.MouseDrag) {
      button = lastMouseButton;
    }
    CommandBinding binding = 
      new CommandBinding(invocationType, recType, button);
    CommandEvent comEvent = new CommandEvent(binding, receiver, e, lastMouseLocation);
    manager.invokeCommands(comEvent);
  }

  @Override
  public void keyTyped(KeyEvent e) {
    keyEvent(e);
  }

  @Override
  public void keyPressed(KeyEvent e) {
    keyEvent(e);
  }

  @Override
  public void keyReleased(KeyEvent e) {
    keyEvent(e);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    mouseEvent(e);
    lastMouseLocation = e.getPoint();
  }

  @Override
  public void mousePressed(MouseEvent e) {
    mouseEvent(e);
    lastMouseLocation = e.getPoint();
    lastMouseButton = e.getButton();
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    mouseEvent(e);
    lastMouseLocation = e.getPoint();
    lastMouseButton = e.getButton();
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    mouseEvent(e);
//    lastMouseLocation = e.getPoint();
  }

  @Override
  public void mouseExited(MouseEvent e) {
    mouseEvent(e);
//    lastMouseLocation = e.getPoint();
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    mouseEvent(e);
    lastMouseLocation = e.getPoint();
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    mouseEvent(e);
//    lastMouseLocation = e.getPoint();
  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    mouseEvent(e);
    lastMouseLocation = e.getPoint();
  }
}
