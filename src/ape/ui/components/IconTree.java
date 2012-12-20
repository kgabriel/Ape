/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.components;

import ape.util.EnumIcon;
import javax.swing.JTree;
import javax.swing.tree.*;

/**
 *
 * @author Gabriel
 */
public class IconTree extends JTree {

  private IconTreeNode root;
  private DefaultTreeModel treeModel;

  public IconTree() {
    setRootVisible(false);
    root = new IconTreeNode("Elements", EnumIcon.BlankSmall);
    treeModel = new DefaultTreeModel(root);
    setModel(treeModel);
    setCellRenderer(new IconTreeCellRenderer());
  }

  public void refresh() {
    refresh(root);
  }

  public void refresh(TreeNode node) {
    treeModel.reload(node);
  }

  public void expandNode(IconTreeNode node) {
    TreePath path = new TreePath(node.getPath());
    expandPath(path);
  }
  
  public IconTreeNode addNode(IconTreeNode parent, Object object, EnumIcon icon) {
    IconTreeNode node = new IconTreeNode(object, icon);
    parent.insert(node, parent.getChildCount());
    expandNode(parent);
    return node;
  }
  
  public IconTreeNode addNode(Object object, EnumIcon icon) {
    return addNode(root, object, icon);
  }
}
