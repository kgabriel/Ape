/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.generic;

import ape.petri.generic.net.NodeData;
import ape.util.EnumPropertyType;
import ape.util.Property;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * This abstract {@link Visual} subclass is the superclass for all visual node elements like
 * places and transitions.
 * @author Gabriel
 */
public abstract class NodeVisual extends ModelElementVisual {

  protected Collection<ArcVisual> connectedArcs;
  
  public static final int PROPERTY_X = 0;
  public static final int PROPERTY_Y = 1;

  public NodeVisual(Graphics2D superGraphics, NodeData data, int modelElementId) {
    super(superGraphics, data, modelElementId);
    this.connectedArcs = new HashSet<>();
  }
  
  public NodeVisual(Graphics2D superGraphics, Rectangle bounds, NodeData data, int modelElementId) {
    super(superGraphics, bounds, data, modelElementId);
    this.connectedArcs = new HashSet<>();
  }
  
  @Override
  public List<Property> getProperties() {
    List<Property> properties = super.getProperties();
    properties.add(new Property(Property.CATEGORY_VIEW, this, EnumPropertyType.Integer, "X") {
      @Override
      public Object getValue() {
        return (int) getCenterX();
      }

      @Override
      public void setValue(Object value) {
        setCenter((int) value, (int) getCenterY());
      }      
    });
   
    properties.add(new Property(Property.CATEGORY_VIEW, this, EnumPropertyType.Integer, "Y") {
      @Override
      public Object getValue() {
        return (int) getCenterY();
      }

      @Override
      public void setValue(Object value) {
        setCenter((int) getCenterX(), (Integer) value);
      }      
    });
    return properties;
  }
  
  protected void addConnectedArc(ArcVisual av) {
    connectedArcs.add(av);
  }
  
  protected boolean removeConnectedArc(ArcVisual av) {
    return connectedArcs.remove(av);
  }

}
