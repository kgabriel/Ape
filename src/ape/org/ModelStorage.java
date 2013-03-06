/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.org;

import ape.petri.generic.EnumModelType;
import ape.petri.generic.EnumNetType;
import ape.petri.generic.Model;
import ape.ui.graphics.modelview.generic.ModelView;
import ape.util.EnumPropertyType;
import ape.util.Property;
import ape.util.PropertyConstant;
import ape.util.aml.AMLNode;
import ape.util.aml.AMLWritable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Gabriel
 */
public class ModelStorage extends Storage {
  private EnumModelType type;
  private Model model;
  private ModelView view; 

  public ModelStorage(EnumModelType type, Model model, ModelView view) {
    super(type.getName());
    this.type = type;
    this.model = model;
    this.view = view;
  }

  public EnumModelType getType() {
    return type;
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
    properties.add(0, new Property(Property.CATEGORY_PROPERTIES, this, EnumPropertyType.String, "Name", true) {
      @Override
      public Object getValue() {
        return getName();
      }

      @Override
      public void setValue(Object value) {
        setName((String) value);
      }
    });
    properties.add(0, new PropertyConstant(Property.CATEGORY_PROPERTIES, this, EnumPropertyType.String, "Entity Type", "Model"));
    return properties;
  }

  @Override
  public String getAMLTagName() {
    return "ModelStorage";
  }

  @Override
  public AMLNode getAMLNode() {
    AMLNode node = super.getAMLNode();
    node.putAttribute("type", type.name());
    node.addChild(model.getAMLNode());
    node.addChild(view.getAMLNode());
    return node;
  }

  

  @Override
  public void readAMLNode(AMLNode node) {
    super.readAMLNode(node);
    this.type = EnumModelType.valueOf(node.getAttribute("type"));
    AMLNode modelNode = node.getFirstChild("Model");
    model = type.createModel(EnumNetType.valueOf(modelNode.getAttribute("netType")));
    model.readAMLNode(modelNode); 
    AMLNode modelViewNode = node.getFirstChild("modelView");
    view = new ModelView(null, model, false);
    view.readAMLNode(modelViewNode);
  }
  
}
