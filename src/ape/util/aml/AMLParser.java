/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.util.aml;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gabriel
 */
public class AMLParser {

  private String input;
  private int position;
  private int line;
  private int lastLineBreakPosition;
  private List<AMLNode> nodes;
  
  public AMLParser(String input) {
    this.input = input;
    this.position = 0;
    this.line = 1;
    this.lastLineBreakPosition = 0;
    nodes = new ArrayList<>();
  }
  
  public List<AMLNode> getNodes() {
    return nodes;
  }
  
  public void parse() throws AMLParserException {
    consumeWhitespaces();
    while(! isEndOfInput()) {
      nodes.add(parseNode());
      consumeWhitespaces();
    }
  }
  
  private AMLNode parseNode() throws AMLParserException {
    AMLNode node = new AMLNode();
    parseOpenTag(node);
    consumeWhitespaces();
    while(! lookAhead("</")) {
      node.addChild(parseNode());
      consumeWhitespaces();
    }
    parseCloseTag(node);
    return node;
  } 

  private void parseOpenTag(AMLNode node) throws AMLParserException {
    consumeNextChar('<');
    node.setTagName(parseIdentifier());
    consumeWhitespaces();
    while(lookupNextChar() != '>') {
      parseAttribute(node);
      consumeWhitespaces();
    }
    consumeNextChar('>');
  }
  
  private void parseCloseTag(AMLNode node) throws AMLParserException {
    consumeNextChar('<');
    consumeNextChar('/');
    consumeWhitespaces();
    consumeString(node.getTagName());
    consumeWhitespaces();
    consumeNextChar('>');
  }
  
  
  private String parseIdentifier() throws AMLParserException {
    char first = lookupNextChar();
    int start = position;
    if(! Character.isUnicodeIdentifierStart(first)) {
      throw new AMLParserException(this, "Expected identifier, but found '" + first + "' instead.");
    }
    position++;
    while(Character.isUnicodeIdentifierPart(lookupNextChar())) {
      position++;
    }
    return input.substring(start,position);
  }
  
  private void parseAttribute(AMLNode node) throws AMLParserException {
    String key = parseIdentifier();
    consumeWhitespaces();
    consumeNextChar('=');
    consumeWhitespaces();
    String value = parseQuotedString();
    node.putAttribute(key, value);
  }
  
  private String parseQuotedString() throws AMLParserException {
    consumeNextChar('"');
    int start = position;
    boolean escaped = false;
    char nextChar;
    do {
      nextChar = consumeNextChar();
      if(nextChar == '\\') escaped = true;
    } while(nextChar != '"' || escaped);
    return input.substring(start, position-1);
  }
  
  private boolean isEndOfInput() {
    return position == input.length();
  }
  
  private char lookupNextChar() throws AMLParserException {
    checkForFileEnd();
    return input.charAt(position);
  }
  
  private char consumeNextChar() throws AMLParserException {
    checkForFileEnd();
    return input.charAt(position++);
  }
  
  private void consumeNextChar(char expectedChar) throws AMLParserException {
    char nextChar = consumeNextChar();
    if(expectedChar != nextChar) {
      throw new AMLParserException(this, "Expected '" + expectedChar + "', but found '" + nextChar + "' instead.");
    }
  }
  
  private void consumeWhitespaces() throws AMLParserException {
    char nextChar;
    while(! isEndOfInput() && Character.isWhitespace(nextChar = lookupNextChar())) {
      if(nextChar == '\n') {
        lastLineBreakPosition = line;
        line++;
      }
      position++;
    }
  }
  
  private boolean lookAhead(String expectedString) {
    return input.regionMatches(false, position, expectedString, 0, expectedString.length());
  }
  
  private void consumeString(String expectedString) throws AMLParserException {
    int length = expectedString.length();
    if(! lookAhead(expectedString)) {
      throw new AMLParserException(this, "Expected identifier '" + expectedString + "'.");
    }
    position += length;
  }
  
  private void checkForFileEnd() throws AMLParserException {
    if(position >= input.length()) {
      throw new AMLParserException(this, "Unexpected end of input.");
    }
  }
  
  public int getLineNumber() {
    return line;
  }
  
  public int getPositionInLine() {
    return position - lastLineBreakPosition;
  }

  public String getCurrentLine() {
    int lineEnd = input.indexOf('\n',lastLineBreakPosition);
    if(lineEnd == -1) return input.substring(lastLineBreakPosition);
    return input.substring(lastLineBreakPosition, lineEnd);
  }
}
