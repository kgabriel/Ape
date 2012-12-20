/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape;

import ape.org.ModelStorage;
import ape.petri.generic.Model;
import ape.petri.generic.net.Net;
import ape.petri.generic.net.Place;
import ape.petri.generic.net.Transition;
import ape.petri.pt.PTNet;
import ape.petri.pt.PTPlaceData;
import ape.petri.pt.PTTransitionData;
import ape.ui.UI;
import ape.ui.modelview.generic.ModelView;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gabriel
 */
public class Test extends Thread implements KeyListener {
  
  Ape ape;
  boolean running;
  
  public Test(Ape ape) {
    running = true;
    this.ape = ape;
    ape.ui.getModelViewCanvas().addKeyListener(this);
  }
  
  public static ModelStorage loadModel(String fileName, UI ui) {
    ModelStorage modelStorage = (ModelStorage) load(fileName);
    if(modelStorage == null) return null;
    ModelView view = modelStorage.getView();
    view.setUI(ui);
    return modelStorage;
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
  
  public void test() {
    String file = "d:\\code\\java\\ape\\test\\test.net";
    ModelStorage modelStorage = (ModelStorage) loadModel(file, ape.ui);
    if(modelStorage == null) {
      System.out.println("No net obtained. Create new one.");
      Net net = new PTNet();
      Place p = net.addPlace(new PTPlaceData("Place 0 \n: wavelet")); //\rwith \rsome extra \rtext\rand extra lines \r\rand whatnotall...
      Transition t = net.addTransition(new PTTransitionData("Transition 0"));
      modelStorage = new ModelStorage(net, new ModelView(ape.ui, net), "Net 0");
      save(modelStorage, file);
    }

    ape.ui.setActiveModelView(modelStorage.getView());
    
    ModelView modelView = modelStorage.getView();

    modelView.repaint();
    
//    start();
  }
    
  @Override
  public void run() {
    while(true) {
      if(running) {
      }
      try {
        sleep(1000);
      } catch (InterruptedException ex) {
        Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyPressed(KeyEvent e) {
    running = ! running;
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }
}
