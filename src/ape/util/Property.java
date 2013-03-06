/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.util;

import java.io.Serializable;

/**
 *
 * @author Gabriel
 */
public abstract class Property implements Comparable<Property>, Serializable {

  protected Object object;
  private EnumPropertyType type;
  private String key;
  private boolean editable;
  private String category;

  public static final String CATEGORY_PROPERTIES = "Properties";
  public static final String CATEGORY_VALUES = "Values";
  public static final String CATEGORY_VIEW = "View";
  
  /**
   * Constructor for a <code>Property</code> of a specified object.
   * @param object the object that has this property
   * @param type the type of this property (for example string or integer)
   * @param key the key/name of this property
   * @param editable <code>true</code> if this property can be edited by the user
   */
  public Property(String category, Object object, EnumPropertyType type, String key, boolean editable) {
    this.category = category;
    this.object = object;
    this.type = type;
    this.key = key;
    this.editable = editable;
  }

  public String getCategory() {
    return category;
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
  public EnumPropertyType getType() {
    return type;
  }
  
  /**
   * Returns the name of this property as set in the constructor.
   * @return the name of this property
   */
  public String getKey() {
    return key;
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

  @Override
  public int compareTo(Property o) {
    return this.key.compareTo(o.key);
  }
  
  
}
