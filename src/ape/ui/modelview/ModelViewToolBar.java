/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.modelview;

import ape.petri.generic.ModelElement;
import ape.ui.UI;
import ape.ui.modelview.generic.ModelView;
import ape.ui.modelview.generic.Visual;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JToolBar;

/**
 *
 * @author Gabriel
 */
public class ModelViewToolBar extends JToolBar implements ActionListener, ModelViewListener {
  
  private UI ui;
  
  private Map<EnumModelViewAction, ModelViewToolBarButton> buttons;
  
  public ModelViewToolBar(UI ui, int orientation) {
    super();
    this.ui = ui;
    setOrientation(orientation);
    buttons = new HashMap<>();
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
      addButton(action);
    }

    selectAction(EnumModelViewAction.Selection);
  }

  private void addButton(EnumModelViewAction action) {
    ModelViewToolBarButton button = new ModelViewToolBarButton(action);
    button.addActionListener(this);
    buttons.put(action, button);
    add(button);
  }
  
  public void reset() {
    removeAll();
  }
  
  
  public void selectAction(EnumModelViewAction selectedAction) {
    for(ModelViewToolBarButton button : buttons.values()) {
      button.setSelected(false);
    }
    buttons.get(selectedAction).setSelected(true);
    
    /* tell the model view canvas that the selected action has changed */
    ui.getModelViewCanvas().setSelectedAction(selectedAction);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    ModelViewToolBarButton button = (ModelViewToolBarButton) e.getSource();
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
}
