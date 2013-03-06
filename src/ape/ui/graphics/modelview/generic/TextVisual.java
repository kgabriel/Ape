/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.generic;

import ape.util.aml.AMLNode;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a {@link Visual} that contains text. It is not intended to work
 * as independent visual component, but rather as a subcomponent for <code>Visual</code>s
 * that contain text.
 * <br />
 * A visual contains a {@link TextVisual#text} value and a list of 
 * {@link java.awt.font.TextLayout}s, representing the single lines of the text as they
 * are visualized in the component.
 * <br />
 * Moreover, a <code>TextVisual</code> can be <i>compact</i> or <i>non-compact</i>.
 * A <i>compact</i> <code>TextVisual</code> will always be only as big as necessary, which means
 * that the component's inner bounds are always vertically and horizontally fitting to the bounds
 * of its contained text. Also, a compact text visual will never break a line unless it
 * is explicitly demanded (that is, by insertion of a line break character).
 * <br />
 * In contrast, a <i>non-compact</i> <code>TextVisual</code> does not change its horizontal
 * width, but it will break lines at its width (additionally to usual line breaks in the text).
 * Like the compact text visual, also the non-compact visual will always adapt its 
 * height such that it fits to the bounds of the contained text.
 * @author Gabriel
 */
public class TextVisual extends Visual {

  /** The text contained in this <code>Visual</code>. */
  protected String text;
  
  /** The visual representation of the single lines of the text. These lines are drawn
   * on this <code>Visual</code>'s graphics, vertically separated by a distance of
   * {@link VisualGlobalValues#textLineSep}.
   */
  protected transient List<TextLayout> lines;
  
  /** A boolean value whether this text visual is compact or not. Compact visuals
   * always adapt their size (horizontally and vertically) to the size the contained
   * text. Non-compact visuals only adapt their vertical size, and break lines at the
   * width.
   */
  protected boolean compact;
  
  /** A boolean value whether the text is centered. */
  protected boolean centered;
  
  /** The text-color of this <code>TextVisual</code>. This should be set by the <code>Visual</code>
   * having this text visual as a subcomponent.
   */
  protected Color textColor;
  
  /** The background-color of this <code>TextVisual</code>. This should be set by the <code>Visual</code>
   * having this text visual as a subcomponent.
   */
  protected Color backgroundColor;
  
  /**
   * Constructor for a new text visual with the given superior graphics. The boundaries
   * of this visual are of zero size and it is positioned in the top-left corner. The text
   * of this visual is empty. It is compact and not centered.
   * @param superGraphics the graphics from which this <code>Visual</code> obtains its
   * own graphics
   * @see TextVisual#TextVisual(java.awt.Graphics2D, java.awt.Rectangle, java.lang.String, boolean, boolean) 
   */
  public TextVisual(Graphics2D superGraphics) {
    this(superGraphics, new Rectangle(0,0,0,0), "", true, false);
  }
  
  /**
   * Constructor for a new text visual with the given superior graphics, and given boundaries. The text
   * of this visual is empty. It is compact and not centered.
   * @param superGraphics the graphics from which this <code>Visual</code> obtains its
   * own graphics
   * @param bounds the location and size of this <code>Visual</code>
   * @see TextVisual#TextVisual(java.awt.Graphics2D, java.awt.Rectangle, java.lang.String, boolean, boolean) 
   */
  public TextVisual(Graphics2D superGraphics, Rectangle bounds) {
    this(superGraphics, bounds, "", true, false);
  }
  
  /**
   * Constructor for a new text visual with the given superior graphics, and given text. The boundaries
   * of this visual are of zero size and it is positioned in the top-left corner. 
   * It is compact and not centered.
   * @param superGraphics the graphics from which this <code>Visual</code> obtains its
   * own graphics
   * @param text the text that is to be visualized by this component
   * @see TextVisual#TextVisual(java.awt.Graphics2D, java.awt.Rectangle, java.lang.String, boolean, boolean) 
   */
  public TextVisual(Graphics2D superGraphics, String text) {
    this(superGraphics, new Rectangle(0,0,0,0), text, true, false);
  }
  
  /**
   * Constructor for a new text visual with the given superior graphics, and given text. The boundaries
   * of this visual are of zero size and it is positioned in the top-left corner. 
   * It is not centered.
   * @param superGraphics the graphics from which this <code>Visual</code> obtains its
   * own graphics
   * @param text the text that is to be visualized by this component
   * @param compact <code>true</code> if this <code>Visual</code> is compact
   * @see TextVisual#TextVisual(java.awt.Graphics2D, java.awt.Rectangle, java.lang.String, boolean, boolean) 
   */
  public TextVisual(Graphics2D superGraphics, String text, boolean compact) {
    this(superGraphics, new Rectangle(0,0,0,0), text, compact, false);
  }
  
  
  /**
   * Constructor for a new text visual with the given superior graphics, and given boundaries and text. 
   * @param superGraphics the graphics from which this <code>Visual</code> obtains its
   * own graphics
   * @param bounds the location and size of this <code>Visual</code>
   * @param text the text that is to be visualized by this component
   * @param compact <code>true</code> if this <code>Visual</code> is compact
   * @param centered <code>true</code> if this <code>Visual</code> is centered
   * @see TextVisual#TextVisual(java.awt.Graphics2D, java.awt.Rectangle, java.lang.String, boolean, boolean) 
   */
  public TextVisual(Graphics2D superGraphics, Rectangle bounds, String text, boolean compact, boolean centered) {
    super(superGraphics, bounds);
    this.text = text;
    this.compact = compact;
    this.centered = centered;
    
    /*
     * This can probably be removed.
     * A text element should not be resized directly by a user. Instead, the resizing
     * is handled by the component that contains it.
     */
    setResizable(! compact,false);
    
    /* set color to some visible dummy value;
     * it is the best when it is set explicitly for each component
     */
    this.textColor = Color.BLUE;
    updateContent();
  }
  
  
  
  /**
   * Sets the text of this component. The text is sliced into lines, and the
   * size of this component is adapted, according to its compactness.
   * @param text the text to be set
   */
  public void setText(String text) {
    this.text = text;
    updateContent();
  }

  public String getText() {
    return text;
  }
  
  

  /**
   * Returns whether the text in this component is centered.
   * @return <code>true</code> if the text is centered
   */
  public boolean isCentered() {
    return centered;
  }

  /**
   * Sets whether the text is this component is center.
   * @param centered <code>true</code> for centered text
   */
  public void setCentered(boolean centered) {
    this.centered = centered;
  }

  /**
   * Returns whether this component is compact. Compact text visuals
   * always adapt their size (horizontally and vertically) to the size the contained
   * text. Non-compact visuals only adapt their vertical size, and break lines at the
   * width.
   * @return <code>true</code> if this component is compact
   */
  public boolean isCompact() {
    return compact;
  }

  /**
   * Sets whether this component is compact.
   * @param compact <code>true</code> for a compact component
   * @see TextVisual#isCompact() 
   */
  public void setCompact(boolean compact) {
    this.compact = compact;
  }

  /**
   * Returns the text color set for this component.
   * @return the text color 
   */
  public Color getTextColor() {
    return textColor;
  }

  /** 
   * Sets the text color for this component.
   * @param textColor the color to set
   */
  public void setTextColor(Color textColor) {
    this.textColor = textColor;
  }

  /**
   * Returns the background color set for this component.
   * @return the background color 
   */
  public Color getBackgroundColor() {
    return backgroundColor;
  }
  
  /** 
   * Sets the background color for this component.
   * @param color the color to set
   */
  public void setBackgroundColor(Color color) {
    this.backgroundColor = color;
  }

  /**
   * Updates the (visual text) content of this component. This means, that the text
   * is wrapped, and the size of the component is adapted, according to this component's compactness.
   */
  private void updateContent() {
    if(superGraphics == null) return;
    wrapText();
    Dimension d = drawText(true);
    if(compact) {
      fitToInnerSize(d.width, d.height);
    } else {
      fitToInnerHeight(d.height);
    }
  }

  /**
   * Overridden <code>Visual</code> method which is called on resizing the component.
   * It updates the (visual text) content of this component. This means, that the text
   * is wrapped, and the size of the component is adapted, according to this component's compactness.
   */
  @Override
  protected void updateOnResize() {
    updateContent();
  }

  /**
   * Overridden <code>Visual</code> method which is called on moving the component.
   * It does nothing.
   */
  @Override
  protected void updateOnMove() {}
  

  /**
   * Adapts the size of this component such that the specified size is the component's inner size.
   * The inner size is what is within the outer size (width and height of this component's bounds)
   * inside the text-margin. This method is called by compact components, when updating the
   * content.
   * @param width the new inner width of this component
   * @param height the new inner height of this component
   */
  private void fitToInnerSize(int width, int height) {
    width += 2 * VisualGlobalValues.textMargin;
    height += 2 * VisualGlobalValues.textMargin;
    setSize(width, height);
  }
  
  
  /**
   * Adapts the height of this component such that the specified height is the component's 
   * inner height.
   * The inner height is what is within the outer height height of this component's bounds)
   * inside the text-margin.
   * This method is called by non-compact components, when updating the content.
   * @param height the new inner height of this component
   */
  private void fitToInnerHeight(int height) {
    height += 2 * VisualGlobalValues.textMargin;
    setSize((int) getWidth(), height);
  }
  
  /**
   * Returns the inner bounds of this component which is basically the view of the bounds from
   * inside the component (how the text would see it). The size of the inner bounds is
   * the size of the outer bounds, but with subtracted margins on all four sides.
   * The position of the inner bounds is <code>(margin,margin)</code>, 
   * because in the local view of this component, the bounds start in the top-left corner.
   * @return the location <code>(margin,margin)</code> and size of the inner bounds, which is
   * basically <code>(width-2*margin, height-2*margin)</code>
   */
  protected Rectangle getInnerBounds() {
    Rectangle innerBounds = new Rectangle(0, 0, (int) getWidth(), (int) getHeight());
    innerBounds.grow(-VisualGlobalValues.textMargin, -VisualGlobalValues.textMargin);
    return innerBounds;
  }
  
  /**
   * Wraps the text according to the compactness.
   */
  private void wrapText() {
    if(compact) {
      wrapTextOnLineBreak();
    } else {
      wrapTextOnWidth();
    }
  }
  
  /**
   * Wraps text only on line breaks, and stores it in the {@link TextVisual#lines} list.
   */
  private void wrapTextOnLineBreak() {
    lines = new ArrayList<>();
    if(text == null || text.length() == 0) return;
    Font font = graphics.getFont();
    FontRenderContext fontRenderContext = graphics.getFontRenderContext();
    TextLayout layout;
    int pos = 0;
    boolean stop = false;
    while(!stop) {
      int nextBreak = text.indexOf("\n", pos + 1);
      if(nextBreak == -1) {
        stop = true;
        nextBreak = text.length();
      }
      layout = new TextLayout(text.substring(pos, nextBreak),font,fontRenderContext);
      pos = nextBreak;
      lines.add(layout);
    }
  }
  
  /**
   * Wraps text on the width of the component, and also on usual line breaks, and
   * stores it in the {@link TextVisual#lines} list.
   */
  private void wrapTextOnWidth() {
    lines = new ArrayList<>();
    if(text == null || text.length() == 0) return;
    int wrappingWidth = getInnerBounds().width;
    AttributedCharacterIterator charIterator = new AttributedString(text).getIterator();
    FontRenderContext fontRenderContext = graphics.getFontRenderContext();
    LineBreakMeasurer lineBreakMeasurer = new LineBreakMeasurer(charIterator, fontRenderContext);
    TextLayout layout;
    while(true) {
      int pos = lineBreakMeasurer.getPosition();
      int nextBreak = text.indexOf("\n", pos + 1);
      if(nextBreak != -1) {
        layout = lineBreakMeasurer.nextLayout(wrappingWidth,nextBreak,false);
      } else {
        layout = lineBreakMeasurer.nextLayout(wrappingWidth);
      }
      if(layout == null) break;
      lines.add(layout);
    }
  }

  /**
   * Draws the lines of text in this component on its {@link Visual#graphics}. The text is
   * drawn inside the inner bounds of this component. In front of every line except for the
   * first line there is a vertical space of length {@link VisualGlobalValues#textLineSep}.
   * @param airDraw when this is <code>true</code>, the text is only drawn into air; that is,
   * all calculations are done, but the graphics of the component remains untouched
   * @return the width and height, the drawn text occupies
   * @see TextVisual#getInnerBounds() 
   */
  protected Dimension drawText(boolean airDraw) {
    int yOffset = 0;
    int width = 0;
    boolean firstLine = true;
    Rectangle innerBounds = getInnerBounds();
    if(lines == null) wrapText();
    for(TextLayout line : lines) {
      Rectangle2D lineBounds = line.getBounds();
      yOffset += (int) lineBounds.getHeight();
      if(firstLine) {
        firstLine = false;
      } else {
        yOffset += VisualGlobalValues.textLineSep;
      }
      int lineWidth = (int) lineBounds.getWidth();
      if(lineWidth > width) width = lineWidth;
      int xOffset = innerBounds.x + (centered ? (innerBounds.width - lineWidth) / 2 : 0);
      if(! airDraw) {
        /* for some unknown reason, the TextLayout.draw-method sometimes prduces a
         * NullPointerException (even if it worked before, and nothing changed); 
         * I decided to try again and ignore it if it fails again...
         */
        try {
          line.draw(graphics, xOffset, innerBounds.y + yOffset);
        } catch(NullPointerException e) {
          System.err.println("TextLayout crashed. Try one more time...");
          try {
            line.draw(graphics, xOffset, innerBounds.y + yOffset);
          } catch(NullPointerException e2) {
            System.err.println("TextLayout crashed again. Some TextVisual was not drawn correctly.");
          }
        }
      }
    }
    return new Dimension(width, yOffset);
  }
  
  /**
   * Draws this text on its graphics. It sets the color of the graphics to this component's
   * {@link TextVisual#textColor} and calls {@link TextVisual#drawText(boolean)} without using
   * the air-drawing option.
   */
  @Override
  public void draw(int status) {
    if(backgroundColor != null) {
      graphics.setColor(backgroundColor);
      graphics.fillRect(0, 0, getWidth(), getHeight());
    }
    graphics.setColor(textColor);
    drawText(false);
  }

  @Override
  protected void updateOnUserMove() {}

  @Override
  protected void updateOnUserMoveFinished() {}

  @Override
  protected void updateOnUserResize() {}

  @Override
  public Shape getShape() {
    return getBounds();
  }

  @Override
  protected void destroy() {}

  @Override
  public String getElementTypeName() {
    return "Text";
  }

  @Override
  public AMLNode getAMLNode() {
    AMLNode node = super.getAMLNode();
    node.putAttribute("elementType", getElementTypeName());
    node.putAttribute("compact", compact);
    node.putAttribute("centered", centered);
    node.putAttribute("text", text);
    node.putAttribute("backgroundColor", backgroundColor);
    node.putAttribute("textColor", textColor);
    return node;
  }

  @Override
  public void readAMLNode(AMLNode node) {
    super.readAMLNode(node);
    compact = node.getAttributeBoolean("compact");
    centered = node.getAttributeBoolean("centered");
    setText(node.getAttribute("text"));
    backgroundColor = node.getAttributeColor("backgroundColor");
    textColor = node.getAttributeColor("textColor");
  }
  
  
}
