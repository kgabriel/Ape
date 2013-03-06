/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.components;

import ape.util.EnumIcon;
import java.util.Enumeration;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

/**
 *
 * @author Gabriel
 */
public class IconTree extends JTree implements TreeSelectionListener {

  private IconTreeNode root;
  private DefaultTreeModel treeModel;
  private TreePath selectionPath;

  public IconTree() {
    setRootVisible(false);
    root = new IconTreeNode("Elements", EnumIcon.BlankSmall);
    treeModel = new DefaultTreeModel(root);
    setModel(treeModel);
    setCellRenderer(new IconTreeCellRenderer());
    addTreeSelectionListener(this);
    setToggleClickCount(1);
  }
  
  public void clear() {
    for(int i=treeModel.getChildCount(root)-1;i>=0;i--) {
      treeModel.removeNodeFromParent((IconTreeNode) treeModel.getChild(root, i));
    }
  }

  public void refresh() {
    refresh(root);
  }

  public void refresh(IconTreeNode node) {
    treeModel.reload(node);
    if(selectionPath != null) expandExplicitly(selectionPath);
    traverseAndExpandImplicitly(node);
  }
  
  private void traverseAndExpandImplicitly(IconTreeNode node) {
    if(node.isExpanded()) expandNode(node);
    Enumeration children = node.children();
    while(children.hasMoreElements()) {
      traverseAndExpandImplicitly((IconTreeNode) children.nextElement());
    }
  }
  
  private void expandExplicitly(TreePath path) {
    TreeNode node = (TreeNode) path.getLastPathComponent();
    if(node.isLeaf()) {
      expandPath(path.getParentPath());
    } else {
      expandPath(path);
    }
  }
  
  private void expandNode(IconTreeNode node) {
    expandExplicitly(new TreePath(node.getPath()));
  }

  public IconTreeNode addNode(IconTreeNode parent, Object object, EnumIcon icon) {
    IconTreeNode node = new IconTreeNode(object, icon);
    parent.insert(node, parent.getChildCount());
    return node;
  }
  
  public IconTreeNode addNode(Object object, EnumIcon icon) {
    return addNode(root, object, icon);
  }
  
  public void setSelection(IconTreeNode node) {
    selectionPath = new TreePath(node.getPath());
  }

  @Override
  public void valueChanged(TreeSelectionEvent e) {
    selectionPath = e.getPath();
  }
}
