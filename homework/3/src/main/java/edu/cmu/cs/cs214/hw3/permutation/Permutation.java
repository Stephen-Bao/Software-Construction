package edu.cmu.cs.cs214.hw3.permutation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * A reusable permutation generator which is able to generate all possible permutations
 * given an object list. The objects can be of any reference type.
 * @param <E> The generic object type.
 */
public class Permutation<E> {
    private ArrayList<E> initialList;
    private ArrayList<ArrayList<E>> permutationList;

    /**
     * Constructor. Takes a generic list as input.
     * @param list A list whose elements' type is generic.
     */
    public Permutation(ArrayList<E> list) {
        initialList = new ArrayList<E>(list);
        permutationList = new ArrayList<ArrayList<E>>();
    }

    /**
     * Generates all possible permutations from elem index m to n and stores in permutationList.
     * @param m Starting elem index.
     * @param n Ending elem index.
     */
    public void generate(int m, int n) {
        if (m == n) {
            permutationList.add(new ArrayList<E>(initialList));
        }
        else {
            for (int i = m; i <= n; i++) {
                E temp = initialList.get(m);
                initialList.set(m, initialList.get(i));
                initialList.set(i, temp);
                generate(m + 1, n);
                temp = initialList.get(m);
                initialList.set(m, initialList.get(i));
                initialList.set(i, temp);
            }
        }
    }

    /**
     * Return a specific permutation.
     * @param index Position of permutation.
     * @return Permuted list of objects.
     */
    public ArrayList<E> getPermutation(int index) {
        return permutationList.get(index);
    }

    /**
     * Getter for size.
     * @return Size of permutationList.
     */
    public int getSize() { return permutationList.size(); }

    /**
     * Removes a permutation by its index.
     * @param index A given position.
     */
    public void removePermutation(int index) {
        permutationList.remove(index);
    }
}











