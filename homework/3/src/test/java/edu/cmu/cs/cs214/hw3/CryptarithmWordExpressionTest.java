package edu.cmu.cs.cs214.hw3;

import edu.cmu.cs.cs214.hw3.cryptarithm.expression.CryptarithmExpression;
import edu.cmu.cs.cs214.hw3.cryptarithm.expression.CryptarithmExpressionContext;
import edu.cmu.cs.cs214.hw3.cryptarithm.expression.CryptarithmWordExpression;
import org.junit.*;

import static org.junit.Assert.*;

public class CryptarithmWordExpressionTest {

    @Test
    public void testEval() {
        String[] s = {"SEND", "+", "MORE", "=", "MONEY"};
        CryptarithmExpressionContext context = new CryptarithmExpressionContext(s);
        context.printMappings();
        CryptarithmExpression e = new CryptarithmWordExpression("SEND");
        long val = e.eval(context);
        assertEquals(val, 123);
    }
}












