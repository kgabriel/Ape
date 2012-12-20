/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.modelview.generic;

import ape.petri.generic.net.DataChangeListener;
import ape.petri.generic.net.EnumArcDirection;
import ape.petri.generic.net.ArcCollection;
import ape.petri.generic.net.Data;
import ape.math.Ray2D;
import ape.math.Vector2D;
import ape.petri.generic.*;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * An <code>ArcVisual</code> is the graphical representation of an {@link Arc}, more precisely
 * an {@link ArcCollection}. It is represented as an arrow between a place and a transition
 * with one of the two possible directions. The arrow can be bended at bending points,
 * that are visualized by {@link BendingPointVisual}s.
 * <br />
 * Each arc has at least one bending point, and every bending point starts its existence
 * as a <i>servant</i>. 
 * When a servant bending point is moved by the user, it becomes a <i>master</i>,
 * and a new servant is inserted between the moved point and each of its neighbors.
 * A servant unlike a master does not have a fixed position. This means that moving an
 * adjacent point of a servant, also moves the servant accordingly, such that it lies
 * exactly on the line that connects the neighbors of the servant.
 * <br />
 * If the distance of a master point to the line that connects adjacent masters is less
 * than or equal to a specific value (<code>4.0</code> pixels hard coded), then the bending
 * point (and its servants, if it has some) is removed and a new servant is inserted instead.
 * @see BendingPointVisual
 * @author Gabriel
 */
public abstract class ArcVisual extends Visual implements DataChangeListener {

  /**
   * The {@link PlaceVisual} representing the {@link Place} of the {@link Arc} that
   * is represented by this <code>ArcVisual</code>.
   */
  private PlaceVisual placeVisual;

  /**
   * The {@link TransitionVisual} representing the {@link Transition} of the {@link Arc} that
   * is represented by this <code>ArcVisual</code>.
   */
  private TransitionVisual transitionVisual;
  
  /**
   * The direction of the arc.
   */
  private EnumArcDirection direction;
  
  /**
   * The data of the arc.
   */
  protected ArcCollection arc;
  
  /**
   * The label that displays the data of the arc.
   */
  private TextVisual label;
  
  /**
   * The relative position of the label on the path from place to transition.
   */
  private double labelPosition;
  
  /** The bending points, having an integer key. Lower key means closer to place.
   * Higher key means closer to transition.
   */
  private TreeMap<Integer,BendingPointVisual> bendingPoints;

  /**
   * Constructor for new <code>ArcVisual</code> connecting the specified {@link NodeVisual}s
   * in the specified direction, and having the specified data collection.
   * @param superGraphics the superior graphics object
   * @param pv the <code>Visual</code> for the place of the arc
   * @param tv the <code>Visual</code> for the transition of the arc
   * @param dir the direction of the arc
   * @param data the data of the arc
   */
  public ArcVisual(Graphics2D superGraphics, PlaceVisual pv, TransitionVisual tv, EnumArcDirection dir,
          ArcCollection arc) {
    super(superGraphics);
    initConnection(pv, tv);
    this.direction = dir;
    this.bendingPoints = new TreeMap<>();
    addBendingPoint(0, false, new BendingPointVisual(superGraphics, this));
    setResizable(false, false);
    setMovable(false);
    setSelectable(false);
    label = new TextVisual(superGraphics);
    addChild(label);
    label.setTextColor(VisualGlobalValues.arcLabelTextColor);
    label.setBackgroundColor(VisualGlobalValues.arcLabelBackgroundColor);
    labelPosition = 0.7;
    updateLocation();
    setArc(arc);
    initProperties();
  }
  
  /**
   * Registers the <code>ArcVisual</code> at the place and transition visual.
   * @param pv the place visual
   * @param tv the transition visual
   */
  private void initConnection(PlaceVisual pv, TransitionVisual tv) {
    this.placeVisual = pv;
    placeVisual.addConnectedArc(this);
    this.transitionVisual = tv;
    transitionVisual.addConnectedArc(this);
  }
  
  private void initProperties() {
    /* arc direction: pre or post arc */
    String dir = (direction == EnumArcDirection.PT ? "Pre Arc" : "Post Arc");
    addProperty(new VisualPropertyConstant(this, EnumVisualPropertyType.String, "Arc Type", dir));
    
    /* place of the arc */
    addProperty(new VisualProperty(this, EnumVisualPropertyType.String, "Place", false) {
      @Override
      public Object getValue() {
        return placeVisual.getDataName();
      }

      @Override
      public void setValue(Object value) {}
    });
    
    /* transition of the arc */
   addProperty(new VisualProperty(this, EnumVisualPropertyType.String, "Transition", false) {
      @Override
      public Object getValue() {
        return transitionVisual.getDataName();
      }

      @Override
      public void setValue(Object value) {}
    });
   
   /* position of the label */
   addProperty(new VisualProperty(this, EnumVisualPropertyType.Interval, "Label Position", true) {
      @Override
      public Object getValue() {
        return labelPosition;
      }

      @Override
      public void setValue(Object value) {
        labelPosition = (double) value;
      }
    });
  }
  
  /**
   * Adds a bending point into the tree map as a neighbor (or, effectively a servant) of the specified parent.
   * Also, the position of the new bending point is set to the middle point between its two neighbors.
   * @param masterKey the master of the newly inserted servant
   * @param forward if <code>true</code>, the new bending point is inserted after the master; otherwise
   * it is inserted at the current position of the master; all preceeding bending point's indices are shifted
   * @param b the bending point to insert
   */
  private void addBendingPoint(int masterKey, boolean forward, BendingPointVisual b) {
    int key = (forward ? masterKey + 1 : masterKey);

    /* shift all successor elements by incrementing their key */
    TreeMap<Integer,BendingPointVisual> shifted = new TreeMap<>();
    ArrayList<Integer> keysToRemove = new ArrayList<>();
    for(int existentKey : bendingPoints.tailMap(key,true).keySet()) {
      BendingPointVisual v = bendingPoints.get(existentKey);
      shifted.put(existentKey + 1,v);
      keysToRemove.add(existentKey);
      v.setKey(existentKey + 1);
    }
    for(int existentKey : keysToRemove) {
      bendingPoints.remove(existentKey);
    }
    bendingPoints.putAll(shifted);
    
    /* set position of new bending point between predessor and successor */
    Vector2D lowerPos = getBendingPointPredecessorVector(key);
    Vector2D higherPos = getBendingPointSuccessorVector(key);
    b.setCenter(Vector2D.middlePoint(lowerPos, higherPos).toPoint());

    /* add new point to map (this has to be done after the position is calculated) */
    bendingPoints.put(key, b);
    b.setKey(key);
    
    /* add the new point as a selectable child */
    addChild(b,true);
  }
  
  /**
   * Removes a bending point that corresponds to the specified key.
   * @param key the key of the bending point to remove
   */
  private void removeBendingPoint(Integer key) {
    if(key == null) return;
    BendingPointVisual v = bendingPoints.remove(key);
    removeChild(v);
  }
  
  /**
   * Removes a bending point only if it is a servant.
   * @param key the key of the bending point to remove
   */
  private void removeBendingPointIfServant(Integer key) {
    if(key == null) return;
    BendingPointVisual v = bendingPoints.get(key);
    if(v.isServant()) removeBendingPoint(key);
  }
    
  /**
   * A bending point can be removed only if it is a master and an imaginary straight line that connects
   * its master neighbors is inside a given range. The method does not only check, but it actually
   * removes the bending point if it can be removed. If the master already had servants, also its
   * servants are removed. Afterwards, a new servant is inserted, replacing the removed master.
   * @param key the key of the master bending point to check if obsolete
   * @param wasServant <code>true</code> if the bending point recently was a servant which
   * means that it does not yet has its own servants (and thus, its direct neighbors are the
   * nearest masters)
   * @return <code>true</code> if the bending point was removed
   */
  private boolean checkIfBendingPointCanBeRemoved(int key, boolean wasServant) {
    BendingPointVisual bp = bendingPoints.get(key);
    if(bp == null || bp.isServant()) return false;
    Vector2D pre;
    Vector2D succ;
    if(wasServant) {
      pre = getBendingPointPredecessorVector(key);
      succ = getBendingPointSuccessorVector(key);
    } else {
      pre = getSecondBendingPointPredecessorVector(key);
      succ = getSecondBendingPointSuccessorVector(key);
    }
     
    Line2D line = new Line2D.Double(pre, succ);
    double distSq = line.ptSegDist(bendingPoints.get(key).getCenterVector());
    if (distSq > 16.0) return false;
    
    /* can be removed */
    if(! wasServant) {
      Integer preKey = bendingPoints.lowerKey(key);
      Integer succKey = bendingPoints.higherKey(key);
      removeBendingPointIfServant(preKey);
      removeBendingPointIfServant(succKey);
    }
    removeBendingPoint(key);
    addBendingPoint(key, true, new BendingPointVisual(superGraphics, this));
    return true;
  }
  
  /**
   * Returns the vector of the predecessor of the specified key. If there is no predecessor bending point,
   * the intersection of the <code>PlaceVisual</code> and the line, connecting to the first
   * bending point is returned.
   * @param key the key of the successor of the returned vector
   * @return the predecessor's vector
   */
  private Vector2D getBendingPointPredecessorVector(int key) {
    Integer lower = bendingPoints.lowerKey(key);
    return (lower == null ? getPlaceIntersection() : bendingPoints.get(lower).getCenterVector());
  }
  
  /**
   * Returns the vector of the predecessor of the predecessor of the specified key. 
   * If there is no predecessor bending point or no predecessor of the predecessor,
   * the intersection of the <code>PlaceVisual</code> and the line, connecting to the first
   * bending point is returned.
   * @param key the key of the second successor of the returned vector
   * @return the second predecessor's vector
   */
  private Vector2D getSecondBendingPointPredecessorVector(int key) {
    Integer lower = bendingPoints.lowerKey(key);
    if(lower == null) return getPlaceIntersection();
    lower = bendingPoints.lowerKey(lower);
    return (lower == null ? getPlaceIntersection() : bendingPoints.get(lower).getCenterVector());
  }
  
  /**
   * Returns the vector of the successor of the specified key. If there is no successor bending point,
   * the intersection of the <code>TransitionVisual</code> and the line, connecting to the last
   * bending point is returned.
   * @param key the key of the predecessor of the returned vector
   * @return the successor's vector
   */
  private Vector2D getBendingPointSuccessorVector(int key) {
    Integer higher = bendingPoints.higherKey(key);
    return (higher == null ? getTransitionIntersection() : bendingPoints.get(higher).getCenterVector());
  }

  /**
   * Returns the vector of the successor of the successor of the specified key. 
   * If there is no successor bending point, or no successor of the successor,
   * the intersection of the <code>TransitionVisual</code> and the line, connecting to the last
   * bending point is returned.
   * @param key the key of the second predecessor of the returned vector
   * @return the second successor's vector
   */
  private Vector2D getSecondBendingPointSuccessorVector(int key) {
    Integer higher = bendingPoints.higherKey(key);
    if(higher == null) return getTransitionIntersection();
    higher = bendingPoints.higherKey(higher);
    return (higher == null ? getTransitionIntersection() : bendingPoints.get(higher).getCenterVector());
  }
  
  /**
   * Sets the data of the arc. Invokes an update of the label's content.
   * @param data the data of the {@link ArcCollection} corresponding to the <code>ArcVisual</code>
   */
  private void setArc(ArcCollection arc) {
    this.arc = arc;
    arc.getData().addDataChangeListener(this);
    updateLabelContent();
  }

  /**
   * Returns the {@link ArcCollection} that corresponds to this <code>ArcVisual</code>
   * @return the data
   */
  protected ArcCollection getArc() {
    return arc;
  }

  /**
   * Returns the label of this <code>ArcVisual</code>.
   * @return the label
   */
  protected TextVisual getLabel() {
    return label;
  }
  
  /**
   * Updates the location.
   */
  private void updateLocation() {
    updateBounds();
  }

  /**
   * Sets the bounds to the smallest rectangle that contains the place's and transition's centers.
   */
  private void updateBounds() {
    int x1 = (int) Math.min(placeVisual.getCenterX(), transitionVisual.getCenterX());
    int y1 = (int) Math.min(placeVisual.getCenterY(), transitionVisual.getCenterY());
    int x2 = (int) Math.max(placeVisual.getCenterX(), transitionVisual.getCenterX());
    int y2 = (int) Math.max(placeVisual.getCenterY(), transitionVisual.getCenterY());
    setBounds(x1,y1,x2-x1,y2-y1);
  }
  
  /**
   * Returns the intersection of the arc with the place.
   * @return the intersection of the circle of the place with the line that connects the
   * center of the place with the first bending point
   */
  private Vector2D getPlaceIntersection() {
    Vector2D placeCenter = placeVisual.getCenterVector();
    Vector2D firstPoint;
    if(bendingPoints.isEmpty()) {
      firstPoint = transitionVisual.getCenterVector();
    } else {
      firstPoint = bendingPoints.get(bendingPoints.firstKey()).getCenterVector();
    }
    Ray2D placeToPoint = Ray2D.fromPoints(placeCenter, firstPoint);
    return placeToPoint.circleLeavingIntersection(VisualGlobalValues.placeRadius);
  }
  
  /**
   * Returns the intersection of the arc with the transition.
   * @return the intersection of the rectangle of the transition with the line that connects
   * the center of the transition with the last bending point
   */
  private Vector2D getTransitionIntersection() {
    Vector2D transitionCenter = transitionVisual.getCenterVector();
    Vector2D lastPoint;
    if(bendingPoints.isEmpty()) {
      lastPoint = placeVisual.getCenterVector();
    } else {
      lastPoint = bendingPoints.get(bendingPoints.lastKey()).getCenterVector();
    }
    Ray2D transitionToPoint = Ray2D.fromPoints(transitionCenter, lastPoint);
    return transitionToPoint.rectangleLeavingIntersection(transitionVisual.getWidth(), transitionVisual.getHeight());
  }
  
  /**
   * Draws the <code>ArcVisual</code> as a line from the place to the transition via all bending points.
   * Also it draws an arrow head directly pointing at the target of the arc. The label's position
   * is set along the line according to its relative position.
   * @param status the status of this <code>Visual</code>
   */
  @Override
  public void draw(int status) {
    Vector2D[] line = new Vector2D[bendingPoints.size()+2];
    calculateLineVectorsAndSetLabelPosition(line);
    for(int i=0; i<line.length-1;i++) {
      superGraphics.drawLine((int) line[i].x, (int) line[i].y, (int) line[i+1].x, (int) line[i+1].y);
    }

    if(direction == EnumArcDirection.PT) {
      drawArcHead(superGraphics, Ray2D.fromPoints(line[line.length-1], line[line.length-2]));
    } else {
      drawArcHead(superGraphics, Ray2D.fromPoints(line[0],line[1]));
    }

    label.redraw(status);
  }
  
  /**
   * Calculates the line that connects the place with the transition and that visits all
   * bending points in their order in the tree map. Also, the length of each line segment
   * is calculated, and based on that calculation, the label is repositioned on the line
   * according to its relative position.
   * @param line an array of vectors that has a suitable size (number of bending points
   * + 2 for place and transition), that is filled with the positions of the single points
   * of the line
   */
  private void calculateLineVectorsAndSetLabelPosition(Vector2D[] line) {
    superGraphics.setColor(VisualGlobalValues.arcColor);
    
    double[] lineLength = new double[bendingPoints.size()+1];

    Vector2D placeVec = getPlaceIntersection();
    Vector2D previousVec = placeVec;

    double lineLengthSum = 0.0;
    int i = 0;
    line[i++] = placeVec;
    for(BendingPointVisual bendingPoint : bendingPoints.values()) {
      Vector2D nextVec = bendingPoint.getCenterVector();
      line[i] = nextVec;
      lineLength[i-1] += previousVec.distance(nextVec);
      lineLengthSum += lineLength[i-1];
      i++;
      
      previousVec = nextVec;
    }
    Vector2D transitionVec = getTransitionIntersection();
    
    line[i] = transitionVec;
    lineLength[i-1] += previousVec.distance(transitionVec);
    lineLengthSum += lineLength[i-1];
    
    double absoluteLabelPosition = lineLengthSum * labelPosition;
    lineLengthSum = 0.0;
    for(i=0;i<lineLength.length;i++) {
      if(lineLengthSum + lineLength[i] >= absoluteLabelPosition) {
        double relativeLabelPosition = absoluteLabelPosition - lineLengthSum;
        Ray2D currentLine = Ray2D.fromPoints(line[i], line[i+1]);
        label.setCenter(currentLine.getTargetVector(relativeLabelPosition).toPoint());
        return;
      }
      lineLengthSum += lineLength[i];
    }
  }

  /**
   * Draws an arrow head at the starting point of the specified ray. The ray should point
   * in the opposite direction the resulting arrow head will point at.
   * @param graphics the graphics, the arrow head is drawn onto
   * @param arcHeadOpposite a ray with starting location where the head points to and
   * direction directly opposing the direction of the arrow head
   */
  private static void drawArcHead(Graphics2D graphics, Ray2D arcHeadOpposite) {
    double headSize = VisualGlobalValues.arcHeadSize;
    double headAngle = Math.PI / 8.0;
    Vector2D head1 = arcHeadOpposite.getPositionVector();
    Vector2D head2 = arcHeadOpposite.getRotated(headAngle).getTargetVector(headSize);
    Vector2D head3 = arcHeadOpposite.getRotated(-headAngle).getTargetVector(headSize);
    GeneralPath path = new GeneralPath();
    path.moveTo(head1.x, head1.y);
    path.lineTo(head2.x, head2.y);
    path.lineTo(head3.x, head3.y);
    path.closePath();
    graphics.fill(path);
  }
  
  /**
   * Draws an arc &quot;phantom&quot;, that is an arc that is only connected to one node
   * and a dangling point (for example the mouse pointer). The arc does not have any bending
   * points and it always points towards the dangling point.
   * @param graphics the graphics, the arc phantom is drawn onto
   * @param start the single node ({@link PlaceVisual} or {@link TransitionVisual}, the arc
   * is connected to
   * @param end a point where the arc is pointing at
   */
  public static void drawArcPhantom(Graphics2D graphics, NodeVisual start, Point end) {
    Vector2D endVec = new Vector2D(end);
    Ray2D arcRay = Ray2D.fromPoints(start.getCenterVector(), endVec);
    Vector2D startVec;
    if(start instanceof PlaceVisual) {
      startVec = arcRay.circleLeavingIntersection(VisualGlobalValues.placeRadius);
    } else {
      double width = start.getWidth();
      double height = start.getHeight();
      startVec = arcRay.rectangleLeavingIntersection(width, height);
    }
    graphics.setColor(VisualGlobalValues.arcColor);
    graphics.drawLine((int) startVec.x, (int) startVec.y, (int) endVec.x, (int) endVec.y);
    drawArcHead(graphics, Ray2D.fromPoints(endVec, startVec));
  }

  @Override
  protected void updateOnResize() {
  }

  @Override
  protected void updateOnMove() {
  }

  @Override
  protected void updateOnUserMove() {}

  @Override
  protected void updateOnUserMoveFinished() {}
  
  @Override
  protected void updateOnUserResize() {}
  
  /**
   * Invokes an update of the label's content when the data has changed.
   * @param changedData the changed data
   */
  @Override
  public void dataChanged(Data changedData) {
    updateLabelContent();
  }
  
  protected abstract void updateLabelContent();
  
  /**
   * Moves a bending point if it was not changed by the user (that is, it is a servant).
   * If this is the case, the bending point is positioned to the exact middle point between
   * its neighbors.
   * @param key the key of the bending point that is to move
   */
  private void moveServantBendingPoint(Integer key) {
    if(key == null) return;
    BendingPointVisual v = bendingPoints.get(key);
    if(v.isMaster()) return;
    Vector2D pre = getBendingPointPredecessorVector(key);
    Vector2D succ = getBendingPointSuccessorVector(key);
    v.setCenter(Vector2D.middlePoint(pre, succ).toPoint());
  }
  
  /**
   * Moves the neighbors of a bending point, if these are servants.
   * @param key the key of the bending point whose neighbors are to be moved
   */
  private void moveBendingPointServantNeighbors(int key) {
    moveServantBendingPoint(bendingPoints.lowerKey(key));
    moveServantBendingPoint(bendingPoints.higherKey(key));
  }
  
  /**
   * This method should be called, when a bending point changed its location due to user interaction.
   * It checks whether the bending point can be removed and replaced by a servant. Also, if the
   * moved bending point was a servant before, it is now a master, and it gets two new neighboring
   * servants. If it was not a servant before, it already has its own servants which are
   * moved accordingly.
   * @param changedPoint the bending point that changed its position due to user interaction
   * @param wasServant <code>true</code> if the bending point was moved the first time by the
   * user, and thus, it recently was a servant
   */
  protected void bendingPointChangedLocationByUser(BendingPointVisual changedPoint, boolean wasServant) {
    int key = changedPoint.getKey();
    boolean removed = checkIfBendingPointCanBeRemoved(key, wasServant);
    if(wasServant & ! removed) {
      addBendingPoint(key, true, new BendingPointVisual(superGraphics, this));
      addBendingPoint(key, false, new BendingPointVisual(superGraphics, this));
    } else {
      moveBendingPointServantNeighbors(key);
    }
  }
  
  /**
   * This method should be called, when a bending point changed its location. It moves the
   * servant neighbors of the point accordingly, if there are any.
   * @param changedPoint the bending point that changed its location
   */
  protected void bendingPointChangedLocation(BendingPointVisual changedPoint) {
    moveBendingPointServantNeighbors(changedPoint.getKey());
  }
  
  
  /**
   * This method should be called, when the place changed its location. It moves the
   * servant neighbors of the place accordingly, if there are any.
   */
  protected void placeChangedLocation() {
    moveServantBendingPoint(bendingPoints.firstKey());
  }
  
  /**
   * This method should be called, when the transition changed its location. It moves the
   * servant neighbors of the transition accordingly, if there are any.
   */
  protected void transitionChangedLocation() {
    moveServantBendingPoint(bendingPoints.lastKey());
  }
  
  /**
   * Returns the shape of this <code>ArcVisual</code>. The shape is composed of the shapes
   * of all bending points.
   * @return a shape that is the composition of the shapes of all bending points on this arc
   */
  @Override
  public Shape getShape() {
    GeneralPath path = new GeneralPath();
    for(Visual v : bendingPoints.values()) {
      path.append(v.getShape(), true);
    }
    return path;
  }

  /**
   * Destroys this <code>ArcVisual</code>. It destroys the {@link Visual} and de-registers
   * itself from the {@link PlaceVisual} and {@link TransitionVisual} it was connected to.
   */
  @Override
  protected void destroy() {
    super.destroy();
    placeVisual.removeConnectedArc(this);
    transitionVisual.removeConnectedArc(this);
  } 
}
