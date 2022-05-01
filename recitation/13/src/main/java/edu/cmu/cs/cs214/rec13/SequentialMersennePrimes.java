package edu.cmu.cs.cs214.rec13;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;

import static java.math.BigInteger.ONE;

/**
 *  Prints the first 20 Mersenne primes.  A Mersenne prime is a prime
 *  number of the form 2^p - 1, for some p that is itself prime.
 *  This implementation iterates through increasingly large primes p,
 *  computing and checking the primality of 2^p - 1 for each prime p until
 *  we have found 20 such primes.
 */
public class SequentialMersennePrimes {
    private static final int LIMIT = 20; // Number of Mersenne primes to find
    private static final BigInteger TWO = new BigInteger("2");

    public static void main(String[] args) {
        Instant startTime = Instant.now(); // Record the start time for simple benchmarking

        int count = 0; // Number of Mersenne primes found so far
        BigInteger p = TWO; // Start iterating from the smallest prime
        while (count < LIMIT) {
            BigInteger candidate = TWO.pow(p.intValueExact()).subtract(ONE); // A candidate Mersenne number
            if (candidate.isProbablePrime(50)) { // this is an expensive operation
                count++;  // (probably) found a Mersenne prime!
                System.out.println(candidate);
            }
            p = p.nextProbablePrime(); // Advances to the next prime p
        }

        Duration taskTime = Duration.between(startTime, Instant.now());
        System.out.printf("It took %d.%d seconds to find %d Mersenne primes.\n",
                taskTime.getSeconds(), taskTime.toMillisPart(), LIMIT);

    }
}
