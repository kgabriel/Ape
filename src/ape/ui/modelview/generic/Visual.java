/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.modelview.generic;

import ape.math.Vector2D;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * This abstract class is the superclass of all visual net components.
 * All resizing and moving operations on visual net components are encapsulated in this class,
 * ensuring that every <code>Visual</code> is operating on the right portion of
 * its superior {@link java.awt.Graphics2D} object, and that the <code>Visual</code>
 * is aware of all of these operations.
 * @author Gabriel
 */
public abstract class Visual implements Serializable {
  
  public final static int STATUS_NORMAL = 0;
  public final static int STATUS_HOVER = 1;
  public final static int STATUS_SELECTED = 2;
  public final static int STATUS_PARENT_HOVER = 4;
  public final static int STATUS_PARENT_SELECTED = 8;
  
  /** The outer bounds of the visual component. */
  private Rectangle bounds;
  
  /** The graphics element from which this <code>Visual</code> gets its own graphics. This should not be used
   * directly. It is only stored to get passed along to subcomponents of this <code>Visual</code>, 
   * like for example the label orbiting a {@link PlaceVisual} or the text elements in a
   * {@link TransitionVisual}.
   */
  protected transient Graphics2D superGraphics;
  
  /**
   * The graphics of this element. It is used in the {@link Visual#draw(int)} method
   * of subclasses to draw the <code>Visual</code>. The graphics is always adapted
   * when the bounds of this <code>Visual</code> change, and it always refers to the
   * portion of the {@link Visual#superGraphics} that corresponds to the <code>Visual</code>'s
   * bounds. 
   */
  protected transient Graphics2D graphics;
  
  /** A boolean value, whether the visual component is horizontally resizable by user input.
   * It does not in any way affect the behavior of the usual resizing methods of this class.
   * It only affects the behavior of the special resizing methods
   * {@link Visual#setSizeIfPossible(int, int)} and {@link Visual#setSizeIfPossible(java.awt.Dimension)}.
   */
  protected boolean resizableX;
  
  /** A boolean value, whether the visual component is vertically resizable by user input.
   * It does not in any way affect the behavior of resizing methods of this class.
   * It only affects the behavior of the special resizing methods
   * {@link Visual#setSizeIfPossible(int, int)} and {@link Visual#setSizeIfPossible(java.awt.Dimension)}.
   */
  protected boolean resizableY;
  
  /** A boolean value, whether the visual component can be moved by user input.
   * It does not in any way affect the behavior of usual moving operations of this class.
   * It only affects the behavior of the special moving methods
   * {@link Visual#setLocationIfPossible(int, int)}, {@link Visual#setLocationIfPossible(java.awt.Point)}
   * and {@link Visual#translateIfPossible(int, int)}.
   */
  protected boolean movable;
  
  /**
   * A boolean value, whether this visual component can be selected by the user.
   * Usually, this is set to false for subcomponents that are completely contained in another
   * selectable visual, or, for components with selectable subcomponents, like the
   * {@link ArcVisual}.
   */
  protected boolean selectable;
  
  /**
   * A boolean value, whether this visual is hidden. If this is set to true, the component
   * will not be drawn when {@link Visual#redraw(int)} is called.
   */
  protected boolean hidden;
  
  /**
   * The child visuals of this visual.
   * 
   */
  private Collection<Visual> childVisuals;
  private Collection<Visual> selectableChildVisuals;
  
  
  private java.util.List<VisualProperty> properties;
  
  /**
   * Construct a new <code>Visual</code> with the specific superior 
   * {@link java.awt.Graphics2D}. The visual has a zero-size
   * and is positioned in the top-left corner. The visual is fully resizable by the
   * user.
   * @param superGraphics the graphics from which this <code>Visual</code> obtains its
   * own graphics
   */
  public Visual(Graphics2D superGraphics) {
    this(superGraphics, new Rectangle(0,0,0,0));
  }
  
  /**
   * Construct a new <code>Visual</code> with the specific superior 
   * {@link java.awt.Graphics2D} and bounds.
   * By default, the visual is fully resizable, movable and selectable by the user,
   * and not hidden.
   * @param superGraphics the graphics from which this <code>Visual</code> obtains its
   * own graphics
   * @param bounds the location and size of this <code>Visual</code>
   */
  public Visual(Graphics2D superGraphics, Rectangle bounds) {
    this.bounds = bounds;
    this.childVisuals = new HashSet<>();
    this.selectableChildVisuals = new HashSet<>();
    this.properties = new ArrayList<>();
    setSuperGraphics(superGraphics);
    this.resizableX = true;
    this.resizableY = true;
    this.movable = true;
    this.selectable = true;
    this.hidden = false;
    initProperties();
  }
  
  protected final void setSuperGraphics(Graphics2D superGraphics) {
    this.superGraphics = superGraphics;
    clipToBounds();
    for(Visual child : childVisuals) {
      child.setSuperGraphics(superGraphics);
    }
  }
  
  protected void addChild(Visual v, boolean selectable) {
    childVisuals.add(v);
    if(selectable) selectableChildVisuals.add(v);
  }
  
  protected void addChild(Visual v) {
    addChild(v,false);
  }
  
  protected void removeChild(Visual v) {
    childVisuals.remove(v);
    selectableChildVisuals.remove(v);
  }
  
  public boolean hasSelectableChildren() {
    return ! selectableChildVisuals.isEmpty();
  }
  
  protected Collection<Visual> getSelectableChildren() {
    return new HashSet<>(selectableChildVisuals);
  }
  
  protected void addProperty(VisualProperty prop) {
    properties.add(prop);
  }
  
  protected void addPropertyInFront(VisualProperty prop) {
    properties.add(0, prop);
  }
  
  public Collection<VisualProperty> getProperties() {
    return properties;
  }
  
  private void initProperties() {
    addProperty(new VisualPropertyConstant(this, EnumVisualPropertyType.String, "Element Type", getElementTypeName()));
  }
  
  public void redraw(int status) {
    if(! hidden) draw(status);
  }
  
  /**
   * This abstract method should be overwritten by every subclass. It is called to
   * draw the <code>Visual</code> on its {@link java.awt.Graphics2D}.
   */
  public abstract void draw(int status);
  
  /**
   * This method is always called on resizing the component. Note that this does not happen
   * on every call of a method that is capable of resizing the component, but only if the
   * size of the component has actually changed.
   */
  protected abstract void updateOnResize();
  
  /**
   * This method is always called on moving the component. Note that this does not happen
   * on every call of a method that is capable of moving the component, but only if the
   * location of the component has actually changed.
   */
  protected abstract void updateOnMove();
  
  /**
   * This method is always called on resizing the component by the user. This method is always called 
   * when {@link Visual#setSizeIfPossible(java.awt.Dimension)} or
   * {@link Visual#setSizeIfPossible(int, int)} was called. Note that these methods
   * also invoke {@link Visual#updateOnResize()} if the size of this visual actually changed.
   */
  protected abstract void updateOnUserResize();
  
  /**
   * This method is always called on moving the component by the user. This method is always called 
   * when {@link Visual#setLocationIfPossible(java.awt.Point)} or
   * {@link Visual#setLocationIfPossible(int, int)} was called. Note that these methods
   * also invoke {@link Visual#updateOnMove()} if the location of this visual actually changed.
   */
  protected abstract void updateOnUserMove();
  
  /**
   * This method is always called when a user action moving this component by the user has ended.
   * The caller of this method is the corresponding {@link ModelView}.
   */
  protected abstract void updateOnUserMoveFinished();
  
  /**
   * This method should always be called, when this visual is completely removed. Subclasses
   * of visuals can override this method to clean up before destruction. The implementation
   * in this class disposes the graphics object of the visual.
   */
  protected void destroy() {
    graphics.dispose();
  }

  /**
   * Updates the {@link java.awt.Graphics2D} object {@link Visual#graphics} to be exactly
   * the portion of {@link Visual#superGraphics} that corresponds to its bounds.
   */
  protected void clipToBounds() {
    if(superGraphics == null) return;
    if(this.graphics != null) graphics.dispose();
    this.graphics = (Graphics2D) superGraphics.create(bounds.x, bounds.y, bounds.width, bounds.height);
  }

  /**
   * Returns whether this <code>Visual</code> is horizontally resizable through user action.
   * @return the {@link Visual#resizableX} value of this <code>Visual</code>
   * @see Visual#resizableX
   * @see Visual#setResizableX(boolean) 
   */
  public boolean isResizableX() {
    return resizableX;
  }

  /**
   * Sets whether this <code>Visual</code> is horizontally resizable through user action.
   * @param resizableX <code>true</code> if this component should be horizontally resizable by the user,
   * otherwise <code>false</code>
   * @see Visual#resizableX
   * @see Visual#isResizableX() 
   * @see Visual#setResizableX(boolean) 
   * @see Visual#setResizableY(boolean) 
   */
  public void setResizableX(boolean resizableX) {
    this.resizableX = resizableX;
  }

  /**
   * Returns whether this <code>Visual</code> is vertically resizable through user action.
   * @return the {@link Visual#resizableY} value of this <code>Visual</code>
   * @see Visual#resizableY
   * @see Visual#isResizableX() 
   * @see Visual#setResizableY(boolean) 
   */
  public boolean isResizableY() {
    return resizableY;
  }

  /**
   * Sets whether this <code>Visual</code> is vertically resizable through user action.
   * @param resizableY <code>true</code> if this component should be vertically resizable by the user,
   * otherwise <code>false</code>
   * @see Visual#resizableX
   * @see Visual#resizableY
   * @see Visual#isResizableX() 
   * @see Visual#isResizableY() 
   * @see Visual#setResizableX(boolean) 
   * @see Visual#setResizable(boolean, boolean) 
   */
  public void setResizableY(boolean resizableY) {
    this.resizableY = resizableY;
  }
  
  /**
   * Sets whether this <code>Visual</code> is resizable through user action.
   * @param horizontally <code>true</code> if this component should be horizontally resizable by the user
   * @param vertically <code>true</code> if this component should be vertically resizable by the user,
   * otherwise <code>false</code>
   * @see Visual#resizableX
   * @see Visual#resizableY
   * @see Visual#isResizableX() 
   * @see Visual#isResizableY() 
   * @see Visual#setResizableX(boolean) 
   * @see Visual#setResizableY(boolean) 
   */
  public void setResizable(boolean horizontally, boolean vertically) {
    setResizableX(horizontally);
    setResizableY(vertically);
  }

  /**
   * Returns whether this <code>Visual</code> is set to be moved through user action.
   * @return <code>true</code> if this component should be movable by the user
   * @see Visual#movable
   * @see Visual#setMovable(boolean) 
   * @see Visual#setLocationIfPossible(java.awt.Point) 
   * @see Visual#setLocationIfPossible(int, int) 
   */
  public boolean isMovable() {
    return movable;
  }
  
  public boolean isSelectable() {
    return selectable;
  }
  
  public void setSelectable(boolean selectable) {
    this.selectable = selectable;
  }

  /**
   * Sets whether this <code>Visual</code> can be moved through user action.
   * @param movable <code>true</code> if this component should be movable by the user,
   * @see Visual#movable
   * @see Visual#isMovable() 
   * @see Visual#setLocationIfPossible(java.awt.Point) 
   * @see Visual#setLocationIfPossible(int, int) 
   */
  public void setMovable(boolean movable) {
    this.movable = movable;
  }

  /**
   * Translates this component by the given values. It basically adds the given values
   * to the current location of the component's bounds.
   * @param dx the <code>x</code> value to add to this component's <code>x</code> value
   * @param dy the <code>y</code> value to add to this component's <code>y</code> value
   */
  public void translate(int dx, int dy) {
    setLocation(bounds.x + dx, bounds.y + dy);
  }
    
  /**
   * Returns whether the given width and height are different from this component's width
   * and height, and thus, setting these values would resize the component.
   * @param width a new width for this component
   * @param height a new height for this component
   * @return <code>true</code> if the specified width or height differs from this
   * component's width or height, respectively
   */
  private boolean isSizeChanging(int width, int height) {
    return (width != bounds.width || height != bounds.height);
  }

  /**
   * Sets the size of this component to the specified width and height.
   * @param width the width to be set
   * @param height the height to be set
   * @see Visual#setSizeIfPossible(int, int) 
   */
  public void setSize(int width, int height) {
    if(! isSizeChanging(width, height)) return;
    bounds.setSize(width, height);
    clipToBounds();
    updateOnResize();
  }

  /**
   * Sets the size of this component to the specified dimension.
   * @param d the new width and height to be set
   * @see Visual#setSize(int, int) 
   * @see Visual#setSizeIfPossible(java.awt.Dimension) 
   */
  public void setSize(Dimension d) {
    setSize(d.width,d.height);
  }
  
  /**
   * Sets the size of this component to the specified width and height,
   * taking into account if the component is resizable. If <code>resizableX</code>
   * is set to <code>false</code>, the width remains unchanged. The same holds for
   * <code>resizableY</code> and the height.
   * @param width the new width of this visual, if <code>resizableX</code> is <code>true</code>
   * @param height the new height of this visual, if <code>resizableY</code> is <code>true</code> 
   * @see Visual#resizableX
   * @see Visual#resizableY
   * @see Visual#isResizableX() 
   * @see Visual#isResizableY() 
   * @see Visual#setResizable(boolean, boolean) 
   * @see Visual#setResizableX(boolean) 
   * @see Visual#setResizableY(boolean) 
   * @see Visual#setSize(int, int) 
   */
  public void setSizeIfPossible(int width, int height) {
    if(! resizableX) width = bounds.width;
    if(! resizableY) height = bounds.height;
    setSize(width, height);
    updateOnUserResize();
  }
  
  /**
   * Sets the size of this component to the specified width and height,
   * taking into account if the component is resizable. If <code>resizableX</code>
   * is set to <code>false</code>, the width remains unchanged. The same holds for
   * <code>resizableY</code> and the height.
   * @param d the new dimension to set for this visual
   * @see Visual#setSizeIfPossible(int, int) 
   */
  public void setSizeIfPossible(Dimension d) {
    setSizeIfPossible(d.width, d.height);
  }
  
  /** Returns whether the given position is different from this component's location,
   * and thus, setting these values would move the component.
   * @param x a new x coordinate for this component
   * @param y a new y coordinate for this component
   * @return <code>true</code> if the specified position is horizontally or vertically
   * different from this component's location
   */
  private boolean isMoving(int x, int y) {
    return (x != bounds.x || y != bounds.y);
  }

  /** Sets the location of this component.
   * @param x the new horizontal position
   * @param y the new vertical position
   * @see Visual#setLocationIfPossible(int, int) 
   */
  public void setLocation(int x, int y) {
    if(! isMoving(x,y)) return;
    bounds.setLocation(x, y);
    clipToBounds();
    updateOnMove();
  }

  /** Sets the location of this component.
   * @param p the new position
   * @see Visual#setLocation(int, int) 
   * @see Visual#setLocationIfPossible(java.awt.Point) 
   */
  public void setLocation(Point p) {
    setLocation(p.x, p.y);
  }
  
  /**
   * Sets the location of this component, if <code>movable</code> is set to <code>true</code>.
   * @param x the new x-coordinate for this visual to be set
   * @param y the new y-coordinate for this visual to be set
   * @see Visual#movable
   * @see Visual#isMovable() 
   * @see Visual#setMovable(boolean) 
   * @see Visual#setLocation(int, int) 
   */
  public void setLocationIfPossible(int x, int y) {
    if(! movable) return;
    setLocation(x, y);
    updateOnUserMove();
  }
  
  /**
   * Sets the location of this component, if <code>movable</code> is set to <code>true</code>.
   * @param p the new position for this visual to be set
   * @see Visual#setLocationIfPossible(int, int) 
   */
  public void setLocationIfPossible(Point p) {
    setLocationIfPossible(p.x, p.y);
  }
  
  /**
   * Moves the location of this component by the specified values, 
   * if <code>movable</code> is set to <code>true</code>.
   * @param dx the horizontal value, this visual is to be translated
   * @param dy the vertical value, this visual is to be translated
   * @see Visual#setLocationIfPossible(int, int) 
   */
  public void translateIfPossible(int dx, int dy) {
    setLocationIfPossible(bounds.x + dx, bounds.y + dy);
  }

  /** Sets the location and size of this component.
   * @param x the new horizontal position
   * @param y the new vertical position
   * @param width the new width
   * @param height the new height
   */
  public void setBounds(int x, int y, int width, int height) {
    setLocation(x,y);
    setSize(width,height);
  }

  /** Sets the location and size of this component.
   * @param r the new outer bounds of this component
   */
  public void setBounds(Rectangle r) {
    setBounds(r.x,r.y,r.width,r.height);
  }
  
  public Rectangle getBounds() {
    return bounds.getBounds();
  }
 
  /**
   * Grows this component by the specified values. After this operation, the
   * component was translated by <code>(-h,-v)</code> and resized by
   * <code>(2*h,2*v)</code>. Negative values cause the component to shrink accordingly.
   * @param h the value to grow by horizontally
   * @param v the value to grow by vertically
   */
  public void grow(int h, int v) {
    if(h == 0 && v == 0) return;
    bounds.grow(h, v);
    clipToBounds();
    updateOnResize();
  }

  /** Returns the horizontal position of the top-left corner this component. 
   * @return the <code>x</code> value of this component's boundary
   */
  public int getX() {
    return bounds.x;
  }

  /** Returns the vertical position of the top-left this component. 
   * @return the <code>y</code> value of this component's boundary
   */
  public int getY() {
    return bounds.y;
  }

  /** Returns the horizontal position of the center of this component. 
   * @return the <code>x</code> value of this component's center
   */
  public double getCenterX() {
    return bounds.getCenterX();
  }

  /** Returns the vertical position of the center of this component. 
   * @return the <code>y</code> value of this component's center
   */
  public double getCenterY() {
    return bounds.getCenterY();
  }
  
  /**
   * Returns the position of the center of this component as a vector.
   * @return a vector pointing at the center of this component
   */
  public Vector2D getCenterVector() {
    return new Vector2D(getCenterX(), getCenterY());
  }

  /**
   * Sets the center of this visual. This means that the location is set
   * to the sum of the specified coordinates and the half size of this visual.
   * @param x the <code>x</code> coordinate for the center of this visual
   * @param y the <code>y</code> coordinate for the center of this visual
   */
  public void setCenter(int x, int y) {
    setLocation(x - (getWidth()/2), y - (getHeight()/2));
  }

  /**
   * Sets the center of this visual. This means that the location is set
   * to the sum of the specified coordinate and the half size of this visual.
   * @param p the location for the center of this visual
   */
  public void setCenter(Point p) {
    setCenter(p.x, p.y);
  }

  /** Returns the width of this component.
   * @return The <code>width</code> value of this component's boundary
   */
  public int getWidth() {
    return bounds.width;
  }

  /** Returns the height of this component.
   * @return The <code>height</code> value of this component's boundary
   */
  public int getHeight() {
    return bounds.height;
  }

  public boolean isHidden() {
    return hidden;
  }

  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }
  
  /** 
   * Returns the shape of this visual for selection.
   * @return the selectable shape of this visual
   */
  public abstract Shape getShape();
  
  public abstract String getElementTypeName();
}
