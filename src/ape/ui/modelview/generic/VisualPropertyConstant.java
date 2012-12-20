/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.modelview.generic;

/**
 *
 * @author Gabriel
 */
public class VisualPropertyConstant extends VisualProperty {
  
  private final Object value;

  public VisualPropertyConstant(Object object, EnumVisualPropertyType type, String name, Object value) {
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
