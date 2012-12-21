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

  public PropertyConstant(Object object, EnumPropertyType type, String name, Object value) {
    super(object, type, name, false);
    this.value = value;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public void setValue(Object value) {}
}
