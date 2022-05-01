package edu.cmu.cs.cs214.hw3;

import edu.cmu.cs.cs214.hw3.cryptarithm.expression.CryptarithmSolve;
import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class EndToEndTest {

    private ByteArrayOutputStream outContent1;
    private ByteArrayOutputStream outContent2;

    @Before
    public void setUp() {
        outContent1 = new ByteArrayOutputStream();
        outContent2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent1));
    }

    @After
    public void tearDown() throws IOException {
        outContent1.close();
        outContent2.close();
        System.setOut(System.out);
    }

    @Test
    public void testProgram() {
        String[] args = {"SEND", "+", "MORE", "=", "MONEY"};
        CryptarithmSolve.main(args);
        assertEquals("1 solution(s):\nS=9 E=5 N=6 D=7 M=1 O=0 R=8 Y=2 \n",
                outContent1.toString());

        System.setOut(new PrintStream(outContent2));
        String[] newArgs = {"NORTH", "'*'", "WEST", "=", "SOUTH", "'*'", "EAST"};
        CryptarithmSolve.main(newArgs);
        assertEquals("1 solution(s):\nN=5 O=1 R=3 T=0 H=4 W=8 E=7 S=6 U=9 A=2 \n",
                outContent2.toString());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testException() {
        String[] args = {"SEND", "MORE", "=", "MONEY"};
        CryptarithmSolve.main(args);
    }

}











