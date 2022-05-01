package edu.cmu.cs.cs214.hw1;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

/**
 * The Document class records the word frequency of a html web page given its URL,
 * and is able to calculate the cosine similarity wrt another web page.
 */
public class Document {

    private final HashMap<String, Integer> wordFrequency;
    private final double modulus;
    private final String urlString;

    /**
     * Constructor of the Document class. Generates a word frequency hashmap and the frequency vector's
     * modulus based on the web URL. The modulus caching is for speeding up.
     * @param s This is the input URL.
     * @throws IOException On input error.
     */
    public Document(String s) throws IOException {
        URL url = new URL(s);
        Scanner in = new Scanner(url.openStream());
        wordFrequency = new HashMap<>();
        urlString = s;

        // Construct the HashMap
        while (in.hasNext()) {
            var word = in.next();
            wordFrequency.putIfAbsent(word, 0);
            wordFrequency.put(word, wordFrequency.get(word) + 1);
        }
        // System.out.println("wordFrequeny counts over. There are " + wordFrequency.size() + " different words");

        // Cache the modulus of vector
        double sum = 0;
        for (int value : wordFrequency.values()) {
            sum += Math.pow(value, 2);
        }
        modulus = Math.sqrt(sum);
        // System.out.println("Modulus calculates over. The value is " + modulus);
    }

    double computeSimilarity(Document doc) {
        double cosSimilarity;
        double numerator = 0.0;
        for (String word : wordFrequency.keySet()) {
            if (doc.wordFrequency.containsKey(word)) {
                numerator += this.wordFrequency.get(word) * doc.wordFrequency.get(word);
            }
        }
        double denominator = this.modulus * doc.modulus;
        cosSimilarity = numerator / denominator;

        return cosSimilarity;
    }

    /**
     * This method overrides the super class Object's toString and returns the web page's URL.
     * @return Web page's URL.
     */
    public String toString() {
        return urlString;
    }

    /**
     * This is the main method for testing the Document class.
     * @param args Command line arguments.
     * @throws IOException On input error.
     */
    public static void main(String[] args) throws IOException {
        String url1 = args[0];
        String url2 = args[1];
        Document doc1 = new Document(url1);
        Document doc2 = new Document(url2);

        double similarity = doc1.computeSimilarity(doc2);
        System.out.println("Similarity between doc1 and doc2 is " + similarity);
        System.out.println("The URL of doc1 is " + doc1.toString());
        System.out.println("The URL of doc2 is " + doc2.toString());
    }
}











