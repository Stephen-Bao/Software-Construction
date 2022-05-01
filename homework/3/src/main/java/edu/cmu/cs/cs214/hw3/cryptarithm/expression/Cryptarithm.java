package edu.cmu.cs.cs214.hw3.cryptarithm.expression;

import java.util.HashSet;
import java.util.Set;

/**
 * A class that represents a cryptarithm and is able to evaluate the expression value
 * on both hand sides of the equation.
 */
public class Cryptarithm {
    public static final int MAX_LETTERS = 10;

    private String[] lhs;
    private String[] rhs;
    private CryptarithmExpressionContext context;

    /**
     * Constructor for a new cryptaritm.
     * @param equation The string array that represents a cryptarithm.
     */
    public Cryptarithm(String[] equation) {
        // Vanity check
        formatCheck(equation);
        // Split lhs and rhs expressions
        int index = 0;
        for (int i = 0; i < equation.length; i++) {
            if (equation[i].equals("=")) {
                index = i;
            }
        }
        lhs = new String[index];
        rhs = new String[equation.length - index - 1];
        for (int i = 0; i < index; i++) {
            lhs[i] = equation[i];
        }
        for (int i = index + 1; i < equation.length; i++) {
            rhs[i - index - 1] = equation[i];
        }
        // Initialize context
        context = new CryptarithmExpressionContext(equation);
    }

    private void formatCheck(String[] equation) {
        // Check parity
        if (equation.length % 2 == 0) {
            throw new IllegalArgumentException();
        }
        // Check "="
        int index = 0;
        for (int i = 0; i < equation.length; i++) {
            if (equation[i].equals("=") && index == 0) {
                index = i;
            }
            else if (equation[i].equals("=") && index != 0) {
                // More than one "="
                throw new IllegalArgumentException();
            }
        }
        // Check operator
        for (int i = 1; i < equation.length; i += 2) {
            String word = equation[i];
            if (!word.equals("+") && !word.equals("-") && !word.equals("*") && !word.equals("=")) {
                throw new IllegalArgumentException();
            }
        }
        // Check operand
        Set<Character> letters = new HashSet<Character>();
        for (int i = 0; i < equation.length; i += 2) {
            String word = equation[i];
            for (int j = 0; j < word.length(); j++) {
                letters.add(word.charAt(j));
            }
        }
        if (letters.size() > MAX_LETTERS) {
            throw new IllegalArgumentException();
        }
        for (Character c : letters) {
            if (!Character.isAlphabetic(c)) {
                throw new IllegalArgumentException();
            }
        }
        // System.out.println("Syntax OK!");
    }

    /**
     * Evaluate the value of an expression from left to right using the
     * letter-digit mapping provided by context.
     * @param expr The string array which represents an expression.
     * @return The expression's value under current context.
     */
    private long evalExpr(String[] expr) {
        // Only 1 word
        if (expr.length == 1) {
            CryptarithmExpression e = new CryptarithmWordExpression(expr[0]);
            return e.eval(context);
        }
        // Multipule words
        int wordCount = (expr.length + 1) / 2;
        CryptarithmExpression left = new CryptarithmWordExpression(expr[0]);
        CryptarithmExpression right = new CryptarithmWordExpression(expr[2]);
        CryptarithmOperator op;
        switch (expr[1]) {
            case "+":
                op = CryptarithmOperator.PLUS;
                break;
            case "-":
                op = CryptarithmOperator.MINUS;
                break;
            case "*":
                op = CryptarithmOperator.TIMES;
                break;
            default:
                throw new IllegalArgumentException();
        }
        CryptarithmExpression e = new CryptarithmBinaryExpression(op, left, right);
        wordCount -= 2;
        for (int i = 0; i < wordCount; i++) {
            CryptarithmExpression newWord = new CryptarithmWordExpression(expr[4 + 2 * i]);
            CryptarithmOperator newOp;
            switch (expr[3 + 2 * i]) {
                case "+":
                    newOp = CryptarithmOperator.PLUS;
                    break;
                case "-":
                    newOp = CryptarithmOperator.MINUS;
                    break;
                case "*":
                    newOp = CryptarithmOperator.TIMES;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            e = new CryptarithmBinaryExpression(newOp, e, newWord);
        }
        return e.eval(context);
    }

    public long evalLhs() { return evalExpr(lhs); }

    public long evalRhs() { return evalExpr(rhs); }

    public CryptarithmExpressionContext getContext() { return context; }

    /**
     * Print left hand side expr of the equation.
     */
    public void printLhs() {
        for (String s : lhs) {
            System.out.print(s + " ");
        }
        System.out.println();
    }

    /**
     * Print right hand side expr of the equation.
     */
    public void printRhs() {
        for (String s : rhs) {
            System.out.print(s + " ");
        }
        System.out.println();
    }

}






