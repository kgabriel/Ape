/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui;

import ape.Ape;
import ape.org.ModelStorage;
import ape.petri.generic.EnumModelType;
import ape.petri.generic.ModelElement;
import ape.ui.control.*;
import ape.ui.control.commands.*;
import ape.ui.graphics.MainFrame;
import ape.ui.graphics.MainMenu;
import ape.ui.graphics.ProjectTree;
import ape.ui.graphics.PropertyTable;
import ape.ui.graphics.modelview.ModelTreeView;
import ape.ui.graphics.modelview.ModelViewCanvas;
import ape.ui.graphics.modelview.ModelViewListener;
import ape.ui.graphics.modelview.ModelViewToolBar;
import ape.ui.graphics.modelview.ModelView;
import ape.ui.graphics.modelview.generic.Visual;
import ape.util.aml.ApeML;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashSet;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

/**
 *
 * @author Gabriel
 */
public class UI implements ModelViewListener {

  public Ape theApe;
  public CommandManager commandManager;
  public MainFrame mainFrame;
  public MainMenu mainMenu;
  public ProjectTree projectTree;
  public ModelViewCanvas modelViewCanvas;
  public ModelViewToolBar modelViewToolbar;
  public ModelTreeView modelTreeView;
  public PropertyTable propertyTable;
  
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
    mainFrame.setTitle("APE Algebraic High-Level Net and Process Editor");
    
    mainMenu = new MainMenu(theApe);
    mainFrame.setJMenuBar(mainMenu);
    
    projectTree = new ProjectTree(theApe.projects, this);
    mainFrame.addTo(projectTree, MainFrame.LEFT_PANEL, false);
    
    modelViewCanvas = new ModelViewCanvas(this);
    mainFrame.addTo(modelViewCanvas, MainFrame.CENTER_PANEL, false);
    
    modelViewToolbar = new ModelViewToolBar(this, ModelViewToolBar.HORIZONTAL);
    setHeightStrict(modelViewToolbar, 30);
    mainFrame.addTo(modelViewToolbar, MainFrame.TOP_PANEL, true);
        
    modelTreeView = new ModelTreeView(this);
    mainFrame.addTo(modelTreeView, MainFrame.TOP_RIGHT_PANEL, false);
    
    propertyTable = new PropertyTable(this);
    mainFrame.addTo(new JScrollPane(propertyTable), MainFrame.BOTTOM_RIGHT_PANEL, false);
    
    mainFrame.setVisible(true);
    mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    
    mainFrame.pack();
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
    
    commandManager.addReceiver(mainFrame);
    commandManager.addReceiver(modelViewCanvas);

    commandManager.readAMLNode(ApeML.readResource("config/commands.cfg").get(0));
    
    /* 
    // activate commands the "old fashioned" way
    Command modelViewClick = new ModelViewMouseClickCommand();
    Command modelViewPress = new ModelViewMousePressCommand();
    Command modelViewRelease = new ModelViewMouseReleaseCommand();
    Command modelViewMove = new ModelViewMouseMoveCommand(false);
    Command modelViewDrag = new ModelViewMouseMoveCommand(true);
    Command reset = new ModelViewResetCommand();
    Command scale = new ModelViewScaleCommand();
    Command scroll = new ModelViewMouseScrollCommand();
    Command rename = new SetPropertyCommand("Name");
    Command setType = new SetPropertyCommand("Type");
    Command setConditions = new SetPropertyCommand("Conditions");
    Command setInscription = new SetPropertyCommand("Inscriptions");

    commandManager.activateCommand(modelViewClick, new CommandBinding(EnumInvocationType.MouseClick, EnumCommandReceiverType.ModelView, MouseEvent.BUTTON1), new CommandBindingModifier(CommandBindingModifier.NO_MASK, CommandBindingModifier.NO_MASK));
    commandManager.activateCommand(modelViewPress, new CommandBinding(EnumInvocationType.MousePress, EnumCommandReceiverType.ModelView, MouseEvent.BUTTON1), new CommandBindingModifier(CommandBindingModifier.NO_MASK, CommandBindingModifier.NO_MASK));
    commandManager.activateCommand(modelViewRelease, new CommandBinding(EnumInvocationType.MouseRelease, EnumCommandReceiverType.ModelView, MouseEvent.BUTTON1), new CommandBindingModifier(CommandBindingModifier.NO_MASK, CommandBindingModifier.NO_MASK));
    commandManager.activateCommand(modelViewMove, new CommandBinding(EnumInvocationType.MouseMove, EnumCommandReceiverType.ModelView, MouseEvent.NOBUTTON), new CommandBindingModifier(CommandBindingModifier.NO_MASK, CommandBindingModifier.NO_MASK));
    commandManager.activateCommand(modelViewDrag, new CommandBinding(EnumInvocationType.MouseDrag, EnumCommandReceiverType.ModelView, MouseEvent.BUTTON1), new CommandBindingModifier(CommandBindingModifier.NO_MASK, CommandBindingModifier.NO_MASK));
    
    commandManager.activateCommand(reset, new CommandBinding(EnumInvocationType.MousePress, EnumCommandReceiverType.ModelView, MouseEvent.BUTTON2), new CommandBindingModifier());
    commandManager.activateCommand(scroll, new CommandBinding(EnumInvocationType.MouseDrag, EnumCommandReceiverType.ModelView, MouseEvent.BUTTON1), new CommandBindingModifier(MouseEvent.ALT_DOWN_MASK));
    commandManager.activateCommand(scroll, new CommandBinding(EnumInvocationType.MouseDrag, EnumCommandReceiverType.ModelView, MouseEvent.BUTTON2), new CommandBindingModifier());
    commandManager.activateCommand(scale, new CommandBinding(EnumInvocationType.MouseWheel, EnumCommandReceiverType.ModelView, MouseEvent.NOBUTTON), new CommandBindingModifier());
    commandManager.activateCommand(reset, new CommandBinding(EnumInvocationType.KeyPress, EnumCommandReceiverType.Global, KeyEvent.VK_C), new CommandBindingModifier(InputEvent.ALT_DOWN_MASK));
    commandManager.activateCommand(rename, new CommandBinding(EnumInvocationType.KeyPress, EnumCommandReceiverType.Global, KeyEvent.VK_R), new CommandBindingModifier(InputEvent.CTRL_DOWN_MASK));
    commandManager.activateCommand(setType, new CommandBinding(EnumInvocationType.KeyPress, EnumCommandReceiverType.Global, KeyEvent.VK_T), new CommandBindingModifier(InputEvent.CTRL_DOWN_MASK));
    commandManager.activateCommand(setConditions, new CommandBinding(EnumInvocationType.KeyPress, EnumCommandReceiverType.Global, KeyEvent.VK_E), new CommandBindingModifier(InputEvent.CTRL_DOWN_MASK));
    commandManager.activateCommand(setInscription, new CommandBinding(EnumInvocationType.KeyPress, EnumCommandReceiverType.Global, KeyEvent.VK_R), new CommandBindingModifier(InputEvent.CTRL_DOWN_MASK));
    */
    
//    commandManager.activateCommand(commandManager.commandProvider.getCommand("Save Project"), new CommandBinding(EnumInvocationType.KeyPress, EnumCommandReceiverType.Global, KeyEvent.VK_S), new CommandBindingModifier(InputEvent.CTRL_DOWN_MASK));
//    commandManager.activateCommand(commandManager.commandProvider.getCommand("Save Project As"), new CommandBinding(EnumInvocationType.KeyPress, EnumCommandReceiverType.Global, KeyEvent.VK_S), new CommandBindingModifier(InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));

   // store command bindings in file
    ApeML.writeResource(commandManager.getAMLNode(), "config", "commands.cfg");
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
  
  public void setActiveModelViewToActiveModel() {
    ModelStorage activeModel = theApe.getActiveModel();
    if(activeModel == null) return;
    setActiveModelView(activeModel.getView());
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
  
  public PropertyTable getPropertyTable() {
    return propertyTable;
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

  @Override
  public void visualElementHasChangedData(ModelElement e, Visual v) {
    for(ModelViewListener listener : modelViewListeners) {
      listener.visualElementHasChangedData(e, v);
    }
  }
}
