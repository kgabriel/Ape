/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.modelview;

import ape.ui.modelview.generic.EnumVisualPropertyType;
import ape.ui.modelview.generic.VisualProperty;
import java.awt.Color;
import java.awt.Component;
import javax.swing.BoundedRangeModel;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Gabriel
 */
public class VisualTableCellRenderer implements TableCellRenderer {

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                                                 boolean hasFocus, int row, int column) {
    
    /* get type of property and if it is editable*/
    EnumVisualPropertyType type = EnumVisualPropertyType.String;
    boolean editable = false;
    if(column == 1) {
      VisualProperty prop = ((VisualTableModel) table.getModel()).getPropertyAt(row);
      if(prop != null) {
        type = prop.getType();
        editable = prop.isEditable();
      }
    }
    /* even or odd row (for alternating background color) */
    boolean evenRow = row % 2 == 0;
    
    switch (type) {
      case Interval:
        return createSlider(value, editable);
      case Integer:
        return createLabel(value, JLabel.RIGHT, evenRow, hasFocus, editable);
      case String:
      default:
        return createLabel(value, JLabel.LEFT, evenRow, hasFocus, editable);
    }
  }
  
  private JSlider createSlider(Object value, boolean editable) {
    int max = 1000000000;
    JSlider slider = new JSlider(0, max);
    slider.setValue((int) ((double) value * max));
    slider.setToolTipText(value.toString());
    return slider;
  }
  
  private JLabel createLabel(Object content, int alignment, boolean evenRow, boolean hasFocus, boolean editable) {
    JLabel label = new JLabel();
    label.setOpaque(true); 
   String text = (content != null ? content.toString() : ""); 
    label.setText(text);
    label.setToolTipText(text);
    label.setBorder(new EmptyBorder(3, 3, 3, 3));
    label.setHorizontalAlignment(alignment);
    if(hasFocus) {
      if(editable) {
        label.setBackground(new Color(240,255,245));
      } else {
        label.setBackground(new Color(255,245,240));
      }
    } else {
      if(evenRow) {
        label.setBackground(Color.white);
      } else {
        label.setBackground(new Color(240,240,240));
      }
    }
    return label;
  }
  
}
