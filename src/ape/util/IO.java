/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.util;

import ape.org.ModelStorage;
import ape.org.ProjectStorage;
import ape.petri.generic.EnumModelType;
import ape.ui.UI;
import ape.ui.graphics.modelview.generic.ModelView;
import ape.util.aml.AMLNode;
import ape.util.aml.ApeML;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Gabriel
 */
public class IO {
  
  public final static String PROJECT_EXTENSION = "ape";
  
  public static File singleFileChooser(JFrame parent, boolean isOpening, String extensionName, 
          String extension, String title) {
    return singleFileChooser(parent, isOpening, extensionName, extension, title, null);
  }

    public static File singleFileChooser(JFrame parent, boolean isOpening, String extensionName, 
          String extension, String title, String fileNameSuggestion) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileFilter(new FileNameExtensionFilter(extensionName + " (*." + extension + ")", extension));
    fileChooser.setDialogTitle(title);
    if(fileNameSuggestion != null) fileChooser.setSelectedFile(new File(fileNameSuggestion));
    int status;
    if(isOpening) {
      status = fileChooser.showOpenDialog(parent);
    } else {
      status = fileChooser.showSaveDialog(parent);
    }
    if(status == JFileChooser.APPROVE_OPTION) {
      return fileChooser.getSelectedFile();
    }
    return null;
  }
  
  public static List<ProjectStorage> loadProjects(UI ui) {
    File file = singleFileChooser(ui.getMainFrame(), true, "Projects", PROJECT_EXTENSION, "Open Project");
    if(file == null) return null;
    return loadProjects(file.getAbsolutePath(), ui);
  }

  public static List<ProjectStorage> loadProjects(String fileName, UI ui) {
    //    ProjectStorage projectStorage = (ProjectStorage) load(fileName);
    List<AMLNode> nodes = ApeML.read(fileName);
    List<ProjectStorage> storages = new ArrayList<>();
    for(AMLNode node : nodes) {
      ProjectStorage projectStorage = new ProjectStorage();
      projectStorage.readAMLNode(node);

      if(projectStorage == null) continue;
      for(ModelStorage model : projectStorage.getStorages()) {
        model.setContainer(projectStorage);
        ModelView view = model.getView();
        view.setUI(ui);
      }
      storages.add(projectStorage);
    }
    return storages;
  }
  
  public static List<ModelStorage> loadModels(UI ui, EnumModelType modelType) {
    String modelName = modelType.getName();
    File file = singleFileChooser(ui.getMainFrame(), true, modelName, modelType.getFileExtension(), "Open " + modelName);
    if(file == null) return null;
    return loadModels(file.getAbsolutePath(), ui);
  }
  
  public static List<ModelStorage> loadModels(String fileName, UI ui) {
    //ModelStorage modelStorage = (ModelStorage) load(fileName);
    List<AMLNode> nodes = ApeML.read(fileName);
    List<ModelStorage> storages = new ArrayList<>();
    for(AMLNode node : nodes) {
      ModelStorage modelStorage = new ModelStorage(EnumModelType.Rule, null, null);
      modelStorage.readAMLNode(node);

      if(modelStorage == null) continue;
      ModelView view = modelStorage.getView();
      view.setUI(ui);
      storages.add(modelStorage);
    }
    return storages;
  }
  
  public static Object load(String fileName) {
    Object object = null;
    try {
      FileInputStream fileIn = new FileInputStream(fileName);
      ObjectInputStream in = new ObjectInputStream(fileIn);
      object = in.readObject();
      in.close();
      fileIn.close();
    } catch(ClassNotFoundException | IOException e) {
      e.printStackTrace();
    }
    return object;
  }
  
  public static void saveProject(ProjectStorage project, UI ui) {
    File file = singleFileChooser(ui.getMainFrame(), false, "Projects", PROJECT_EXTENSION, 
            "Save Project", project.getName());
    if(file == null) return;
//    save(project, ensureExtension(file.getAbsolutePath(),PROJECT_EXTENSION));
    ApeML.write(project.getAMLNode(), ensureExtension(file.getAbsolutePath(),PROJECT_EXTENSION));
  }
  
  public static void saveModel(ModelStorage model, UI ui) {
    EnumModelType modelType = model.getType();
    String modelName = modelType.getName();
    File file = singleFileChooser(ui.getMainFrame(), false, modelName, 
            modelType.getFileExtension(), "Save " + modelName, model.getName());
    if(file == null) return;
//    save(model, ensureExtension(file.getAbsolutePath(), modelType.getFileExtension()));
    ApeML.write(model.getAMLNode(), ensureExtension(file.getAbsolutePath(), modelType.getFileExtension()));
  }
  
  public static String ensureExtension(String fileName, String extension) {
    if(fileName.endsWith(extension)) return fileName;
    return fileName + "." + extension;
  }
  
  public static void save(Object object, String fileName) {
    try {
      FileOutputStream fileOut = new FileOutputStream(fileName);
      ObjectOutputStream out = new ObjectOutputStream(fileOut);
      out.writeObject(object);
      out.close();
      fileOut.close();
    } catch(IOException e) {
      e.printStackTrace();
    }
  }
}
