/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.ahl;

import ape.petri.generic.net.TransitionData;
import ape.prolog.Atom;
import ape.prolog.Compound;
import ape.prolog.Prolog;
import ape.util.EnumPropertyType;
import ape.util.Property;
import ape.util.aml.AMLNode;
import java.util.*;

/**
 *
 * @author Gabriel
 */
public class AHLTransitionData extends TransitionData {
  
  /** the firing conditions of this transition given as a set of Prolog terms */
  private Set<Compound> conditions;
  private Set<Atom> conditionVars;
  
  /**
   * Creates a new transition data element for a P/T net, containing the specified name and
   * an empty set of firing conditions.
   * @param name the name of the transition, stored this data
   */
  public AHLTransitionData(String name) {
    this(name, new HashSet<Compound>());
  }

  /**
   * Creates a new transition data element for a P/T net, containing the specified name.
   * @param name the name of the transition, stored this data
   * @param conditions the firing conditions of this transition given as a set of
   * Prolog terms
   */
  public AHLTransitionData(String name, Set<Compound> conditions) {
    super(name);
    this.conditions = conditions;
    computeConditionVariables();
  }
  
  public Set<Compound> getConditions() {
    return new HashSet<>(conditions);
  }
  
  public void setConditions(Set<Compound> conditions) {
    this.conditions.clear();
    this.conditions.addAll(conditions);
    computeConditionVariables();
    dataHasChanged();
  }
  
  private void computeConditionVariables() {
    conditionVars = new HashSet<>();
    for(Compound cond : getConditions()) {
      conditionVars.addAll(Arrays.asList(cond.args()));
    }
  }
  
  public Set<Atom> getConditionVariables() {
    return new HashSet<>(conditionVars);
  }

  @Override
  public List<Property> getProperties() {
    List<Property> properties = super.getProperties();
    properties.add(new Property(Property.CATEGORY_VALUES, this, EnumPropertyType.MultiLineText, "Conditions") {

      @Override
      public Object getValue() {
        return Prolog.toString(getConditions());
      }

      @Override
      public void setValue(Object value) {
        Collection<Compound> terms = Prolog.parseCompoundSequence((String) value, false);
        Set<Compound> cond = new HashSet<>(terms.size());
        cond.addAll(terms);
        setConditions(cond);
      }
    });
    return properties;
  }

  @Override
  public AMLNode getAMLNode() {
    AMLNode node = super.getAMLNode();
    for(Compound condition : conditions) {
      AMLNode condNode = new AMLNode("Condition");
      condNode.putAttribute("content", condition.toString());
      node.addChild(condNode);
    }
    return node;
  }

  @Override
  public void readAMLNode(AMLNode node) {
    super.readAMLNode(node);
    for(AMLNode condNode : node.getChildren("Condition")) {
      conditions.add(Prolog.parseCompound(condNode.getAttribute("content")));
    }
  }
  
  
}
