/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control.settings;

import javax.swing.table.DefaultTableModel;
import ape.ui.control.CommandManager;

/**
 *
 * @author Gabriel
 */
public class CommandSettingsTableModel extends DefaultTableModel {
  
  public CommandSettingsTableModel(CommandManager manager) {
    
  }

  private String[] columnNames = new String[] {
    "Context", 
    "Command", 
    "Description", 
    "Device Support", 
    "Key/Button", 
    "Optional"
  };
/*  private Class[] columnTypes = new Class [] {
      EnumCommandReceiverType.class, 
      Command.class, 
      java.lang.String.class, 
      java.lang.String.class,
      java.lang.String.class,
  };

  @Override
  public Class getColumnClass(int columnIndex) {
    return columnTypes [columnIndex];
  }*/
}
