package edu.cmu.cs.cs214.hw3.cryptarithm.expression;

/**
 * An expression representing a binary (2-operand) operation that occurs in a cryptarithm.
 */

public class CryptarithmBinaryExpression implements CryptarithmExpression {
    /** This expression's operator */
    private final CryptarithmOperator operator;

    /** This expression's first (left hand) operand. */
    private final CryptarithmExpression operand1;

    /** This expression's second (right hand) operand*/
    private final CryptarithmExpression operand2;

    /** Creates a binary cryptarithm expression with the given operator and operands.
     * @param operator The operator in the cryptarithm expression
     * @param operand1 The first sub-expression of the cryptarithm expression
     * @param operand2 The second sub-expression of the cryptarithm expression
     * */
    public CryptarithmBinaryExpression(CryptarithmOperator operator,
                                       CryptarithmExpression operand1, CryptarithmExpression operand2) {
        this.operator = operator;
        this.operand1 = operand1;
        this.operand2 = operand2;
    }

    @Override
    public long eval(CryptarithmExpressionContext ctx) {
        return operator.applyAsLong(operand1.eval(ctx), operand2.eval(ctx));
    }

    @Override public String toString() {
        return String.format("(%s %s %s)", operand1, operator, operand2);
    }
}












