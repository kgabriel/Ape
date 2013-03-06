/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.util.aml;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
