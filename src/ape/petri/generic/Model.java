/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic;

import ape.petri.validity.Validity;
import ape.util.aml.AMLNode;
import ape.util.aml.AMLWritable;
import java.util.Collection;

/**
 *
 * @author Gabriel
 */
public abstract class Model implements AMLWritable {
  
  /** the next free Id in this model */
  private int freeId;

  private EnumNetType netType;

  private EnumModelType modelType;
  
  public Model(EnumModelType modelType, EnumNetType netType) {
    this.modelType = modelType;
    this.netType = netType;
    freeId = 0;
  }
  
  public EnumModelType getModelType() {
    return modelType;
  }
  
  public EnumNetType getNetType() {
    return netType;
  }
  
  public abstract Collection<ModelElement> getAllElements();
  
  public abstract ModelElement getModelElementById(int id);
    
  /** 
   * This method gives an unused Id for a new element. It returns the current value
   * of an integer variable, increasing on every call.
   * @return a fresh Id
   */
  public int freeElementId() {
    return freeId++;
  }
  
  protected void setFreeElementId(int freeId) {
    this.freeId = freeId;
  }
  
  /**
   * Returns whether this model is valid with respect to its theoretical definition.
   * @return a {@link ape.petri.validity.Validity} object that reflects the validity of this
   * model
   */
  public abstract Validity validate();

  @Override
  public String getAMLTagName() {
    return "Model";
  }

  @Override
  public AMLNode getAMLNode() {
    AMLNode node = new AMLNode(getAMLTagName());
    node.putAttribute("freeId", freeId);
    node.putAttribute("modelType", modelType.name());
    node.putAttribute("netType", netType.name());
    return node;
  }

  @Override
  public void readAMLNode(AMLNode node) {
    freeId = node.getAttributeInt("freeId");
    modelType = EnumModelType.valueOf(node.getAttribute("modelType"));
    netType = EnumNetType.valueOf(node.getAttribute("netType"));
  }
}
