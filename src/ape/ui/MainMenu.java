/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui;

import ape.Ape;
import ape.org.ModelStorage;
import ape.org.ProjectStorage;
import ape.petri.generic.EnumModelType;
import ape.petri.generic.net.EnumNetType;
import ape.petri.generic.net.Net;
import ape.ui.components.MenuItem;
import ape.ui.modelview.generic.ModelView;
import ape.util.IO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Gabriel
 */
public class MainMenu extends JMenuBar implements ActionListener {
  
  public final String NEW_PROJECT = "New Project";
  
  private Ape ape;
  
  public MainMenu(Ape ape) {
    this.ape = ape;
    init();
  }
  
  private void init() {
    JMenu mFile = new JMenu("File");
    mFile.setMnemonic(KeyEvent.VK_F);
    add(mFile);

    addCreateNewMenuTo(mFile);
    addOpenMenuTo(mFile);
    addSaveMenuTo(mFile);
  }
  
  public void addCreateNewMenuTo(JMenu menu) {
    
    JMenu mNew = new JMenu("Create new...");
    mNew.setMnemonic(KeyEvent.VK_N);
    menu.add(mNew);
    
    JMenuItem mNewProject = new MenuItem("Project", KeyEvent.VK_P){
      @Override
      public void action(ActionEvent e) {
        ProjectStorage newProject = new ProjectStorage();
        ape.projects.addStorage(newProject);
        ape.projects.setActiveStorage(newProject);
      }
    };
    mNew.add(mNewProject);
    
    for(final EnumModelType modelType : EnumModelType.values()) {
      mNew.addSeparator();

      JMenu mNewModels = new JMenu(modelType.getName());
      mNew.add(mNewModels);

      for(final EnumNetType netType : EnumNetType.values()) {
        JMenuItem mNewModel = new MenuItem(netType.getName()) {
          @Override
          public void action(ActionEvent e) {
            Net net = modelType.createNet(netType);
            ModelStorage netStorage = new ModelStorage(EnumModelType.Net, net, new ModelView(ape.ui, net));
            ape.projects.getActiveStorage().addStorage(netStorage);
          }
        };
        mNewModels.add(mNewModel);
      }
    }
  }
  
  public void addOpenMenuTo(JMenu menu) {
    
    JMenu mOpen = new JMenu("Open...");
    mOpen.setMnemonic(KeyEvent.VK_O);
    menu.add(mOpen);
    
    JMenuItem mOpenProject = new MenuItem("Project", KeyEvent.VK_P){
      @Override
      public void action(ActionEvent e) {
        ProjectStorage project = IO.loadProject(ape.ui);
        if(project == null) return;
        ape.projects.addStorage(project);
      }
    };
    mOpen.add(mOpenProject);
    
    mOpen.addSeparator();
    
    for(final EnumModelType modelType : EnumModelType.values()) {
      JMenuItem mOpenModel = new MenuItem(modelType.getName()) {
        @Override
        public void action(ActionEvent e) {
          ModelStorage model = IO.loadModel(ape.ui, modelType);
          if(model == null) return;
          ape.projects.getActiveStorage().addStorage(model);
        }
      };
      mOpen.add(mOpenModel);
    }
  }
  
  public void addSaveMenuTo(JMenu menu) {
    
    JMenu mSave = new JMenu("Save...");
    mSave.setMnemonic(KeyEvent.VK_S);
    menu.add(mSave);
    
    JMenuItem mSaveProject = new MenuItem("Project", KeyEvent.VK_P){
      @Override
      public void action(ActionEvent e) {
        ProjectStorage activeProject = ape.projects.getActiveStorage();
        if(activeProject == null) return;
        IO.saveProject(activeProject, ape.ui);
      }
    };
    mSave.add(mSaveProject);
    
    mSave.addSeparator();
    
    JMenuItem mSaveModel = new MenuItem("Model") {
      @Override
      public void action(ActionEvent e) {
        ProjectStorage activeProject = ape.projects.getActiveStorage();
        if(activeProject == null) return;
        ModelStorage activeModel = activeProject.getActiveStorage();
        if(activeModel == null) return;
        IO.saveModel(activeModel, ape.ui);
      }
    };
    mSave.add(mSaveModel);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
  }
}
