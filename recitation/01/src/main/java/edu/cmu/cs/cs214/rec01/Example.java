package edu.cmu.cs.cs214.rec01;

import java.util.List;
import java.util.ArrayList;

/**
 * Class to be completed for this recitation.
 */
public final class Example {

  /**
   * Creates, prints out, and returns a <code>List</code> of <code>Integer</code>s
   * from 1..size
   *
   * A value of 5 for size would print out 1 2 3 4 5 and return a list composed of
   * the elements 1, 2, 3, 4, 5 in order.
   *
   * @param size
   *         Positive size of the <code>ArrayList</code> to create and print
   * @return A list whose elements in order are 1..size
   */
  public List<Integer> printList(int size) {
    ArrayList<Integer> integerList = new ArrayList<Integer>();

    for(int i = 0; i < size + 1; i++) {
      System.out.println(i + " ");
      integerList.add(i);
    }

    return integerList;
  }

}
