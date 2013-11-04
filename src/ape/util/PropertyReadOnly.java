/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.util;

/**
 *
 * @author Gabriel
 */
public abstract class PropertyReadOnly extends Property {

  public PropertyReadOnly(String category, Object object, EnumPropertyType type, String key) {
    super(category, object, type, key);
    this.editable = false;
  }

  @Override
  public void setValue(Object value) {}
}
