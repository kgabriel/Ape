/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.org;

import ape.util.EnumPropertyType;
import ape.util.Property;
import ape.util.PropertyConstant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class ProjectStorage extends StorageContainer<ModelStorage> {

  public ProjectStorage() {
    super("Project");
  }

  @Override
  public List<Property> getProperties() {
    List<Property> properties = new ArrayList<>();
    properties.add(new Property(this, EnumPropertyType.String, "Project Name", true) {
      @Override
      public Object getValue() {
        return getName();
      }

      @Override
      public void setValue(Object value) {
        setName((String) value);
      }
    });
    return properties;
  }
  
  
}
