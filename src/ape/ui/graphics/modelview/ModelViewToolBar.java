/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview;

import ape.petri.generic.Model;
import ape.petri.generic.ModelElement;
import ape.petri.generic.net.*;
import ape.ui.UI;
import ape.ui.graphics.modelview.generic.ModelView;
import ape.ui.graphics.modelview.generic.Visual;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToolBar;

/**
 *
 * @author Gabriel
 */
public class ModelViewToolBar extends JToolBar implements ActionListener, ModelViewListener {
  
  private UI ui;
  
  private Map<EnumModelViewAction, ModelViewToolBarToggleButton> toggleButtons;
  
  public ModelViewToolBar(UI ui, int orientation) {
    super();
    this.ui = ui;
    setOrientation(orientation);
    toggleButtons = new HashMap<>();
    init();
  }
  
  private void init() {
    ui.addModelViewListener(this);
  }

  private void refresh() {
    reset();
    
    switch(ui.getActiveModelType()) {
      case Net:
        initNet();
        break;
      default:
        throw new UnsupportedOperationException("Toolbar for model type " + ui.getActiveModelType() + " not yet implemented.");
    }
  }

  private void initNet() {
    setName("Net Toolbar");
    
    for(EnumModelViewAction action : EnumModelViewAction.values()) {
      addToggleButton(action);
    }
    
    add(new Separator());
    addButton(new AbstractAction("Delete", null) {

      @Override
      public void actionPerformed(ActionEvent e) {
        ModelView modelView = ui.getActiveModelView();
        Net net = (Net) modelView.getModel();
        Set<ModelElement> selection = modelView.getSelection();
        for(ModelElement element : selection) {
          if(element instanceof Place) {
            removeDanglingArcVisuals(modelView, (Node) element);
            net.removePlace((Place) element);
          } else if(element instanceof Transition) {
            removeDanglingArcVisuals(modelView, (Node) element);
            net.removeTransition((Transition) element);
          } else if(element instanceof ArcCollection) {
            net.removeArcCollection((ArcCollection) element);
          }
          modelView.removeVisualFor(element);
        }
        modelView.repaint();
      }
    });

    selectAction(EnumModelViewAction.Selection);
  }
  
  private void removeDanglingArcVisuals(ModelView modelView, Node n) {
    for(ArcCollection c: n.getIncomingArcs()) {
      modelView.removeVisualFor(c);
    }
    for(ArcCollection c: n.getOutgoingArcs()) {
      modelView.removeVisualFor(c);
    }
  }

  private void addToggleButton(EnumModelViewAction action) {
    ModelViewToolBarToggleButton button = new ModelViewToolBarToggleButton(action);
    button.addActionListener(this);
    toggleButtons.put(action, button);
    add(button);
  }
  
  private void addButton(Action action) {
    JButton button = new JButton(action);
    add(button);
  }
  
  public void reset() {
    removeAll();
  }
  
  
  public void selectAction(EnumModelViewAction selectedAction) {
    for(ModelViewToolBarToggleButton button : toggleButtons.values()) {
      button.setSelected(false);
    }
    toggleButtons.get(selectedAction).setSelected(true);
    
    /* tell the model view canvas that the selected action has changed */
    ui.getModelViewCanvas().setSelectedAction(selectedAction);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    ModelViewToolBarToggleButton button = (ModelViewToolBarToggleButton) e.getSource();
    selectAction(button.getModelViewAction());
  }

  @Override
  public void activeModelViewChanged(ModelView activeView) {
    refresh();
  }

  @Override
  public void visualElementAddedToActiveModelView(ModelElement e, Visual v) {}

  @Override
  public void visualElementRemovedFromActiveModelView(ModelElement e, Visual v) {}

  @Override
  public void visualElementHasChangedData(ModelElement e, Visual v) {}
}
