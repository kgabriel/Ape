/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics;

import ape.ui.graphics.PropertyTableModel;
import ape.util.EnumPropertyType;
import ape.util.Property;
import java.awt.Color;
import java.awt.Component;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Gabriel
 */
public class PropertyTableCellRenderer implements TableCellRenderer {

  private final static int SLIDER_GRANULARITY = 1000;

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                                                 boolean hasFocus, int row, int column) {
    
    PropertyTableModel tableModel = ((PropertyTableModel) table.getModel());
    
    String category = tableModel.categoryAt(row);
    if(category != null) {
      return createCategoryLabel(category, column == 0, tableModel.isCategoryVisible(category));
    }
    
    /* get type of property and if it is editable*/
    EnumPropertyType type = EnumPropertyType.String;
    boolean editable = false;
    if(column == 1) {
      Property prop = tableModel.getPropertyAt(row);
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
    int max = SLIDER_GRANULARITY;
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
  
  private JLabel createCategoryLabel(String category, boolean firstColumn, boolean visible) {
    JLabel label = new JLabel();
    label.setOpaque(true); 
    label.setToolTipText(category);
    label.setBorder(new EmptyBorder(3, 1, 3, 3));
    label.setBackground(new Color(200,200,200));
    if(firstColumn) {
      label.setText(category);
      String iconName = "Tree." + (visible ? "expanded" : "collapsed") + "Icon";
      label.setIcon(UIManager.getIcon(iconName));
    }
    return label;
  }
}
