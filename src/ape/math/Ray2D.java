/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.math;

/**
 * This class provides a representation of two-dimensional rays, having a (start) location
 * vector and a normalized direction vector. Since there may arise confusion about the
 * meaning of a vector with respect to a ray, there is no public constructor for obtaining
 * a ray from specific vectors. Instead, one has to use one of the static functions, provided
 * by this class, such as {@link Ray2D#fromPositionAndNormalDirection(ape.math.Vector2D, ape.math.Vector2D)},
 * {@link Ray2D#fromPositionAndDirection(ape.math.Vector2D, ape.math.Vector2D)}
 * and {@link Ray2D#fromPoints(ape.math.Vector2D, ape.math.Vector2D)} to create a new
 * ray from a vectorial description.
 * @author Gabriel
 * @see Vector2D
 */
public class Ray2D {
  
  /**
   * The start position of this ray.
   */
  protected Vector2D positionVector;
  
  /**
   * The normalized direction vector of this ray.
   */
  protected Vector2D normalVector;
  
  /**
   * A ray describing the x axis of the coordinate system. The ray has its starting point
   * at the {@link Vector2D#zero} vector and the {@link Vector2D#baseX} vector as direction
   * vector.
   */
  public static final Ray2D baseX = new Ray2D(Vector2D.zero, Vector2D.baseX);
  
  /**
   * A ray describing the y axis of the coordinate system. The ray has its starting point
   * at the {@link Vector2D#zero} vector and the {@link Vector2D#baseY} vector as direction
   * vector.
   */
  public static final Ray2D baseY = new Ray2D(Vector2D.zero, Vector2D.baseY);
  
  /**
   * A constructor for a ray created from a position and direction vector.
   * The normal vector should in any case be of length <code>1.0</code>, otherwise the
   * ray may not behave right.
   * The constructor is private, in order to prevent confusion that may arise from different
   * interpretations of the role of a vector (as point or direction).
   * @param positionVector the start position of the ray given as a vector
   * @param normalVector the normalized direction vector of this ray with length <code>1.0</code>
   */
  private Ray2D(Vector2D positionVector, Vector2D normalVector) {
    this.positionVector = positionVector;
    this.normalVector = normalVector;
  }
  
  /**
   * A constructor for a ray created from another ray. The new ray has the same start position
   * and direction as the argument.
   * @param ray a ray to provide a position and direction for this ray
   */
  public Ray2D(Ray2D ray) {
    this.positionVector = new Vector2D(ray.positionVector);
    this.normalVector = new Vector2D(ray.normalVector);
  }
  
  /**
   * Creates a new ray from a (start) position vector and a normalized direction vector.
   * The normal vector should in any case be of length <code>1.0</code>, otherwise the
   * ray may not behave right.
   * @param position the start position of the ray given as a vector
   * @param normal the normalized direction vector of this ray with length <code>1.0</code>
   * @return a new ray with specified start position and direction
   * @see Ray2D#fromPositionAndDirection(ape.math.Vector2D, ape.math.Vector2D) 
   */
  public static Ray2D fromPositionAndNormalDirection(Vector2D position, Vector2D normal) {
    return new Ray2D(position, normal);
  }
  
  
  /**
   * Creates a new ray from a (start) position vector and an arbitrary direction vector.
   * The direction of the ray is set to be the normalization of the specified direction vector.
   * The direction vector does not have to be normalized. If one can be sure to have a normalized
   * direction vector, one should use {@link Ray2D#fromPositionAndNormalDirection(ape.math.Vector2D, ape.math.Vector2D)}
   * instead, because it is cheaper.
   * @param position the start position of the ray given as a vector
   * @param direction a direction vector for this ray
   * @return a new ray with specified start position and direction
   * @see Ray2D#fromPositionAndNormalDirection(ape.math.Vector2D, ape.math.Vector2D) 
   * @see Vector2D#normalize() 
   * @see Vector2D#getNormalVector() 
   */
  public static Ray2D fromPositionAndDirection(Vector2D position, Vector2D direction) {
    Vector2D normal = direction.getNormalVector();
    return new Ray2D(position, normal);
  }
  
  /**
   * Creates a new ray from a (start) position vector and a point vector, lying on
   * the ray. The point vector <code>onRay</code> should be distinct from the start position
   * vector, to ensure that the direction of the ray is uniquely determined. Otherwise,
   * the direction of the ray is the normalization of the {@link Vector2D#zero} vector
   * as defined in {@link Vector2D#normalize()}.
   * @param start the start position of the ray given as vector
   * @param onRay a point given as vector, the ray should pass through
   * @return a new ray starting at <code>start</code> and going through <code>onRay</code>
   * @see Vector2D#normalize() 
   * @see Ray2D#fromPositionAndDirection(ape.math.Vector2D, ape.math.Vector2D) 
   */
  public static Ray2D fromPoints(Vector2D start, Vector2D onRay) {
    Vector2D direction = new Vector2D(onRay).subtract(start);
    return fromPositionAndDirection(start, direction);
  }
  
  /**
   * Returns a copy of the position vector of this ray.
   * @return a vector with the same coordinates as the position vector of this ray
   */
  public Vector2D getPositionVector() {
    return new Vector2D(positionVector);
  }
  
  /**
   * Returns a copy of the normal direction vector of this ray.
   * @return a vector pointing in the direction of this ray with length <code>1.0</code>
   */
  public Vector2D getNormalDirectionVector() {
    return new Vector2D(normalVector);
  }
  
  /**
   * Returns a vector representing a point on this ray at the specified distance outgoing
   * from this ray's position vector.
   * @param distance the scalar distance of the target from the position vector of the ray along
   * its direction
   * @return the target vector of ray at the specified distance
   */
  public Vector2D getTargetVector(double distance) {
    return getNormalDirectionVector().multiply(distance).add(positionVector);
  }

  /**
   * Translates the ray by translating its position vector.
   * @param dx the horizontal delta of the translation
   * @param dy the vertical delta of the translation
   * @return this translated ray for convenience
   */
  public Ray2D translate(double dx, double dy) {
    positionVector.translate(dx, dy);
    return this;
  }
  
  /**
   * Translates the ray by the <code>x</code> and <code>y</code> values of the specified
   * vector.
   * @param dv the vector containing <code>x</code> and <code>y</code> values this
   * rays is to be translated by
   * @return this translated ray for convenience
   */
  public Ray2D translate(Vector2D dv) {
    positionVector.add(dv);
    return this;
  }
  
  /**
   * Translates the ray horizontally by translating its position vector.
   * @param dx the horizontal delta of the translation
   * @return this translated ray for convenience
   */
  public Ray2D translateX(double dx) {
    positionVector.translateX(dx);
    return this;
  }

  /**
   * Translates the ray vertically by translating its position vector.
   * @param dy the vertical delta of the translation
   * @return this translated ray for convenience
   */
  public Ray2D translateY(double dy) {
    positionVector.translateY(dy);
    return this;
  }

  /**
   * Inverts the direction of the ray by negating its normal direction vector.
   * @return this inverted ray for convenience
   * @see Ray2D#getInverted() 
   */
  public Ray2D invert() {
    normalVector.negate();
    return this;
  }
  
  /**
   * Returns a ray starting at the same position as this ray, but going in the opposite direction.
   * The method does not change this ray.
   * @return an inverted copy of this ray
   * @see Ray2D#invert() 
   */
  public Ray2D getInverted() {
    return new Ray2D(positionVector, normalVector.getNegation());
  }
  
  /**
   * Returns a ray starting at the specified distance on this ray, but going in the opposite direction.
   * The method does not change this ray.
   * @param dist the distance on this ray, where the returned ray has its starting point
   * @return a ray opposing this ray, and starting at the specified distance on this ray
   */
  public Ray2D getOppositeFrom(double dist) {
    return new Ray2D(getTargetVector(dist), normalVector.getNegation());
  }

  /**
   * Rotates this ray around its start point by the specified angle.
   * @param theta the rotation angle in radian measure
   * @return this rotated ray for convenience
   * @see Ray2D#getRotated(double) 
   * @see Ray2D#rotateAroundOrigin(double) 
   * @see Ray2D#rotateAround(ape.math.Vector2D, double) 
   */
  public Ray2D rotate(double theta) {
    normalVector.rotate(theta);
    return this;
  }
  
  /**
   * Returns a copy of this ray that is rotated by the specified angle around the
   * start point of this ray. The method does not change this ray.
   * @param theta the rotation angle in radian measure
   * @return a rotated copy of this ray
   * @see Ray2D#rotate(double) 
   */
  public Ray2D getRotated(double theta) {
    return new Ray2D(this).rotate(theta);
  }
  
  /**
   * Rotates this ray around the coordinate origin <code>(0,0)</code> by the specified angle.
   * This involves rotating the position as well as the direction vector.
   * @param theta the rotation angle in radian measure
   * @return this rotated ray for convenience
   * @see Ray2D#rotate(double) 
   * @see Ray2D#getRotated(double) 
   * @see Ray2D#rotateAround(ape.math.Vector2D, double) 
   */
  public Ray2D rotateAroundOrigin(double theta) {
    positionVector.rotate(theta);
    normalVector.rotate(theta);
    return this;
  }
  
  /**
   * Rotates this ray around the specified rotation center point by the specified angle.
   * This involves rotating the position as well as the direction vector.
   * @param rotationCenter the point vector around which this ray should be rotated
   * @param theta the rotation angle in radian measure
   * @return this rotated ray for convenience
   * @see Ray2D#rotate(double) 
   * @see Ray2D#getRotated(double) 
   * @see Ray2D#rotateAroundOrigin(double) 
   */
  public Ray2D rotateAround(Vector2D rotationCenter, double theta) {
    positionVector.subtract(rotationCenter);
    rotateAroundOrigin(theta);
    positionVector.add(rotationCenter);
    return this;
  }
    
  /**
   * Returns the location as a vector, where this ray leaves a circle with specified radius
   * that has its center point exactly at the starting point of the ray. The method returns
   * the target vector of this ray at the distance of the radius.
   * @param radius the radius of the circle
   * @return the intersection of this ray and the circle as a vector
   * @see Ray2D#getTargetVector(double) 
   * @see Ray2D#rectangleLeavingIntersection(double, double) 
   */
  public Vector2D circleLeavingIntersection(double radius) {
    return getTargetVector(radius);
  }
  
  
  /**
   * Returns the distance on this ray, where this ray leaves a rectangle with specified width
   * and height that has its center exactly located at the starting point of this ray.
   * The method checks the distances to each of the four surrounding lines and returns
   * the minimal value.
   * @param width the width of the surrounding rectangle
   * @param height the height of the surrounding rectangle
   * @return the distance to the intersection of this ray and the rectangle
   * @see Ray2D#getTargetVector(double) 
   * @see Ray2D#rectangleLeavingIntersection(double, double) 
   */
  public double rectangleLeavingDistance(double width, double height) {
    Vector2D absRectCorner = new Vector2D(width / 2.0, height / 2.0);
    Vector2D absVec = normalVector.getAbsoluteVector();
    
    double distX = Double.POSITIVE_INFINITY;
    double distY = Double.POSITIVE_INFINITY;
    
    if(absVec.x != 0.0) {
      distX = absRectCorner.x / absVec.x;
    }
    if(absVec.y != 0.0) {
      distY = absRectCorner.y / absVec.y;
    }
    
    return Math.min(distX, distY);
  }
  
  /**
   * Returns the location as a vector, where this ray leaves a rectangle with specified width
   * and height that has its center exactly located at the starting point of this ray.
   * The method checks the minimal distance to one of the four surrounding lines and returns
   * the target vector of this ray for that distance.
   * @param width the width of the surrounding rectangle
   * @param height the height of the surrounding rectangle
   * @return the intersection of this ray and the rectangle as a vector
   * @see Ray2D#rectangleLeavingDistance(double, double) 
   * @see Ray2D#circleLeavingIntersection(double) 
   */
  public Vector2D rectangleLeavingIntersection(double width, double height) {
    return getTargetVector(rectangleLeavingDistance(width, height));
  }

  @Override
  public String toString() {
    return "Ray2D[" + positionVector + "," + normalVector + "]";
  }
}
