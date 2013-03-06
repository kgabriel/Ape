/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.generic;

import ape.petri.generic.net.Data;
import ape.petri.generic.net.DataChangeListener;
import ape.petri.generic.net.Transition;
import ape.util.EnumPropertyType;
import ape.util.Property;
import ape.util.aml.AMLNode;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;

/**
 *
 * @author Gabriel
 */
public abstract class TransitionVisual extends NodeVisual implements DataChangeListener {
  protected Transition transition;
  protected TextVisual nameVisual;
  private int nameHeight;
  private int dataHeight;
  protected TextVisual dataVisual;
  protected TextVisual extendedDataVisual;

  public TransitionVisual(Graphics2D superGraphics, Transition transition) {
    this(superGraphics, new Point(0,0), transition);
  }

  public TransitionVisual(Graphics2D superGraphics, Point position, Transition transition) {
    super(superGraphics);
    setResizable(true,false);
    setTransition(transition);
    this.nameVisual = new TextVisual(superGraphics);
    addChild(nameVisual);
    nameVisual.setCompact(false);
    nameVisual.setCentered(true);
    nameVisual.setTextColor(VisualGlobalValues.transitionForegroundColor);
    this.dataVisual = new TextVisual(superGraphics);
    addChild(dataVisual);
    dataVisual.setCompact(false);
    dataVisual.setTextColor(VisualGlobalValues.transitionForegroundColor);
    this.extendedDataVisual = new TextVisual(superGraphics);
    addChild(extendedDataVisual);
    extendedDataVisual.setCompact(false);
    extendedDataVisual.setTextColor(VisualGlobalValues.transitionExtendedDataForegroundColor);
    
    setLocation(position);
    updateLocation();
    setSize(VisualGlobalValues.transitionWidth, 0);
    updateContent();
    updateSize();
    initProperties();
  }
  
  private void initProperties() {
    addProperty(new Property(Property.CATEGORY_VIEW, this, EnumPropertyType.Integer, "Width", true) {
     @Override
      public Object getValue() {
        return getWidth();
      }

      @Override
      public void setValue(Object value) {
        int w = (int) value;
        if(w < 10) w = 10;
        setSize(w, getHeight());
        updateOnUserResize();
      }
    });
  }
  
  
  private void setTransition(Transition transition) {
    this.transition = transition;
    transition.getData().addDataChangeListener(this);
  }

  @Override
  public String getDataName() {
    return transition.getData().getName();
  }

  @Override
  public void setDataName(String name) {
    transition.getData().setName(name);
  }

  @Override
  public int getNodeId() {
    return transition.getId();
  } 

  private void updateLocation() {
    int x = getX();
    int y = getY();
    nameVisual.setLocation(x, y);
    dataVisual.setLocation(x, nameHeight + y);
    extendedDataVisual.setLocation(x, dataHeight + y);
  }
  
  protected void updateContent() {
    String transitionName = transition.getData().getName();
    nameVisual.setText(transitionName);
    updateDataVisualContent();
    updateExtendedDataVisualContent();
    updateLocation();
  }
  
  protected void updateSize() {
    nameHeight = nameVisual.getHeight();
    dataVisual.setLocation(getX(), getY() + nameHeight);
    int height = nameHeight;
    dataHeight = nameHeight;
    if(dataVisual.text.length() != 0) {
      height += dataVisual.getHeight();
      dataHeight += dataVisual.getHeight();
    }
    if(extendedDataVisual.text.length() != 0) {
      height += extendedDataVisual.getHeight();
    }
    setSize(getWidth(), height);
  }

  protected abstract void updateDataVisualContent();
  protected abstract void updateExtendedDataVisualContent();

  @Override
  public void draw(int status) {
    int width = getWidth();
    int height = getHeight();
    graphics.setColor(VisualGlobalValues.transitionBackgroundColor);
    graphics.fillRect(0, 0, width-1, height-1);
    if((status & STATUS_HOVER) != 0) {
      graphics.setColor(VisualGlobalValues.modelHoverColor);
      graphics.fillRect(0, 0, width-1, height-1);
    }
    if((status & STATUS_SELECTED) != 0) {
      graphics.setColor(VisualGlobalValues.modelSelectionColor);
      graphics.fillRect(0, 0, width-1, height-1);
    }
    graphics.setColor(VisualGlobalValues.transitionForegroundColor);
    graphics.drawRect(0, 0, width-1, height-1);
    nameVisual.redraw(status);
    graphics.drawLine(0, nameHeight, width-1, nameHeight);
    dataVisual.redraw(status);
    graphics.drawLine(0, dataHeight, width-1, dataHeight);
    extendedDataVisual.redraw(status);
  }

  @Override
  protected void updateOnResize() {
    int width = getWidth();
    nameVisual.setSize(width, 0);
    nameVisual.updateOnResize();
    dataVisual.setSize(width, 0);
    dataVisual.updateOnResize();
    extendedDataVisual.setSize(width, 0);
    extendedDataVisual.updateOnResize();
    updateSize();
  }

  @Override
  protected void updateOnMove() {
    updateLocation();
  }

  private void notifyArcsOnMoveAndResize() {
    for(ArcVisual av : connectedArcs) {
      av.transitionChangedLocation();
    }
  }
  @Override
  protected void updateOnUserMove() {
    notifyArcsOnMoveAndResize();
  }

  @Override
  protected void updateOnUserMoveFinished() {}

  @Override
  protected void updateOnUserResize() {
    notifyArcsOnMoveAndResize();
  }
  
  @Override
  public void dataChanged(Data changedData) {
    updateContent();
    updateSize();
    dataHasChanged();
  }

  @Override
  public Shape getShape() {
    return getBounds();
  }
  
  @Override
  public String getElementTypeName() {
    return "Transition";
  }

  @Override
  public AMLNode getAMLNode() {
    AMLNode node = super.getAMLNode();
    node.putAttribute("elementType", getElementTypeName());
    return node;
  }

}
