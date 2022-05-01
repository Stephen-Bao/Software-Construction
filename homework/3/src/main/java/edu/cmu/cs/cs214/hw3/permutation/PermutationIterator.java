package edu.cmu.cs.cs214.hw3.permutation;

import java.util.Iterator;

/**
 * Iterator for accessing over all elements of a Permutation instance.
 * @param <E> The type returned by the iterator.
 */
public class PermutationIterator<E> implements Iterator<E> {
    private Permutation p;
    private int index;

    /**
     * Returns a premutation iterator to iterate over permutations.
     * @param newP A permutation type.
     */
    public PermutationIterator(Permutation newP) {
        p = newP;
        index = -1;
    }

    /**
     * Check if there's a next permutation.
     * @return True or false.
     */
    @Override
    public boolean hasNext() {
        return index + 1 < p.getSize();
    }

    /**
     * Returns the next permutation.
     * @return Next available permutation.
     */
    @SuppressWarnings("unchecked")
    @Override
    public E next() {
        index++;
        return (E)p.getPermutation(index);
    }

    /**
     * Removes the permutation in the current position.
     */
    @Override
    public void remove() {
        p.removePermutation(index);
        index--;
    }
}













