/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.modelview;

import ape.ui.UI;
import ape.ui.modelview.generic.Visual;
import ape.util.Property;
import java.util.Collection;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gabriel
 */
public class VisualTableModel extends DefaultTableModel {
  
  private Collection<Property> properties;
  private UI ui;

  public VisualTableModel(UI ui) {
    super(new String[] { "Property", "Value" }, 1);
    this.ui = ui;
  }
  
  public void clearTable() {
    for(int i=getRowCount()-1;i>=0;i--) {
      removeRow(i);
    }
  }
  
  public void setProperties(Collection<Property> properties) {
    this.properties = properties;
    refresh();
  }
  
  public void refresh() {
    clearTable();
    if(properties != null) fillTable();
  }
  
  private void fillTable() {
    for(Property prop : properties) {
      addRow(prop.getKey(), prop.getValue());
    }
  }
  
  public void addRow(String propertyName, Object value) {
    addRow(new Object[] { propertyName, value });
  }
  
  public void insertRow(int row, String propertyName, Object value) {
    insertRow(row, new Object[] { propertyName, value });
  }
  
  public Property getPropertyAt(int row) {
    if(properties == null) return null;
    return (Property) properties.toArray()[row];
  }
  
  
  @Override
  public boolean isCellEditable(int row, int col) {
    if (col != 1) return false;
    return getPropertyAt(row).isEditable();
  }
  
  @Override
  public void setValueAt(Object value, int row, int col) {
    if(col != 1) return;
    Property prop = getPropertyAt(row);
    try {
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
    } catch (Exception e) {}
    ui.getModelViewCanvas().repaint();
  }
}
