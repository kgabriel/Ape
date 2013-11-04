/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview;

import ape.petri.generic.EnumModelType;
import ape.petri.generic.ModelElement;
import ape.ui.UI;
import ape.ui.control.actions.modelview.DeleteNetElementsAction;
import ape.ui.graphics.modelview.generic.Visual;
import ape.util.EnumIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
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
  
  private EnumModelViewAction selectedAction;
  
  public ModelViewToolBar(UI ui, int orientation) {
    super();
    this.ui = ui;
    setOrientation(orientation);
    toggleButtons = new HashMap<>();
    init();
  }
  
  public EnumModelViewAction getSelectedAction() {
    return selectedAction;
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
      if(action.getModelType() == EnumModelType.Net) {
        addToggleButton(action);
      }
    }
    
    add(new Separator());
    addButton(new DeleteNetElementsAction(ui.theApe).getSwingAction(EnumIcon.Delete.getIcon()));

    selectAction(EnumModelViewAction.NetSelection);
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
    this.selectedAction = selectedAction;
    ui.modelViewCanvas.selectedActionChanged(selectedAction);
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
