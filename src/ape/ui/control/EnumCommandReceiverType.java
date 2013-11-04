/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control;

import ape.ui.control.commands.Command;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author Gabriel
 */
public enum EnumCommandReceiverType {

  Global("Global"),
  ModelView("Model View");

  private String name;

  EnumCommandReceiverType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static int numberOfTypes() {
    return values().length;
  }
  
  public static Collection<EnumCommandReceiverType> receiverTypeSet(Command com) {
    Collection<EnumCommandReceiverType> types = new HashSet<>();
    for(EnumCommandReceiverType type : EnumCommandReceiverType.values()) {
      if(com.receivedBy(type)) {
        types.add(type);
      }
    }
    return types;
  }

  public static String userFriendlyString(Command com) {
    return userFriendlyString(receiverTypeSet(com));
  }

  public static String userFriendlyString(Collection<EnumCommandReceiverType> receiverTypes) {
    String str = "{";
    for(EnumCommandReceiverType type : receiverTypes) {
      str += type.name;
    }
    str += "}";
    return str;
  }
}
