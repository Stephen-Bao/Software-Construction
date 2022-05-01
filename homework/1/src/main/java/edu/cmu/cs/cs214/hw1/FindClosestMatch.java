package edu.cmu.cs.cs214.hw1;

import java.io.IOException;

/**
 * Takes a list of URLs on the command line and prints the two URL whose web
 * pages have the highest cosine similarity. Prints a stack trace if any of the
 * URLs are invalid, or if an exception occurs while reading data from the URLs.
 */
public class FindClosestMatch {
  /**
   * The main method takes several URLs and prints out the two URLs that have the
   * highest cosine similarity.
   * @param args Command line args, consisting of a number of URLs.
   * @throws IOException On input error.
   */
  public static void main(String[] args) throws IOException {
    Document[] docArray = new Document[args.length];
    for (int i = 0; i < args.length; i++) {
      docArray[i] = new Document(args[i]);
    }
    int row = 0, column = 1;
    double maxSimilarity = docArray[0].computeSimilarity(docArray[1]);
    for (int i = 0; i < docArray.length; i++) {
      double curSimilarity;
      for (int j = i + 1; j < docArray.length; j++) {
        curSimilarity = docArray[i].computeSimilarity(docArray[j]);
        if (curSimilarity > maxSimilarity) {
          row = i; column = j;
          maxSimilarity = curSimilarity;
        }
      }
    }

    System.out.println("The most similar web pages are of index " + row + " and " + column + " (starting from 0)");
    System.out.println("Their URLs are:");
    System.out.println(docArray[row]);
    System.out.println(docArray[column]);
    System.out.println("Cosine similarity: " + maxSimilarity);

  }
}














