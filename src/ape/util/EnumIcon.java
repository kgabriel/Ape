/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.util;

import ape.Ape;
import ape.petri.generic.EnumModelType;
import ape.petri.generic.net.EnumArcDirection;
import ape.petri.generic.net.EnumElementType;
import ape.petri.generic.EnumNetType;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author Gabriel
 */
public enum EnumIcon {
  
  Blank("blank", false),
  BlankSmall("blank_small", true),
  
  Selection("selection", false),
  
  Arc("arc", false),
  ArcSmall("arc_small", true),
  PreArc("pre_arc", false),
  PreArcSmall("pre_arc_small", true),
  PostArc("post_arc", false),
  PostArcSmall("post_arc_small", true),
  
  Place("place", false),
  PlaceSmall("place_small", true),
  PlacesSmall("places_small",true),
  
  Transition("transition", false),
  TransitionSmall("transition_small",true),
  TransitionsSmall("transitions_small",true),
  
  PTSmall("pt_small",true),
  AHLSmall("ahl_small", true),
  
  NetSmall("net_small", true),
  RuleSmall("rule_small", true),
  
  ProjectSmall("project_small", true);

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
  
  public static ImageIcon mergeIcons(EnumIcon topIcon, EnumIcon bottomIcon) {
    return mergeIcons(topIcon.getIcon(), bottomIcon.getIcon());
  }
  
  public static ImageIcon mergeIcons(ImageIcon topIcon, ImageIcon bottomIcon) {
    Image img = new BufferedImage(bottomIcon.getIconWidth(),
            bottomIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics g = img.getGraphics();
    g.drawImage(bottomIcon.getImage(), 0, 0, null);
    g.drawImage(topIcon.getImage(), 0, 0, null);
    return new ImageIcon(img);
  }
  
  public ImageIcon getIcon() {
    return icon;
  }
  
  public static EnumIcon fromElementType(EnumElementType type, boolean small) {
    switch(type) {
      case ArcElement:
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
  
  public static EnumIcon fromNetType(EnumNetType netType, boolean small) {
    switch(netType) {
      case PTNet:
        return PTSmall;
      case AHLNet:
        return AHLSmall;
      case AHLInstantiation:
        return AHLSmall;
      default:
        throw new UnsupportedOperationException("No icon specified for net type " + netType + ".");
    }
  }
  
  public static EnumIcon fromModelType(EnumModelType modelType, boolean small) {
    switch(modelType) {
      case Net:
        return NetSmall;
      case Rule:
        return RuleSmall;
      default:
        throw new UnsupportedOperationException("No icon specified for model type " + modelType + ".");
    }
  }
}
