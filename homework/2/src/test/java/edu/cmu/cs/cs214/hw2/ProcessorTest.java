package edu.cmu.cs.cs214.hw2;

import org.junit.*;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * This test class tests the code in Processor.java.
 */
public class ProcessorTest {
    private Processor p;

    @Before
    public void setUp() {
        p = new Processor();
    }

    @Test
    public void testGetterSetter() {
        for (int i = 0; i < 6; i++) {
            p.setRegister(i, i);
            assertEquals(i, p.getRegister(i));
        }
        p.setPc(10);
        p.setSp(2040);
        p.setDataMemory(p.getSp(), 5);
        assertEquals(10, p.getPc());
        assertEquals(2040, p.getSp());
        assertEquals(5, p.getDataMemory(p.getSp()));
        p.addPc();
        assertEquals(11, p.getPc());
    }

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "src/main/resources/hello-world-with-comments.asm";
        p.loadFile(filePath);
        assertEquals("LOADI R0 72", p.getInstructionMemory(0));
        assertEquals("WRITE R0", p.getInstructionMemory(1));
        assertEquals("LOADI R0 101", p.getInstructionMemory(2));
    }

    @Test
    public void testCheckFormat() throws IOException {
        String filePath = "src/main/resources/interactive-gcd.asm";
        p.loadFile(filePath);
        assertEquals("LOADI R3 'i'", p.getInstructionMemory(0));
        assertEquals("WRITE R3", p.getInstructionMemory(1));
        assertEquals("LOADI R3 32", p.getInstructionMemory(4));
    }

}












