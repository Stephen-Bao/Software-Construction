package edu.cmu.cs.cs214.hw3.cryptarithm.expression;

import edu.cmu.cs.cs214.hw3.permutation.Permutation;
import edu.cmu.cs.cs214.hw3.permutation.PermutationIterator;

import java.util.*;

/**
 * The context for evaluating a cryptarithm expression. An instance of this class represents a mapping
 * from the letters that occur in a cryptarithm to digits.
 */
public class CryptarithmExpressionContext {
    public static final int MAX_DIGITS = 10;

    private LinkedHashMap<Character, Integer> mappings;
    private Permutation<Integer> p;
    private PermutationIterator<ArrayList<Integer>> it;
    private List<Integer> bitVecList;
    private int bitVecIndex;

    /**
     * Returns a context that associates the given letters with the given digits.
     * @param equation A String array that represents a cryptarithm equation.
     */
    public CryptarithmExpressionContext( String[] equation ) {
        mappings = new LinkedHashMap<Character, Integer>();
        bitVecList = new ArrayList<Integer>();
        HashSet<Character> letters = new LinkedHashSet<Character>();
        ArrayList<Integer> digits = new ArrayList<Integer>();
        List<String> validWords = new ArrayList<String>();
        bitVecIndex = 0;
        int bitVec = 0;
        // Delete operators
        for (String word : equation) {
            if (!word.equals("+") && !word.equals("-") && !word.equals("*") && !word.equals("=")) {
                validWords.add(word);
            }
        }
        // Add letters
        for (String word : validWords) {
            for (int i = 0; i < word.length(); i++) {
                letters.add(word.charAt(i));
            }
        }
        // Initialize bitVec
        int len = letters.size();
        while (bitVec < 1 << MAX_DIGITS) {
            bitVec++;
            if (Integer.bitCount(bitVec) == len) {
                bitVecList.add(bitVec);
            }
        }
        bitVec = bitVecList.get(0);
        // First digit combination
        for (int i = 0; i < MAX_DIGITS; i++) {
            if ((bitVec & (1 << i)) == (1 << i)) {
                digits.add(i);
            }
        }
        // Initialize first mapping
        int index = 0;
        for (Character c : letters) {
            mappings.put(c, digits.get(index));
            index++;
        }
        // Initialize Permutation
        p = new Permutation<Integer>(digits);
        p.generate(0, len - 1);
        it = new PermutationIterator<ArrayList<Integer>>(p);
        it.next();  // It should point to the first permutation
    }

    /**
     * Updates the current mapping context by the next one.
     * @return Returns true if the update is successful.
     */
    public boolean updateContext() {
        ArrayList<Integer> newDigits = new ArrayList<Integer>();
        if (it.hasNext()) {
            newDigits = it.next();
        }
        else if (bitVecIndex < bitVecList.size() - 1) {
            int bitVec = bitVecList.get(bitVecIndex + 1);
            bitVecIndex++;
            for (int i = 0; i < MAX_DIGITS; i++) {
                if ((bitVec & (1 << i)) == (1 << i)) {
                    newDigits.add(i);
                }
            }
            // Update permutation and iterator
            p = new Permutation<Integer>(newDigits);
            p.generate(0, mappings.size() - 1);
            it = new PermutationIterator<ArrayList<Integer>>(p);
            it.next();
        }
        else {
            return false;
        }
        // Update mappings
        int index = 0;
        for (Character c : mappings.keySet()) {
            mappings.replace(c, newDigits.get(index));
            index++;
        }

        return true;
    }

    Integer requireValue(Character c) { return mappings.get(c); }

    //public int getBitVec() { return bitVec; }
    public LinkedHashMap<Character, Integer> getMappings() { return mappings; }
    public Permutation<Integer> getP() { return p; }

    /**
     * Prints out the current mapping.
     */
    public void printMappings() {
        for (Character c : mappings.keySet()) {
            System.out.print(c + "=" + mappings.get(c) + " ");
        }
        System.out.println();
    }

    /**
     * Prints out the bitVecList.
     */
    public void printBitVecList() {
        for (int bitVec : bitVecList) {
            System.out.println(Integer.toBinaryString(bitVec));
        }
        System.out.println("List size = " + bitVecList.size());
    }
}










