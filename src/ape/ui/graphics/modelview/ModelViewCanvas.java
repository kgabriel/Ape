/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview;

import ape.ui.UI;
import ape.ui.control.CommandReceiver;
import ape.ui.control.EnumCommandReceiverType;
import ape.ui.graphics.modelview.generic.Visual;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JComponent;

/**
 * A canvas that is used to display {@link ModelView}s. 
 * @author Gabriel
 */
public class ModelViewCanvas extends JComponent implements CommandReceiver, ComponentListener {
  
  /**
   * The <code>ModelView</code> that is currently displayed.
   */
  private ModelView modelView;
  
  /**
   * The user interface of the application.
   */
  protected UI ui;
  
  /**
   * Constructor for a new <code>ModelViewCanvas</code> inside the specified <code>UI</code>.
   * @param ui the user interface of the application
   */
  public ModelViewCanvas(UI ui) {
    this.ui = ui;
    init();
  }

  private void init() {
    addComponentListener(this);
  }
  
  /**
   * Returns the currently displayed <code>ModelView</code>.
   * @return the model view that is currently displayed
   */
  public ModelView getModelView() {
    return modelView;
  }

  /**
   * Sets the currently displayed <code>ModelView</code>. If there was another model view
   * that was displayed before, that <code>ModelView</code> is detached from this 
   * <code>ModelViewCanvas</code>.
   * @param modelView the model view that is to be displayed by this canvas
   */
  public void setModelView(ModelView modelView) {
    if(this.modelView != null) {
      this.modelView.setCanvas(null);
    }
    this.modelView = modelView;
    modelView.setCanvas(this);
    
    /* notify all listeners in the UI */
    ui.activeModelViewChanged(modelView);
  }
  
  /**
   * Tells commands the component of this {@link CommandReceiver}.
   * @return this <code>ModelViewCanvas</code>
   */
  @Override
  public Component getComponent() {
    return this;
  }

  /**
   * Tells commands of which type this {@link CommandReceiver} is.
   * @return {@link EnumCommandReceiverType#ModelView}
   */
  @Override
  public EnumCommandReceiverType getCommandReceiverType() {
    return EnumCommandReceiverType.ModelView;
  }

  @Override
  public void update(Graphics g) {
    paint(g);
  }
  
  @Override
  public void paint(Graphics g) {
    if(modelView != null) {
      modelView.draw(g);
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    paint(g);
  }
  
  public void selectedActionChanged(EnumModelViewAction action) {
    if(modelView != null) {
      modelView.selectedActionChanged(action);
    }
  }
  
  

  @Override
  public void componentResized(ComponentEvent e) {
    if(modelView != null) {
      modelView.setViewSizeToCanvas();
    }
    repaint();
  }
  
  @Override
  public void componentShown(ComponentEvent e) {}
  
  @Override
  public void componentHidden(ComponentEvent e) {}

  @Override
  public void componentMoved(ComponentEvent e) {}

}
