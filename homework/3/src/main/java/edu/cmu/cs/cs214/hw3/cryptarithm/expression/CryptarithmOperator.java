package edu.cmu.cs.cs214.hw3.cryptarithm.expression;

import java.util.function.LongBinaryOperator;

/**
 * Operations for cryptarithms. Per the handout, we only support plus, minus and times
 * (but one could trivially add more operations to this file, and good solution would
 * then permit their use in cryptarithms). Note that operands and results are longs.
 */
public enum CryptarithmOperator implements LongBinaryOperator {
    PLUS("+",  (x, y) -> x + y),
    MINUS("-", (x, y) -> x - y),
    TIMES("*", (x, y) -> x * y);

    /** The standard symbol for this operator. */
    private final String symbol;

    /** Implementation of this operation. */
    private final LongBinaryOperator operation;

    CryptarithmOperator(String symbol, LongBinaryOperator operation) {
        this.symbol = symbol;
        this.operation = operation;
    }

    @Override
    public long applyAsLong(long x, long y) {
        return operation.applyAsLong(x, y);
    }

    @Override public String toString() {
        return symbol;
    }
}
