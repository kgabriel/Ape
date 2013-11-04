/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.util.aml;

import ape.util.ResourceIO;
import java.io.*;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class ApeML {

  public static List<AMLNode> read(String fileName) {
    AMLParser parser = new AMLParser(readStringFromFile(fileName));
    try {
      parser.parse();
    } catch(AMLParserException e) {
      System.out.println(e);
    }
    return parser.getNodes();
  }
  
  public static List<AMLNode> readResource(String resourceName) {
    return read(ResourceIO.getResourcePath(resourceName));
  }
  
  private static String readStringFromFile(String fileName) {
    String string = "";
    try {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));
      StringBuilder sb = new StringBuilder();
      String line = reader.readLine();

      while (line != null) {
          string += line + "\n";
          line = reader.readLine();
      }
    } catch(IOException e) {
      e.printStackTrace();
    }
    return string;
  }
  
  public static void write(AMLNode node, String fileName) {
    writeStringToFile(node.write(), fileName);
  }
  
  public static void writeResource(AMLNode node, String localResourcePath, String resourceName) {
    write(node, ResourceIO.getResourcePath(localResourcePath) + "/" + resourceName);
  }
  
  private static void writeStringToFile(String string, String fileName) {
    try {
      PrintWriter out = new PrintWriter(new FileWriter(fileName));
      out.print(string);
      out.close();
    } catch(IOException e) {
      e.printStackTrace();
    }
  }
}
