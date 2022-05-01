package edu.cmu.cs.cs214.hw1;

import java.io.IOException;

/**
 * Takes a list of URLs on the command line and prints the most similar URL for each input.
 * Prints a stack trace if any of the URLs are invalid, or if an exception occurs while reading
 * data from the URLs.
 */
public class FindClosestMatches {
  /**
   * The main method takes several URLs and prints out the most similar URL for each web page.
   * @param args Cmd line args.
   * @throws IOException On input error.
   */
  public static void main(String[] args) throws IOException {
    int size = args.length;
    Document[] docArray = new Document[size];
    for (int i = 0; i < size; i++) {
      docArray[i] = new Document(args[i]);
    }

    double[][] similarityArray = new double[size][size];
    for (int i = 0; i < size; i++) {
      similarityArray[i][i] = 0.0;
    }
    for (int i = 0; i < size; i++) {
      for (int j = i + 1; j < size; j++) {
        double curSimilarity = docArray[i].computeSimilarity(docArray[j]);
        similarityArray[i][j] = curSimilarity;
        similarityArray[j][i] = curSimilarity;
      }
    }

    // print the similarity matrix
    /*
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        System.out.print(similarityArray[i][j] + " ");
      }
      System.out.print("\n");
    }
    */

    System.out.println("The most similar web page pairs are:");
    for (int i = 0; i < size; i++) {
      int index = 0;
      double maxSimilarity = 0.0;
      for (int j = 0; j < size; j++) {
        double curSimilarity = similarityArray[i][j];
        if (curSimilarity > maxSimilarity) {
          index = j;
          maxSimilarity = curSimilarity;
        }
      }
      System.out.println(docArray[i].toString() + "<->" + docArray[index].toString());
      System.out.println("Cosine similarity: " + similarityArray[i][index]);
    }

  }

}














