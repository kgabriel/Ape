/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.util;

import ape.Ape;
import ape.petri.generic.net.EnumArcDirection;
import ape.petri.generic.net.EnumElementType;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author Gabriel
 */
public enum EnumIcon {
  
  Blank("blank", false),
  BlankSmall("blank_small", true),
  Arc("arc", false),
  ArcSmall("arc_small", true),
  PreArc("pre_arc", false),
  PreArcSmall("pre_arc_small", true),
  PostArc("post_arc", false),
  PostArcSmall("post_arc_small", true),
  Place("place", false),
  PlaceSmall("place_small", true),
  Selection("selection", false),
  Transition("transition", false),
  TransitionSmall("transition_small",true);

  private String iconName;
  private boolean small;
  private ImageIcon icon;
        
  EnumIcon(String iconName, boolean small) {
    this.iconName = iconName;
    this.small = small;
    loadIcon();
  }

  public String getIconName() {
    return iconName;
  }
  
  private void loadIcon() {
    
    String iconLocation = "icons/" + iconName + ".png";
    URL iconURL = Ape.class.getResource(iconLocation);
    
    if(iconURL != null) {
      icon = new ImageIcon(iconURL);
    } else {
      System.err.println("Could not find resource: " + iconLocation);
    }
  }
  
  public ImageIcon getIcon() {
    return icon;
  }
  
  public static EnumIcon fromElementType(EnumElementType type, boolean small) {
    switch(type) {
      case Arc:
        return (small ? ArcSmall : Arc);
      case Place:
        return (small ? PlaceSmall : Place);
      case Transition:
        return (small ? TransitionSmall : Transition);
      default:
        throw new UnsupportedOperationException("No icon specified yet for element type " + type + ".");
    }
  }
  
  public static EnumIcon fromArcDirection(EnumArcDirection dir, boolean small) {
    switch(dir) {
      case PT:
        return (small ? PreArcSmall : PreArc);
      case TP:
        return (small ? PostArcSmall : PostArc);
      default:
        throw new UnsupportedOperationException("No icon specified for direction " + dir + ".");
    }
  }
}
