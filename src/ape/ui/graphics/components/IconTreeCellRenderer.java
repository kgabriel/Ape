/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.components;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Gabriel
 */
public class IconTreeCellRenderer extends DefaultTreeCellRenderer {

  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, 
                            boolean expanded, boolean leaf, int row, boolean hasFocus) {
    
    if(value instanceof IconTreeNode) {
      IconTreeNode node = (IconTreeNode) value;
      ImageIcon icon = node.getIcon();
      setLeafIcon(icon);
      setOpenIcon(icon);
      if(node.hasClosedIcon()) {
        setClosedIcon(node.getClosedIcon());
      } else {
        setClosedIcon(icon);
      }
    }
              
    return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
  }
}
