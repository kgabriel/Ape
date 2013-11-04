/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.generic;

import ape.petri.generic.DataChangeListener;
import ape.petri.generic.net.Data;
import ape.util.aml.AMLNode;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author Gabriel
 */
public abstract class ModelElementVisual extends Visual implements DataChangeListener {

  protected Data data;
  private int modelElementId;
  
  public ModelElementVisual(Graphics2D superGraphics, Rectangle bounds, Data data, int modelElementId) {
    super(superGraphics, bounds);
    this.data = data;
    data.addDataChangeListener(this);
    this.modelElementId = modelElementId;
  }

  public ModelElementVisual(Graphics2D superGraphics, Data data, int modelElementId) {
    super(superGraphics);
    this.data = data;
    data.addDataChangeListener(this);
    this.modelElementId = modelElementId;
  }

  public int getModelElementId() {
    return modelElementId;
  }

  @Override
  public AMLNode getAMLNode() {
    AMLNode node = super.getAMLNode();
    node.putAttribute("id", modelElementId);
    return node;
  }

  @Override
  public void readAMLNode(AMLNode node) {
    super.readAMLNode(node);
    this.modelElementId = node.getAttributeInt("id");
  }
  
  
}
