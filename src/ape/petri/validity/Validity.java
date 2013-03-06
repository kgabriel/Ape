/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.validity;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Gabriel
 */
public class Validity {

  private boolean valid;
  private Collection<InvalidityReason> invalidityReasons;
  
  public Validity(boolean valid) {
    this.valid = valid;
    invalidityReasons = new ArrayList<>();
  }

  public Collection<InvalidityReason> getInvalidityReasons() {
    return invalidityReasons;
  }

  public void addInvalidityReason(InvalidityReason invalidityReason) {
    invalidityReasons.add(invalidityReason);
    valid = false;
  }

  public void addInvalidityReasons(Collection<InvalidityReason> invalidityReasons) {
    this.invalidityReasons.addAll(invalidityReasons);
    if(invalidityReasons.size() > 0) valid = false;
  }
  
  public void addValidity(Validity v) {
    valid &= v.valid;
    addInvalidityReasons(v.getInvalidityReasons());
  }

  public boolean isValid() {
    return valid;
  }
  
  @Override
  public String toString() {
    if(valid) return "The model is valid.";
    
    String string = "The model is invalid due to the following reasons:\n";
    for(InvalidityReason reason : invalidityReasons) {
      string += reason.toString();
    }
    
    return string;
  }
}
