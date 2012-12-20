/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic;

import ape.petri.generic.net.EnumNetType;
import ape.petri.validity.InvalidityReason;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author Gabriel
 */
public interface Model extends Serializable {
  
  public EnumModelType getModelType();
  
  public EnumNetType getNetType();
  
  public Collection<ModelElement> getAllElements();
  
  /**
   * Returns whether this model is valid with respect to its theoretical definition.
   * This method should always be consistent with {@link Model#getInvalidityReasons()}.
   * @see Model#getInvalidityReasons()
   * @return <code>true</code> if this <code>Model</code> is a valid model; otherwise, if
   * there are reasons that this in not a valid model, it returns <code>false</code>
   */
  public boolean isValid();
  
  /**
   * Returns all reasons that make this model invalid with respect to its theoretical definition.
   * If there are different possible representations for one reason (for example, a loop
   * in a graphical model that should be acyclic can have different locations), then the
   * method may return only one of the possible representations. 
   * @return <code>null</code> if the model is valid; otherwise it returns a collection
   * of all reasons, preventing this model from being invalid
   */
  public Collection<InvalidityReason> getInvalidityReasons();
}
