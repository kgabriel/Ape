/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.org;

import ape.util.EnumPropertyType;
import ape.util.Property;
import ape.util.PropertyConstant;
import ape.util.PropertyReadOnly;
import ape.util.aml.AMLNode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class ProjectStorage extends StorageContainer<ModelStorage> {

  private String fileName;
  private boolean unsavedChanges;
  public static final String NO_FILE = "<none>";

  public ProjectStorage() {
    this(NO_FILE);
  }
  
  public ProjectStorage(String fileName) {
    super("Project");
    this.fileName = fileName;
    this.unsavedChanges = false;
  }

  @Override
  public List<Property> getProperties() {
    List<Property> properties = new ArrayList<>();
    properties.add(new PropertyConstant(Property.CATEGORY_PROPERTIES, this, EnumPropertyType.SingleLineText, "Entity Type", "Project"));
    properties.add(new Property(Property.CATEGORY_PROPERTIES, this, EnumPropertyType.SingleLineText, "Name") {
      @Override
      public Object getValue() {
        return getName();
      }

      @Override
      public void setValue(Object value) {
        setName((String) value);
      }
    });
    properties.add(new PropertyConstant(Property.CATEGORY_PROPERTIES, this, EnumPropertyType.SingleLineText, "File",fileName));
    return properties;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
    storageHasChanged();
  }

  @Override
  public void storageHasChanged() {
    unsavedChanges = true;
    super.storageHasChanged();
  }
  
  public void onSave() {
    unsavedChanges = false;
    super.storageHasChanged();
  }
  
  public boolean hasUnsavedChanges() {
    return unsavedChanges;
  }
  
  public boolean hasFileName() {
    return ! fileName.equals(NO_FILE);
  }
  
  @Override
  public String getAMLTagName() {
    return "ProjectStorage";
  }

  @Override
  public void readAMLNode(AMLNode node) {
    super.readAMLNode(node);
    clear();
    for(AMLNode child : node.getChildren("ModelStorage")) {
      ModelStorage modelStorage = new ModelStorage(null, null);
      modelStorage.readAMLNode(child);
      addStorage(modelStorage);
    }
    unsavedChanges = false;
  }
  
  
}
