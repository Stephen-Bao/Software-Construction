package edu.cmu.cs.cs214.hw2;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * This test class tests the code in ZeroArgOperation.java.
 */
public class ZeroArgOperationTest {
    private Processor p;

    @Before
    public void setUp() {
        p = new Processor();
    }

    @Test
    public void testRet() {
        OneArgOperation op1 = OneArgOperation.CALL;
        ZeroArgOperation op2 = ZeroArgOperation.RET;

        op1.execute(p, 22);
        op2.execute(p);
        assertEquals(p.getPc(), 1);
        assertEquals(p.getSp(), Processor.memorySize - 1);
    }

    @Test
    public void testHalt() {
        ZeroArgOperation op = ZeroArgOperation.HALT;
        op.execute(p);
        assertEquals(p.getEndFlag(), true);
    }
}
















