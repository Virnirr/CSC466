package DocumentClasses;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TextVector implements Serializable {

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

        for (Map.Entry<String, Integer> entry : this.rawVector.entrySet()) {
            String key = entry.getKey();
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
}
