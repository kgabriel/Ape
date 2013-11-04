/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.util;

import ape.Ape;
import java.net.URL;

/**
 *
 * @author Gabriel
 */
public class ResourceIO {
  
  public static URL getResourceURL(String localResourcePath) {
    return Ape.class.getResource(localResourcePath);
  }
  
  public static String getResourcePath(String localResourcePath) {
    return getResourceURL(localResourcePath).getFile();
  }
}
