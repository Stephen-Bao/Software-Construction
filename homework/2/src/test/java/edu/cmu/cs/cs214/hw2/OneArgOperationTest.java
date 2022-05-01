package edu.cmu.cs.cs214.hw2;

import org.junit.*;

import java.io.*;

import static org.junit.Assert.*;

/**
 * This test class tests the code in OneArgOperation.java.
 */
public class OneArgOperationTest {
    private Processor p;

    @Before
    public void setUp() {
        p = new Processor();
    }

    @Test
    public void testReadWrite() {
        String consoleInput = "a\n";
        System.setIn(new ByteArrayInputStream(consoleInput.getBytes()));
        p.setBr(new BufferedReader(new InputStreamReader(System.in)));
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        OneArgOperation op1 = OneArgOperation.READ;
        OneArgOperation op2 = OneArgOperation.WRITE;

        op1.execute(p, 0);
        assertEquals(p.getRegister(0), 97);
        op2.execute(p, 0);
        assertEquals(outContent.toString(), "a");
    }

    @Test
    public void testJump() {
        OneArgOperation op = OneArgOperation.JUMP;
        op.execute(p, 98);
        assertEquals(p.getPc(), 98);
    }

    @Test
    public void testPushPop() {
        OneArgOperation op1 = OneArgOperation.PUSH;
        OneArgOperation op2 = OneArgOperation.POP;

        p.setRegister(0, 5);
        op1.execute(p, 0);
        assertEquals(p.getDataMemory(p.getSp() + 1), 5);
        assertEquals(p.getSp(), Processor.memorySize - 2);

        op2.execute(p, 1);
        assertEquals(p.getRegister(1), 5);
        assertEquals(p.getSp(), Processor.memorySize - 1);
    }

    @Test
    public void testCall() {
        OneArgOperation op = OneArgOperation.CALL;
        op.execute(p, 100);
        assertEquals(p.getPc(), 100);
        assertEquals(p.getDataMemory(p.getSp() + 1), 1);
    }
}













