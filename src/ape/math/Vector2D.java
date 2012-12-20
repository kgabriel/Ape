/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.math;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * This class provides a representation of two-dimensional vectors as
 * a pair of <code>x</code> and <code>y</code> values stored as <code>double</code>s.
 * @author Gabriel
 */
public class Vector2D extends Point2D.Double {
  
  /**
   * The base vector <code>(1,0)</code> of the x-axis. 
   */
  public static final Vector2D baseX = new Vector2D(1.0, 0.0);

  /**
   * The base vector <code>(0,1)</code> of the y-axis.
   */
  public static final Vector2D baseY = new Vector2D(0.0, 1.0);
  
  /**
   * The zero vector <code>(0,0)</code>.
   */
  public static final Vector2D zero = new Vector2D(0.0, 0.0);
  
  /**
   * A new Vector with specified <code>x</code> and <code>y</code> values.
   * @param x the <code>x</code> value of the vector
   * @param y the <code>y</code> value of the vector 
   */
  public Vector2D(double x, double y) {
    super(x,y);
  }
  
  /**
   * A new Vector with the same coordinates as the specified vector.
   * @param vec the vector providing coordinates for this vector
   */
  public Vector2D(Vector2D vec) {
    this(vec.x,vec.y);
  }
  
  /**
   * A new Vector equal to {@link Vector2D#zero}. 
   */
  public Vector2D() {
    this(0.0, 0.0);
  }
 
  /**
   * A new Vector with <code>x</code> and <code>y</code> values of the specified
   * {@link Point2D}.
   * @param p the point providing coordinates for this vector
   */
  public Vector2D(Point2D p) {
    this(p.getX(),p.getY());
  }
  
  /**
   * Translates this vector by the specified values.
   * @param dx the horizontal delta of the translation
   * @param dy the vertical delta of the translation
   * @return this translated vector for convenience
   */
  public Vector2D translate(double dx, double dy) {
    setLocation(x + dx, y + dy);
    return this;
  }
  
  /**
   * Translates this vector horizontally.
   * @param dx the horizontal delta of this translation
   * @return this translated vector for convenience
   */
  public Vector2D translateX(double dx) {
    return translate(dx, 0.0);
  }
  
  /**
   * Translates this vector vertically.
   * @param dy the vertical delta of this translation
   * @return this translated vector for convenience
   */
  public Vector2D translateY(double dy) {
    return translate(0.0, dy);
  }
  
  /**
   * Adds another vector to this vector. As result, the specified vector's components 
   * are added to this vector's components.
   * @param vec the vector to add to this vector
   * @return this vector after addition for convenience
   */
  public Vector2D add(Vector2D vec) {
    translate(vec.x, vec.y);
    return this;
  }
  
  /**
   * Subtracts another vector from this vector. As result, the specified vector's component's
   * are subtracted from this vector's components.
   * @param vec the vector to subtract
   * @return this vector after subtraction for convenience
   */
  public Vector2D subtract(Vector2D vec) {
    translate(-vec.x, -vec.y);
    return this;
  }
  
  /** 
   * Negates this vector by additive negation of its components.
   * @return this negated vector for convenience
   */
  public Vector2D negate() {
    x = -x;
    y = -y;
    return this;
  }
  
  /**
   * Returns the additive inverse <code>(-x,-y)</code> of this vector. The method does
   * not change this vector.
   * @return the negation of this vector.
   */
  public Vector2D getNegation() {
    return new Vector2D(-x, -y);
  }
  
  /**
   * Multiplies this vector with a specified scalar. This means that this vector's components
   * are both multiplied with the scalar.
   * @param scalar the scalar to multiply with this vector
   * @return this vector after multiplication for convenience
   */
  public Vector2D multiply(double scalar) {
    x *= scalar;
    y *= scalar;
    return this;
  }
  
  /**
   * Returns the distance of this vector from the coordinate origin {@link Vector2D#zero}.
   * @return the length of this vector
   */
  public double length() {
    return zero.distance(this);
  }
  
  /**
   * Returns the squared distance of this vector from the coordinate origin {@link Vector2D#zero}.
   * @return the squared length of this vector
   */
  public double lengthSq() {
    return zero.distanceSq(this);
  }
  
  /**
   * Returns the horizontal distance of this vector from the coordinate origin {@link Vector2D#zero}.
   * @return the absolute value of this vector's <code>x</code> value
   */
  public double lengthX() {
    return Math.abs(x);
  }
  
  /**
   * Returns the vertical distance of this vector from the coordinate origin {@link Vector2D#zero}.
   * @return the absolute value of this vector's <code>y</code> value
   */
  public double lengthY() {
    return Math.abs(y);
  }
  
  /**
   * Normalizes this vector to have a length of <code>1.0</code>. If this vector is <code>zero</code>,
   * that is, it has coordinates <code>(0,0)</code>, then its normalization yields the
   * base vector <code>(1,0)</code> of the x axis.
   * @return this normalized vector for convenience
   * @see Vector2D#getNormalVector() 
   */
  public Vector2D normalize() {
    /* we define (1,0) being the normal of (0,0), to prevent division by zero */
    if(x == 0.0 && y == 0.0) {
      x = 1.0;
      y = 0.0;
    }
    double abs = length();
    x /= abs;
    y /= abs;
    return this;
  }
  
  /**
   * Returns the normal vector of this vector. The normal vector is a vector with the same direction,
   * but with length <code>1.0</code>. The method does not change the values of this vector.
   * In fact, the method creates a copy of this vector and calls the {@link Vector2D#normalize()} 
   * method of the copy.
   * @return the normal vector of this vector
   * @see Vector2D#normalize() 
   */
  public Vector2D getNormalVector() {
    return new Vector2D(this).normalize();
  }
  
  /**
   * Rotates this vector by the specified angle.
   * @param theta the rotation angle in radian measure
   * @return this rotated vector for convenience
   * @see Vector2D#getRotatedVector(double) 
   */
  public Vector2D rotate(double theta) {
    AffineTransform trafo = AffineTransform.getRotateInstance(theta);
    trafo.transform(this, this);
    return this;
  }
  
  /**
   * Returns a version of this vector, rotated by the specified angle.
   * @param theta the rotation angle in radian measure
   * @return a rotated version of this vector rotated by <code>theta</code>
   * @see Vector2D#rotate(double) 
   */
  public Vector2D getRotatedVector(double theta) {
    return new Vector2D(this).rotate(theta);
  }
  
  /**
   * Sets the <code>x</code> and <code>y</code> coordinates of this vector to their (positive)
   * absolute values. This has the effect that the vector will in any case lie inside
   * the first quadrant of the coordinate system.
   * @return this vector with positive <code>x</code> and <code>y</code> values for convenience
   */
  public Vector2D abs() {
    x = Math.abs(x);
    y = Math.abs(y);
    return this;
  }
  
  /**
   * Returns a new vector, having the absolute values of the <code>x</code> and <code>y</code> coordinates 
   * of this vector as its coordinates. This has the effect that the new 
   * vector will in any case lie inside the first quadrant of the coordinate system.
   * @return a new vector with positive absolute <code>x</code> and <code>y</code> values 
   * computed from the <code>x</code> and <code>y</code> values of this vector
   */
  public Vector2D getAbsoluteVector() {
    return new Vector2D(this).abs();
  }
  
  /**
   * Returns a point that lies right in the middle between the two specified points.
   * @param point1 the first point
   * @param point2 the second point
   * @return a point exactly halfway between <code>point1</code> and <code>point2</code>
   */
  public static Vector2D middlePoint(Vector2D point1, Vector2D point2) {
    Ray2D from1To2 = Ray2D.fromPoints(point1, point2);
    double halfDist = point1.distance(point2) / 2.0;
    return from1To2.getTargetVector(halfDist);
  }
  
  /**
   * Returns a {@link Point} with rounded values of this vector.
   * @return a point with coordinates <code>(round(x), round(y))</code>
   */
  public Point toPoint() {
    return new Point((int) Math.round(x), (int) Math.round(y));
  }

  @Override
  public String toString() {
    return "Vector2D[" + x + "," + y + "]";
  }
}
