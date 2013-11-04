/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.ahl.instantiation;

import ape.petri.ahl.AHLTransitionData;
import ape.petri.generic.net.Transition;
import ape.petri.validity.EnumInvalidityType;
import ape.petri.validity.InvalidityReason;
import ape.petri.validity.Validity;
import ape.prolog.Atom;
import ape.prolog.Compound;
import ape.prolog.Prolog;
import ape.prolog.ValueTerm;
import ape.prolog.exception.PrologOperationArityException;
import ape.prolog.exception.PrologOperationNotDeclaredException;
import ape.prolog.exception.PrologParserException;
import ape.prolog.exception.PrologTypeClashException;
import ape.util.EnumPropertyType;
import ape.util.Property;
import ape.util.aml.AMLNode;
import java.util.*;
import javax.swing.JOptionPane;
import jpl.Query;
import jpl.Term;
import jpl.Util;

/**
 *
 * @author Gabriel
 */
public class AHLInstTransitionData extends AHLTransitionData {
  
  private Map<Atom,ValueTerm> assignment;

  public AHLInstTransitionData(String name, Set<Compound> conditions, Map<Atom,ValueTerm> assignment) {
    super(name, conditions);
    this.assignment = assignment;
  }
  
  public Map<Atom,ValueTerm> getAssignment() {
    return assignment;
  }
  
  public void setAssignment(Map<Atom,ValueTerm> assignment) {
    this.assignment.clear();
    this.assignment.putAll(assignment);
    dataHasChanged();
  }

  public Validity validate(Transition parent, Map<Atom,Atom> transitionVars) {
    Validity validity = new Validity(true);
    
    /* check if assignment is only partial */
    if(! assignment.keySet().containsAll(transitionVars.keySet())) {
      validity.addInvalidityReason(
              new InvalidityReason(EnumInvalidityType.InstTransitionAssignmentPartial, "Transition '" + parent.toString() + "'"));
    }
    
    return validity;
  }

  public void getTypedConditionVariables(Map<Atom,Atom> typedVars) {
    Map<Atom,Set<Atom>> connections = new HashMap<>();
    for(Compound cond : getConditions()) {
      String functor = cond.name();
      if(functor.equals("=")) {
        Atom var1 = cond.arg(1);
        Atom var2 = cond.arg(2);
        addTypeConnection(connections, var1, var2);
        addTypeConnection(connections, var2, var1);
        continue;
      } 
      
      Query functorQuery = new Query("op_declaration", new Term[]{ new jpl.Atom(functor), new jpl.Variable("X") });
      boolean solutionExists = functorQuery.hasMoreSolutions();
      if(! solutionExists) {
        throw new PrologOperationNotDeclaredException(functor);
      }
      Map solution = functorQuery.nextSolution();
      Term[] functorTypes = Util.listToTermArray((Term) solution.get("X"));
      if(functorTypes.length != cond.arity()) {
        throw new PrologOperationArityException(functor, functorTypes.length, cond.arity());
      }
      for(int i=0;i<functorTypes.length;i++) {
        Atom var = cond.arg(i+1);
        Atom typeAtom = new Atom(functorTypes[i].toString());
        if(typedVars.containsKey(var)) {
          Atom existingType = typedVars.get(var);
          if(! existingType.equals(typeAtom) && ! existingType.equals(Atom.unknown_type)) {
            throw new PrologTypeClashException(var.toString(), existingType.toString(), typeAtom.toString());
          }
        }
        typedVars.put(var, typeAtom);
      }
    }
    
    for(Atom var : connections.keySet()) {
      if(typedVars.containsKey(var)) {
        Atom type = typedVars.get(var);
        propagateTypeViaConnections(typedVars, connections, var, type);
      } else {
        typedVars.put(var, Atom.unknown_type);
      }
    }
  }
  
  private void propagateTypeViaConnections(Map<Atom,Atom> typedVars, Map<Atom,Set<Atom>> connections, Atom var, Atom type) {
    for(Atom var2 : connections.get(var)) {
      if(typedVars.containsKey(var2)) {
        Atom type2 = typedVars.get(var2);
        if(! type.equals(type2)) {
          if(! type2.equals(Atom.unknown_type)) {
          throw new PrologTypeClashException(var2, type, type2);
          } else {
            typedVars.put(var2,type);
            propagateTypeViaConnections(typedVars, connections, var2, type);
          }
        }
      }
    }
  }
  
  private void addTypeConnection(Map<Atom,Set<Atom>> connections, Atom var1, Atom var2) {
    if(connections.containsKey(var1)) {
      connections.get(var1).add(var2);
    } else {
      Set<Atom> set = new HashSet<>();
      set.add(var2);
      connections.put(var1, set);
    }
  }

  @Override
  public List<Property> getProperties() {
    List<Property> properties = super.getProperties();
    properties.add(new Property(Property.CATEGORY_VALUES, this, EnumPropertyType.MultiLineText, "Assignment") {

      @Override
      public Object getValue() {
        Map<Atom,ValueTerm> assignment = getAssignment();
        String value = "";
        boolean first = true;
        for(Atom var : assignment.keySet()) {
          if(! first) value += ",\n";
          value += var + "=" + assignment.get(var);
          first = false;
        }
        return value;
      }

      @Override
      public void setValue(Object value) {
        Map<Atom,ValueTerm> assignment = Prolog.parseAssignmentSequence((String) value);
        setAssignment(assignment);
      }
    });
    return properties;
  }

  @Override
  public AMLNode getAMLNode() {
    AMLNode node = super.getAMLNode();
    for(Atom assKey : assignment.keySet()) {
      AMLNode assNode = new AMLNode("Assignment");
      assNode.putAttribute("variable", assKey.toString());
      assNode.putAttribute("value", assignment.get(assKey).toString());
      node.addChild(assNode);
    }
    return node;
  }

  @Override
  public void readAMLNode(AMLNode node) {
    super.readAMLNode(node);
    for(AMLNode assNode : node.getChildren("Assignment")) {
      Atom key = new Atom(assNode.getAttribute("variable"));
      ValueTerm value = new ValueTerm(assNode.getAttribute("value"));
      assignment.put(key, value);
    }
  }
  
}
