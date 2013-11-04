/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview;

import ape.petri.generic.EnumModelType;
import ape.util.EnumIcon;
import javax.swing.ImageIcon;

/**
 * An enumeration of possible user actions on a {@link ModelView}.
 * @author Gabriel
 */
public enum EnumModelViewAction {
  
  NetSelection(EnumModelType.Net, "Select", "Select Elements", EnumIcon.Selection),
  NetNewPlace(EnumModelType.Net, "New Place", "Create a new Place", EnumIcon.Place),
  NetNewTransition(EnumModelType.Net, "New Transition", "Create a new Transition", EnumIcon.Transition),
  NetNewArc(EnumModelType.Net, "New Arc", "Create a new Arc", EnumIcon.Arc);

  private EnumModelType modelType;
  private String name;
  private String description;
  private EnumIcon icon;

  EnumModelViewAction(EnumModelType modelType, String name, String description, EnumIcon icon) {
    this.modelType = modelType;
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
  
  public EnumModelType getModelType() {
    return modelType;
  }
}
