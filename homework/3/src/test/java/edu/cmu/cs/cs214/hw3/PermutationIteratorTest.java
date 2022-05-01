package edu.cmu.cs.cs214.hw3;

import edu.cmu.cs.cs214.hw3.permutation.Permutation;
import edu.cmu.cs.cs214.hw3.permutation.PermutationIterator;
import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class PermutationIteratorTest {
    @Test
    public void testNext() {
        ArrayList<Integer> a = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        Permutation<Integer> p = new Permutation<>(a);
        p.generate(0, 3);
        PermutationIterator<ArrayList<Integer>> it = new PermutationIterator<>(p);
        it.next();
        ArrayList<Integer> temp = new ArrayList<>(it.next());
        int[] result = new int[4];
        int[] expected = {1, 2, 4, 3};
        for (int i = 0; i < temp.size(); i++) {
            result[i] = temp.get(i);
        }
        assertArrayEquals(result, expected);
    }
}















