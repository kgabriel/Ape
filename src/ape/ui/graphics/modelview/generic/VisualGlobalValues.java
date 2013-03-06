/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.graphics.modelview.generic;

import java.awt.Color;

/**
 *
 * @author Gabriel
 */
public final class VisualGlobalValues {

  public static int textMargin = 5;
  public static int textLineSep = 5;
  
  public static Color arcColor = Color.black;
  public static double arcHeadSize = 12.0;
  public static Color arcLabelTextColor = Color.black;
  public static Color arcLabelBackgroundColor = new Color(255,255,252,220);
  

  public static int bendingPointDiameter = 20;
  public static Color bendingPointHoverColor = new Color(250,230,170,240);
  public static Color bendingPointServantArcHoverColor = new Color(200,250,200,210);
  public static Color bendingPointMasterArcHoverColor = new Color(250,240,180,230);
  public static Color bendingPointSelectedColor = new Color(200,250,255,240);
  
  public static int placeRadius = 15;
  public static int placeLabelDistance = 5;
  public static double placeLabelStandardPosition = 0.0; //Math.PI / 2;
  public static Color placeBackgroundColor = Color.WHITE;
  public static Color placeForegroundColor = Color.BLACK;
  
  public static int transitionWidth = 130;
  public static Color transitionBackgroundColor = Color.WHITE;
  public static Color transitionForegroundColor = Color.BLACK;
  public static Color transitionExtendedDataForegroundColor = new Color(20,70,20);
  
  public static Color modelBackgroundColor = new Color(255,255,252);
  public static Color modelSelectionColor = new Color(230,240,250,120);
  public static Color modelHoverColor = new Color(240,250,240,200);
  public static Color modelSelectionBorderColor = new Color(170,200,240,230);

  public static Color modelGridColor = new Color(235,240,245);
  public static Color modelMainGridColor = new Color(220,225,230);
  public static int modelGridSize = 50;
  public static int modelMainGridSize = 500;
  
  public static double modelMaxScale = 3.0;
  public static double modelMinScale = 1.0/8.0;
  
}
