package edu.cmu.cs.cs214.hw2;

import org.junit.*;

import static org.junit.Assert.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * This is the class for end-to-end test cases.
 */
public class EndToEndTest {
    private Processor myProcessor;
    private ByteArrayOutputStream outContent1;
    private ByteArrayOutputStream outContent2;

    /**
     * Initialize processor and redirect system output.
     */
    @Before
    public void setUp() {
        myProcessor = new Processor();
        outContent1 = new ByteArrayOutputStream();
        outContent2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent1));
    }

    /**
     * Set the system output back.
     * @throws IOException On closing error.
     */
    @After
    public void tearDown() throws IOException {
        outContent1.close();
        outContent2.close();
        System.setOut(System.out);
    }

    @Test
    public void testHelloWorld() throws IOException {
        String filePath = "src/main/resources/hello-world.asm";
        myProcessor.loadFile(filePath);
        myProcessor.execute();
        assertEquals("Hello World!\n", outContent1.toString());

        myProcessor.reset();
        System.setOut(new PrintStream(outContent2));
        filePath = "src/main/resources/hello-world-with-comments.asm";
        myProcessor.loadFile(filePath);
        myProcessor.execute();
        assertEquals("Hello World!\n", outContent2.toString());
    }

    @Test
    public void testFibonacci() throws IOException {
        String filePath = "src/main/resources/fibonacci-without-subroutines.asm";
        myProcessor.loadFile(filePath);
        myProcessor.execute();
        assertEquals(89, myProcessor.getRegister(5));

        myProcessor.reset();
        filePath = "src/main/resources/fibonacci.asm";
        myProcessor.loadFile(filePath);
        myProcessor.execute();
        assertEquals("89\n", outContent1.toString());
    }

    @Test
    public void testPrintDecimal() throws IOException {
        String filePath = "src/main/resources/print-decimal.asm";
        myProcessor.loadFile(filePath);
        myProcessor.execute();
        assertEquals("1337\n", outContent1.toString());
    }

    @Test
    public void testEcho() throws IOException {
        String filePath = "src/main/resources/echo.asm";
        myProcessor.loadFile(filePath);
        String consoleInput = "happy\n\n";
        System.setIn(new ByteArrayInputStream(consoleInput.getBytes()));
        myProcessor.setBr(new BufferedReader(new InputStreamReader(System.in)));
        myProcessor.execute();
        assertEquals("happy\n\n", outContent1.toString());
        System.setIn(System.in);
    }

    @Test
    public void testGcd() throws IOException {
        String filePath = "src/main/resources/gcd.asm";
        myProcessor.loadFile(filePath);
        myProcessor.execute();
        assertEquals("12", outContent1.toString());

        myProcessor.reset();
        System.setOut(new PrintStream(outContent2));
        filePath = "src/main/resources/interactive-gcd.asm";
        myProcessor.loadFile(filePath);
        String consoleInput = "256\n16\n";
        System.setIn(new ByteArrayInputStream(consoleInput.getBytes()));
        myProcessor.setBr(new BufferedReader(new InputStreamReader(System.in)));
        myProcessor.execute();
        assertEquals("i: j: gcd(i,j): 16", outContent2.toString());
        System.setIn(System.in);
    }

    @Test
    public void testTwentyQuestions() throws IOException {
        String filePath = "src/main/resources/twenty-questions.asm";
        myProcessor.loadFile(filePath);
        String consoleInput = "n\nn\nn\ny\ny\nn\nn\nn\ny\nn\ny\n";
        System.setIn(new ByteArrayInputStream(consoleInput.getBytes()));
        myProcessor.setBr(new BufferedReader(new InputStreamReader(System.in)));
        myProcessor.execute();
        assertEquals("Is your # > 500 (y/n)? Is your # > 250 (y/n)? Is your # > 125 (y/n)? " +
                "Is your # > 63 (y/n)? Is your # > 94 (y/n)? Is your # > 110 (y/n)? Is your # > 102 (y/n)? " +
                "Is your # > 98 (y/n)? Is your # > 96 (y/n)? Is your # > 97 (y/n)? " +
                "Your # is 97\n", outContent1.toString());
        System.setIn(System.in);
    }
}















