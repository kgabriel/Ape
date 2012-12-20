/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.ui.control;

import java.awt.event.InputEvent;
/**
  * This class defines a mask of modifiers (<code>Alt</code>, <code>Alt Graph</code>, <code>Ctrl</code>,
  * <code>Meta</code> or <code>Shift</code>) that have to be pressed together with a binding
  * in order to activate it.
  * @see CommandBinding
  * @see CommandEvent
  * @see CommandManager
  * @see CommandManager#activeCommands
  * @author Gabriel
  */
public class CommandBindingModifier {
  
  /** Modifiers that should be pressed for activation. */
  private int onMask;
  
  /** Modifiers that must not be pressed for activation. */
  private int offMask;

  /** An empty modifier mask. No modifiers are specified. */
  public CommandBindingModifier() {
    this(0);
  }
  
  /** A new modifier mask from a mask of modifiers of an {@link InputEvent}. 
   * The new binding modifier requires that exactly the modifiers in the specified mask
   * are used, and all modifiers not specified in the mask are forbidden.
   * @param onMask a mask of modifiers that have to be pressed for activation of
   * a corresponding command binding; all other modifiers must not be pressed for activation
   */
  public CommandBindingModifier(int onMask) {
    this(onMask,inverseMask(onMask));
  }
  
  /** A new modifier mask from a mask of modifiers of an {@link InputEvent}. 
   * The new binding modifier requires that exactly the modifiers in the specified <code>onMask</code>
   * are used, and no modifiers specified in the <code>offMask</code> are used.
   * Note that only those modifiers are stored and taken into account that are
   * present in the mask returned by {@link CommandBindingModifier#allModifiersMask()}.
   * @param onMask a mask of modifiers that have to be pressed for activation of
   * a corresponding command binding
   * @param offMask a mask of modifiers that must not be pressed for activation of
   * a corresponding command binding
   */
  public CommandBindingModifier(int onMask, int offMask) {
    this.onMask = onMask & allModifiersMask();
    this.offMask = offMask & allModifiersMask();
  }

  /**
   * Returns a mask of all modifiers.
   * @return a mask, containing <code>Alt</code>, <code>Alt Graph</code>, <code>Ctrl</code>,
   * <code>Meta</code> and <code>Shift</code> modifier keys.
   */
  public static int allModifiersMask() {
    int mask = InputEvent.ALT_DOWN_MASK;
    mask |= InputEvent.ALT_GRAPH_DOWN_MASK;
    mask |= InputEvent.CTRL_DOWN_MASK;
    mask |= InputEvent.META_DOWN_MASK;
    mask |= InputEvent.SHIFT_DOWN_MASK;
    return mask;
  }

  /** The inverse mask are all modifiers  as returned
   * by {@link CommandBindingModifier#allModifiersMask()} exclusive-or the specified mask.
   * @param mask a mask of modifiers
   * @return a mask of all modifiers except the ones specified in <code>mask</code>
   */
  public static int inverseMask(int mask) {
    return mask ^ allModifiersMask();
  }
  
  public boolean hasModifiers() {
    return onMask != 0;
  }

  /**
   * Checks if the modifier masks of this binding conform to the specified modifiers.
   * @param modifiers the modifiers to check 
   * @return <code>true</code> if <code>modifiers</code> contains all modifiers in this
   * binding's on-mask and no modifiers in the off-mask
   */
  public boolean fitsToModifiers(int modifiers) {
    return (modifiers & (onMask | offMask)) == onMask;
  }
  
  public String toUserFriendlyString() {
    return InputEvent.getModifiersExText(onMask);
  }

  @Override
  public String toString() {
    String str = "[";
    str += InputEvent.getModifiersExText(onMask) + "," + InputEvent.getModifiersExText(offMask);
    str += "]";
    return str;
  }
  
  /** Compares this binding to another {@link CommandBindingModifier}s. Two {@link CommandBindingModifier}s are 
   * equal if they have the same on- and off-mask.
   * @param o the object to compare this object to
   * @return <code>true</code> if <code>o</code> is a {@link CommandBindingModifier} with
   * the same <code>onMask</code> and <code>offMask</code>
   */
  @Override
  public boolean equals(Object o) {
    if(! (o instanceof CommandBindingModifier)) return false;
    CommandBindingModifier other = (CommandBindingModifier) o;
    return this.onMask == other.onMask && this.offMask == other.offMask;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 53 * hash + this.onMask;
    hash = 53 * hash + this.offMask;
    return hash;
  }
}
