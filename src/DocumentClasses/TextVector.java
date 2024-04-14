package DocumentClasses;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public abstract class TextVector implements Serializable {

    private final HashMap<String, Integer> rawVector;
    public TextVector() {
        this.rawVector = new HashMap<>();
    }

    public Set<Map.Entry<String, Integer>> getRawVectorEntrySet() {
        /* Returns a mapping from each word to its frequency */
        return this.rawVector.entrySet();
    }

    public void add(String word) {
        /* Increment the word in rawVector */
        this.rawVector.put(word, rawVector.getOrDefault(word, 0) + 1);
    }

    public boolean contains(String word) {
        /* returns true if the word is in the rawVector and false otherwise. */
        return this.rawVector.containsKey(word);
    }

    public int getRawFrequency(String word) {
        /* Return the frequency of the word, if exist, else 0 */
        return this.rawVector.getOrDefault(word, 0);
    }

    public int getTotalWordCount() {
        /* returns the total number of non-noise words that are stored for the
        document (e.g., if frequency =2, then count the word twice). */

        int total_word_count = 0;
        for (int value: this.rawVector.values()) {
            total_word_count += value;
        }
        return total_word_count;
    }

    public int getDistinctWordCount() {
        /* return distinct keys in document (not including noise words) */
        return this.rawVector.size();
    }

    public int getHighestRawFrequency() {
        /* return highest word count */

        int highest_freq = 0;

        for (Map.Entry<String, Integer> entry : getRawVectorEntrySet()) {
            int value = entry.getValue();
            if (value > highest_freq) {
                highest_freq = value;
            }
        }

        return highest_freq;
    }

    public String getMostFrequentWord() {
        /* Return the word with the highest frequency */

        int highest_freq = 0;
        String word_to_highest_freq = null;

        for (Map.Entry<String, Integer> entry : this.rawVector.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();
            if (value > highest_freq) {
                highest_freq = value;
                word_to_highest_freq = key;
            }
        }

        return word_to_highest_freq;
    }
    //returns the normalized frequency for each word
    public abstract Set<Map.Entry<String, Double>> getNormalizedVectorEntrySet();

    //will normalize the frequency of each word using the TF-IDF formula
    public abstract void normalize(DocumentCollection dc);

    //will return the normalized frequency of the word
    public abstract double getNormalizedFrequency(String word);
    public double getL2Norm() {
        /*
            method to get the normalized frequencies. Then it returns the square root
            of the sum of the squares of the frequencies

            Note: returns the size of the vector (normalized)
        */
        double total_normalized_freq_val = 0.0;

        for (Map.Entry<String, Double> entry : this.getNormalizedVectorEntrySet()) {
            total_normalized_freq_val += (Math.pow(entry.getValue(), 2));
        }

        return Math.sqrt(total_normalized_freq_val);
    }

    public ArrayList<Integer> findClosestDocuments(DocumentCollection documents, DocumentDistance distanceAlg) {
        /*
            method that returns the 20 closest documents as an ArrayList<Integer>
        */

        // <Document Number, Similarity Score>
        HashMap<Integer, Double> documentByClosest = new HashMap<>();

        // calculate the closest documents to current query vector
        for (Map.Entry<Integer, TextVector> document : documents.getEntrySet()) {
            int document_num = document.getKey();
            double similarity_score = distanceAlg.findDistance(this, document.getValue(), documents);
            documentByClosest.put(document_num, similarity_score);
        }

        // return sorted list and top 20 sorted in descending order by similarity score
        return documentByClosest.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(20)
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
