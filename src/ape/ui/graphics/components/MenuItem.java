/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.components;

import ape.ui.control.actions.Action;
import ape.util.EnumIcon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

/**
 *
 * @author Gabriel
 */
public class MenuItem extends JMenuItem {
  
  Action action;
  
  public MenuItem(String caption, Action action) {
    this(caption, -1, EnumIcon.BlankSmall, action);
  }

  public MenuItem(String caption, int mnemonic, Action action) {
    this(caption, mnemonic, EnumIcon.BlankSmall, action);
  }
  
  public MenuItem(String caption, EnumIcon icon, Action action) {
    this(caption, -1, icon, action);
  }
  
  public MenuItem(String caption, ImageIcon icon, Action action) {
    this(caption, -1, icon, action);
  }
 
  public MenuItem(String caption, int mnemonic, EnumIcon icon, Action action) {
    this(caption, mnemonic, icon.getIcon(), action);
  }

  public MenuItem(String caption, int mnemonic, ImageIcon icon, Action action) {
    this.action = action;
    setAction(action.getSwingAction(caption, icon));
    setText(caption);
    if(mnemonic != -1) setMnemonic(mnemonic);
    setIcon(icon);
  }
}
