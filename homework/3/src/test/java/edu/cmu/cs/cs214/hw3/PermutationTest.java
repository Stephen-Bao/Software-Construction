package edu.cmu.cs.cs214.hw3;

import edu.cmu.cs.cs214.hw3.permutation.Permutation;
import edu.cmu.cs.cs214.hw3.permutation.PermutationIterator;
import org.junit.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class PermutationTest {

    @Test
    public void testGenerate() {
        ArrayList<Double> a = new ArrayList<Double>(Arrays.asList(0.3, 1.4, 2.7));
        Permutation<Double> p = new Permutation<>(a);
        p.generate(0, 2);
        assertEquals(p.getSize(), 6);
    }

    @Test
    public void testRemove() {
        ArrayList<Integer> a = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        Permutation<Integer> p = new Permutation<>(a);
        p.generate(0, 3);
        p.removePermutation(0);
        assertEquals(p.getSize(), 23);
    }
}













