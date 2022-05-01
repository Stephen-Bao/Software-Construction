package edu.cmu.cs.cs214.hw3.cryptarithm.expression;

/**
 * An expression suitable for representing the right and left hand sides of a cryptarithm.
 */

public interface CryptarithmExpression {
	/**
	 * Returns the value to which this expression evaluates under the given context (which maps letters to digits).
	 *
	 * @param ctx A mapping of letters to digits, for the evaluation of the cryptarithm expression
	 * @return The value to which this expression evaluates with the given mapping
	 */
	long eval(CryptarithmExpressionContext ctx);
	
	/**
	 * @return  the string form of this expression  (e.g., "(IF - IT * (IS + (TO + BE))"). Note
	 * that the string will have "extra" parentheses as we have no notion of operator precedence.
	 */
	String toString();
}
