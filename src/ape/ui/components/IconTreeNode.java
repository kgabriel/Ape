/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.components;

import ape.util.EnumIcon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Gabriel
 */
public class IconTreeNode extends DefaultMutableTreeNode {
  
  EnumIcon icon;
  EnumIcon closedIcon;
  
  public IconTreeNode(Object userObject, EnumIcon icon) {
    this(userObject, icon, null);
  }
  
  public IconTreeNode(Object userObject, EnumIcon icon, EnumIcon closedIcon) {
    super(userObject);
    this.icon = icon;
    this.closedIcon = closedIcon;
  }
  
  public ImageIcon getIcon() {
    return icon.getIcon();
  }
  
  public boolean hasClosedIcon() {
    return closedIcon != null;
  }
  
  public ImageIcon getClosedIcon() {
    return closedIcon.getIcon();
  }
}
