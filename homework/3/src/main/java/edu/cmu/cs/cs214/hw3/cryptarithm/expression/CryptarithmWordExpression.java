package edu.cmu.cs.cs214.hw3.cryptarithm.expression;

import java.util.ArrayList;

/**
 * An expression representing a cryptarithm word.
 */
public class CryptarithmWordExpression implements CryptarithmExpression {
    private String word;
     
    /** Constructs a cryptarithm expression corresponding to the given word.
     * @param word  The word which this cryptarithm expression should represent.
     */
    public CryptarithmWordExpression(String word) {
        // Replace the statement below with your own implementation
        this.word = word;
    }

    /** Returns the value of this word expression for the given context (i.e., letter-digit mapping). */
    @Override
    public long eval(CryptarithmExpressionContext ctx) {
        ArrayList<Character> letters = new ArrayList<Character>();
        for (int i = word.length() - 1; i > -1; i--) {
            char c = word.charAt(i);
            letters.add(c);
        }
        long ret = 0;
        for (int i = 0; i < letters.size(); i++) {
            ret += ctx.requireValue(letters.get(i)) * Math.pow(10, i);
        }

        return ret;
    }

    /** Returns the word represented by this object (e.g., "SEND") */
    @Override public String toString() {
        return word;
    }
}









