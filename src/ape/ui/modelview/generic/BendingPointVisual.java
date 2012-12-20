/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.modelview.generic;

import ape.petri.generic.net.EnumArcDirection;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.Collection;

/**
 *
 * @author Gabriel
 */
public class BendingPointVisual extends Visual {

  /* The key of this bending point. */
  private int key;
  
  /** The arc visual, this bending point belongs to. */
  private ArcVisual parent;
  
  /** A boolean value, whether this bending point is a servant of another one.
   * A bending point becomes a master of two servants itself if it has been moved by the user. 
   */
  private boolean servant;
  private boolean recentlyPromoted;
  
  public BendingPointVisual(Graphics2D superGraphics, ArcVisual parent) {
    super(superGraphics);
    this.parent = parent;
    this.servant = true;
    this.recentlyPromoted = false;
    setSize(VisualGlobalValues.bendingPointDiameter,VisualGlobalValues.bendingPointDiameter);
    setResizable(false, false);
    setMovable(true);
  }

  @Override
  public Collection<VisualProperty> getProperties() {
    return parent.getProperties();
  }
  
  public int getKey() {
    return key;
  }

  protected void setKey(int number) {
    this.key = number;
  }
  
  protected boolean isServant() {
    return servant;
  }
  
  protected boolean isMaster() {
    return ! servant;
  }
  
  @Override
  public void draw(int status) {
    int width = getWidth() - 2;
    int height = getHeight() - 2;
    if((status & STATUS_PARENT_HOVER) != 0) {
      if(servant) {
        graphics.setColor(VisualGlobalValues.bendingPointServantArcHoverColor);
      } else {
        graphics.setColor(VisualGlobalValues.bendingPointMasterArcHoverColor);
      }
      graphics.fillOval(1, 1, width, height);
    }
    if((status & STATUS_HOVER) != 0) {
      graphics.setColor(VisualGlobalValues.bendingPointHoverColor);
      graphics.fillOval(1, 1, width, height);
    } 
    if((status & STATUS_SELECTED) != 0) {
      graphics.setColor(VisualGlobalValues.bendingPointSelectedColor);
      graphics.fillOval(1, 1, width, height);
    }
  }

  @Override
  protected void updateOnResize() {}

  @Override
  protected void updateOnMove() {}

  @Override
  protected void updateOnUserMove() {
    parent.bendingPointChangedLocation(this);
    if(servant) {
      servant = false;
      recentlyPromoted = true;
    }
  }

  @Override
  protected void updateOnUserMoveFinished() {
    parent.bendingPointChangedLocationByUser(this,recentlyPromoted);
    recentlyPromoted = false;
}

  @Override
  protected void updateOnUserResize() {}

  @Override
  public Shape getShape() {
    float x = getX();
    float y = getY();
    float width = getWidth();
    float height = getHeight();
    return new Ellipse2D.Float(x,y,width,height);
  }

  @Override
  public String getElementTypeName() {
    return "Arc / Bending Point";
  } 
}
