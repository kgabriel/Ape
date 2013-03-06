/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics;

import ape.org.ModelStorage;
import ape.org.ProjectStorage;
import ape.org.Storage;
import ape.ui.graphics.components.IconTreeCellRenderer;
import ape.ui.graphics.components.IconTreeNode;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTree;

/**
 *
 * @author Gabriel
 */
public class ProjectTreeCellRenderer extends IconTreeCellRenderer {
  
  ProjectTree projectTree;

  public ProjectTreeCellRenderer(ProjectTree projectTree) {
    this.projectTree = projectTree;
  }

  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

    super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
    if(value instanceof IconTreeNode) {
      IconTreeNode node = (IconTreeNode) value;
      Storage storage = projectTree.getStorage(node);
      if(storage instanceof ProjectStorage) {
        ProjectStorage project = (ProjectStorage) storage;
        if(projectTree.isActive(project)) {
          setForActiveProject();
        } else {
          setForInactive();
        }
      } else if(storage instanceof ModelStorage) {
        ModelStorage model = (ModelStorage) storage;
        if(projectTree.isActive(model)) {
          setForActiveModel();
        } else {
          setForInactive();
        }
      } 
    }
    return this;
  }
  
  private void setForInactive() {
    setBackgroundNonSelectionColor(Color.white);
    setBackgroundSelectionColor(new Color(200,200,255));
    setForeground(Color.black);
  }
  
  private void setForActiveProject() {
    Color color = new Color(255,255,210);
    setBackgroundNonSelectionColor(color);
    setBackgroundSelectionColor(color);
    setForeground(Color.black);
  }
  
  private void setForActiveModel() {
    Color color = new Color(220,255,230);
    setBackgroundNonSelectionColor(color);
    setBackgroundSelectionColor(color);
    setForeground(Color.black);
  }
}
