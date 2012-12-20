/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.modelview.generic;

import java.io.Serializable;

/**
 *
 * @author Gabriel
 */
public abstract class VisualProperty implements Serializable {

  protected Object object;
  private EnumVisualPropertyType type;
  private String name;
  private boolean editable;

  /**
   * Constructor for a <code>VisualProperty</code> of a specified object.
   * @param object the object that has this property
   * @param type the type of this property (for example string or integer)
   * @param name the name of this property
   * @param editable <code>true</code> if this property can be edited by the user
   */
  public VisualProperty(Object object, EnumVisualPropertyType type, String name, boolean editable) {
    this.object = object;
    this.type = type;
    this.name = name;
    this.editable = editable;
  }
  
  /**
   * Returns the object that has this property.
   * @return the object specified in the constructor
   */
  public Object getObject() {
    return object;
  }
  
  /**
   * Returns the property type of this property (for example string or integer).
   * @return the type of this property
   */
  public EnumVisualPropertyType getType() {
    return type;
  }
  
  /**
   * Returns the name of this property as set in the constructor.
   * @return the name of this property
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the value of this property. This method has to be overridden in order to make
   * sure, the value is obtained properly from the object.
   * @return the value of this property
   */
  public abstract Object getValue();
  
  /**
   * Sets the value of this property. This method has to be overridden in order to make
   * sure, the change of the value is properly reflected in the object.
   * @param value the new value of this property
   */
  public abstract void setValue(Object value);

  /**
   * Returns whether this property can be edited by the user.
   * @return <code>true</code> if the property is editable
   */
  public boolean isEditable() {
    return editable;
  }
}
