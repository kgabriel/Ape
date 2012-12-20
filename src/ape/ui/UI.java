/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui;

import ape.Ape;
import ape.petri.generic.EnumModelType;
import ape.petri.generic.ModelElement;
import ape.ui.modelview.ModelTreeView;
import ape.ui.modelview.ModelViewCanvas;
import ape.ui.modelview.ModelViewToolBar;
import ape.ui.control.*;
import ape.ui.control.commands.Command;
import ape.ui.control.commands.CommandModelViewMouseScroll;
import ape.ui.control.commands.CommandModelViewReset;
import ape.ui.control.commands.CommandModelViewScale;
import ape.ui.modelview.*;
import ape.ui.modelview.generic.ModelView;
import ape.ui.modelview.generic.Visual;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashSet;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 *
 * @author Gabriel
 */
public class UI implements ModelViewListener {

  protected Ape theApe;
  protected CommandManager commandManager;
  protected MainFrame mainFrame;
  protected ModelViewCanvas modelViewCanvas;
  protected ModelViewToolBar modelViewToolbar;
  protected ModelTreeView modelTreeView;
  protected VisualTable visualTable;
  private Collection<ModelViewListener> modelViewListeners;
  
  public UI(Ape ape) {
    this.theApe = ape;
    this.modelViewListeners = new HashSet<>();
    initLookAndFeel();
    initUI();
  }
  
  private void initLookAndFeel() {
    try {
      // Set System Look&Feel
      javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }

  }
  
  private void initUI() {
    initComponents();
    initControl();
  }

  private void initComponents() {
    mainFrame = new MainFrame();
    
    modelViewCanvas = new ModelViewCanvas(this);
    mainFrame.addTo(modelViewCanvas, MainFrame.CENTER_PANEL, false);
    
    modelViewToolbar = new ModelViewToolBar(this, ModelViewToolBar.HORIZONTAL);
    setHeightStrict(modelViewToolbar, 30);
    mainFrame.addTo(modelViewToolbar, MainFrame.TOP_PANEL, true);
        
    modelTreeView = new ModelTreeView(this);
    mainFrame.addTo(new JScrollPane(modelTreeView), MainFrame.TOP_RIGHT_PANEL, false);
    
    visualTable = new VisualTable(this);
    mainFrame.addTo(new JScrollPane(visualTable), MainFrame.BOTTOM_RIGHT_PANEL, false);
    
    mainFrame.setVisible(true);
    mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
  }
  
  public static void setWidthStrict(Component c, int width) {
    setSizeStrict(c, new Dimension(width, Short.MAX_VALUE), new Dimension(width, 0));
  }
  
  public static void setHeightStrict(Component c, int height) {
    setSizeStrict(c, new Dimension(height, Short.MIN_VALUE), new Dimension(0,height));
  }
  
  public static void setSizeStrict(Component c, Dimension max, Dimension min) {
    c.setMaximumSize(max);
    c.setMinimumSize(min);
  }

  private void initControl() {
    commandManager = new CommandManager(theApe);
    
    commandManager.addReceiver(modelViewCanvas);

    Command reset = new CommandModelViewReset();
    Command scale = new CommandModelViewScale();
    Command scroll = new CommandModelViewMouseScroll();
    
    commandManager.addCommand(reset);
    commandManager.addCommand(scale);

    commandManager.activateCommand(reset, new CommandBinding(EnumInvocationType.MousePress, EnumCommandReceiverType.ModelView, MouseEvent.BUTTON2), new CommandBindingModifier(MouseEvent.CTRL_DOWN_MASK));
    commandManager.activateCommand(scroll, new CommandBinding(EnumInvocationType.MouseDrag, EnumCommandReceiverType.ModelView, MouseEvent.BUTTON1), new CommandBindingModifier(MouseEvent.ALT_DOWN_MASK));
    commandManager.activateCommand(scale, new CommandBinding(EnumInvocationType.MouseWheel, EnumCommandReceiverType.ModelView, MouseEvent.NOBUTTON), new CommandBindingModifier());
    commandManager.activateCommand(reset, new CommandBinding(EnumInvocationType.KeyPress, EnumCommandReceiverType.ModelView, KeyEvent.VK_C), new CommandBindingModifier(InputEvent.ALT_DOWN_MASK));
  }

  public MainFrame getMainFrame() {
    return mainFrame;
  }

  public ModelViewCanvas getModelViewCanvas() {
    return modelViewCanvas;
  }
  
  public void setActiveModelView(ModelView view) {
    modelViewCanvas.setModelView(view);
  }
  
  public ModelView getActiveModelView() {
    return modelViewCanvas.getModelView();
  }
  
  public EnumModelType getActiveModelType() {
    return getActiveModelView().getModelType();
  }

  public ModelViewToolBar getModelViewToolbar() {
    return modelViewToolbar;
  }
  
  public VisualTable getVisualTable() {
    return visualTable;
  }
  
  public void addModelViewListener(ModelViewListener listener) {
    modelViewListeners.add(listener);
  }
  
  public boolean removeModelViewListener(ModelViewListener listener) {
    return modelViewListeners.remove(listener);
  }

  @Override
  public void activeModelViewChanged(ModelView activeView) {
    for(ModelViewListener listener : modelViewListeners) {
      listener.activeModelViewChanged(activeView);
    }
  }

  @Override
  public void visualElementAddedToActiveModelView(ModelElement e, Visual v) {
    for(ModelViewListener listener : modelViewListeners) {
      listener.visualElementAddedToActiveModelView(e, v);
    }
  }

  @Override
  public void visualElementRemovedFromActiveModelView(ModelElement e, Visual v) {
    for(ModelViewListener listener : modelViewListeners) {
      listener.visualElementRemovedFromActiveModelView(e, v);
    }
  }
}
