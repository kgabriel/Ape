/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics;

import ape.org.*;
import ape.ui.UI;
import ape.ui.graphics.components.IconTree;
import ape.ui.graphics.components.IconTreeNode;
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
  public class ProjectTree extends JPanel implements TreeSelectionListener, StorageListener, StorageContainerListener {
  
  private IconTree tree;
  private ProjectCollection projectsStorages;
  private Map<IconTreeNode,Storage> storages;
  private Map<Storage,IconTreeNode> nodes;
  private UI ui;
  
  public ProjectTree(ProjectCollection projectStorages, UI ui) {
    this.projectsStorages = projectStorages;
    projectStorages.addStorageListener(this);
    projectStorages.addStorageContainerListener(this);
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
    tree.setCellRenderer(new ProjectTreeCellRenderer(this));
    
    updateTree();
  }
  
  private void putStorage(Storage storage, IconTreeNode node) {
    storages.put(node, storage);
    nodes.put(storage, node);
  }
  
  private void removeStorage(Storage storage) {
    IconTreeNode node = nodes.remove(storage);
    storages.remove(node);
  }
  
  private String modelName(ModelStorage model) {
    String modelName = model.getName();
    modelName += " (" + model.getModel().getNetType() + " " + model.getModelType().getName() + ")";
    return modelName;
  }
  
  public void updateTree() {
    for(ProjectStorage project : projectsStorages.getStorages()) {
      IconTreeNode projectNode;
      if(storages.containsValue(project)) {
        projectNode = nodes.get(project);
        projectNode.setUserObject(project.getName() + (project.hasUnsavedChanges() ? "*" : ""));
      } else {
        projectNode = tree.addNode(project.getName()+ (project.hasUnsavedChanges() ? "*" : ""), EnumIcon.ProjectSmall);
        putStorage(project, projectNode);
        tree.setSelection(projectNode);
      }
      
      for(ModelStorage model : project.getStorages()) {
        IconTreeNode modelNode;
        String modelName = modelName(model);
        if(storages.containsValue(model)) {
          modelNode = nodes.get(model);
          modelNode.setUserObject(modelName);
        } else {
          modelNode = tree.addNode(projectNode, modelName, EnumIcon.fromModelType(model.getModelType(), true));
          tree.setSelection(modelNode);
          putStorage(model, modelNode);
        }
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
      ui.setActiveModelViewToActiveModel();
    }
    if(storage instanceof ModelStorage) {
      ModelStorage model = (ModelStorage) storage;
      ProjectStorage project = ((ProjectStorage) model.getContainer());
      project.setActiveStorage(model);  
      projectsStorages.setActiveStorage(project);
      ui.setActiveModelViewToActiveModel();
    }
    tree.repaint();
  }

  @Override
  public void storageChanged(Storage changedStorage) {
    updateTree();
    ProjectStorage activeStorage = projectsStorages.getActiveStorage();
    if(activeStorage != null) {
      ui.propertyTable.displayProperties(activeStorage);
    }
  }
  
  protected Storage getStorage(IconTreeNode node) {
    return storages.get(node);
  }
  
  protected IconTreeNode getNode(Storage storage) {
    return nodes.get(storage);
  }
  
  protected boolean isActive(ProjectStorage project) {
    ProjectStorage activeStorage = projectsStorages.getActiveStorage();
    if(activeStorage == null) return false;
    return activeStorage.equals(project);
  }
  
  protected boolean isActive(ModelStorage model) {
    ProjectStorage activeProject = projectsStorages.getActiveStorage();
    if(activeProject == null) return false;
    ModelStorage activeModel = activeProject.getActiveStorage();
    if(activeModel == null) return false;
    return activeModel.equals(model);
  }

  @Override
  public void storageSelectionChanged(StorageContainer container) {}

  @Override
  public void storageAdded(Storage addedStorage, StorageContainer container) {
    updateTree();
  }

  @Override
  public void storageRemoved(Storage removedStorage, StorageContainer container) {
    System.out.println(removedStorage);
    IconTreeNode node = nodes.get(removedStorage);
    ((IconTreeNode) node.getParent()).remove(node);
    removeStorage(removedStorage);
    
    updateTree();
  }
}
