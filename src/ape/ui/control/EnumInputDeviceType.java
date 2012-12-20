/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 *
 * @author Gabriel
 */
public enum EnumInputDeviceType {

  Mouse("Mouse"),
  Keyboard("Keyboard");

  private String name;

  EnumInputDeviceType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static int numberOfTypes() {
    return values().length;
  }

  public static EnumInputDeviceType getType(InputEvent e) {
    if(e instanceof KeyEvent) {
      return Keyboard;
    } 
    if(e instanceof MouseEvent) {
      return Mouse;
    }
    return null;
  }

  @Override
  public String toString() {
    return name;
  }
}
