/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui;

import ape.org.*;
import ape.ui.components.IconTree;
import ape.ui.components.IconTreeNode;
import ape.util.EnumIcon;
import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author Gabriel
 */
public class ProjectTree extends JPanel implements TreeSelectionListener, StorageListener {
  
  private IconTree tree;
  private StorageContainer<ProjectStorage> projectsStorages;
  private Map<IconTreeNode,Storage> storages;
  private Map<Storage,IconTreeNode> nodes;
  private UI ui;
  
  public ProjectTree(StorageContainer<ProjectStorage> projectStorages, UI ui) {
    this.projectsStorages = projectStorages;
    projectStorages.addStorageListener(this);
    this.ui = ui;
    storages = new HashMap<>();
    nodes = new HashMap<>();
    tree = new IconTree();
    init();
  }

  private void init() {
    setLayout(new BorderLayout());
    JScrollPane scrollPane = new JScrollPane(tree);
    add(scrollPane, BorderLayout.CENTER);
    validate();
    
    tree.setRootVisible(false);
    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    tree.addTreeSelectionListener(this);
    
    updateTree();
  }
  
  private void putStorage(Storage storage, IconTreeNode node) {
    storages.put(node, storage);
    nodes.put(storage, node);
  }
  
  public void updateTree() {
    for(ProjectStorage project : projectsStorages.getStorages()) {
      IconTreeNode projectNode;
      if(storages.containsValue(project)) {
        projectNode = nodes.get(project);
        projectNode.setUserObject(project.getName());
      } else {
        projectNode = tree.addNode(project.getName(), EnumIcon.Blank);
        putStorage(project, projectNode);
      }
      
      for(ModelStorage model : project.getStorages()) {
        System.out.println(model);
        IconTreeNode modelNode;
        if(storages.containsValue(model)) {
          modelNode = nodes.get(model);
          modelNode.setUserObject(model.getName());
          continue;
        }
        modelNode = tree.addNode(projectNode, model.getName(), EnumIcon.Blank);
        putStorage(model, modelNode);
      }
    }
    tree.refresh();
  }

  @Override
  public void valueChanged(TreeSelectionEvent e) {
    IconTreeNode node = (IconTreeNode) tree.getLastSelectedPathComponent();
    Storage storage = storages.get(node);
    ui.propertyTable.displayProperties(storage);
  
    if(storage instanceof ProjectStorage) {
      projectsStorages.setActiveStorage((ProjectStorage) storage);
    }
    if(storage instanceof ModelStorage) {
      ModelStorage model = (ModelStorage) storage;
      ProjectStorage project = ((ProjectStorage) model.getContainer());
      project.setActiveStorage(model);  
      projectsStorages.setActiveStorage(project);
      ui.setActiveModelView(model.getView());
    }
    tree.expandNode(nodes.get(projectsStorages.getActiveStorage()));
  }

  @Override
  public void storageChanged(Storage changedStorage) {
    updateTree();
  }
}
