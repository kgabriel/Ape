/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview;

import ape.petri.generic.ModelElement;
import ape.petri.generic.net.*;
import ape.ui.UI;
import ape.ui.graphics.components.IconTree;
import ape.ui.graphics.components.IconTreeNode;
import ape.ui.graphics.modelview.generic.Visual;
import ape.util.EnumIcon;
import java.awt.BorderLayout;
import java.util.Collection;
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
public class ModelTreeView extends JPanel implements ModelViewListener, TreeSelectionListener {
  
  UI ui;
  IconTree tree;
  Map<EnumNetElementType,IconTreeNode> typeRoots;
  Map<ModelElement,IconTreeNode> elementNodes;
  Map<IconTreeNode,ModelElement> nodeElements;
  Map<IconTreeNode,IconTreeNode> arcNodeParents;
  
  public ModelTreeView(UI ui) {
    this.ui = ui;
    this.tree = new IconTree();
    this.typeRoots = new HashMap<>();
    this.elementNodes = new HashMap<>();
    this.nodeElements = new HashMap<>();
    this.arcNodeParents = new HashMap<>();
    init();
  }
  
  private void init() {
    ui.addModelViewListener(this);
    clearModelTree();
    setLayout(new BorderLayout());
    JScrollPane scrollPane = new JScrollPane(tree);
    add(scrollPane, BorderLayout.CENTER);
    validate();
    
    tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    tree.addTreeSelectionListener(this);
  }
  
  public void clearModelTree() {
    typeRoots.clear();
    tree.clear();
  }
  
  public void updateModelTree() {
    clearModelTree();
    ModelView modelView = ui.getActiveModelView();
    if(modelView == null) return;
    switch(modelView.getModelType()) {
      case Net:
        Net net = (Net) modelView.getModel();
        updateNetTree(net);
        break;
      default:
        throw new UnsupportedOperationException("Model Tree not yet implemented for model type " + modelView.getModelType());
    }
    
    for(IconTreeNode typeRoot: typeRoots.values()) {
      typeRoot.setExpanded(true);
    }
    
    tree.refresh();
  }
  
  private void updateNetTree(Net net) {
    Collection<Place> places = net.getPlaces();
    Collection<Transition> transitions = net.getTransitions();
    IconTreeNode placeRoot = tree.addNode("Places", EnumIcon.PlacesSmall);
    typeRoots.put(EnumNetElementType.Place, placeRoot);
    for(Place p : places) {
      addNodeByElement(p);
    }
    placeRoot.setExpanded(true);
    IconTreeNode transitionRoot = tree.addNode("Transitions",EnumIcon.TransitionsSmall);
    typeRoots.put(EnumNetElementType.Transition, transitionRoot);
    for(Transition t : transitions) {
      IconTreeNode transitionNode = addNodeByElement(t);
      transitionNode.isExpanded();
      for(ArcCollection preArc : t.getIncomingArcs()) {
        addArcNode(preArc);
      }
      for(ArcCollection postArc : t.getOutgoingArcs()) {
        addArcNode(postArc);
      }
    }
    transitionRoot.setExpanded(true);
  }
  
  private String nodeCaptionByElement(ModelElement element) {
    EnumNetElementType type = element.getElementType();
    switch(type) {
      case Place:
        Place p = (Place) element;
        return p.getData().getName();
      case Transition:
        Transition t = (Transition) element;
        return t.getData().getName();
      default:
        throw new UnsupportedOperationException("Node caption not yet implemented for element type " + type + ".");
    }
  }
  
  private IconTreeNode addArcNode(ArcCollection arc) {
    Place p = arc.getPlace();
    String caption = nodeCaptionByElement(p);

    Transition t = arc.getTransition();
    EnumArcDirection dir = arc.getDirection();
    int index = (dir == EnumArcDirection.PT ? 0 : 1);
    IconTreeNode parent = (IconTreeNode) elementNodes.get(t).getChildAt(index);
    EnumIcon icon = EnumIcon.fromElementType(EnumNetElementType.Place, true);
    IconTreeNode node = tree.addNode(parent, caption, icon);
    elementNodes.put(arc, node);
    nodeElements.put(node,arc);
    arcNodeParents.put(node,parent);
    return parent;
  }
  
  /**
   * 
   * @param element
   * @return the parent node, this element was added to
   */
  private IconTreeNode addNodeByElement(ModelElement element) {
    EnumNetElementType type = element.getElementType();
    if(type == EnumNetElementType.ArcCollection) {
      return addArcNode((ArcCollection) element);
    }
    
    IconTreeNode parent = typeRoots.get(type);
    if(parent == null) return null;
    EnumIcon icon = EnumIcon.fromElementType(type,true);
    String caption = nodeCaptionByElement(element);
    IconTreeNode node = tree.addNode(parent, caption, icon);
    elementNodes.put(element, node);
    nodeElements.put(node,element);
    
    if(type == EnumNetElementType.Transition) {
      tree.addNode(node, "Pre Arcs", EnumIcon.fromArcDirection(EnumArcDirection.PT, true));
      tree.addNode(node, "Post Arcs", EnumIcon.fromArcDirection(EnumArcDirection.TP, true));
    }
    
    return parent;
  }
  
  /**
   * 
   * @param element
   * @return the parent node, this element was removed from
   */
  private IconTreeNode removeNodeByElement(ModelElement element) {
    EnumNetElementType type = element.getElementType();
    IconTreeNode node = elementNodes.remove(element);
    if(node == null) return null;
    nodeElements.remove(null);
    IconTreeNode parent = typeRoots.get(type);
    if(parent == null) {
      parent = arcNodeParents.get(node);
      if(parent == null) return null;
    }
    parent.remove(node);
    return parent;
  }

  @Override
  public void activeModelViewChanged(ModelView activeView) {
    updateModelTree();
  }

  @Override
  public void visualElementAddedToActiveModelView(ModelElement e, Visual v) {
    IconTreeNode node = addNodeByElement(e);
    tree.refresh(node);
  }

  @Override
  public void visualElementRemovedFromActiveModelView(ModelElement e, Visual v) {
    IconTreeNode parent = removeNodeByElement(e);
    tree.refresh(parent);
  }

  @Override
  public void visualElementHasChangedData(ModelElement e, Visual v) {
    IconTreeNode node = elementNodes.get(e);
    if(e instanceof Place) {
      node.setUserObject(((Place) e).getData().getName());
    } else if (e instanceof Transition) {
      node.setUserObject(((Transition) e).getData().getName());
    } else if (e instanceof ArcCollection) {
      node.setUserObject(((ArcCollection) e).getPlace().getData().getName());
    } else {
      throw new UnsupportedOperationException("Update of tree view not yet implemented for model element of type " + e.getElementType());
    }
    tree.refresh(node);
  }
  
  @Override
  public void valueChanged(TreeSelectionEvent e) {
    IconTreeNode node = (IconTreeNode) tree.getLastSelectedPathComponent();
    if(typeRoots.containsValue(node)) return;
    ModelElement m = nodeElements.get(node);
    if(m == null) return;
    ModelView modelView = ui.getActiveModelView();
    modelView.setSelectionAndViewCenterTo(m);
    modelView.repaint();
  }

}
