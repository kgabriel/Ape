/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.modelview;

import ape.ui.UI;
import ape.ui.control.CommandReceiver;
import ape.ui.control.EnumCommandReceiverType;
import ape.ui.modelview.generic.ModelView;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.*;
import javax.swing.JComponent;

/**
 * A canvas that is used to display {@link ModelView}s. 
 * @author Gabriel
 */
public class ModelViewCanvas extends JComponent implements CommandReceiver, ComponentListener, 
        MouseMotionListener, MouseListener {
  
  /**
   * The action that is currently selected.
   */
  private EnumModelViewAction selectedAction;

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
    addMouseMotionListener(this);
    addMouseListener(this);
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
//    super.paintComponent(g);
    paint(g);
  }
  
  

  @Override
  public void componentResized(ComponentEvent e) {
    if(modelView != null) {
      modelView.setViewSizeToCanvas();
    }
    repaint();
  }
  
  public EnumModelViewAction getSelectedAction() {
    return selectedAction;
  }
  
  public void setSelectedAction(EnumModelViewAction action) {
    selectedAction = action;
    if(modelView != null) {
      modelView.selectedActionChanged(action);
    }
  }

  @Override
  public void componentShown(ComponentEvent e) {}
  
  @Override
  public void componentHidden(ComponentEvent e) {}

  @Override
  public void componentMoved(ComponentEvent e) {}

  @Override
  public void mouseDragged(MouseEvent e) {
    if(modelView == null || selectedAction == null) return;
    Point lastMouseLocation = modelView.getMouseLocation();
    Point mouseLocation = modelView.toViewCoordinate(e.getPoint());
    modelView.setMouseLocation(mouseLocation);
    int dx = mouseLocation.x - lastMouseLocation.x;
    int dy = mouseLocation.y - lastMouseLocation.y;
    modelView.mouseMoved(selectedAction, dx, dy);
    repaint();
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    if(modelView == null || selectedAction == null) return;
    Point lastMouseLocation = modelView.toDeviceCoordinate(modelView.getMouseLocation());
    Point mouseLocation = e.getPoint();
    modelView.setMouseLocation(modelView.toViewCoordinate(mouseLocation));
    int dx = lastMouseLocation.x - mouseLocation.x;
    int dy = lastMouseLocation.y - mouseLocation.y;
    modelView.mouseMoved(selectedAction, dx, dy);
    repaint();
  }
  
  private static boolean leftButton(MouseEvent e) {
    return e.getButton() == MouseEvent.BUTTON1;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if(modelView == null || selectedAction == null) return;
    if(! leftButton(e)) return;
    
    Point location = modelView.toViewCoordinate(e.getPoint());
    modelView.mouseClick(selectedAction, e.getModifiersEx());
    repaint();
  }

  @Override
  public void mousePressed(MouseEvent e) {
    if(modelView == null || selectedAction == null) return;
    if(! leftButton(e)) return;
    
    modelView.setMouseDown(true);
    modelView.mousePress(selectedAction, e.getModifiersEx());
    repaint();
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    if(modelView == null || selectedAction == null) return;
    if(! leftButton(e)) return;

    modelView.setMouseDown(false);
    modelView.mouseRelease(selectedAction, e.getModifiersEx());
    repaint();
  }

  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}

}
