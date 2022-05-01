package edu.cmu.cs.cs214.hw3;

import edu.cmu.cs.cs214.hw3.cryptarithm.expression.Cryptarithm;
import org.junit.*;

import static org.junit.Assert.*;

public class CryptarithmTest {

    @Test
    public void testEvalExpr() {
        String[] equation = {"NORTH", "*", "WEST", "=", "SOUTH", "*", "EAST"};
        Cryptarithm solver = new Cryptarithm(equation);
        solver.getContext().printMappings();
        //solver.printLhs();
        //System.out.println("lhs evals: " + solver.evalLhs());
        assertEquals(solver.evalLhs(), 7000482);
        //solver.printRhs();
        //System.out.println("rhs evals: " + solver.evalRhs());
        assertEquals(solver.evalRhs(), 500898482);

    }
}









