/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics;

import ape.Ape;
import ape.org.ModelStorage;
import ape.org.Storage;
import ape.org.StorageContainer;
import ape.org.StorageContainerListener;
import ape.petri.generic.EnumModelType;
import ape.petri.generic.EnumNetType;
import ape.petri.generic.Model;
import ape.ui.control.actions.ahlinstantiation.ClearInstantiationAction;
import ape.ui.control.actions.ahlinstantiation.CreatePrologNetAction;
import ape.ui.control.actions.ahlinstantiation.GetSolutionAction;
import ape.ui.control.actions.ahlinstantiation.ValidateInstantiationAction;
import ape.ui.control.actions.global.*;
import ape.ui.graphics.components.MenuItem;
import ape.util.EnumIcon;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

/**
 *
 * @author Gabriel
 */
public class MainMenu extends JMenuBar implements StorageContainerListener {
  
  
  private Ape theApe;
  private Map<String,JMenuItem> menuItems;
  
  public MainMenu(Ape theApe) {
    this.theApe = theApe;
    this.menuItems = new HashMap<>();
    init();
  }
  
  private void init() {
    theApe.projects.addStorageContainerListener(this);
    initMenu();
  }  
  
  private void initMenu() {
    JMenu mFile = new JMenu("File");
    mFile.setMnemonic(KeyEvent.VK_F);
    add(mFile);
    menuItems.put("File", mFile);

    addCreateNewMenuTo(mFile);
    addLoadMenuTo(mFile);
    addSaveMenuTo(mFile);
    addCloseMenuTo(mFile);
    
    JMenu mInst = new JMenu("Instantiation");
    mInst.setMnemonic(KeyEvent.VK_I);
    add(mInst);
    menuItems.put("Instantiation", mInst);
    
    addInstMenuTo(mInst);
    
    updateEnabledStatus();
  }
  
  public void addCreateNewMenuTo(JMenu menu) {
    
    JMenu mNew = new JMenu("Create new...");
    mNew.setIcon(UIManager.getIcon("FileView.fileIcon"));
    mNew.setMnemonic(KeyEvent.VK_N);
    menu.add(mNew);
    menuItems.put("File>New", mNew);
    
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
  
  public void addLoadMenuTo(JMenu menu) {
    
    JMenu mLoad = new JMenu("Load...");
    System.out.println(UIManager.getLookAndFeel().getDescription());
    mLoad.setIcon(UIManager.getIcon("FileView.directoryIcon"));
    mLoad.setMnemonic(KeyEvent.VK_O);
    menu.add(mLoad);
    menuItems.put("File>Load", mLoad);
    
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
    mSave.setIcon(UIManager.getIcon("FileView.floppyDriveIcon"));
    mSave.setMnemonic(KeyEvent.VK_S);
    menu.add(mSave);
    menuItems.put("File>Save", mSave);
    
    JMenuItem mSaveProject = 
            new MenuItem("Project", KeyEvent.VK_P, EnumIcon.ProjectSmall, new SaveProjectAction(theApe, false));
    mSave.add(mSaveProject);
    menuItems.put("File>Save>Project", mSaveProject);
    
    JMenuItem mSaveProjectAs = 
            new MenuItem("Project As", KeyEvent.VK_A, EnumIcon.ProjectSmall, new SaveProjectAction(theApe, true));
    mSave.add(mSaveProjectAs);
    menuItems.put("File>Save>Project As", mSaveProject);

    JMenuItem mSaveModel = new MenuItem("Model", EnumIcon.NetSmall, new SaveModelAction(theApe));
    mSave.add(mSaveModel);
    menuItems.put("File>Save>Model", mSaveModel);
  }

  private void addCloseMenuTo(JMenu menu) {
    JMenu mClose = new JMenu("Close...");
    mClose.setIcon(EnumIcon.DeleteSmall.getIcon());
    mClose.setMnemonic(KeyEvent.VK_C);
    menu.add(mClose);
    menuItems.put("File>Close", mClose);
    
    JMenuItem mCloseProject = new MenuItem("Project", KeyEvent.VK_P, EnumIcon.ProjectSmall, new CloseActiveStorageAction(theApe, true));
    mClose.add(mCloseProject);
    menuItems.put("File>Close>Project", mCloseProject);

    JMenuItem mCloseModel = new MenuItem("Model", KeyEvent.VK_P, EnumIcon.NetSmall, new CloseActiveStorageAction(theApe, false));
    mClose.add(mCloseModel);
    menuItems.put("File>Close>Model", mCloseModel);
  }
              
  public void addInstMenuTo(JMenu menu) {
    JMenuItem mClear = new MenuItem("Clear", EnumIcon.BlankSmall, new ClearInstantiationAction(theApe));
    
    menu.add(mClear);
    menuItems.put("Instantiation>Clear", mClear);
    
    JMenuItem mValidate = new MenuItem("Validate", EnumIcon.BlankSmall, new ValidateInstantiationAction(theApe));
    
    menu.add(mValidate);
    menuItems.put("Instantiation>Validate", mValidate);
    
    JMenuItem mCreatePrologNet = 
            new MenuItem("Create Prolog Net and Query", EnumIcon.BlankSmall, new CreatePrologNetAction(theApe));
    
    menu.add(mCreatePrologNet);
    menuItems.put("Instantiation>Create Prolog Net", mCreatePrologNet);
    
    JMenuItem mGetSolution = new MenuItem("Get Solution", EnumIcon.BlankSmall, new GetSolutionAction(theApe));
    
    menu.add(mGetSolution);
    menuItems.put("Instantiation>Get Solution", mGetSolution);
  }
  
  public void updateEnabledStatus() {
    ModelStorage activeModelStorage = theApe.getActiveModel();
    if(activeModelStorage == null) {
      menuItems.get("File>Save>Model").setEnabled(false);
      menuItems.get("Instantiation").setEnabled(false);
      menuItems.get("File>Close>Model").setEnabled(false);
      return;
    } else {
      menuItems.get("File>Save>Model").setEnabled(true);
      menuItems.get("File>Close>Model").setEnabled(true);
    }
    
    Model activeModel = activeModelStorage.getModel();
    if(activeModel.getModelType() == EnumModelType.Net && activeModel.getNetType() == EnumNetType.AHLInstantiation) {
      menuItems.get("Instantiation").setEnabled(true);
    } else {
      menuItems.get("Instantiation").setEnabled(false);
    }
  }

  @Override
  public void storageSelectionChanged(StorageContainer container) {
    updateEnabledStatus();
  }

  @Override
  public void storageAdded(Storage addedStorage, StorageContainer container) {
    updateEnabledStatus();
  }

  @Override
  public void storageRemoved(Storage removedStorage, StorageContainer container) {
    updateEnabledStatus();
  }
}
