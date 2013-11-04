/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.util.aml;

import java.awt.Color;
import java.util.*;

/**
 *
 * @author Gabriel
 */
public class AMLNode {

  String tagName;
  Map<String,String> attributes;
  Map<String,List<AMLNode>> children;
  
  public AMLNode() {
    this(null);
  }
  
  public AMLNode(String tagName) {
    if(tagName != null) checkIdentifier(tagName);
    this.tagName = tagName;
    this.attributes = new HashMap<>();
    this.children = new HashMap<>();
  }

  public Map<String, String> getAttributes() {
    return new HashMap<>(attributes);
  }
  
  public String getAttribute(String key) {
    return attributes.get(key);
  }
  
  public int getAttributeInt(String key) {
    return Integer.parseInt(getAttribute(key));
  }
  
  public boolean getAttributeBoolean(String key) {
    String value = getAttribute(key);
    return value.equals("1") || value.equals("true");
  }
  
  public Color getAttributeColor(String key) {
    return Color.decode(getAttribute(key));
  }
  
  public double getAttributeDouble(String key) {
    return Double.parseDouble(getAttribute(key));
  }
  
  public void putAttribute(String key, String value) {
    checkIdentifier(key);
    this.attributes.put(key, value);
  }
  
  public void putAttribute(String key, int value) {
    putAttribute(key, "" + value);
  }
  
  public void putAttribute(String key, boolean value) {
    putAttribute(key, value ? "1" : "0");
  }
  
  public void putAttribute(String key, Color value) {
    putAttribute(key, Integer.toHexString(value.getRGB()));
  }
  
  public void putAttribute(String key, double value) {
    putAttribute(key, "" + value);
  }

  public List<AMLNode> getChildren(String tagName) {
    List<AMLNode> list = new ArrayList<>();
    if(children.containsKey(tagName.toLowerCase())) {
      list.addAll(children.get(tagName.toLowerCase()));
    }
    return list;
  }
  
  public AMLNode getFirstChild(String tagName) {
    List<AMLNode> list = getChildren(tagName);
    if(list.isEmpty()) return null;
    return list.get(0);
  }
  
  public boolean hasTagName(String tagName) {
    return this.tagName.equalsIgnoreCase(tagName);
  }
  
  public void addChild(AMLNode child) {
    List<AMLNode> list = children.get(child.tagName.toLowerCase());
    if(list == null) {
      list = new ArrayList<>();
      children.put(child.tagName.toLowerCase(), list);
    }
    list.add(child);
  }

  public void addChildren(Collection<AMLNode> children) {
    for(AMLNode child : children) {
      addChild(child);
    }
  }

  public String getTagName() {
    return tagName;
  }

  public void setTagName(String name) {
    checkIdentifier(name);
    this.tagName = name;
  }
  
  public String write() {
    return write(0);
  }

  public String write(int indentWidth) {
    String indentation = writeIndent(indentWidth);
    String output = indentation + "<" + tagName;
    for(String attributeKey : attributes.keySet()) {
      output += writeAttribute(attributeKey, attributes.get(attributeKey));
    }
    if(children.isEmpty()) {
      output += " />\n";
    } else {
      output += ">\n";
      for(List<AMLNode> childList : children.values()) {
        for(AMLNode child : childList) {
          output += child.write(indentWidth + 1);
        }
      }
      output += indentation + "</" + tagName + ">\n";
    }
    return output;
  }
    
  private String writeIndent(int indentWidth) {
    String indentation = "";
    for(int i=0;i<indentWidth;i++) {
      indentation += "  ";
    }
    return indentation;
  }
  
  private static String writeAttribute(String key, String value) {
    return " " + key + "=\"" + maskAttributeValue(value) + "\"";
  }
  
  private static String maskAttributeValue(String value) {
    return value.replace("\\", "\\\\");
  }
  
  public static boolean isValidIdentifier(String name) {
    if(! Character.isUnicodeIdentifierStart(name.charAt(0))) return false;
    for(int i=1;i<name.length();i++) {
      if(! Character.isUnicodeIdentifierPart(name.charAt(i))) return false;
    }
    return true;
  }
  
  public static void checkIdentifier(String name) {
    if(! isValidIdentifier(name)) throw new IdentifierException(name);
  }
}
