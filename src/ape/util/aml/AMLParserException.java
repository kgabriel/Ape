/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.util.aml;

/**
 *
 * @author Gabriel
 */
public class AMLParserException extends Exception {
  
  private AMLParser parser;
  private String problem;
  
  public AMLParserException(AMLParser parser, String msg) {
    this.parser = parser;
    this.problem = msg;
  }
  
  @Override
  public String getMessage() {
    String msg = "Error on parsing AML!\n";
    msg += "Problem: " + problem + "\n";
    msg += "Line: " + parser.getLineNumber() + ", ";
    int pos = parser.getPositionInLine();
    msg += "Position: " + pos + "\n";
    msg += parser.getCurrentLine();
    for(int i=0;i<pos;i++) msg += " ";
    msg += "^";
    return msg;
  }
}
