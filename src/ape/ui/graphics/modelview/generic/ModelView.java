/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.generic;

import ape.util.EnumPropertyType;
import ape.util.Property;
import ape.util.PropertyConstant;
import ape.petri.generic.EnumModelType;
import ape.petri.generic.Model;
import ape.petri.generic.ModelElement;
import ape.petri.generic.net.*;
import ape.ui.UI;
import ape.ui.graphics.modelview.EnumModelViewAction;
import ape.ui.graphics.modelview.ModelViewCanvas;
import ape.ui.graphics.modelview.ModelViewListener;
import ape.util.PropertyContainer;
import ape.util.aml.AMLNode;
import ape.util.aml.AMLWritable;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.List;
import java.util.*;

/**
 * A {@link ModelView} is the graphical representation of exactly one {@link Model}. 
 * For this purpose, the <code>ModelView</code> creates and manages {@link Visual}s for
 * the {@link ModelElement}s contained in the <code>ModelView</code>'s <code>Model</code>.
 * The <code>Visual</code>s are managed in maps that correlate to a 1-to-1-correspondence
 * between <code>ModelElement</code>s and their <code>Visual</code>s. Moreover, every
 * <code>Visual</code> has a z-order as an integer. A <code>Visual</code> with a higher z-order 
 * is drawn over <code>Visual</code>s with lower z-orders, if they have overlapping boundaries.
 * <br />
 * The <code>ModelView</code> is displayed by a {@link ModelViewCanvas}, and all relevant user
 * input is delegated from the <code>ModelViewCanvas</code> to the <code>ModelView</code>. 
 * The <code>ModelView</code> contains an off-screen image that is drawn on the
 * <code>ModelViewCanvas</code> whenever the <code>ModelViewCanvas</code> is repainted.
 * <br />
 * It is possible to scroll and scale the view of the model. A consequence of this is that
 * there is a difference between <i>device coordinates</i> and <i>view coordinates</i>. A
 * point that is a <i>device coordinate</i>, determines a point relative to the boundaries
 * of the {@link ModelViewCanvas}. That is, a device coordinate <code>(0,0)</code> represents
 * a point that is displayed in the top-left corner of the <code>ModelViewCanvas</code>.
 * In contrast, a <i>view coordinate</i> is a device-independent coordinate, that depends
 * on the current scrolling position and scaling of the <code>ModelView</code>. At creation
 * time of a new <code>ModelView</code>, a <i>view coordinate</i> of <code>(0,0)</code>
 * represents a point that is displayed in the center of the <code>ModelViewCanvas</code>.
 * When the view is scrolled to the right, the <i>device coordinate</i> pendant of each <i>view coordinate</i> 
 * is translated to the left, and the amount of the translation is multiplied with the scaling
 * of the <code>ModelView</code>. For the other directions it works accordingly.
 * The boundaries of the {@link Visual}s in the <code>ModelView</code> are stored in
 * <i>view coordinates</i>.
 * <br />
 * It is possible to get a view coordinate from a device coordinate, using
 * {@link ModelView#toViewCoordinate(java.awt.Point)}. Vice versa, to get a device coordinate 
 * from a view coordinate, one can use {@link ModelView#toDeviceCoordinate(java.awt.Point)}.
 * <br />
 * The <i>view coordinates</i> are especially relevant if someone wants to draw something
 * on the <code>ModelView</code>'s off-screen image, because you have to use view coordinates
 * for all drawing operations. 
 * @author Gabriel
 */
public class ModelView implements PropertyContainer, Serializable, AMLWritable, VisualListener {

  /**
   * The center of the view in <i>view coordinates</i>. As long this was not set manually,
   * its value is <code>(0,0)</code>. 
   */
  private Point viewCenter;
  
  /**
   * The size of the view in <i>device coordinates</i>. This should always be about the size of the
   * corresponding <code>ModelViewCanvas</code>, if there is one. 
   */
  private Dimension viewSize;
  
  /**
   * The scaling of the view, as a factor for view coordinates from device coordinates.
   * A <code>scale</code> of <code>1.0</code> means that everything
   * is displayed in normal size. A <code>scale</code> greater than <code>1.0</code> means that everything
   * is displayed larger, and a <code>scale</code> lower than <code>1.0</code> means that
   * everything is displayed smaller.
   */
  private double scale;
  
  /**
   * The current mouse location. It is updated on every mouse move event.
   */
  private Point mouseLocation;
  
  /**
   * A boolean value, whether one of the mouse buttons is currently pressed. It is <code>true</code>
   * if at least one button is pressed.
   */
  private boolean mouseDown;
  
  /**
   * The start position of the selection area in <i>view coordinates</i>. 
   * This point is set, when the left mouse button
   * is pressed, and the currently selected action is {@link ModelView#ACTION_SELECT}.
   */
  private Point selectionStartPoint;
  
  /**
   * The end position of the selection area in <i>view coordinates</i>. 
   * It is set, when the left mouse button
   * is released, and the currently selected action is {@link ModelView#ACTION_SELECT}.
   * Note that directly after setting the selection end point, the selection of visuals
   * is updated in correspondence to the rectangle determined by the selection start and end point,
   * and afterwards, the selection start and end point are reset to <code>null</code>.
   */
  private Point selectionEndPoint;
  
  /**
   * The start point of an arc creation. This point is set, when the left mouse button
   * is pressed, and the currently selected action is {@link ModelView#ACTION_NEW_ARC},
   * but only if there was no start point set before (that is, <code>arcStart</code>
   * was <code>null</code>). Also, it is only set, if the upmost visual in the mouse focus
   * represents a node (place or transition).
   * @see ModelView#arcEnd
   */
  private NodeVisual arcStart;
  
  /**
   * The end point of an arc creation. This point is set, when the left mouse button
   * is pressed, and the currently selected action is {@link ModelView#ACTION_NEW_ARC},
   * but only if the start point is not set to <code>null</code>. Also, it is only set,
   * if the upmost visual in the focus represents a node (place or transition).
   * If <code>arcStart</code> and <code>arcEnd</code> are set, the <code>ModelView</code>
   * tries to create an an arc between these nodes.
   * @see ModelView#arcStart
   * @see ModelView#tryToCreateArcFromSelectedNodes() 
   */
  private NodeVisual arcEnd;
  
  /**
   * The UI of the program, containing this <code>ModelView</code>.
   */
  private transient UI ui;
  
  /**
   * The <code>ModelViewCanvas</code>, displaying this <code>ModelView</code>.
   */
  private transient ModelViewCanvas canvas;
  
  /**
   * The image, where the contained {@link Visual}s of this <code>ModelView</code> are drawn to.
   * The image is drawn on the corresponding <code>ModelViewCanvas</code>, whenever this
   * <code>ModelView</code> is redrawn.
   */
  private transient BufferedImage image;
  
  /**
   * The graphics of the {@link ModelView#image}. It is used to draw the content of this
   * <code>ModelView</code>.
   */
  private transient Graphics2D graphics;
  
  /**
   * A mapping between <code>ModelElement</code>s of this <code>ModelView</code>'s <code>Model</code>
   * and their corresponding <code>Visual</code>s. 
   * <br />
   * Elements should never be added or removed by hand, but instead, using 
   * {@link ModelView#addVisual(ape.petri.generic.ModelElement, ape.ui.modelview.generic.Visual)}
   * and {@link ModelView#removeVisual(ape.ui.modelview.generic.Visual)}, respectively.
   * @see ModelView#modelElements
   */
  private Map<ModelElement,Visual> visualElements;
  
  /**
   * A mapping between <code>Visual</code>s and the corresponding <code>ModelElement</code>,
   * the <code>Visual</code> represents.
   * <br />
   * Elements should never be added or removed by hand, but instead, using 
   * {@link ModelView#addVisual(ape.petri.generic.ModelElement, ape.ui.modelview.generic.Visual)}
   * and {@link ModelView#removeVisual(ape.ui.modelview.generic.Visual)}, respectively.
   * @see ModelView#visualElements
   */
  private Map<Visual,ModelElement> modelElements;
  
  /**
   * A mapping between <code>Visual</code>s and their z-ordering value. <code>Visual</code>s
   * with higher z-order are drawn over <code>Visual</code>s with lower z-orders, if their
   * bounds are overlapping. Every <code>Visual</code> has a unique z-order, and for every
   * integer there is at most one <code>Visual</code> with that integer as its z-order.
   * <br />
   * Elements should never be added or removed by hand, but instead, using 
   * {@link ModelView#addVisual(ape.petri.generic.ModelElement, ape.ui.modelview.generic.Visual)}
   * and {@link ModelView#removeVisual(ape.ui.modelview.generic.Visual)}, respectively.
   * @see ModelView#zOrderedVisuals
   */
  private Map<Visual,Integer> visualZOrders;
  
  /**
   * A mapping between z-order values and <code>Visual</code>s with that z-order.
   * <br />
   * Elements should never be added or removed by hand, but instead, using 
   * {@link ModelView#addVisual(ape.petri.generic.ModelElement, ape.ui.modelview.generic.Visual)}
   * and {@link ModelView#removeVisual(ape.ui.modelview.generic.Visual)}, respectively.
   * @see ModelView#visualZOrders
   */
  private TreeMap<Integer,Visual> zOrderedVisuals;
  
  /**
   * A list of all the <code>Visual</code>s that are in the focus, that is,
   * they are currently under the mouse pointer.
   * @see ModelView#updateVisualsInFocus() 
   * @see ModelView#upmostVisualInFocus
   */
  private List<Visual> visualsInFocus;
  
  /**
   * The <code>Visual</code> in {@link ModelView#visualsInFocus} with the highest z-order.
   * @see ModelView#updateVisualsInFocus() 
   * @see ModelView#visualsInFocus
   */
  private Visual upmostVisualInFocus;
  
  /**
   * A set of all currently selected <code>Visual</code>s.
   */
  private Set<Visual> selectedVisuals;
  
  /**
   * The model, for which this <code>ModelView</code> is the graphical representation.
   */
  private Model model;
  
  /**
   * The factory that produces new <code>Visual</code>s of the {@link ModelView#model}s
   * model type for this <code>ModelView</code>.
   */
  private VisualFactory factory;
  
  /**
   * A new <code>ModelView</code> inside the specified UI, representing the specified model
   * @param ui the UI, containing this <code>ModelView</code>
   * @param model the {@link Model} for which this <code>ModelView</code> is the graphical
   * representation
   */
  public ModelView(UI ui, Model model) {
    this(ui,model,true);
  }

  public ModelView(UI ui, Model model, boolean updateVisualsFromModel) {
    this.ui = ui;
    this.model = model;
    this.visualElements = new HashMap<>();
    this.modelElements = new HashMap<>();
    this.visualZOrders = new HashMap<>();
    this.zOrderedVisuals = new TreeMap<>();
    this.mouseLocation = new Point();
    this.mouseDown = false;
    this.visualsInFocus = new ArrayList<>();
    this.upmostVisualInFocus = null;
    this.selectedVisuals = new HashSet<>();
    init(updateVisualsFromModel);
  }
  
  /**
   * Initializes the <code>ModelView</code>. Creates a factory for production of new visuals,
   * resets the view, and updates all visuals that are contained in the {@link ModelView#model}.
   */
  private void init(boolean updateVisualsFromModel) {
    factory = new VisualFactory(this);
    resetView();
    if(updateVisualsFromModel) updateVisualsFromModel();
  }

  public UI getUI() {
    return ui;
  }

  public void setUI(UI ui) {
    this.ui = ui;
  }
  
  /**
   * Removes all visuals from this <code>ModelView</code> and destroys them.
   */
  private void clearAllVisuals() {
    for(Visual v : visualElements.values()) {
      v.destroy();
    }
    visualElements.clear();
    modelElements.clear();
    visualZOrders.clear();
    zOrderedVisuals.clear();
  }
  
  /**
   * Updates all visuals corresponding to the model. It removes all existing visuals
   * and creates a new visual for every model element in the model.
   */
  private void updateVisualsFromModel() {
    clearAllVisuals();
    if(model == null) return;
    Map<ModelElement,Visual> newVisuals = factory.createVisuals(model.getAllElements());
    for (ModelElement e : newVisuals.keySet()) {
      addVisual(e, newVisuals.get(e));
    }
  }
  
  /**
   * Resets the view. The view is centered to the <i>view coordinate</i> origin,
   * and the scaling is reset to <code>1.0</code>.
   */
  public void resetView() {
    this.viewCenter = new Point(0,0);
    this.scale = 1.0;
    setViewSizeToCanvas();
  }

  /**
   * Returns the <code>ModelViewCanvas</code>, currently displaying this <code>ModelView</code>.
   * @return the <code>ModelViewCanvas</code> that displays this <code>ModelView</code>, or
   * <code>null</code> if the <code>ModelView</code> is currently not displayed.
   */
  public ModelViewCanvas getCanvas() {
    return canvas;
  }

  /**
   * Sets the <code>ModelViewCanvas</code> that displays this <code>ModelView</code>. This
   * method is usually called, when a <code>ModelViewCanvas</code> starts or stops displaying
   * this <code>ModelView</code>, setting the <code>ModelViewCanvas</code> to itself or
   * <code>null</code>, respectively.
   * @param canvas the <code>ModelViewCanvas</code> that is now displaying this <code>ModelView</code>,
   * or <code>null</code> if this <code>ModelView</code> is not displayed anymore
   */
  public final void setCanvas(ModelViewCanvas canvas) {
    this.canvas = canvas;
    if(canvas != null) {
      resetView();
      repaint();
    }
  }
  
  /**
   * Returns the model type of the <code>Model</code> that is represented by this <code>ModelView</code>.
   * @return the <code>EnumModelType</code> of this <code>ModelView</code>'s <code>Model</code>
   */
  public EnumModelType getModelType() {
    return model.getModelType();
  }
  
  /**
   * Adds a <code>Visual</code> to this <code>ModelView</code> that represents the specified
   * <code>ModelElement</code>. The <code>Visual</code> is created by this <code>ModelView</code>'s
   * {@link VisualFactory}.
   * @param e a <code>ModelElement</code> for which a visual representation is to be added to
   * this <code>ModelView</code>
   * @return the <code>Visual</code> that was created and added to this <code>ModelView</code>
   * as visual representation for <code>e</code>
   */
  public Visual addVisualFor(ModelElement e) {
    Visual v = factory.createVisual(e);
    addVisual(e,v);
    return v;
  }

  /**
   * Adds a <code>Visual</code> to this <code>ModelView</code> that represents the specified
   * <code>ModelElement</code>. The method updates all mappings that are used to manage
   * <code>ModelElements</code> and <code>Visuals</code>. The method does nothing if there
   * already is a mapping from <code>e</code> to some <code>Visual</code> in {@link ModelView#visualElements}.
   * This method should always be used
   * to do this job, because it also notifies all {@link ModelViewListener}s registered in
   * the {@link UI}.
   * It is not checked, whether the specified model element and visual actually fit to each other. 
   * @param e the <code>ModelElement</code>, the <code>Visual</code> <code>v</code> is representing
   * @param v the <code>Visual</code> that is to be added to this <code>ModelView</code>
   */
  private void addVisual(ModelElement e, Visual v) {
    if(visualElements.containsKey(e)) return;
    
    int zOrder = nextHighestZOrder();
    addVisual(e, v, zOrder);
  }

  private void addVisual(ModelElement modelElement, Visual visual, int zOrder) {
    visualElements.put(modelElement, visual);
    modelElements.put(visual, modelElement);
    zOrderedVisuals.put(zOrder, visual);
    visualZOrders.put(visual, zOrder);
    visual.addVisualListener(this);
    
    /* notify all listeners in the UI */
    if(ui != null) ui.visualElementAddedToActiveModelView(modelElement, visual);
  }

  
  /**
   * Returns the next highest z-order that does not already correspond to a <code>Visual</code>
   * in this <code>ModelView</code>.
   * @return <code>0</code> if there are no <code>Visual</code>s in this <code>ModelView</code>;
   * otherwise it takes the highest z-order of an existing <code>Visual</code> and increments
   * it by <code>1</code>
   */
  private int nextHighestZOrder() {
    if(zOrderedVisuals.isEmpty()) return 0;
    return zOrderedVisuals.lastKey() + 1;
  }
  
  /**
   * Removes the <code>Visual</code> that represents the specified <code>ModelElement</code>
   * from this <code>ModelView</code>.
   * @param e the <code>ModelElement</code> that shall not be represented by a <code>Visual</code>
   * anymore
   */
  public void removeVisualFor(ModelElement e) {
    removeVisual(visualElements.get(e));
  }
  
  /**
   * Removes the specified <code>Visual</code> from this <code>ModelView</code>, by destroying
   * it and removing it from a maps managing the <code>Visual</code>s of this <code>ModelView</code>.
   * Also, it notifies all {@link ModelViewListener}s that are registered in the {@link UI}.
   * This method should always be used to properly remove a <code>Visual</code>.
   * @param v the <code>Visual</code> that is to be removed
   */
  public void removeVisual(Visual v) {
    if(v == null) return;
    v.destroy();
    ModelElement m = modelElements.remove(v);
    visualElements.remove(m);
    Integer zOrder = visualZOrders.remove(v);
    zOrderedVisuals.remove(zOrder);

    /* notify all listeners in the UI */
    ui.visualElementRemovedFromActiveModelView(m, v);
  }
  
  /**
   * Returns the <code>Visual</code> that is the graphical representation for the given
   * <code>ModelElement</code> in this <code>ModelView</code>.
   * @param e the <code>ModelElement</code> corresponding to the returned <code>Visual</code>
   * @return the <code>Visual</code> element that represents <code>e</code>, or <code>null</code>
   * if there is no visual representation for <code>e</code>
   * @see ModelView#getModelElement(ape.ui.modelview.generic.Visual) 
   */
  protected Visual getVisual(ModelElement e) {
    return visualElements.get(e);
  }

  /**
   * Returns the <code>ModelElement</code> that is represented by the specified <code>Visual</code>.
   * @param v the given <code>Visual</code> that represents a <code>ModelElement</code>
   * @return the <code>ModelElement</code> that is represented by <code>v</code> in this
   * <code>ModelView</code>, or <code>null</code> if <code>v</code> does not correspond to
   * any <code>ModelElement</code>
   */
  protected ModelElement getModelElement(Visual v) {
    return modelElements.get(v);
  }
  
  /**
   * Initializes the off-screen image of this <code>ModelView</code>. The method ensures that
   * the view size's width and height is are least <code>1</code>. Then it creates an image
   * large enough for the view size and initializes the corresponding graphics object.
   */
  private void initImage() {
    if(viewSize.width < 1) viewSize.width = 1;
    if(viewSize.height < 1) viewSize.height = 1;
    image = new BufferedImage(unscaleUpTo(viewSize.width), unscaleUpTo(viewSize.height), BufferedImage.TYPE_INT_ARGB);
    initGraphics();
  }
  
  /**
   * Initializes the graphics object corresponding to the off-screen image of this <code>ModelView</code>
   * by applying the transformations corresponding to the position and scaling of the current view.
   */
  private void initGraphics() {
    this.graphics = (Graphics2D) image.getGraphics();
    translateGraphics(false);
    scaleGraphics(false);
  }
  
  /**
   * Propagates the current graphics of this <code>ModelView</code>'s off-screen image to the
   * <code>Visual</code>s. This is necessary whenever the last graphics object becomes invalid
   * because the off-screen image is not corresponding to the old graphics anymore.
   */
  private void propagateGraphics() {
    for (Visual v : visualElements.values()) {
      v.setSuperGraphics(graphics);
    }
  }
  
  /**
   * Returns the graphics object that is currently corresponding to the off-screen image of this
   * <code>ModelView</code>. Note that it is not ensured that the graphics remains functional
   * over a longer period of time, because some operations on the off-screen image enforce it
   * to create a new graphics, rendering the previous graphics useless. 
   * So, it is important to always get the currently valid graphics using this method.
   * @return a currently valid graphics object for this <code>ModelView</code>'s off-screen image
   */
  public Graphics2D getCurrentGraphics() {
    return graphics;
  }
  
  /**
   * Draws a grid in the size of the view on the <code>ModelView</code>'s offscreen-image. 
   * The size of the vertical and horizontal gaps between
   * grid lines are determined by {@link VisualGlobalValues#modelGridSize}. Moreover, a <i>main grid</i>
   * is drawn, determined by {@link VisualGlobalValues#modelMainGridSize} which should always be
   * a multiple of {@link VisualGlobalValues#modelGridSize}.
   * @see VisualGlobalValues#modelGridSize
   * @see VisualGlobalValues#modelGridColor
   * @see VisualGlobalValues#modelMainGridSize
   * @see VisualGlobalValues#modelMainGridColor
   */
  private void drawGrid() {
    Point zero = toViewCoordinate(0, 0);
    Point center = unscale(viewCenter.x, viewCenter.y);
    Point size = toViewCoordinate(viewSize.width, viewSize.height); 
    Point nearestMainLine = new Point(center.x - (center.x % VisualGlobalValues.modelMainGridSize), 
            center.y - (center.y % VisualGlobalValues.modelMainGridSize));

    for(int x=0; x <=  unscale(viewSize.width)+VisualGlobalValues.modelMainGridSize; x+=VisualGlobalValues.modelGridSize) {
      if(x % VisualGlobalValues.modelMainGridSize == 0) {
        graphics.setColor(VisualGlobalValues.modelMainGridColor);
      } else {
        graphics.setColor(VisualGlobalValues.modelGridColor);
      }
      graphics.drawLine(nearestMainLine.x + x, zero.y, nearestMainLine.x + x, size.y);
      graphics.drawLine(nearestMainLine.x - x, zero.y, nearestMainLine.x - x, size.y);
    }
    for(int y=0; y <= unscale(viewSize.height)+VisualGlobalValues.modelMainGridSize; y+=VisualGlobalValues.modelGridSize) {
      if(y % VisualGlobalValues.modelMainGridSize == 0) {
        graphics.setColor(VisualGlobalValues.modelMainGridColor);
      } else {
        graphics.setColor(VisualGlobalValues.modelGridColor);
      }
      graphics.drawLine(zero.x, nearestMainLine.y + y, size.x, nearestMainLine.y + y);
      graphics.drawLine(zero.x, nearestMainLine.y - y, size.x, nearestMainLine.y - y);
    }
  }
  
  /**
   * Fills the off-screen image of this <code>ModelView</code> with the
   * {@link VisualGlobalValues#modelBackgroundColor}.
   */
  private void drawBackground() {
    Point zeroInView = toViewCoordinate(0, 0);
    Point sizeInView = toViewCoordinate(-viewCenter.x + 2 * viewSize.width, -viewCenter.y + 2 * viewSize.height);

    graphics.setColor(VisualGlobalValues.modelBackgroundColor);
    graphics.fillRect(zeroInView.x-1, zeroInView.y-1, sizeInView.x+1, sizeInView.y+1);
  }
  
  /**
   * Draws the coordinates of the mouse location adjacent to the mouse pointer. This
   * should only be used for debugging, because it is not very fancy.
   */
  private void drawCoordinateInfo() {
    graphics.setColor(Color.white);
    graphics.fillRect(mouseLocation.x + 8, mouseLocation.y + 10, 50, 20);
    graphics.setColor(Color.black);
    graphics.drawString("[" + mouseLocation.x + "," + mouseLocation.y + "]", mouseLocation.x + 10, mouseLocation.y + 30);
  }
  
  /**
   * Returns a <i>view coordinate</i> rectangle that represents the current user selection.
   * The user selection is set, when the currently active action is {@link EnumModelViewAction#Selection}
   * and the user presses (setting the start point) the left mouse button, and
   * afterwards moves (setting the end point) the mouse.
   * Note that right after the mouse button was released, the set of selected elements is updated and
   * the start and end point of the selection are unset.
   * @return if no selection start point was set, the method returns <code>null</code>;
   * if only a start point but no end point is set, the method returns a zero-sized rectangle
   * at the location of the start point;
   * if a start and end point is set, the smallest rectangle is returned that contains
   * the start and the end point
   */
  protected Rectangle selectionRectangle() {
    if(selectionStartPoint == null) return null;
    if(selectionEndPoint == null) return new Rectangle(selectionStartPoint.x, selectionStartPoint.y, 0, 0);
    int x1 = (int) Math.min(selectionStartPoint.x, selectionEndPoint.x);
    int y1 = (int) Math.min(selectionStartPoint.y, selectionEndPoint.y);
    int x2 = (int) Math.max(selectionStartPoint.x, selectionEndPoint.x);
    int y2 = (int) Math.max(selectionStartPoint.y, selectionEndPoint.y);
    return new Rectangle(x1,y1, x2-x1, y2-y1);
  }
  
  /**
   * Draws the user selection rectangle on this <code>ModelView</code>'s off-screen image.
   * @see ModelView#selectionRectangle() 
   */
  private void drawSelection() {
    if(selectionStartPoint != null && selectionEndPoint != null) {
      Rectangle selection = selectionRectangle();
      graphics.setColor(VisualGlobalValues.modelSelectionColor);
      graphics.fillRect(selection.x, selection.y, selection.width, selection.height);
      graphics.setColor(VisualGlobalValues.modelSelectionBorderColor);
      graphics.drawRect(selection.x, selection.y, selection.width, selection.height);
    }
  }
  
  /**
   * Draws the given <code>Visual</code> on this <code>ModelView</code>'s off-screen image,
   * using the <code>Visual</code>'s {@link Visual#redraw(int)} method with the current status
   * of the <code>Visual</code>. A <code>Visual</code> is only drawn if it intersects with
   * the specified drawing area.
   * If the specified <code>Visual</code> has children, then also the children are drawn
   * recursively.
   * @param v the visual that is to be drawn
   * @param drawingArea the visible drawing area
   * @param parentInFocus <code>true</code> if <code>v</code> is the child of another <code>Visual</code>
   * that is in the focus
   * @param parentSelected <code>true</code> if <code>v</code> is the child of another <code>Visual</code>
   * that is selected
   */
  private void drawVisual(Visual v, boolean parentInFocus, boolean parentSelected) {
    boolean selected = selectedVisuals.contains(v);

    int status = Visual.STATUS_NORMAL;

    /* set status depending on parent */
    if(parentInFocus) {
      status |= Visual.STATUS_PARENT_HOVER;
    } 
    if(parentSelected) {
      status |= Visual.STATUS_PARENT_SELECTED;
    }

    /* visual itself in focus ? */
    if(v.equals(upmostVisualInFocus)) {
      status |= Visual.STATUS_HOVER;
    }
    /* visual itself selected ? */
    if(selected) {
      status |= Visual.STATUS_SELECTED;
    }
    v.redraw(status);

    if(v.hasSelectableChildren()) {
      /* parent is considered to be in focus if it is at the mouse location;
       * it does not have to be the upmost visual, because it is very probable
       * that it is hidden behind one of its children
       */
      boolean inFocus = visualsInFocus.contains(v);
      for(Visual c : v.getSelectableChildren()) {
        drawVisual(c, inFocus, selected);
      }
    }
  }
  
  private Rectangle drawingArea() {
    Point zeroInView = toViewCoordinate(0, 0);
    Point sizeInView = toViewCoordinate(-viewCenter.x + 2 * viewSize.width, -viewCenter.y + 2 * viewSize.height);
    return new Rectangle(zeroInView.x, zeroInView.y, sizeInView.x, sizeInView.y);
  }
  
  /**
   * Draws all <code>Visuals</code> of this <code>ModelView</code> that intersect with the
   * visible area of the view.
   */
  private void drawVisuals() {
    for(Visual v : zOrderedVisuals.values()) {
      drawVisual(v, false, false);
    }
  }
  
  /**
   * Draws an arc that is currently in creation. That is, if a start node for an arc was chosen,
   * an arc phantom (that is, an arc without proper end node) is drawn from the start
   * node to the mouse location.
   */
  public void drawArcInCreation() {
    if(arcStart == null) return;
    ArcVisual.drawArcPhantom(graphics, arcStart, mouseLocation);
  }
  
  /**
   * Draws all content of this <code>ModelView</code> first on its off-screen image, and draws
   * all content of the off-screen image on the <code>ModelViewCanvas</code>.
   */
  public void draw(Graphics g) {
    drawBackground();
    drawGrid();
    drawVisuals();
    drawSelection();
    drawArcInCreation();
    g.drawImage(image, 0, 0, canvas);
    ui.getPropertyTable().refresh();
  }
  
  /**
   * Scales the graphics of this <code>ModelView</code>'s off-screen image according to this
   * <code>ModelView</code>'s scaling. This has the effect that all following drawing operations on the
   * graphics are scaled accordingly. If <code>backwards</code> is <code>true</code>, it performs
   * the exact inverse scaling.
   * @param backwards <code>false</code> for scaling the off-screen image according to this
   * <code>ModelView</code>'s scaling factor, and <code>true</code> for undoing the scaling
   * by scaling with the inverse factor
   */
  private void scaleGraphics(boolean backwards) {
    double effectiveScale = (backwards ? 1.0 / scale : scale);
    graphics.scale(effectiveScale, effectiveScale);
    propagateGraphics();
  }
  
  /**
   * Translates the graphics of this <code>ModelView</code>'s off-screen image according to
   * the current scroll position. The scroll position is determined by the view center and
   * the view size. The graphics is translated by the inverse of the view center and by half
   * of the view size. If <code>backwards</code> is true, it performs the exact inverse translation.
   * @param backwards <code>false</code> for translate the off-screen image according to this
   * <code>ModelView</code>'s scrolling position, and <code>true</code> for undoing the translation
   * by translate in the opposite direction
   */
  private void translateGraphics(boolean backwards) {
    int sign = (backwards ? -1 : 1);
    Point trans = new Point(-viewCenter.x, -viewCenter.y);
    trans.translate(viewSize.width/2, viewSize.height/2);
    graphics.translate(sign * trans.x, sign * trans.y);
  }
  
  
  /**
   * Sets the view center to the specified view coordinates and translates the graphics
   * of this <code>ModelView</code> accordingly. This method should always be used to
   * change the location of the view, such that it is ensured that all drawing operations
   * on the off-screen image work properly.
   * @param x the horizontal location of the center of the view to set
   * @param y the vertical location of the center of the view to set
   */
  private void setViewCenterAndTranslateGraphics(int x, int y) {
    scaleGraphics(true);
    translateGraphics(true);
    viewCenter.x = x;
    viewCenter.y = y;
    translateGraphics(false);
    scaleGraphics(false);
    propagateGraphics();
  }
  
  /**
   * Translates device coordinates into view coordinates. This is done by subtraction of
   * half of the view size and addition of the view center. Also, the coordinates are &quot;un-scaled&quot;. 
   * @param deviceX the horizontal value of the coordinate to translate
   * @param deviceY the vertical value of the coordinate to translate
   * @return a new point representing the specified device coordinate as a view coordinate
   * @see ModelView#toViewCoordinate(java.awt.Point) 
   * @see ModelView#toDeviceCoordinate(int, int) 
   */
  public Point toViewCoordinate(int deviceX, int deviceY) {
    Point p = new Point(deviceX, deviceY);
    p.translate(-viewSize.width / 2, -viewSize.height/2);
    p.translate(viewCenter.x, viewCenter.y);
    p = new Point(unscale(p.x), unscale(p.y));
    return p;
  }
  
  /**
   * Translates device coordinates into view coordinates.
   * @param p a point in device coordinates that is to be translated
   * @return a new point in view coordinates that corresponds to <code>p</code>
   * @see ModelView#toViewCoordinate(int, int) 
   * @see ModelView#toDeviceCoordinate(java.awt.Point) 
   */
  public Point toViewCoordinate(Point p) {
    return toViewCoordinate(p.x, p.y);
  }
  
  /**
   * Translates view coordinates into device coordinates. This is done by scaling the point
   * with this <code>ModelView</code>'s scaling factor, and afterwards, addition of
   * half of the view size and subtraction of the view center.
   * @param viewX the horizontal value of the view coordinate to translate
   * @param viewY the vertical value of the view coordinate to translate
   * @return a new point representing the specified view coordinate as a device coordinate
   * @see ModelView#toDeviceCoordinate(java.awt.Point) 
   * @see ModelView#toViewCoordinate(int, int) 
   */
  public Point toDeviceCoordinate(int viewX, int viewY) {
    Point p = new Point(scale(viewX), scale(viewY));
    p.translate(viewSize.width / 2, viewSize.height / 2);
    p.translate(-viewCenter.x, -viewCenter.y);
    p = new Point(p.x, p.y);
    return p;
  }
  
  
  /**
   * Translates view coordinates into device coordinates. 
   * @param p the view coordinate to translate
   * @return a new point representing the specified view coordinate as a device coordinate
   * @see ModelView#toDeviceCoordinate(int, int) 
   * @see ModelView#toViewCoordinate(java.awt.Point) 
   */
  public Point toDeviceCoordinate(Point p) {
    return toDeviceCoordinate(p.x, p.y);
  }
    
  /**
   * Sets the coordinate center of this <code>ModelView</code> to be the view coordinate of the 
   * specified device coordinate. 
   * Effectively, the coordinate center always remains the coordinate <code>(0,0)</code>, but
   * the view is translated as if the new coordinate center would be the specified point.
   * @param xInDevice the horizontal value of the device coordinate
   * @param yInDevice the vertical value of the device coordinate
   * @see ModelView#setCoordinateCenterFromView(int, int) 
   * @see ModelView#toViewCoordinate(int, int) 
   */
  public void setCoordinateCenterFromDevice(int xInDevice, int yInDevice) {
    setCoordinateCenterFromView(toViewCoordinate(xInDevice, yInDevice));
  }

  /**
   * Sets the coordinate center to be the view coordinate of the specified device coordinate.
   * Effectively, the coordinate center always remains the coordinate <code>(0,0)</code>, but
   * the view is translated as if the new coordinate center would be the specified point.
   * @param pointInDevice the device coordinate
   * @see ModelView#setCoordinateCenterFromView(int, int) 
   * @see ModelView#toViewCoordinate(java.awt.Point) 
   */
  public void setCoordinateCenterFromDevice(Point pointInDevice) {
    setCoordinateCenterFromView(toViewCoordinate(pointInDevice));
  }

  /**
   * Sets the coordinate center of this <code>ModelView</code> to be the specified view coordinate.
   * Effectively, the coordinate center always remains the coordinate <code>(0,0)</code>, but
   * the view is translated as if the new coordinate center would be the specified point.
   * @param xInView the horizontal value
   * @param yInView the vertical value
   */
  public void setCoordinateCenterFromView(int xInView, int yInView) {
    setViewCenterAndTranslateGraphics(viewCenter.x-scale(xInView), viewCenter.y-scale(yInView));
  }
  
  /**
   * Sets the coordinate center of this <code>ModelView</code> to be the specified view coordinate.
   * Effectively, the coordinate center always remains the coordinate <code>(0,0)</code>, but
   * the view is translated as if the new coordinate center would be the specified point.
   * @param pointInView the view coordinate that is to be the new coordinate center
   * @see ModelView#setCoordinateCenterFromView(int, int) 
   */
  public void setCoordinateCenterFromView(Point pointInView) {
    setCoordinateCenterFromView(pointInView.x, pointInView.y);
  }

  /**
   * Sets the view center of this <code>ModelView</code> to the view coordinates of the
   * specified device coordinates.
   * @param xInDevice the horizontal location in device for the view center
   * @param yInDevice the vertical location in device for the view center
   */
  public void setViewCenterFromDevice(int xInDevice, int yInDevice) {
    Point view = scale(toViewCoordinate(xInDevice, yInDevice));
    setViewCenterAndTranslateGraphics(view.x, view.y);
  }

  /**
   * Sets the view center of this <code>ModelView</code> to the view coordinates of the
   * specified device coordinates.
   * @param pInDevice the location in device for the view center
   */
  public void setViewCenterFromDevice(Point pInDevice) {
    setViewCenterFromDevice(pInDevice.x, pInDevice.y);
  }

  /**
   * Translates the view center by the specified values given in device coordinates.
   * @param dxInDevice the horizontal offset
   * @param dyInDevice the vertical offset
   */
  public void translateViewCenter(int dxInDevice, int dyInDevice) {
    setViewCenterAndTranslateGraphics(dxInDevice + viewCenter.x, dyInDevice + viewCenter.y);
  }

  
  /**
   * Translates the view center by the specified values given in device coordinates.
   * @param dpInDevice the offset of the translation
   */
  public void translateViewCenter(Point dpInDevice) {
    translateViewCenter(dpInDevice.x,dpInDevice.y);
  }


  /**
   * Returns the view center of this <code>ModelView</code>.
   * @return a new point representing the location of this <code>ModelView</code> in view coordinates
   */
  public Point getViewCenter() {
    return viewCenter.getLocation();
  }

  /**
   * Sets the view size of this <code>ModelView</code> to the specified size and initializes
   * the off-screen image to adapt to the new size.
   * @param width the width to be set
   * @param height the height to be set
   */
  private void setViewSizeAndInitImage(int width, int height) {
    viewSize = new Dimension(width, height);
    initImage();
  }
  
  /**
   * Sets the view size of this <code>ModelView</code>. This effectively resizes the off-screen
   * image of the <code>ModelView</code> to fit to the specified size.
   * @param width the width to be set
   * @param height the height to be set
   */
  public void setViewSize(int width, int height) {
    setViewSizeAndInitImage(width, height);
  }

  /**
   * Sets the view size of this <code>ModelView</code>. This effectively resizes the off-screen
   * image of the <code>ModelView</code> to fit to the specified size.
   * @param size the size to be set
   */
  public void setViewSize(Dimension size) {
    setViewSizeAndInitImage(size.width, size.height);
  }
  
  /**
   * Updates this <code>ModelView</code>'s view size to fit the size of its {@link ModelViewCanvas}.
   * If there is no <code>ModelViewCanvas</code> corresponding to this <code>ModelView</code>,
   * the size is set to <code>(1,1)</code>.
   */
  public void setViewSizeToCanvas() {
    if(canvas == null) {
      setViewSize(1,1);
    } else {
      setViewSize(canvas.getSize());
    }
  }

  /**
   * Returns the view size of this <code>ModelView</code>. The view size is the size of the
   * <code>ModelView</code>'s off-screen image and usually it should coincide with the size
   * of the {@link ModelViewCanvas} that displays the <code>ModelView</code>.
   * @return a dimension with size of this <code>ModelView</code>'s view size
   */
  public Dimension getViewSize() {
    return viewSize.getSize();
  }
  
  /**
   * Returns the scaling factor of the model view.
   * @return the amount of scaling that is applied to each drawing operation of this <code>ModelView</code>
   */
  public double getScale() {
    return scale;
  }

  /**
   * Sets the scaling factor and initializes the graphics by applying the new scaling to it.
   * @param scale the scaling factor for all drawing operations
   */
  private void setScaleAndInitGraphics(double scale) {
    scaleGraphics(true);
    this.scale = scale;
    scaleGraphics(false);
    initGraphics();
  }
  
  /**
   * Sets the scaling factor of the model view that is applied to all its drawing operations.
   * If the specified value lies outside the bounds defined by {@link VisualGlobalValues#modelMaxScale}
   * or {@link VisualGlobalValues#modelMinScale}, respectively, it is set to the nearest value
   * inside the bounds.
   * @param scale the scaling factor to be set for this <code>ModelView</code>
   */
  public void setScale(double scale) {
    if(scale > VisualGlobalValues.modelMaxScale) scale = VisualGlobalValues.modelMaxScale;
    else if(scale < VisualGlobalValues.modelMinScale) scale = VisualGlobalValues.modelMinScale;
    setScaleAndInitGraphics(scale);
  }
  
  /**
   * Changes the scaling of the view by the specified value, by multiplication with the
   * currently set scaling factor.
   * If the resulting factor lies outside the bounds defined by {@link VisualGlobalValues#modelMaxScale}
   * or {@link VisualGlobalValues#modelMinScale}, respectively, it is set to the nearest value
   * inside the bounds.
   * <br />
   * Moreover, the view is altered a bit in direction of the specified mouse position in view coordinates.
   * The alteration depends on the resulting scaling factor. A larger scale means less alteration.
   * @param scaleChange a factor to apply to the currently set scaling; a value of <code>1.0</code>
   * means that the scaling remains unchanged; larger values zoom in; lower values zoom out
   * @param mousePositionInView the location of the mouse in view coordinates
   */
  public void scaleViewFromView(double scaleChange, Point mousePositionInView) {
    if(scale * scaleChange > VisualGlobalValues.modelMaxScale) scaleChange = VisualGlobalValues.modelMaxScale / scale;
    else if(scale * scaleChange < VisualGlobalValues.modelMinScale) scaleChange = VisualGlobalValues.modelMinScale / scale;
    if(scaleChange == 1.0) return;
    
    scaleGraphics(true);
    this.scale *= scaleChange;
    scaleGraphics(false);

    initGraphics();
  }
  
  /**
   * Changes the scaling of the view by the specified value, by multiplication with the
   * currently set scaling factor.
   * If the resulting factor lies outside the bounds defined by {@link VisualGlobalValues#modelMaxScale}
   * or {@link VisualGlobalValues#modelMinScale}, respectively, it is set to the nearest value
   * inside the bounds.
   * <br />
   * Moreover, the view is altered a bit in direction of the specified mouse position in view coordinates.
   * The alteration depends on the resulting scaling factor. A larger scale means less alteration.
   * @param scaleChange a factor to apply to the currently set scaling; a value of <code>1.0</code>
   * means that the scaling remains unchanged; larger values zoom in; lower values zoom out
   * @param mousePositionInDevice the location of the mouse in device coordinates
   * @see ModelView#scaleViewFromView(double, java.awt.Point) 
   */
  public void scaleViewFromDevice(double scaleChange, Point mousePositionInDevice) {
    scaleViewFromView(scaleChange, toViewCoordinate(mousePositionInDevice));
  }

  /**
   * Scales a scalar value by the scale of the view.
   * @param x the value to be scaled
   * @return the scaled value
   */
  private int scale(int x) {
    return (int) (x * scale);
  }
  
  /**
   * Scales a coordinate by the scale of the view.
   * @param p the coordinate to be scaled
   * @return the scaled coordinate
   */
  private Point scale(Point p) {
    return new Point(scale(p.x), scale(p.y));
  }
  
  /**
   * Scales a coordinate by the scale of the view.
   * @param x the horizontal value of the coordinate to be scaled
   * @param y the vertical value of the coordinate to be scaled
   * @return the scaled coordinate
   */
  private Point scale(int x, int y) {
    return new Point(scale(x), scale(y));
  }

  /**
   * Un-scales a scaled scalar value by the scaling factor of the view.
   * @param x a scaled value
   * @return the value that results in <code>x</code> when scaled by the scaling of the view
   */
  private int unscale(int x) {
    return (int) (x / scale);
  }
  
  /**
   * Un-scales a scaled scalar value but only up to the value itself, that is, the method
   * returns <code>max{unscale(x),x}</code>.
   * @param x a scaled scalar value
   * @return <code>max{unscale(x),x}</code>
   * @see ModelView#unscale(int) 
   */
  private int unscaleUpTo(int x) {
    return (int) Math.max(unscale(x),x);
  }

  /**
   * Un-scales a scaled coordinate by the scaling factor of the view.
   * @param p a scaled coordinate
   * @return the coordinate that results in <code>p</code> when scaled by the scaling of the view
   */
  private Point unscale(Point p) {
    return new Point(unscale(p.x), unscale(p.y));
  }
  
  /**
   * Un-scales a scaled coordinate by the scaling factor of the view.
   * @param x the horizontal value of a scaled coordinate
   * @param y the vertical value of a scaled coordinate
   * @return the coordinate that results in <code>(x,y)</code> when scaled by the scaling of the view
   */
  private Point unscale(int x, int y) {
    return new Point(unscale(x), unscale(y));
  }
  
  /**
   * Repaints the {@link ModelViewCanvas} displaying this <code>ModelView</code> if set.
   */
  public void repaint() {
    if(canvas != null) canvas.repaint();
  }

  /**
   * Returns the {@link Model} of which this <code>ModelView</code> is the graphical representation.
   * @return this <code>ModelView</code>'s <code>Model</code>
   */
  public Model getModel() {
    return model;
  }

  /**
   * Returns the last known mouse location over this model view.
   * @return the location of this mouse, as far as this <code>ModelView</code> knows it
   * @see ModelView#setMouseLocation(java.awt.Point) 
   */
  public Point getMouseLocation() {
    return mouseLocation;
  }

  /**
   * Tells this <code>ModelView</code> the location of the mouse in view coordinates. This invokes the
   * set of visuals in the mouse focus to be recalculated.
   * @param mouseLocation the location of the mouse over this <code>ModelView</code>
   */
  public void setMouseLocation(Point mouseLocation) {
    this.mouseLocation = mouseLocation;
    updateVisualsInFocus();
  }

  /**
   * Returns whether some mouse button is currently pressed on the {@link ModelViewCanvas}
   * displaying this <code>ModelView</code>.
   * @return <code>true</code> if the mouse is pressed on the <code>ModelViewCanvas</code>,
   * displaying this <code>ModelView</code>
   */
  public boolean isMouseDown() {
    return mouseDown;
  }

  /**
   * Tells the <code>ModelView</code> that the mouse was pressed or released on the 
   * {@link ModelViewCanvas} that displays this <code>ModelView</code>
   * @param mouseDown <code>true</code> when the mouse was pressed; <code>false</code> when
   * the mouse was released
   */
  public void setMouseDown(boolean mouseDown) {
    this.mouseDown = mouseDown;
    
  }
  
  /**
   * Returns the {@link Visual} in focus with the highest z-order.
   * @return the upmost visual in focus or <code>null</code> if there is no visual in focus.
   */
  protected Visual getUpmostVisualInFocus() {
    return upmostVisualInFocus;
  }
  
  /**
   * Forces the <code>ModelView</code> to recalculate the set of {@link Visual}s that
   * are currently at the location of the mouse. A <code>Visual</code> is considered
   * to be at the mouse location, if its shape contains the mouse location.
   * @see ModelView#getUpmostVisualInFocus() 
   * @see Visual#getShape() 
   */
  public void updateVisualsInFocus() {
    visualsInFocus.clear();
    for(Visual v : zOrderedVisuals.values()) {
      if(v.getShape().contains(mouseLocation)) {
        visualsInFocus.add(v);
      }
      if(v.hasSelectableChildren()) {
        for(Visual c : v.getSelectableChildren()) {
          if(c.getShape().contains(mouseLocation)) {
            visualsInFocus.add(c);
          } 
        }
      }      
    }
    if(visualsInFocus.isEmpty()) {
      upmostVisualInFocus = null;
    } else {
      upmostVisualInFocus = visualsInFocus.get(visualsInFocus.size()-1);
    }
  }
  
  public void selectionUpdated() {
    if(selectedVisuals.isEmpty()) {
      ui.getPropertyTable().displayProperties(this);
      return;
    }

    Visual v = (Visual) selectedVisuals.toArray()[0];
    ui.getPropertyTable().displayProperties(v);
  }
  
  @Override
  public List<Property> getProperties() {
    List<Property> props = new ArrayList<>();
    
    props.add(new PropertyConstant(Property.CATEGORY_PROPERTIES, this, EnumPropertyType.String, "Model Type", model.getModelType().getName()));
    
    props.add(new PropertyConstant(Property.CATEGORY_PROPERTIES, this, EnumPropertyType.String, "Net Class", model.getNetType().getName()));
    
    props.add(new Property(Property.CATEGORY_VIEW, this, EnumPropertyType.Integer, "View Center X", true) {

      @Override
      public Object getValue() {
        return viewCenter.x;
      }

      @Override
      public void setValue(Object value) {
        setViewCenterAndTranslateGraphics((int) value, viewCenter.y);
      }
    });

    props.add(new Property(Property.CATEGORY_VIEW, this, EnumPropertyType.Integer, "View Center Y", true) {
      @Override
      public Object getValue() {
        return viewCenter.y;
      }

      @Override
      public void setValue(Object value) {
        setViewCenterAndTranslateGraphics(viewCenter.x, (int) value);
      }
    });
    
    props.add(new Property(Property.CATEGORY_VIEW, this, EnumPropertyType.Interval, "Scaling", true) {
      @Override
      public Object getValue() {
        double range = VisualGlobalValues.modelMaxScale - VisualGlobalValues.modelMinScale;
        double scaleInRange = (scale - VisualGlobalValues.modelMinScale);
        double base = 1 / range * (Math.E-1);
        return Math.log1p(scaleInRange * base);
      }

      @Override
      public void setValue(Object value) {
        double logscale = (double) value;
        double range = VisualGlobalValues.modelMaxScale - VisualGlobalValues.modelMinScale;
        double base = 1 / range * (Math.E-1);
        double expscale = (Math.exp(logscale) - 1.0) / base;
        double actualScale = (expscale + VisualGlobalValues.modelMinScale);
        setScale(actualScale);
      }
    });
    
    return props;
  }
  
  /**
   * Updates the selection of visuals from a selected area. A {@link Visual} is considered
   * to be inside the selected area, if the area intersects with the visual's shape.
   * @param add if this value is set to <code>false</code>, the set of selected visuals
   * is cleared before adding any new elements; if this value is set to <code>true</code>,
   * the previous set of selected visuals is kept, and each newly selected visual is added to
   * that set
   * @param xor this parameter only has an effect, if <code>add</code> is set to <code>true</code>;
   * if this value is set to <code>false</code>, then any newly selected item is always
   * added to the current set of selected visuals; if this value is set to <code>true</code>,
   * then any newly selected visual is only added to the selection set, if it was not selected before,
   * otherwise it is removed
   * @see ModelView#selectionRectangle() 
   * @see ModelView#updateSingleSelection(boolean, boolean) 
   * @see Visual#getShape() 
   */
  public void updateSelection(boolean add, boolean xor) {
    if(! add) selectedVisuals.clear();
    Rectangle selection = selectionRectangle();
    for(Visual v : visualElements.values()) {
      if(! v.getShape().intersects(selection)) continue;
      updateSelection(xor, v);
      if(v.hasSelectableChildren()) {
        for(Visual c : v.getSelectableChildren()) {
          if(! c.getShape().intersects(selection)) continue;
          updateSelection(xor, c);
        }
      }
    }
    selectionUpdated();
  }
  
  /**
   * Updates the selection with respect to a single {@link Visual}. If <code>xor</code> is true,
   * the visual is only added if it is not yet contained in the set of selected visuals,
   * and it is removed, if it is contained. Otherwise, the visual is always added to
   * the set of selected visual, provided that the visual is selectable.
   * @param xor <code>false</code> if the visual should be added in any case, and <code>true</code>
   * if the set of selected visuals should contain <code>v</code> if and only if it was not
   * contained before
   * @param v a visual that is considered to be added to the set of selected visuals
   * @see Visual#isSelectable() 
   */
  private void updateSelection(boolean xor, Visual v) {
    if(v instanceof BendingPointVisual) selectedVisuals.add(((BendingPointVisual)v).getParent());
    if(xor && selectedVisuals.contains(v)) {
      selectedVisuals.remove(v);
    } else {
      if(v.isSelectable()) {
        selectedVisuals.add(v);
      }
    }
  }
  
  /**
   * Updates the selection of visuals from a single click. The single click operates
   * only on the <code>Visual</code> at the current mouse location that has the upmost
   * z-order.
   * @param add if this value is set to <code>false</code>, the set of selected visuals
   * is cleared before adding any new elements; if this value is set to <code>true</code>,
   * the previous set of selected visuals is kept, and each newly selected visual is added to
   * that set
   * @param xor this parameter only has an effect, if <code>add</code> is set to <code>true</code>;
   * if this value is set to <code>false</code>, then the newly selected item is always
   * added to the current set of selected visuals; if this value is set to <code>true</code>,
   * then a newly selected visual is only added to the selection set, if it was not selected before,
   * otherwise it is removed
   * @see ModelView#getUpmostVisualInFocus() 
   * @see ModelView#updateSelection(boolean, boolean) 
   */
  public void updateSingleSelection(boolean add, boolean xor) {
    if(! add) selectedVisuals.clear();
    Visual v = getUpmostVisualInFocus();
    if(v == null) return;
    updateSelection(xor, v);
    if(v.hasSelectableChildren()) {
      for(Visual c : v.getSelectableChildren()) {
        updateSelection(xor, c);
      }
    }
    selectionUpdated();
  }
  
  public void setSelectionTo(Visual v) {
    selectedVisuals.clear();
    selectedVisuals.add(v);
    selectionUpdated();
  }
  
  public void setSelectionTo(ModelElement m) {
    Visual v = visualElements.get(m);
    setSelectionTo(v);
  }
  
  public void setSelectionAndViewCenterTo(ModelElement m) {
    Visual v = visualElements.get(m);
    setSelectionTo(v);
    setViewCenterAndTranslateGraphics(v.getX(), v.getY());
  }
  
  public Set<ModelElement> getSelection() {
    Set<ModelElement> elements = new HashSet<>(selectedVisuals.size());
    for(Visual s : selectedVisuals) {
      elements.add(modelElements.get(s));
    }
    return elements;
  }
  
  /**
   * Translates all currently selected visuals by the specified values.
   * @param dx the horizontal translation amount
   * @param dy the vertical translation amount
   */
  private void translateSelectedVisuals(int dx, int dy) {
    for(Visual v : selectedVisuals) {
      v.translateIfPossible(dx, dy);
    }
  }
  
  /**
   * Notifies all {@link Visual}s in this <code>ModelView</code> that they were
   * moved by the user.
   * @see Visual#updateOnUserMoveFinished() 
   */
  private void notifyMovedVisuals() {
    for(Visual v : selectedVisuals) {
      v.updateOnUserMoveFinished();
    }
  }
  
  /**
   * This method tries to create an arc between the {@link Node}s corresponding to the specified
   * {@link NodeVisual}s, and if it has success, it adds a corresponding {@link ArcVisual} to
   * this <code>ModelView</code>. The creation of the arc is successful, if there is not already
   * an arc between the nodes with the specified direction.
   * <br />
   * The <code>Model</code> of this <code>ModelView</code> has to be a {@link Net}, or otherwise
   * the call of this method will probably lead to an exception.
   * @param pOfArc the <code>PlaceVisual</code>, representing the {@link Place} of the arc that
   * is to be created
   * @param tOfArc the <code>TransitionVisual</code>, representing the {@link Transition} of
   * the arc that is to be created
   * @param dir the direction of the arc that is to be created
   * @return if the creation was successful then the newly created arc is returned, and 
   * <code>null</code> otherwise
   */
  private Visual tryToCreateArc(PlaceVisual pOfArc, TransitionVisual tOfArc, EnumArcDirection dir) {
    Place p = (Place) modelElements.get(pOfArc);
    Transition t = (Transition) modelElements.get(tOfArc);
    if(((Net) model).hasArc(p, t, dir)) return null;
    ArcCollection arc = ((Net) model).addDefaultArc(p ,t , dir);
    return addVisualFor(arc);
  }  

  /**
   * Tries to create an arc in this <code>ModelView</code>'s {@link Model} between the
   * nodes stored in {@link ModelView#arcStart} and {@link ModelView#arcEnd}. The attempt will
   * succeed, if one of the nodes is a place, and the other one is a transition, and the model
   * does not yet contain an arc between the nodes.
   * @return if an arc was successfully created in the <code>Model</code>, then the created arc
   * is returned, otherwise <code>null</code>
   * @see ModelView#tryToCreateArc(ape.ui.modelview.generic.PlaceVisual, ape.ui.modelview.generic.TransitionVisual, ape.petri.generic.EnumArcDirection) 
   */
  private Visual tryToCreateArcFromSelectedNodes() {
    PlaceVisual pOfArc = null;
    TransitionVisual tOfArc = null;
    EnumArcDirection dir = null;
    
    if(arcStart instanceof PlaceVisual) {
      dir = EnumArcDirection.PT;
      pOfArc = (PlaceVisual) arcStart;
    } else if(arcEnd instanceof PlaceVisual) {
      dir = EnumArcDirection.TP;
      pOfArc = (PlaceVisual) arcEnd;
    }
    
    if(arcStart instanceof TransitionVisual) {
      tOfArc = (TransitionVisual) arcStart;
    } else if(arcEnd instanceof TransitionVisual) {
      tOfArc = (TransitionVisual) arcEnd;
    }
    
    if(pOfArc != null && tOfArc != null) {
      return tryToCreateArc(pOfArc, tOfArc, dir);
    }
    return null;
  }
  
  /**
   * This method should be invoked (usually by the {@link ModelViewCanvas})
   * when the selected model view action has changed.
   * @param action the action that is now selected
   */
  public void selectedActionChanged(EnumModelViewAction action) {
    arcStart = null;
    arcEnd = null;
  }

  
  /**
   * This method should be invoked (usually by the {@link ModelViewCanvas})
   * when the mouse was clicked on the {@link ModelViewCanvas}, displaying this <code>ModelView</code>.
   * @param action the action that is now selected
   * @param modifier a mask of active modifies 
   */
  public void mouseClick(EnumModelViewAction action, int modifier) {
    switch(action) {
      case Selection:
        /*
         * update selection, taking into account a 1 pixel square at the mouse location;
         * - if CTRL is pressed: add elements in the area that were not in the selection before and remove
         * elements that were there before
         * - if SHIFT is pressed: add all elements in the area, even if they were there before
         */
        boolean xor = ((modifier & MouseEvent.CTRL_DOWN_MASK) != 0);
        boolean add = ((modifier & MouseEvent.SHIFT_DOWN_MASK) != 0) | xor;
        updateSingleSelection(add, xor);
        break;
    }
  }
  
  /**
   * This method should be invoked (usually by the {@link ModelViewCanvas})
   * when the mouse was moved over the {@link ModelViewCanvas}, displaying this <code>ModelView</code>.
   * @param action the action that is now selected
   * @param dx the horizontal movement since the last event occurred
   * @param dy the vertical movement since the last event occurred
   */
  public void mouseMoved(EnumModelViewAction action, int dx, int dy) {
    switch(action) {
      case Selection:
        if(mouseDown) {
          /* if the user is currently selecting an area */
          if(selectionStartPoint != null) {
            selectionEndPoint = mouseLocation.getLocation();
            
          /* otherwise */
          } else {
            translateSelectedVisuals(dx, dy);
          }
        }
        break;
    }
  }
  
  /**
   * This method should be invoked (usually by the {@link ModelViewCanvas})
   * when the mouse was pressed on the {@link ModelViewCanvas}, displaying this <code>ModelView</code>.
   * @param action the action that is now selected
   * @param modifier a mask of active modifies 
   */
  public void mousePress(EnumModelViewAction action, int modifier) {
    switch(action) {
      case Selection:
        Visual v = getUpmostVisualInFocus();
        if(v == null || ! selectedVisuals.contains(v)) {
          selectionStartPoint = mouseLocation.getLocation();
          break;
        }
        break;
      case NewPlace:
        Place p = ((Net) model).addDefaultPlace();
        Visual pv = addVisualFor(p);
        ((PlaceVisual) pv).setCenter(mouseLocation);
        setSelectionTo(pv);
        break;
      case NewTransition:
        Transition t = ((Net) model).addDefaultTransition();
        Visual tv = addVisualFor(t);
        ((TransitionVisual) tv).setCenter(mouseLocation);
        setSelectionTo(tv);
        break;
      case NewArc:
        /* first set start point of arc if not yet set */
        if(arcStart == null) {
          if(upmostVisualInFocus instanceof NodeVisual) {
            arcStart = (NodeVisual) upmostVisualInFocus;
          }
          return;
          
        /* second set the end point, if start point already is set */
        } else {
          if(upmostVisualInFocus instanceof NodeVisual) {
            arcEnd = (NodeVisual) upmostVisualInFocus;
          } else {
            arcStart = null;
          }
        }
        /* start and end point exists -> try to create an arc */
        if(arcStart != null && arcEnd != null) {
          Visual av = tryToCreateArcFromSelectedNodes();
          if(av != null) {
            repaint();
            setSelectionTo(av);
          }
          arcStart = null;
          arcEnd = null;
        }
        break;
    }
  }
  
  /**
   * This method should be invoked (usually by the {@link ModelViewCanvas})
   * when the mouse was released on the {@link ModelViewCanvas}, displaying this <code>ModelView</code>.
   * @param action the action that is now selected
   * @param modifier a mask of active modifies 
   */
  public void mouseRelease(EnumModelViewAction action, int modifier) {
    switch(action) {
      case Selection:
        /* update selection:
         * - if CTRL is pressed: add elements in the area that were not in the selection before and remove
         * elements that were there before
         * - if SHIFT is pressed: add all elements in the area, even if they were there before
         */
        if(selectionStartPoint != null) {
          boolean xor = ((modifier & MouseEvent.CTRL_DOWN_MASK) != 0);
          boolean add = ((modifier & MouseEvent.SHIFT_DOWN_MASK) != 0) | xor;
          updateSelection(add, xor);
          selectionStartPoint = null;
          selectionEndPoint = null;
          break;
        }
        notifyMovedVisuals();
        break;
    }
  }

  @Override
  public String getAMLTagName() {
    return "ModelView";
  }

  @Override
  public AMLNode getAMLNode() {
    AMLNode node = new AMLNode(getAMLTagName());
    for(Visual visual : modelElements.keySet()) {
      AMLNode visualNode = visual.getAMLNode();
      visualNode.putAttribute("zOrder", visualZOrders.get(visual));
      visualNode.putAttribute("modelElement", modelElements.get(visual).getId());
      node.addChild(visualNode);
    }
    return node;
  }

  @Override
  public void readAMLNode(AMLNode node) {
    for(AMLNode visualNode : node.getChildren("Visual")) {
      ModelElement modelElement = model.getModelElementById(visualNode.getAttributeInt("modelElement"));
      if(modelElement instanceof ArcCollection) continue;
      Visual visual = factory.createVisual(modelElement);
      int zOrder = visualNode.getAttributeInt("zOrder");
      visual.readAMLNode(visualNode);
      addVisual(modelElement, visual, zOrder);
    }
    
    for(AMLNode visualNode : node.getChildren("Visual")) {
      ModelElement modelElement = model.getModelElementById(visualNode.getAttributeInt("modelElement"));
      if(! (modelElement instanceof ArcCollection)) continue;
      Visual visual = factory.createVisual(modelElement);
      int zOrder = visualNode.getAttributeInt("zOrder");
      visual.readAMLNode(visualNode);
      addVisual(modelElement, visual, zOrder);
    }
  }
  
  @Override
  public void visualDataChanged(Visual v) {
    ui.visualElementHasChangedData(modelElements.get(v), v);
  }
}
