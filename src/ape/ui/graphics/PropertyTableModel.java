/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics;

import ape.ui.UI;
import ape.util.Property;
import java.util.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gabriel
 */
public class PropertyTableModel extends DefaultTableModel {
  
  private SortedMap<String,List<Property>> categorizedProperties;
  private Map<String,Boolean> categoryVisible;
  private UI ui;

  public PropertyTableModel(UI ui) {
    super(new String[] { "Property", "Value" }, 1);
    this.categorizedProperties = new TreeMap<>();
    this.categoryVisible = new HashMap<>();
    this.ui = ui;
  }
  
  public void clearTable() {
    for(int i=getRowCount()-1;i>=0;i--) {
      removeRow(i);
    }
  }
  
  private void addProperty(String category, Property property) {
    if(categorizedProperties.containsKey(category)) {
      categorizedProperties.get(category).add(property);
    } else {
      List<Property> categoryProperties = new ArrayList<>();
      categoryProperties.add(property);
      categorizedProperties.put(category, categoryProperties);
      categoryVisible.put(category, Boolean.TRUE);
    }
  }
  
  public void setProperties(Collection<Property> properties) {
    categorizedProperties.clear();
    categoryVisible.clear();
    for(Property property : properties) {
      addProperty(property.getCategory(), property);
    }
    for(String category : categorizedProperties.keySet()) {
      Collections.sort(categorizedProperties.get(category));
    }
    refresh();
  }
  
  public void refresh() {
    clearTable();
    if(categorizedProperties != null) fillTable();
  }
  
  private void fillTable() {
    for(String category : categorizedProperties.keySet()) {
      addRow(category, "");
      if(categoryVisible.get(category).booleanValue()) {
        for(Property prop : categorizedProperties.get(category)) {
          addRow(prop.getKey(), prop.getValue());
        }
      }
    }
  }
  
  public void addRow(String propertyName, Object value) {
    addRow(new Object[] { propertyName, value });
  }
  
  public Property getPropertyAt(int row) {
    if(categorizedProperties == null) return null;
    for(String category : categorizedProperties.keySet()) {
      if(row == 0) return null;
      
      if(categoryVisible.get(category).booleanValue() == false) {
        row--;
        continue;
      }
      
      List<Property> properties = categorizedProperties.get(category);
      if(row <= properties.size()) {
        return properties.get(row-1);
      }
      row -= properties.size() + 1;
    }
    return null;
  }
  
  public boolean isCategoryVisible(String category) {
    Boolean visible = categoryVisible.get(category);
    if(visible == null) return false;
    return visible;
  }
  
  private void toggleCategory(String category) {
    boolean visible = categoryVisible.get(category).booleanValue();
    categoryVisible.put(category, ! visible);
    refresh();
  }
  
  public boolean toggleCategoryAt(int row) {
    String category = categoryAt(row);
    if(category == null) return false;
    toggleCategory(category);
    return true;
  }
  
  public String categoryAt(int row) {
    if(categorizedProperties == null) return null;
    for(String category : categorizedProperties.keySet()) {
      if(row == 0) return category;
      if(categoryVisible.get(category).booleanValue() == false) {
        row--;
        continue;
      }
      int categorySize = categorizedProperties.get(category).size();
      if(row <= categorySize) return null;
      row -= categorySize + 1;
    }
    return null;
  }
  
  public boolean isCategoryAt(int row) {
    return categoryAt(row) != null;
  }
  
  @Override
  public boolean isCellEditable(int row, int col) {
    if(categoryAt(row) != null) return true;
    if (col != 1) return false;
    Property propertyAtRow = getPropertyAt(row);
    if(propertyAtRow == null) return false;
    return propertyAtRow.isEditable();
  }
  
  @Override
  public void setValueAt(Object value, int row, int col) {
    if(col != 1) return;
    if(isCategoryAt(row)) return;
    Property prop = getPropertyAt(row);
    if(prop == null) return;

    switch(prop.getType()) {
      case String:
        prop.setValue(value);
        break;
      case Integer:
        if(value instanceof Integer) {
          prop.setValue(value);
        } else {
          prop.setValue(Integer.parseInt((String) value));
        }
        break;
      case Interval:
        if(value instanceof Double) {
          prop.setValue(value);
        } else {
          prop.setValue(Double.parseDouble((String) value));
        }
        break;
    }
    
    ui.getModelViewCanvas().repaint();
  }
}
