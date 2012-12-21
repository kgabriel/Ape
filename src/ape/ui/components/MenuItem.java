/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.components;

import ape.util.EnumIcon;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

/**
 *
 * @author Gabriel
 */
public abstract class MenuItem extends JMenuItem {
  
  public MenuItem(String caption, int mnemonic, EnumIcon icon) {
    initAction();
    setText(caption);
    if(mnemonic != -1) setMnemonic(mnemonic);
    setIcon(icon.getIcon());
  }
  
  private void initAction() {
    
    setAction(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        action(e);
      }
    });
  }

  public MenuItem(String name, int mnemonic) {
    this(name, mnemonic, EnumIcon.BlankSmall);
  }
  
  public MenuItem(String name, EnumIcon icon) {
    this(name, -1, icon);
  }
  
  public MenuItem(String name) {
    this(name, -1, EnumIcon.BlankSmall);
  }
  
  public abstract void action(ActionEvent e);
}
