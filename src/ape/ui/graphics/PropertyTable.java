/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics;

import ape.ui.UI;
import ape.util.Property;
import ape.util.PropertyContainer;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTable;

/**
 *
 * @author Gabriel
 */
public class PropertyTable extends JTable {

  private PropertyTableModel tableModel;
  private PropertyContainer currentContainer;
  private UI ui;
  
  public PropertyTable(UI ui) {
    super(new PropertyTableModel(ui));
    this.ui = ui;
    tableModel = (PropertyTableModel) getModel();
    setDefaultRenderer(Object.class, new PropertyTableCellRenderer());
    setDefaultEditor(Object.class, new PropertyTableCellEditor(ui, tableModel));
  }
  
  public void refresh() {
    tableModel.refresh();
  }

  public void displayProperties(PropertyContainer container) {
    if(container == null) return;
    if(! container.equals(currentContainer)) {
      if(cellEditor != null) {
        PropertyTableCellEditor propertyCellEditor = (PropertyTableCellEditor) cellEditor;
        propertyCellEditor.cancelEditing();
      }
    }
    currentContainer = container;
    tableModel.setProperties(container.getProperties());

  }
  
  
  public int getPropertyRow(String key) {
    if(tableModel == null) return -1;
    for(int i=0;i<tableModel.getRowCount();i++) {
      Property prop = tableModel.getPropertyAt(i);
      if(prop == null) continue;
      if(prop.getKey().equals(key)) return i;
    }
    return -1;
  }
  
  public void editPropertyAt(int row) {
    boolean editing = editCellAt(row, 1);
    if(! editing) return;
    ((PropertyTableCellEditor) cellEditor).fireEditButton();
  }
}
