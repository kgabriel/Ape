/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.modelview;

import ape.petri.generic.ModelElement;
import ape.ui.modelview.generic.ModelView;
import ape.ui.modelview.generic.Visual;

/**
 *
 * @author Gabriel
 */
public interface ModelViewListener {
  
  public void activeModelViewChanged(ModelView activeView);
  
  public void visualElementAddedToActiveModelView(ModelElement e, Visual v);
  
  public void visualElementRemovedFromActiveModelView(ModelElement e, Visual v);
}
