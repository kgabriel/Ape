/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control;

import ape.ui.control.commands.Command;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashSet;

/**
 * The different invocation types of a command. A command can have different invocation types.
 * The {@link CommandManager} ensures that the command is invoked if an event of that type occurs.
 * @author Gabriel
 */
public enum EnumInvocationType {
  
  MouseClick("Click Mouse", MouseEvent.MOUSE_CLICKED, EnumInputDeviceType.Mouse),
  MousePress("Press Mouse", MouseEvent.MOUSE_PRESSED, EnumInputDeviceType.Mouse),
  MouseRelease("Release Mouse", MouseEvent.MOUSE_RELEASED, EnumInputDeviceType.Mouse),
  MouseDrag("Drag Mouse", MouseEvent.MOUSE_DRAGGED, EnumInputDeviceType.Mouse),
  MouseMove("Move Mouse", MouseEvent.MOUSE_MOVED, EnumInputDeviceType.Mouse),
  MouseWheel("Mouse Wheel", MouseEvent.MOUSE_WHEEL, EnumInputDeviceType.Mouse),
  
  MouseEnter("Enter Mouse", MouseEvent.MOUSE_ENTERED, EnumInputDeviceType.Mouse),
  MouseExit("Exit Mouse", MouseEvent.MOUSE_EXITED, EnumInputDeviceType.Mouse),
  
  KeyPress("Press Key", KeyEvent.KEY_PRESSED, EnumInputDeviceType.Keyboard),
  KeyRelease("Release Key", KeyEvent.KEY_RELEASED, EnumInputDeviceType.Keyboard),
  KeyType("Type Key", KeyEvent.KEY_TYPED, EnumInputDeviceType.Keyboard);
  
  private String name;
  private int value;
  private EnumInputDeviceType deviceType;

  EnumInvocationType(String name, int value, EnumInputDeviceType deviceType) {
    this.name = name;
    this.value = value;
    this.deviceType = deviceType;
  }

  public EnumInputDeviceType getInputDeviceType() {
    return deviceType;
  }

  public int getValue() {
    return value;
  }
  
  public static int numberOfTypes() {
    return values().length;
  }
  
  public static EnumInvocationType getType(InputEvent e) {
    EnumInputDeviceType device = EnumInputDeviceType.getType(e);
    int id = e.getID();
    for(EnumInvocationType type : values()) {
      if(device == type.deviceType && id == type.value) return type;
    }
    return null;
  }
  
  

  @Override
  public String toString() {
    return name;
  }
  
  public static Collection<EnumInvocationType> invocationTypeSet(Command com) {
    Collection<EnumInvocationType> types = new HashSet<>();
    for(EnumInvocationType type : EnumInvocationType.values()) {
      if(com.invokedBy(type)) {
        types.add(type);
      }
    }
    return types;
  }

  public static String userFriendlyString(Command com) {
    return userFriendlyString(invocationTypeSet(com));
  }

  public static String userFriendlyString(Collection<EnumInvocationType> invocationTypes) {
    String str = "{";
    for(EnumInvocationType type : invocationTypes) {
      str += type.name;
    }
    str += "}";
    return str;
  }
}
