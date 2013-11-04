/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.util;

import java.util.List;

/**
 *
 * @author Gabriel
 */
public class AbstractPropertyContainer implements PropertyContainer {
  
  List<Property> properties;
  
  public AbstractPropertyContainer(List<Property> properties) {
    this.properties = properties;
  }

  @Override
  public List<Property> getProperties() {
    return properties;
  }
}
