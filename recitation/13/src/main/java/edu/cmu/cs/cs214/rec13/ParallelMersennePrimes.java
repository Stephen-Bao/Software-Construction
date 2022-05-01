package edu.cmu.cs.cs214.rec13;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.math.BigInteger.ONE;

/**
 *  Prints the first 20 Mersenne primes.  A Mersenne prime is a prime
 *  number of the form 2^p - 1, for some p that is itself prime.
 */
public class ParallelMersennePrimes {
    private static final int LIMIT = 20; // Number of Mersenne primes to find
    private static final BigInteger TWO = new BigInteger("2");

    private static ExecutorService threadPool = Executors.newFixedThreadPool(5);
    private static BlockingQueue<Future<BigInteger>> candidateQueue =
            new ArrayBlockingQueue<>(20);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Instant startTime = Instant.now(); // Record the start time for simple benchmarking


        threadPool.submit(() -> produceCandidates());

        int count = 0;
        while (count < LIMIT) {
            BigInteger candidate = candidateQueue.take().get();
            if (candidate != null) {
                System.out.println(candidate);
                count++;
            }
        }
        threadPool.shutdownNow();

        Duration taskTime = Duration.between(startTime, Instant.now());
        System.out.printf("It took %d.%d seconds to find %d Mersenne primes.\n",
                taskTime.getSeconds(), taskTime.toMillisPart(), LIMIT);
    }

    private static void produceCandidates() {
        BigInteger p = TWO;

        while (true) {
            BigInteger candidate = TWO.pow(p.intValueExact()).subtract(ONE);
            Future<BigInteger> f = threadPool.submit(() -> checkPrimality(candidate));
            try {
                candidateQueue.put(f);
            }
            catch (InterruptedException e) {
                // Do nothing except exit
            }

            p = p.nextProbablePrime();
        }
    }

    private static BigInteger checkPrimality(BigInteger candidate) {
        if (candidate.isProbablePrime(50)) {
            return candidate;
        }
        else {
            return null;
        }
    }
}









