package edu.cmu.cs.cs214.hw3;

import edu.cmu.cs.cs214.hw3.cryptarithm.expression.CryptarithmExpressionContext;
import org.junit.*;

import static org.junit.Assert.*;

public class CryptarithmExpressionContextTest {

    @Test
    public void testUpdateContext() {
        String[] s = {"SEND", "+", "MORE", "=", "MONEY"};
        CryptarithmExpressionContext context = new CryptarithmExpressionContext(s);
        //System.out.println(Integer.toBinaryString(context.getBitVec()));
        int contextCount = 1;
        context.printMappings();
        while (context.updateContext()) {
            contextCount++;
        }
        assertEquals(contextCount, 1814400);
        System.out.println("contextCount = " + contextCount);
        context.printMappings();
    }
}














