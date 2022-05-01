package edu.cmu.cs.cs214.hw2;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * This test class tests the code in TwoArgOperation.java.
 */
public class TwoArgOperationTest {
    private Processor p;

    @Before
    public void setUp() {
        p = new Processor();
    }

    @Test
    public void testDataMovOperation() {
        TwoArgOperation op = TwoArgOperation.MOV;
        p.setRegister(0, 10);
        op.execute(p, 5, 0);
        assertEquals(p.getRegister(5), 10);

        op = TwoArgOperation.LOAD;
        p.setDataMemory(p.getSp(), 22);
        p.setRegister(0, p.getSp());
        op.execute(p, 1, 0);
        assertEquals(p.getRegister(1), 22);

        op = TwoArgOperation.LOADI;
        op.execute(p, 2, 25);
        assertEquals(p.getRegister(2), 25);

        op = TwoArgOperation.STORE;
        op.execute(p, 0, 2);
        assertEquals(p.getDataMemory(p.getSp()), 25);
    }

    @Test
    public void TestAddSub() {
        TwoArgOperation op1 = TwoArgOperation.ADD;
        TwoArgOperation op2 = TwoArgOperation.ADDI;
        TwoArgOperation op3 = TwoArgOperation.SUB;
        TwoArgOperation op4 = TwoArgOperation.SUBI;

        op2.execute(p, 0, 5);
        assertEquals(p.getRegister(0), 5);
        p.setRegister(1, 7);
        op1.execute(p, 0, 1);
        assertEquals(p.getRegister(0), 12);
        op3.execute(p, 0, 1);
        assertEquals(p.getRegister(0), 5);
        op4.execute(p, 0, 8);
        assertEquals(p.getRegister(0), -3);
    }

    @Test
    public void testMulDivMod() {
        TwoArgOperation op1 = TwoArgOperation.MUL;
        TwoArgOperation op2 = TwoArgOperation.MULI;
        TwoArgOperation op3 = TwoArgOperation.DIV;
        TwoArgOperation op4 = TwoArgOperation.DIVI;
        TwoArgOperation op5 = TwoArgOperation.MOD;
        TwoArgOperation op6 = TwoArgOperation.MODI;

        p.setRegister(4, 10);
        p.setRegister(5, 3);
        op1.execute(p, 5, 4);
        assertEquals(p.getRegister(5), 30);
        op2.execute(p, 4, 2);
        assertEquals(p.getRegister(4), 20);
        op3.execute(p, 5, 4);
        assertEquals(p.getRegister(5), 1);
        op4.execute(p, 4, 5);
        assertEquals(p.getRegister(4), 4);
        op5.execute(p, 5, 4);
        assertEquals(p.getRegister(5), 1);
        op6.execute(p, 4, 3);
        assertEquals(p.getRegister(4), 1);
    }

    @Test
    public void testControlFlow() {
        TwoArgOperation op1 = TwoArgOperation.JEQ;
        TwoArgOperation op2 = TwoArgOperation.JNE;
        TwoArgOperation op3 = TwoArgOperation.JGT;

        op1.execute(p, 0, 12);
        assertEquals(p.getPc(), 12);
        p.setRegister(0, -1);
        op2.execute(p, 0, 22);
        assertEquals(p.getPc(), 22);
        op3.execute(p, 0, 15);
        assertEquals(p.getPc(), 23);
    }
}















