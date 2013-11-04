/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.ahl.instantiation;

import ape.petri.ahl.AHLTransitionData;
import ape.petri.exception.NetTypeException;
import ape.petri.generic.EnumNetType;
import ape.petri.generic.net.*;
import ape.petri.validity.EnumInvalidityType;
import ape.petri.validity.InvalidityReason;
import ape.petri.validity.Validity;
import ape.prolog.Atom;
import ape.prolog.PrologNet;
import ape.prolog.exception.PrologException;
import ape.prolog.exception.PrologOperationArityException;
import ape.prolog.exception.PrologOperationNotDeclaredException;
import ape.prolog.exception.PrologTypeClashException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Gabriel
 */
public class AHLInstantiation extends Net {
  
  private PrologNet prologNet = null;
  private int prologNetNumber;
    
  public AHLInstantiation() {
    super(EnumNetType.AHLInstantiation, new AHLInstDataFactory());    
    this.prologNetNumber = 0;
  }

  @Override
  public Validity validate() {
    Validity globalValidity = new Validity(true);
    globalValidity.addValidity(validatePlaces());
    Map<Transition,Map<Atom,Atom>> vars = new HashMap<>();
    globalValidity.addValidity(getTypedVariables(vars));
    return globalValidity;
  }
  
  public Validity validatePlaces() {
    Validity globalValidity = new Validity(true);
    for(Place p : places) {
      AHLInstPlaceData data = (AHLInstPlaceData) p.getData();
      Validity localValidity = data.validate(p);
      globalValidity.addValidity(localValidity);
    }
    return globalValidity;
  }
  
  public Validity getTypedVariables(Map<Transition,Map<Atom,Atom>> typedVars) {
    Validity globalValidity = new Validity(true);
    for(Transition t : transitions) {
      Validity localValidity = new Validity(true);
      AHLInstTransitionData data = (AHLInstTransitionData) t.getData();
      Map<Atom,Atom> typedTransitionVars = new HashMap<>();
      try {
        getTypedVariables(t, typedTransitionVars);
        localValidity.addValidity(data.validate(t, typedTransitionVars));
      } catch(PrologTypeClashException typeClashException) {
        localValidity.addInvalidityReason(
                new InvalidityReason(EnumInvalidityType.InstVarTypeClash, "Transition '" + t
                + "', Variable '" + typeClashException.getVariable() + "', Types: '" + typeClashException.getType1() + "' and '"
                + typeClashException.getType2() + "'"));
      } catch(PrologOperationNotDeclaredException operationNotDeclaredException) {
        localValidity.addInvalidityReason(
                new InvalidityReason(EnumInvalidityType.InstOperationNotDeclared, "Transition '" + t
                + "', Operation '" + operationNotDeclaredException.getOperation() + "'"));
      } catch(PrologOperationArityException operationArityException) {
        localValidity.addInvalidityReason(
                new InvalidityReason(EnumInvalidityType.InstOperationArity, "Transition '" + t
                + "', Operation '" + operationArityException.getOperation() + "' has arity " + 
                operationArityException.getArity() + ", but " + operationArityException.getArguments() + " arguments"));
      }
      typedVars.put(t, typedTransitionVars); 
      globalValidity.addValidity(localValidity);
    }
    
    return globalValidity;
  }

  public Set<Atom> getVariables(Transition t) {
    TransitionData tData = t.getData();
    if(tData.getNetType() != EnumNetType.AHLNet && tData.getNetType() != EnumNetType.AHLInstantiation) {
      throw new NetTypeException(EnumNetType.AHLNet, tData.getNetType());
    }
    AHLTransitionData data = (AHLTransitionData) tData;
    
    Set<Atom> vars = data.getConditionVariables();
    
    for(ArcCollection preCollection : t.getIncomingArcs()) {
      for(ArcElement preArc : preCollection) {
        AHLInstArcElementData preData = (AHLInstArcElementData) preArc.getData();
        vars.add(preData.getInscription());
      }
    }
    
    for(ArcCollection postCollection : t.getOutgoingArcs()) {
      for(ArcElement postArc : postCollection) {
        AHLInstArcElementData postData = (AHLInstArcElementData) postArc.getData();
        vars.add(postData.getInscription());
      }
    }
    
    return vars;
  }

  
  public void getTypedVariables(Transition t, Map<Atom,Atom> typedVars) throws PrologException {
    TransitionData tData = t.getData();
    if(tData.getNetType() != EnumNetType.AHLInstantiation) {
      throw new NetTypeException(EnumNetType.AHLInstantiation, tData.getNetType());
    }
    AHLInstTransitionData data = (AHLInstTransitionData) tData;
    
    for(ArcCollection preCollection : t.getIncomingArcs()) {
      addVarsFromArcCollection(preCollection, typedVars);
    }
    
    for(ArcCollection postCollection : t.getOutgoingArcs()) {
      addVarsFromArcCollection(postCollection, typedVars);
    }
    
    data.getTypedConditionVariables(typedVars);
  }
  
  private void addVarsFromArcCollection(ArcCollection coll, Map<Atom,Atom> typedVars) {
    for(ArcElement arc : coll) {
      AHLInstArcElementData data = (AHLInstArcElementData) arc.getData();
      Atom var = data.getInscription();
      Place place = arc.getPlace();
      AHLInstPlaceData placeData = (AHLInstPlaceData) place.getData();
      Atom type = placeData.getType();
      if(typedVars.containsKey(var)) {
        Atom existingType = typedVars.get(var);
        if(! existingType.equals(type)) {
          throw new PrologTypeClashException(var, existingType, type);
        }
      }
      typedVars.put(var, type);
    }
  }

  public PrologNet getPrologNet() {
    return prologNet;
  }
  
  public void createPrologNet() {
    prologNet = new PrologNet(this, this.toString() + "_" + prologNetNumber++);
  }
}
