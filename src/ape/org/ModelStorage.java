/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.org;

import ape.petri.generic.EnumModelType;
import ape.petri.generic.EnumNetType;
import ape.petri.generic.Model;
import ape.petri.generic.ModelElement;
import ape.ui.UI;
import ape.ui.graphics.modelview.ModelView;
import ape.util.*;
import ape.util.aml.AMLNode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class ModelStorage extends Storage {
  private EnumModelType modelType;
  private Model model;
  private ModelView view; 

  public ModelStorage(Model model, UI ui) {
    this.model = model;
    if(model != null) {
      this.view = new ModelView(ui, this);
      this.modelType = model.getModelType();      
      setNewName(modelType.getName());
    }
  }

  public EnumModelType getModelType() {
    return modelType;
  }
  
  public EnumNetType getNetType() {
    return model.getNetType();
  }
  
  public Model getModel() {
    return model;
  }
  
  public ModelView getView() {
    return view;
  }
  
  @Override
  public List<Property> getProperties() {
    List<Property> properties = view.getProperties();
    properties.add(0, new Property(Property.CATEGORY_PROPERTIES, this, EnumPropertyType.SingleLineText, "Name") {
      @Override
      public Object getValue() {
        return getName();
      }

      @Override
      public void setValue(Object value) {
        setName((String) value);
      }
    });
    properties.add(0, new PropertyConstant(Property.CATEGORY_PROPERTIES, this, EnumPropertyType.SingleLineText, "Entity Type", "Model"));
    return properties;
  }

  public PropertyContainer getModelElementPropertyContainer(int modelElementId) {
    List<Property> properties = new ArrayList<>();
    ModelElement modelElement = model.getModelElementById(modelElementId);
    if(modelElement != null) {
      properties.addAll(modelElement.getData().getProperties());
      properties.addAll(view.getVisual(modelElement).getProperties());
    }
    return new AbstractPropertyContainer(properties);
  }  
  
  @Override
  public String getAMLTagName() {
    return "ModelStorage";
  }

  @Override
  public AMLNode getAMLNode() {
    AMLNode node = super.getAMLNode();
    node.putAttribute("modelType", modelType.name());
    node.addChild(model.getAMLNode());
    node.addChild(view.getAMLNode());
    return node;
  }

  @Override
  public void readAMLNode(AMLNode node) {
    super.readAMLNode(node);
    this.modelType = EnumModelType.valueOf(node.getAttribute("modelType"));
    AMLNode modelNode = node.getFirstChild("Model");
    model = modelType.createModel(EnumNetType.valueOf(modelNode.getAttribute("netType")));
    model.readAMLNode(modelNode); 
    AMLNode modelViewNode = node.getFirstChild("modelView");
    view = new ModelView(null, this);
    view.readAMLNode(modelViewNode);
  }
}
