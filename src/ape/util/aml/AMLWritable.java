/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.util.aml;

/**
 *
 * @author Gabriel
 */
public interface AMLWritable {
  
  public String getAMLTagName();
  public AMLNode getAMLNode();
  public void readAMLNode(AMLNode node);
}
