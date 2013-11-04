/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control;

import ape.util.aml.AMLNode;
import ape.util.aml.AMLWritable;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 *
 * @author Gabriel
 */
public class CommandBinding implements AMLWritable {

  private EnumInvocationType invocationType;
  private EnumCommandReceiverType receiverType;
  private int bindingValue;

  public CommandBinding(EnumInvocationType invocationType, EnumCommandReceiverType receiverType, int bindingValue) {
    this.invocationType = invocationType;
    this.receiverType = receiverType;
    this.bindingValue = bindingValue;
  }
  
  public int getBindingValue() {
    return bindingValue;
  }

  public EnumInvocationType getInvocationType() {
    return invocationType;
  }
  
  public EnumCommandReceiverType getReceiverType() {
    return receiverType;
  }
  
  public String toUserFriendlyString() {
    String str;
    EnumInputDeviceType deviceType = invocationType.getInputDeviceType();
    switch(deviceType) {
      case Keyboard:
        str = KeyEvent.getKeyText(bindingValue);
        break;
      case Mouse:
        if(bindingValue == 0) {
          switch(invocationType) {
            case MouseWheel:
              str = "Mouse Wheel";
              break;
            default:
              str = "No Button";
              break;
          }
        } else {
          str = MouseEvent.getModifiersExText(MouseEvent.getMaskForButton(bindingValue));
        }
        break;    
      default:
        str = "Unknown Device";
    }
    return str;
  }
  
  public String toUserFriendlyString(CommandBindingModifier modifier) {
    String bindingString = toUserFriendlyString();
    if(modifier == null) return bindingString;
    
    String plus = "";
    if(modifier.hasModifiers()) {
      plus = "+";
    }
    
    String modifierString = modifier.toUserFriendlyString();
    
    return modifierString + plus + bindingString;
  }
  
  @Override
  public String toString() {
    String str = "[";
    str += invocationType.toString() + "," + receiverType.toString() + "," + bindingValue;
    str += "]";
    return str;
  }
  
  @Override
  public boolean equals(Object o) {
    if(! (o instanceof CommandBinding)) return false;
    CommandBinding other = (CommandBinding) o;
    return this.invocationType == other.invocationType && this.receiverType == other.receiverType
            && this.bindingValue == other.bindingValue;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 71 * hash + (this.invocationType != null ? this.invocationType.hashCode() : 0);
    hash = 71 * hash + (this.receiverType != null ? this.receiverType.hashCode() : 0);
    hash = 71 * hash + this.bindingValue;
    return hash;
  }

  @Override
  public String getAMLTagName() {
    return "Binding";
  }

  @Override
  public AMLNode getAMLNode() {
    AMLNode node = new AMLNode(getAMLTagName());
    node.putAttribute("invocation", invocationType.name());
    node.putAttribute("receiver", receiverType.name());
    node.putAttribute("value", bindingValue);
    return node;
  }

  @Override
  public void readAMLNode(AMLNode node) {
    this.invocationType = EnumInvocationType.valueOf(node.getAttribute("invocation"));
    this.receiverType = EnumCommandReceiverType.valueOf(node.getAttribute("receiver"));
    this.bindingValue = node.getAttributeInt("value");
  }
}
