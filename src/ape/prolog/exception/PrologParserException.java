/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.prolog.exception;

/**
 *
 * @author Gabriel
 */
public class PrologParserException extends RuntimeException {
  
  /**
   * Creates a new Prolog parser exception with the specific message.
   * @param msg a message that should describe the cause of the exception
   */
  public PrologParserException(String msg) {
    super("Error on parsing Prolog term: " + msg);
  }

  /**
   * Creates a new Prolog parser exception with the specific message.
   * @param msg a message that should describe the cause of the exception
   */
  public PrologParserException(String str, int pos, String reason) {
    super(message(str,pos,reason));
  }
  
  private static String message(String str, int pos, String reason) {
    String msg = "Error on parsing Prolog term:\n";
    msg += "Input: " + str + "\n";
    msg += "Position: " + pos + "\n";
    msg += "Problem: " + reason;
    return msg;
  }
}
