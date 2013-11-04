/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.generic;

import ape.util.EnumPropertyType;
import ape.util.Property;
import ape.math.Ray2D;
import ape.math.Vector2D;
import ape.petri.generic.net.Data;
import ape.petri.generic.DataChangeListener;
import ape.petri.generic.net.Place;
import ape.petri.generic.net.PlaceData;
import ape.util.aml.AMLNode;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.List;

/**
 * This abstract {@link NodeVisual} models the graphical representation of a place.
 * @author Gabriel
 */
public abstract class PlaceVisual extends NodeVisual implements DataChangeListener {
  
  protected TextVisual label;
  protected double labelAngle;
  
  public PlaceVisual(Graphics2D superGraphics, PlaceData data, int modelElementId) {
    this(superGraphics, new Point(0,0), data, modelElementId);
  }
  
  public PlaceVisual(Graphics2D superGraphics, Point position, PlaceData data, int modelElementId) {
    super(superGraphics, data, modelElementId);
    setResizable(false, false);
    
    this.labelAngle = VisualGlobalValues.placeLabelStandardPosition;
    label = new TextVisual(superGraphics);
    addChild(label);
    label.setTextColor(VisualGlobalValues.placeForegroundColor);
    
    int radius = VisualGlobalValues.placeRadius;
    int diameter = 2 * radius;
    Rectangle b = new Rectangle(position.x - radius, position.y - radius, diameter, diameter);
    setBounds(b);

    updateLabelPosition();
  }
  
  @Override
  public List<Property> getProperties() {
    List<Property> properties = super.getProperties();
    properties.add(new Property(Property.CATEGORY_VIEW, this, EnumPropertyType.Interval, "Label Angle") {
      @Override
      public Object getValue() {
        return labelAngle / (2 * Math.PI);
      }

      @Override
      public void setValue(Object value) {
        labelAngle = 2 * Math.PI * (double) value;
        updateLabelPosition();
      }
    });
    return properties;
  }
  
  protected TextVisual getLabel() {
    return label;
  }

  private void updateLabelPosition() {
    /* normal vector of length 1 rotated by label's angle */
    Vector2D normalDirection = Vector2D.baseX.getRotatedVector(labelAngle);
    /* ray with normal direction circle to label, but located at (0,0) */
    Ray2D relRay = Ray2D.fromPositionAndNormalDirection(Vector2D.zero, normalDirection);

    /* inverted relative ray = normal relative ray from label to circle */
    Ray2D invRay = relRay.getInverted();
    
    /* if we want to position the label at its center, we get problems when one side of
     * the label is much larger than the other side;
     * therefore we define the position to be a point inside the label which is 
     * determined by the normal inverted ray and the size of the larger side (X/Y) of the label
     */
    Vector2D labelSize = new Vector2D(label.getWidth(), label.getHeight());
    Vector2D pos = invRay.getNormalDirectionVector().multiply(Math.max(labelSize.x, labelSize.y) / 2.0);
    
    /* which side (X or Y) of the label is larger ? */
    boolean X = true;
    boolean Y = false;
    boolean larger = (labelSize.x > labelSize.y ? X : Y);
    
    /* we only want to take into account the larger side */
    if(larger == X) { pos.y = 0.0; }
    else if(larger == Y) { pos.x = 0.0; }
    
    /* we subtract the relative position of our positioning vector to the center of the label
     * from the size we take into account for bound checking
     */
    Vector2D posSize = new Vector2D(labelSize).subtract(pos.getAbsoluteVector().multiply(2.0));
    
    /* distance from where the ray enters the label to the label's position */
    double labelPtoI = invRay.rectangleLeavingDistance(posSize.x, posSize.y);

    double generalDist = VisualGlobalValues.placeRadius + VisualGlobalValues.placeLabelDistance;
    double dist = generalDist + labelPtoI;

    /* the ray centered at this place visual */
    Ray2D ray = new Ray2D(relRay).translate(getCenterVector());
    
    /* the center of the label calculated from the position we used for positioning */
    Vector2D labelCenter = ray.getTargetVector(dist).subtract(pos);
    
    label.setCenter(labelCenter.toPoint());
  }
  

  public double getLabelAngle() {
    return labelAngle;
  }

  public void setLabelAngle(double labelAngle) {
    labelAngle = Math.IEEEremainder(labelAngle, Math.PI * 2);
    this.labelAngle = labelAngle;
    updateLabelPosition();
  }
  
  public double getLabelAngleInDegrees() {
    return Math.toDegrees(labelAngle);
  }
  
  public void setLabelAngleInDegrees(double degrees) {
    setLabelAngle(Math.toRadians(degrees));
  }

  @Override
  public void draw(int status) {
    int width = getWidth()-2;
    int height =getHeight()-2;
    graphics.setColor(VisualGlobalValues.placeBackgroundColor);
    graphics.fill(new Ellipse2D.Float(1,1, width, height));
    if((status & STATUS_HOVER) != 0) {
      graphics.setColor(VisualGlobalValues.modelHoverColor);
      graphics.fill(new Ellipse2D.Float(1,1, width, height));
    }
    if((status & STATUS_SELECTED) != 0) {
      graphics.setColor(VisualGlobalValues.modelSelectionColor);
      graphics.fill(new Ellipse2D.Float(1,1, width, height));
    }
    graphics.setColor(VisualGlobalValues.placeForegroundColor);
    graphics.draw(new Ellipse2D.Float(1,1, width, height));
    label.redraw(status);
  }
  
  @Override
  public void updateOnResize() {}

  @Override
  public void updateOnMove() {
    updateLabelPosition();
  }
  
  private void notifyArcsOnMove() {
    for(ArcVisual av : connectedArcs) {
      av.placeChangedLocation();
    }
  }

  @Override
  public void updateOnUserMove() {
    notifyArcsOnMove();
  }

  @Override
  public void dataChanged(Data changedData) {
    updateLabelContent();
    updateLabelPosition();
    dataHasChanged();
  }
  
  protected abstract void updateLabelContent();

  @Override
  public Shape getShape() {
    float x = getX();
    float y = getY();
    float width = getWidth();
    float height = getHeight();
    return new Ellipse2D.Float(x,y,width,height);
  }
  
  public String getPlaceName() {
    return ((PlaceData) data).getName();
  }
  
  @Override
  public String getElementTypeName() {
    return "Place";
  }

  @Override
  public AMLNode getAMLNode() {
    AMLNode node = super.getAMLNode();
    node.putAttribute("labelAngle", labelAngle);
    node.putAttribute("elementType", getElementTypeName());
    return node;
  }

  @Override
  public void readAMLNode(AMLNode node) {
    super.readAMLNode(node);
    labelAngle = node.getAttributeDouble("labelAngle");
    updateLabelContent();
    updateLabelPosition();
  }
  
  
}
