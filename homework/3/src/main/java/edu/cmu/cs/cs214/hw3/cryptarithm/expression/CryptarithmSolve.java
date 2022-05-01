package edu.cmu.cs.cs214.hw3.cryptarithm.expression;

import java.util.*;

/**
 * The program for testing cryptarithm solver.
 */
public class CryptarithmSolve {

    /**
     * Receives from cmd line a string array representing a cryptarithm.
     * Note that the symbol * must be entered as '*' in order to be parsed correctly.
     * @param args Cmd line arguments.
     */
    public static void main(String[] args) {
        // Replace the '*' with * in cmd args
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("'*'")) {
                args[i] = "*";
            }
        }
        // Find nonZero characters
        Set<Character> nonZeros = new HashSet<Character>();
        for (int i = 0; i < args.length; i += 2) {
            String word = args[i];
            nonZeros.add(word.charAt(0));
        }
        // Find all valid solutions
        List<LinkedHashMap<Character, Integer>> solutions =
                new ArrayList<LinkedHashMap<Character, Integer>>();
        Cryptarithm solver = new Cryptarithm(args);
        CryptarithmExpressionContext context = solver.getContext();
//        solver.printLhs();
//        solver.printRhs();
        long leftVal, rightVal;
        do {
            boolean valid = true;
            leftVal = solver.evalLhs();
            rightVal = solver.evalRhs();
            if (leftVal == rightVal) {
                for (Character c : nonZeros) {
                    if (context.getMappings().get(c) == 0) {
                        valid = false;
                    }
                }
                if (valid) {
                    solutions.add(new LinkedHashMap<Character, Integer>(context.getMappings()));
                }
            }
        } while (context.updateContext());
        // Print solutions
        if (solutions.size() > 0) {
            System.out.print(solutions.size() + " solution(s):" + "\n");
            for (LinkedHashMap<Character, Integer> mapping : solutions) {
                for (Character c : mapping.keySet()) {
                    System.out.print(c + "=" + mapping.get(c) + " ");
                }
                System.out.print("\n");
            }
        }
    }
}












