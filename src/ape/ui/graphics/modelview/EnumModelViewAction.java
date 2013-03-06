/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview;

import ape.util.EnumIcon;
import javax.swing.ImageIcon;

/**
 * An enumeration of possible user actions on a {@link ModelView}.
 * @author Gabriel
 */
public enum EnumModelViewAction {
  
  Selection("Select", "Select Elements", EnumIcon.Selection),
  NewPlace("New Place", "Create a new Place", EnumIcon.Place),
  NewTransition("New Transition", "Create a new Transition", EnumIcon.Transition),
  NewArc("New Arc", "Create a new Arc", EnumIcon.Arc);

  private String name;
  private String description;
  private EnumIcon icon;

  EnumModelViewAction(String name, String description, EnumIcon icon) {
    this.name = name;
    this.description = description;
    this.icon = icon;
  }

  public String getDescription() {
    return description;
  }

  public ImageIcon getIcon() {
    return icon.getIcon();
  }

  public String getName() {
    return name;
  }
}
