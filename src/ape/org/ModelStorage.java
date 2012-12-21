/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.org;

import ape.petri.generic.EnumModelType;
import ape.petri.generic.Model;
import ape.ui.modelview.generic.ModelView;
import ape.util.EnumPropertyType;
import ape.util.Property;
import ape.util.PropertyConstant;
import java.util.List;

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
    properties.add(0, new Property(this, EnumPropertyType.String, "Model Name", true) {
      @Override
      public Object getValue() {
        return getName();
      }

      @Override
      public void setValue(Object value) {
        setName((String) value);
      }
    });
    return properties;
  }
}
