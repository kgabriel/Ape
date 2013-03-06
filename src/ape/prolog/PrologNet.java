/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.prolog;

import ape.petri.ahl.instantiation.AHLInstArcElementData;
import ape.petri.ahl.instantiation.AHLInstPlaceData;
import ape.petri.ahl.instantiation.AHLInstTransitionData;
import ape.petri.ahl.instantiation.AHLInstantiation;
import ape.petri.generic.net.ArcCollection;
import ape.petri.generic.net.ArcElement;
import ape.petri.generic.net.Place;
import ape.petri.generic.net.Transition;
import ape.prolog.exception.PrologNetException;
import java.util.*;
import jpl.Query;
import jpl.Term;
import jpl.Util;
import jpl.Variable;

/**
 *
 * @author Gabriel
 */
public class PrologNet {

  private AHLInstantiation net;
  private String netName;
  private Query assignmentQuery;
  private boolean shuffle = false;
  
  public PrologNet(AHLInstantiation net, String netName) {
    this.net = net;
    this.netName = netName;
    init();
  }
  
  private void init() {
    createPrologNet();
    this.assignmentQuery = getAssignmentQuery();
  }
  
  private static Term placeTerm(Place p) {
    return new jpl.Integer(p.getId());
  }
  
  private static Term transitionTerm(Transition t) {
    return new jpl.Integer(t.getId());
  }
  
  private static Term pair(Term t1, Term t2) {
    return Util.termArrayToList(new Term[] {t1, t2});
  }
  
  private static Term pair(Term t1, Term t2, Term t3) {
    return Util.termArrayToList(new Term[] {t1, t2, t3});
  }
  
  private Term getNetTerm() {
    return new jpl.Atom(netName);
  }
  
  protected Term getPlacesList() {
    Collection<Place> places = net.getPlaces();
    Term[] listElements = new Term[places.size()];
    int i = 0;
    for(Place p : places) {
      AHLInstPlaceData data = (AHLInstPlaceData) p.getData();
      Atom type = data.getType();
      listElements[i++] = pair(placeTerm(p), type.jplAtom());
    }
    if(shuffle) Collections.shuffle(Arrays.asList(listElements));
    return Util.termArrayToList(listElements);
  }
  
  protected Term getTransitionsList() {
    Collection<Transition> transitions = net.getTransitions();
    Term[] listElements = new Term[transitions.size()];
    int i = 0;
    for(Transition t : transitions) {
      AHLInstTransitionData data = (AHLInstTransitionData) t.getData();
      listElements[i++] = transitionTerm(t);
    }
    if(shuffle) Collections.shuffle(Arrays.asList(listElements));
    return Util.termArrayToList(listElements);
  }
  
  protected Term getVariableList() {
    Map<Transition,Map<Atom,Atom>> transitionVarMap = new HashMap<>();
    net.getTypedVariables(transitionVarMap);
    ArrayList<Term> listElements = new ArrayList<>();
    for(Transition t : transitionVarMap.keySet()) {
      Map<Atom,Atom> varTypeMap = transitionVarMap.get(t);
      for(Atom var: varTypeMap.keySet()) {
        Atom type = varTypeMap.get(var);
        Term distinctVar = pair(var.jplAtom(), transitionTerm(t));
        Term typedDistinctVar = pair(distinctVar, type.jplAtom());
        listElements.add(typedDistinctVar);
      }
    }
    if(shuffle) Collections.shuffle(listElements);
    System.out.println("Net contains " + listElements.size() + " variables.");
    return Util.termArrayToList(listElements.toArray(new Term[listElements.size()]));
  }
  
  protected Term getPreConditionList() {
    return getPreOrPostConditionList(true);
  }
  
  protected Term getPostConditionList() {
    return getPreOrPostConditionList(false);
  }
  
  private Term getPreOrPostConditionList(boolean pre) {
    ArrayList<Term> listElements = new ArrayList<>();
    for(Transition t : net.getTransitions()) {
      Collection<ArcCollection> arcs = (pre ? t.getIncomingArcs() : t.getOutgoingArcs());
      for(ArcCollection arc : arcs) {
        for(ArcElement arcElement : arc) {
          AHLInstArcElementData data = (AHLInstArcElementData) arcElement.getData();
          Atom inscription = data.getInscription();
          Place p = arc.getPlace();
          Term pair = pair(transitionTerm(t),pair(inscription.jplAtom(),transitionTerm(t)),placeTerm(p));
          listElements.add(pair);
        }
      }
    }
    if(shuffle) Collections.shuffle(listElements);
    return Util.termArrayToList(listElements.toArray(new Term[listElements.size()]));
  }
  
  private jpl.Compound getDistinctCondition(Transition t, Compound condition) {
    Term transitionTerm = transitionTerm(t);
    Term[] args = new Term[condition.arity()];
    for(int i = 0;i<args.length;i++) {
      args[i] = pair(condition.arg(i+1).jplAtom(), transitionTerm);
    }
    return new jpl.Compound(condition.name(), args);
  }

  protected Term getConditionList() {
    List<Term> listElements = new ArrayList<>();
    for(Transition t : net.getTransitions()) {
      AHLInstTransitionData data = (AHLInstTransitionData) t.getData();
      for(Compound cond : data.getConditions()) {
        Term pair = pair(transitionTerm(t), getDistinctCondition(t, cond));
        listElements.add(pair);
      }
    }
    if(shuffle) Collections.shuffle(listElements);
    return Util.termArrayToList(listElements.toArray(new Term[listElements.size()]));
  }
  
  protected Term getPartialAssignment() {
    ArrayList<Term> listElements = new ArrayList<>();
    for(Place p : net.getPlaces()) {
      AHLInstPlaceData data = (AHLInstPlaceData) p.getData();
      ValueTerm value = data.getValue();
      if(value == null) continue;
      Term valueTerm = pair(placeTerm(p), value.jplTerm());
      listElements.add(valueTerm);
    }
    for(Transition t : net.getTransitions()) {
      AHLInstTransitionData data = (AHLInstTransitionData) t.getData();
      Map<Atom,ValueTerm> assignment = data.getAssignment();
      for(Atom var : assignment.keySet()) {
        Term typedVar = pair(var.jplAtom(), transitionTerm(t));
        Term typedVarAssignment = pair(typedVar, assignment.get(var).jplTerm());
        listElements.add(typedVarAssignment);
      }
    }
    return Util.termArrayToList(listElements.toArray(new Term[listElements.size()]));
  }
  
  private void createPrologNet() {
    Term name = getNetTerm();
    Term places = getPlacesList();
    Term transitions = getTransitionsList();
    Term vars = getVariableList();
    Term pre = getPreConditionList();
    Term post = getPostConditionList();
    Term cond = getConditionList();
    Query createNetQuery = new Query("create_net", new Term[] {
      name, places, transitions, vars, pre, post, cond
    });
    
    System.out.println(Prolog.termToString(createNetQuery.goal()));
    
    if(! createNetQuery.hasSolution()) {
      throw new PrologNetException("Failed to create the net in prolog."); 
    }
  }
  
  private Query getAssignmentQuery() {
    Query query = new Query("realization_assignment", new Term[] { getNetTerm(), new Variable("Assignment"), getPartialAssignment() });
//    Query query = new Query("net_conditions", new Term[] { getNetTerm(), new Variable("Eq") });
    System.out.println(Prolog.termToString(query.goal()));
    return query;
  }
  
  public boolean getSolution() {
    if(assignmentQuery.hasMoreSolutions()) {
      System.out.println("Prolog found a solution...");
      Map solution = assignmentQuery.nextSolution();
//      System.out.println(Prolog.termToString((Term) solution.get("Assignment")));
      Map<Integer,Map<Atom,ValueTerm>> vars = new HashMap<>();
      Map<Integer,ValueTerm> places = new HashMap<>();
      solutionToAssignment(solution, vars, places);
      setNetToSolution(vars, places);
      return true;
    }
    return false;
  }
  
  private void solutionToAssignment(Map solution, Map<Integer,Map<Atom,ValueTerm>> vars, Map<Integer,ValueTerm> places) {
    if(solution == null) return;
    Term assTerm = (Term) solution.get("Assignment");
    Term[] assElements = Util.listToTermArray(assTerm);
    for(Term assElement : assElements) {
      Term[] mapping = Util.listToTermArray(assElement);
      Term var = mapping[0];
      Term value = mapping[1];
      if(var.isInteger()) {
        places.put(var.intValue(), new ValueTerm(value));
      } else {
        Term[] group = Util.listToTermArray(var);
        Atom varName = new Atom(group[0].toString());
        Integer trans = group[1].intValue();
        if(vars.containsKey(trans)) {
          vars.get(trans).put(varName,new ValueTerm(value));
        } else {
          Map<Atom,ValueTerm> map = new HashMap<>();
          map.put(varName, new ValueTerm(value));
          vars.put(trans, map);
        }
      }
    }
  }
  
  private void setNetToSolution(Map<Integer,Map<Atom,ValueTerm>> vars, Map<Integer,ValueTerm> places) {
    for(int placeId : places.keySet()) {
      for(Place p : net.getPlaces()) {
        if(p.getId() == placeId) {
          AHLInstPlaceData data = (AHLInstPlaceData) p.getData();
          data.setValue(places.get(placeId));
        }
      }
    }
    
    for(int transId : vars.keySet()) {
      for(Transition t : net.getTransitions()) {
        if(t.getId() == transId) {
          AHLInstTransitionData data = (AHLInstTransitionData) t.getData();
          data.setAssignment(vars.get(transId));
        }
      }
    }
  }
}
