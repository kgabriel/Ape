/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.modelview;

import ape.ui.UI;
import ape.ui.modelview.generic.Visual;
import ape.ui.modelview.generic.VisualProperty;
import java.util.Collection;
import javax.swing.JTable;

/**
 *
 * @author Gabriel
 */
public class VisualTable extends JTable {

  private VisualTableModel tableModel;
  private UI ui;
  
  public VisualTable(UI ui) {
    super(new VisualTableModel(ui));
    this.ui = ui;
    tableModel = (VisualTableModel) getModel();
    setDefaultRenderer(Object.class, new VisualTableCellRenderer());
    setDefaultEditor(Object.class, new VisualTableCellEditor(ui, tableModel));
  }
  
  public void refresh() {
    tableModel.refresh();
  }
  
  public void setVisual(Visual visual) {
//    editingCanceled(null);
    tableModel.setProperties(visual.getProperties());
  }
  
  public void setProperties(Collection<VisualProperty> properties) {
//    editingCanceled(null);
    tableModel.setProperties(properties);
  }
}
