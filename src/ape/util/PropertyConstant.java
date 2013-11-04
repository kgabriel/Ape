/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.util;

/**
 *
 * @author Gabriel
 */
public class PropertyConstant extends Property {
  
  private final Object value;

  public PropertyConstant(String category, Object object, EnumPropertyType type, String name, Object value) {
    super(category, object, type, name);
    this.editable = false;
    this.value = value;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public void setValue(Object value) {}
}
