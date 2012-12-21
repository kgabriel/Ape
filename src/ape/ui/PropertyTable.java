/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui;

import ape.ui.UI;
import ape.ui.modelview.VisualTableCellEditor;
import ape.ui.modelview.VisualTableCellRenderer;
import ape.ui.modelview.VisualTableModel;
import ape.ui.modelview.generic.Visual;
import ape.util.Property;
import ape.util.PropertyContainer;
import java.util.Collection;
import javax.swing.JTable;

/**
 *
 * @author Gabriel
 */
public class PropertyTable extends JTable {

  private VisualTableModel tableModel;
  private UI ui;
  
  public PropertyTable(UI ui) {
    super(new VisualTableModel(ui));
    this.ui = ui;
    tableModel = (VisualTableModel) getModel();
    setDefaultRenderer(Object.class, new VisualTableCellRenderer());
    setDefaultEditor(Object.class, new VisualTableCellEditor(ui, tableModel));
  }
  
  public void refresh() {
    tableModel.refresh();
  }

  public void displayProperties(PropertyContainer container) {
    if(container == null) return;
    tableModel.setProperties(container.getProperties());
  }
}
