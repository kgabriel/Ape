/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ape.petri.generic.net;

import ape.petri.generic.EnumNetType;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * This class represents the data of an {@link ArcCollection}.
 * It contains a collection of {@link ArcElementData} that holds all data of the {@link ArcElement}s
 * that are contained in the corresponding {@link ArcCollection}.
 * @author Gabriel
 */
public class ArcCollectionData extends Data implements DataChangeListener {
  
  /** 
   * The collection of {@link ArcElementData} elements contained in this data.
   */
  private Collection<ArcElementData> dataElements;

  /**
   * New data for an {@link ArcCollection}. It contains a set of {@link ArcElementData}s
   * and has element type of {@link EnumElementType#ArcCollection}.
   * @param netType the net type of the arc collection having this data element
   */
  public ArcCollectionData(EnumNetType netType) {
    super(netType, EnumElementType.ArcCollection);
    dataElements = new HashSet<>();
  }
  
  /**
   * Returns all {@link ArcElementData} elements stored in this data element.
   * @return a collection containing all sub data elements of this data element
   */
  public Collection<ArcElementData> getDataElements() {
    return dataElements;
  }
  
  /**
   * Adds a new data element to the collection of {@link ArcElementData}s contained in this element.
   * @param data the data element to be added
   */
  public void addDataElement(ArcElementData data) {
    data.addDataChangeListener(this);
    dataElements.add(data);
  }
  
  /**
   * Removes the specified element from the collection of {@link ArcElementData}s contained in this element.
   * @param data the data element to be removed
   * @return <code>true</code> if the removed data actually was contained in this data's collection
   */
  public boolean removeDataElement(ArcElementData data) {
    data.removeDataChangeListener(this);
    return dataElements.remove(data);
  }
  
  /**
   * Returns the size of the collection of arc element's data contained in this data.
   * @return the number of {@link ArcElementData}s contained in this data
   */
  public int size() {
    return dataElements.size();
  }

  /**
   * The {@link DataChangeListener#dataChanged(ape.petri.generic.Data)} implementation.
   * If one of the components has changed, also the collection has changed.
   * @param changedData the data element in this collection that has changed
   */
  @Override
  public void dataChanged(Data changedData) {
    dataHasChanged();
  }
}
