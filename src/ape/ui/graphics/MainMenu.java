/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics;

import ape.Ape;
import ape.petri.generic.EnumModelType;
import ape.petri.generic.EnumNetType;
import ape.ui.control.actions.ahlinstantiation.ClearInstantiationAction;
import ape.ui.control.actions.ahlinstantiation.CreatePrologNetAction;
import ape.ui.control.actions.ahlinstantiation.GetSolutionAction;
import ape.ui.control.actions.ahlinstantiation.ValidateInstantiationAction;
import ape.ui.control.actions.global.*;
import ape.ui.graphics.components.MenuItem;
import ape.util.EnumIcon;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *
 * @author Gabriel
 */
public class MainMenu extends JMenuBar {
  
  public final String NEW_PROJECT = "New Project";
  
  private Ape theApe;
  
  public MainMenu(Ape theApe) {
    this.theApe = theApe;
    init();
  }
  
  private void init() {
    JMenu mFile = new JMenu("File");
    mFile.setMnemonic(KeyEvent.VK_F);
    add(mFile);

    addCreateNewMenuTo(mFile);
    addOpenMenuTo(mFile);
    addSaveMenuTo(mFile);
    
    JMenu mInst = new JMenu("Instantiation");
    mInst.setMnemonic(KeyEvent.VK_I);
    add(mInst);
    
    addInstMenuTo(mInst);
  }
  
  public void addCreateNewMenuTo(JMenu menu) {
    
    JMenu mNew = new JMenu("Create new...");
    mNew.setMnemonic(KeyEvent.VK_N);
    menu.add(mNew);
    
    JMenuItem mNewProject = new MenuItem("Project", KeyEvent.VK_P, EnumIcon.ProjectSmall, new NewProjectAction(theApe));
    mNew.add(mNewProject);
    
    for(final EnumModelType modelType : EnumModelType.values()) {
      mNew.addSeparator();

      for(final EnumNetType netType : EnumNetType.values()) {
        String menuItemName = netType.getName() + " " + modelType.getName();
        ImageIcon icon = 
                EnumIcon.mergeIcons(EnumIcon.fromNetType(netType, true), EnumIcon.fromModelType(modelType, true));
        JMenuItem mNewModel = 
                new MenuItem(menuItemName, EnumIcon.fromModelType(modelType, true), new NewModelAction(theApe, modelType, netType));
        mNew.add(mNewModel);
      }
    }
  }
  
  public void addOpenMenuTo(JMenu menu) {
    
    JMenu mLoad = new JMenu("Load...");
    mLoad.setMnemonic(KeyEvent.VK_O);
    menu.add(mLoad);
    
    JMenuItem mLoadProject = 
            new MenuItem("Project", KeyEvent.VK_P, EnumIcon.ProjectSmall, new LoadProjectAction(theApe));
    mLoad.add(mLoadProject);
    
    mLoad.addSeparator();
    
    for(final EnumModelType modelType : EnumModelType.values()) {
      JMenuItem mLoadModel = 
              new MenuItem(modelType.getName(), EnumIcon.fromModelType(modelType, true), new LoadModelAction(theApe, modelType));
      mLoad.add(mLoadModel);
    }
  }
  
  public void addSaveMenuTo(JMenu menu) {
    
    JMenu mSave = new JMenu("Save...");
    mSave.setMnemonic(KeyEvent.VK_S);
    menu.add(mSave);
    
    JMenuItem mSaveProject = 
            new MenuItem("Project", KeyEvent.VK_P, EnumIcon.ProjectSmall, new SaveProjectAction(theApe));
    mSave.add(mSaveProject);
    
    mSave.addSeparator();
    
    JMenuItem mSaveModel = new MenuItem("Model", EnumIcon.NetSmall, new SaveModelAction(theApe));
    mSave.add(mSaveModel);
  }

  public void addInstMenuTo(JMenu menu) {
    JMenuItem mClear = new MenuItem("Clear", EnumIcon.BlankSmall,new ClearInstantiationAction(theApe));
    
    menu.add(mClear);
    
    JMenuItem mValidate = new MenuItem("Validate", EnumIcon.BlankSmall, new ValidateInstantiationAction(theApe));
    
    menu.add(mValidate);
    
    JMenuItem mCreatePrologNet = 
            new MenuItem("Create Prolog Net and Query", EnumIcon.BlankSmall, new CreatePrologNetAction(theApe));
    
    menu.add(mCreatePrologNet);
    
    JMenuItem mGetSolution = new MenuItem("Get Solution", EnumIcon.BlankSmall, new GetSolutionAction(theApe));
    
    menu.add(mGetSolution);
  }
              
}
