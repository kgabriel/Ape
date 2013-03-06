/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.org;

import ape.petri.generic.EnumModelType;
import ape.util.EnumPropertyType;
import ape.util.Property;
import ape.util.PropertyConstant;
import ape.util.aml.AMLNode;
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
    properties.add(new PropertyConstant(Property.CATEGORY_PROPERTIES, this, EnumPropertyType.String, "Entity Type", "Project"));
    properties.add(new Property(Property.CATEGORY_PROPERTIES, this, EnumPropertyType.String, "Name", true) {
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

  @Override
  public String getAMLTagName() {
    return "ProjectStorage";
  }

  @Override
  public void readAMLNode(AMLNode node) {
    super.readAMLNode(node);
    clear();
    for(AMLNode child : node.getChildren()) {
      ModelStorage modelStorage = new ModelStorage(EnumModelType.Rule, null, null);
      modelStorage.readAMLNode(child);
      addStorage(modelStorage);
    }
  }
  
  
}
