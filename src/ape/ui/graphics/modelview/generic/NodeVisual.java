/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.generic;

import ape.util.EnumPropertyType;
import ape.util.Property;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.HashSet;

/**
 * This abstract {@link Visual} subclass is the superclass for all visual node elements like
 * places and transitions.
 * @author Gabriel
 */
public abstract class NodeVisual extends Visual {

  protected Collection<ArcVisual> connectedArcs;
  
  public static final int PROPERTY_X = 0;
  public static final int PROPERTY_Y = 1;

  public NodeVisual(Graphics2D superGraphics) {
    super(superGraphics);
    this.connectedArcs = new HashSet<>();
    initProperties();
  }
  
  public NodeVisual(Graphics2D superGraphics, Rectangle bounds) {
    super(superGraphics, bounds);
    this.connectedArcs = new HashSet<>();
    initProperties();
  }
  
  private void initProperties() {
    addProperty(new Property(Property.CATEGORY_PROPERTIES, this, EnumPropertyType.String, "Name", true) {
      @Override
      public Object getValue() {
        return getDataName();
      }

      @Override
      public void setValue(Object value) {
        setDataName((String) value);
      }      
    });
    
    addProperty(new Property(Property.CATEGORY_PROPERTIES, this, EnumPropertyType.Integer, "Element ID", false) {
      @Override
      public Object getValue() {
        return getNodeId();
      }

      @Override
      public void setValue(Object value) {}
    });

   
    addProperty(new Property(Property.CATEGORY_VIEW, this, EnumPropertyType.Integer, "X", true) {
      @Override
      public Object getValue() {
        return (int) getCenterX();
      }

      @Override
      public void setValue(Object value) {
        setCenter((int) value, (int) getCenterY());
      }      
    });
   
    addProperty(new Property(Property.CATEGORY_VIEW, this, EnumPropertyType.Integer, "Y", true) {
      @Override
      public Object getValue() {
        return (int) getCenterY();
      }

      @Override
      public void setValue(Object value) {
        setCenter((int) getCenterX(), (Integer) value);
      }      
    });
  }
  
  public abstract String getDataName();
  
  public abstract void setDataName(String name);
  
  public abstract int getNodeId();
  
  protected void addConnectedArc(ArcVisual av) {
    connectedArcs.add(av);
  }
  
  protected boolean removeConnectedArc(ArcVisual av) {
    return connectedArcs.remove(av);
  }

}
