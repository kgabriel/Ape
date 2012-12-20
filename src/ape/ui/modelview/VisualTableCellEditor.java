/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.modelview;

import ape.ui.UI;
import ape.ui.modelview.generic.VisualProperty;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Gabriel
 */
public class VisualTableCellEditor extends AbstractCellEditor implements TableCellEditor, 
        ActionListener, KeyListener, WindowListener, ChangeListener, FocusListener {

  private VisualProperty cellProperty;
  private Object value;
  private JDialog editFrame;
  private JTextArea editArea;
  private JTextField editField;
  private JSlider editSlider;
  private int row;
  private int editMode;
  private UI ui;
  private VisualTableModel model;
  
  public final static String EDIT_TEXT_ACTION = "Edit";
  public final static String OK_TEXT_ACTION = "OK";
  public final static String CANCEL_TEXT_ACTION = "Cancel";
  
  public final static int EDIT_MODE_SINGLE_LINE_TEXT = 0;
  public final static int EDIT_MODE_MULTI_LINE_TEXT = 1;
  public final static int EDIT_MODE_SLIDE = 2;

  public VisualTableCellEditor(UI ui, VisualTableModel model) {
    this.ui = ui;
    this.model = model;
  }
  
  

  @Override
  public Object getCellEditorValue() {
    return value;
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    
    cellProperty = ((VisualTableModel) table.getModel()).getPropertyAt(row);
    this.value = cellProperty.getValue();
    switch(cellProperty.getType()) {
      default:
      case String:
        JButton button = new JButton();
        button.setActionCommand(EDIT_TEXT_ACTION);
        button.setText(EDIT_TEXT_ACTION);
        button.addActionListener(this);
        editMode = EDIT_MODE_MULTI_LINE_TEXT;
        return button;
        
      case Integer:
        editField = new JTextField(this.value.toString());
        editField.setHorizontalAlignment(JTextField.RIGHT);
        editField.addKeyListener(this);
        editField.addFocusListener(this);
        editMode = EDIT_MODE_SINGLE_LINE_TEXT;
        return editField;
        
      case Interval:
        this.row = row;
        int max = 1000000000;
        editSlider = new JSlider(0, max);
        editSlider.setValue((int) ((double) this.value * max));
        editSlider.addChangeListener(this);
        editSlider.addFocusListener(this);
        return editSlider;
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getActionCommand().equals(EDIT_TEXT_ACTION)) {
      editFrame = new JDialog(ui.getMainFrame(),true);
      editFrame.addWindowListener(this);
      editFrame.setTitle("Edit " + cellProperty.getName());
      editArea = new JTextArea(cellProperty.getValue().toString(),3,100);
      editArea.addKeyListener(this);
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
  
  private void cancelEditing() {
    if(editMode == EDIT_MODE_MULTI_LINE_TEXT) {
      editFrame.dispose();
      editFrame = null;
      editArea = null;
    } else if(editMode == EDIT_MODE_SINGLE_LINE_TEXT) {
      editField = null;
    } else if(editMode == EDIT_MODE_SLIDE) {
      editSlider = null;
    }
    fireEditingCanceled();
  }
  
  private void endEditing() {
      if(editMode == EDIT_MODE_MULTI_LINE_TEXT) {
        this.value = editArea.getText();
        editFrame.dispose();
        editArea = null;
        editFrame = null;
      } else if(editMode == EDIT_MODE_SINGLE_LINE_TEXT) {
        this.value = editField.getText();
        System.out.println("stopped");
        editField = null;
      } else if(editMode == EDIT_MODE_SLIDE) {
        double val = editSlider.getValue() / 1000000000.0;
        value = val;
        editSlider = null;
      }
      fireEditingStopped();
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
    double val = editSlider.getValue() / 1000000000.0;
    value = val;
//    fireEditingStopped();
    model.setValueAt(value, row, 1);
  }

  @Override
  public void focusGained(FocusEvent e) {}

  @Override
  public void focusLost(FocusEvent e) {
    cancelEditing();
  }
  
  
}
