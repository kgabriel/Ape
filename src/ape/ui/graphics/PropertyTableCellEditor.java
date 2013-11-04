/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics;

import ape.ui.UI;
import ape.util.Property;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;
import javax.swing.text.Caret;

/**
 *
 * @author Gabriel
 */
public class PropertyTableCellEditor extends AbstractCellEditor implements TableCellEditor, 
        ActionListener, KeyListener, WindowListener, ChangeListener {

  private Property cellProperty;
  private Object value;
  private JButton editButton;
  private JDialog editFrame;
  private JTextArea editArea;
  private JTextField editField;
  private JSlider editSlider;
  private int row;
  private int editMode;
  private UI ui;
  private PropertyTableModel model;
  
  public final static String EDIT_TEXT_ACTION = "Edit";
  public final static String OK_TEXT_ACTION = "OK";
  public final static String CANCEL_TEXT_ACTION = "Cancel";
  
  public final static int EDIT_MODE_NONE = 0;
  public final static int EDIT_MODE_SINGLE_LINE_TEXT = 1;
  public final static int EDIT_MODE_MULTI_LINE_TEXT = 2;
  public final static int EDIT_MODE_SLIDE = 4;
  
  private final static int SLIDER_GRANULARITY = 1000;

  public PropertyTableCellEditor(UI ui, PropertyTableModel model) {
    this.ui = ui;
    this.model = model;
  }
  
  

  @Override
  public Object getCellEditorValue() {
    return value;
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    
    PropertyTableModel tableModel = ((PropertyTableModel) table.getModel());

    boolean toggledCategory = tableModel.toggleCategoryAt(row);
    if(toggledCategory) {
      fireEditingCanceled();
      editMode = EDIT_MODE_NONE;
      return null;
    }
    
    cellProperty = tableModel.getPropertyAt(row);
    this.value = cellProperty.getValue();
    switch(cellProperty.getType()) {
      default:
      case MultiLineText:
        editMode = EDIT_MODE_MULTI_LINE_TEXT;
        editFrame = new JDialog(ui.getMainFrame(),true);
        editFrame.addWindowListener(this);
        editFrame.setTitle("Edit " + cellProperty.getKey());
        editArea = new JTextArea(cellProperty.getValue().toString(),3,100);
        editArea.addKeyListener(this);
        editArea.setSelectionStart(0);
        editArea.setSelectionEnd(editArea.getText().length());
        editFrame.setLayout(new BorderLayout());
        editFrame.add(new JScrollPane(editArea), BorderLayout.CENTER);
        JButton cancelButton = new JButton();
        cancelButton.setActionCommand(CANCEL_TEXT_ACTION);
        cancelButton.setText("Cancel (Esc)");
        cancelButton.addActionListener(this);
        JButton okButton = new JButton();
        okButton.setActionCommand(OK_TEXT_ACTION);
        okButton.setText("Apply (Ctrl+Enter)");
        okButton.addActionListener(this);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        editFrame.add(buttonPanel,BorderLayout.SOUTH);
        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);
        editFrame.setSize(300,300);
        editFrame.setLocationRelativeTo(ui.getMainFrame());
        
        editButton = new JButton();
        editButton.setActionCommand(EDIT_TEXT_ACTION);
        editButton.setText(EDIT_TEXT_ACTION);
        editButton.addActionListener(this);
        return editButton;
        
      case SingleLineText:
        editField = new JTextField(this.value.toString());
        editField.setHorizontalAlignment(JTextField.LEFT);
        editField.addKeyListener(this);
//        editField.setSelectionStart(0);
//        editField.setSelectionEnd(editField.getText().length());
        Caret caret = editField.getCaret();
        caret.setDot(0);
        caret.moveDot(editField.getText().length());
        editMode = EDIT_MODE_SINGLE_LINE_TEXT;
        return editField;
        
      case Integer:
        editField = new JTextField(this.value.toString());
        editField.setHorizontalAlignment(JTextField.RIGHT);
        editField.addKeyListener(this);
        editField.setSelectionStart(0);
        editField.setSelectionEnd(editField.getText().length());
        editMode = EDIT_MODE_SINGLE_LINE_TEXT;
        return editField;
        
      case Interval:
        this.row = row;
        int max = SLIDER_GRANULARITY;
        editSlider = new JSlider(0, max);
        editSlider.setValue((int) ((double) this.value * max));
        editSlider.addChangeListener(this);
        editMode = EDIT_MODE_SLIDE;
        return editSlider;
    }
  }
  
  public void fireEditButton() {
    if(editButton != null) {
      editButton.doClick();
    } else if(editField != null) {
      editField.requestFocusInWindow();
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getActionCommand().equals(EDIT_TEXT_ACTION)) {
      editFrame = new JDialog(ui.getMainFrame(),true);
      editFrame.addWindowListener(this);
      editFrame.setTitle("Edit " + cellProperty.getKey());
      editArea = new JTextArea(cellProperty.getValue().toString(),3,100);
      editArea.addKeyListener(this);
      editArea.setSelectionStart(0);
      editArea.setSelectionEnd(editArea.getText().length());
      editFrame.setLayout(new BorderLayout());
      editFrame.add(new JScrollPane(editArea), BorderLayout.CENTER);
      JButton cancelButton = new JButton();
      cancelButton.setActionCommand(CANCEL_TEXT_ACTION);
      cancelButton.setText("Cancel (Esc)");
      cancelButton.addActionListener(this);
      JButton okButton = new JButton();
      okButton.setActionCommand(OK_TEXT_ACTION);
      okButton.setText("Apply (Ctrl+Enter)");
      okButton.addActionListener(this);
      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new GridLayout(1, 2));
      editFrame.add(buttonPanel,BorderLayout.SOUTH);
      buttonPanel.add(cancelButton);
      buttonPanel.add(okButton);
      editFrame.setSize(300,300);
      editFrame.setLocationRelativeTo(ui.getMainFrame());
      editFrame.setVisible(true);
    } else if (e.getActionCommand().equals(CANCEL_TEXT_ACTION)) {
      cancelEditing();
    } else if (e.getActionCommand().equals(OK_TEXT_ACTION)) {
      endEditing();
    }
  }
  
  public void cancelEditing() {
    if(editMode == EDIT_MODE_MULTI_LINE_TEXT) {
      editFrame.dispose();
      editFrame = null;
      editArea = null;
    } else if(editMode == EDIT_MODE_SINGLE_LINE_TEXT) {
      editField = null;
    } else if(editMode == EDIT_MODE_SLIDE) {
    }
    if(editMode != EDIT_MODE_NONE) fireEditingCanceled();
    editMode = EDIT_MODE_NONE;
  }
  
  public void endEditing() {
    if(editMode == EDIT_MODE_MULTI_LINE_TEXT) {
      this.value = editArea.getText();
      editFrame.dispose();
      editArea = null;
      editFrame = null;
    } else if(editMode == EDIT_MODE_SINGLE_LINE_TEXT) {
      this.value = editField.getText();
      editField = null;
    } else if(editMode == EDIT_MODE_SLIDE) {
      double val = editSlider.getValue() / (double) SLIDER_GRANULARITY;
      value = val;
      editSlider = null;
    }
    if(editMode != EDIT_MODE_NONE) fireEditingStopped();
    editMode = EDIT_MODE_NONE;
  }

  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyPressed(KeyEvent e) {
    if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
      cancelEditing();
    } else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
      if(editMode != EDIT_MODE_MULTI_LINE_TEXT || (e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0) {
        endEditing();
      }
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {}

  @Override
  public void windowOpened(WindowEvent e) {}

  @Override
  public void windowClosing(WindowEvent e) {
    fireEditingStopped();
  }

  @Override
  public void windowClosed(WindowEvent e) {}

  @Override
  public void windowIconified(WindowEvent e) {}

  @Override
  public void windowDeiconified(WindowEvent e) {}

  @Override
  public void windowActivated(WindowEvent e) {}

  @Override
  public void windowDeactivated(WindowEvent e) {}

  @Override
  public void stateChanged(ChangeEvent e) {
    if(editMode == EDIT_MODE_SLIDE) {
      double val = editSlider.getValue() / (double) SLIDER_GRANULARITY;
      value = val;
      model.setValueAt(value, row, 1);
    }
  }
}
