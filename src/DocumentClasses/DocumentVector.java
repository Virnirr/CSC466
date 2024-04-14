package DocumentClasses;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DocumentVector extends TextVector {
    private final HashMap<String, Double> normalizedVector = new HashMap<String, Double>();

    @Override
    public Set<Map.Entry<String, Double>> getNormalizedVectorEntrySet() {
        //returns the normalized frequency for each word
        return this.normalizedVector.entrySet();
    }

    @Override
    public void normalize(DocumentCollection dc) {
        //will normalize the frequency of each word using the TF-IDF formula

        double total_document = dc.getSize(); // this is m
        double max_frequency_of_vector = super.getHighestRawFrequency(); // this is max

        // go through each word and perform TF-IDF on every word frequency
        // Formula: weight = fi/max(f1,...,fk) * log2(m/dfi)
        for (Map.Entry<String, Integer> document_vector_entry : super.getRawVectorEntrySet()) {

            // calculate df and idf
            String word = document_vector_entry.getKey();

            // calculate tf
            double frequency = document_vector_entry.getValue();
            double tf = frequency / max_frequency_of_vector; // normalized tf

            // calculate idf
            double df = dc.getDocumentFrequency(word);
            // if df=0, that is, the term does not appear in any document, then word is not interested
            double idf = 0.0;
            if (df != 0.0) {
                idf = log2(total_document / df);
            }
            // calculate tf-idf
            double tf_idf = tf*idf;

            // set to normalized tf-idf weight
            this.normalizedVector.put(word, tf_idf);
        }
    }

    public double log2(double x)
    {
        return (Math.log(x) / Math.log(2));
    }

    @Override
    public double getNormalizedFrequency(String word) {
        /*
            will return the normalized frequency of the word
        */

        return this.normalizedVector.getOrDefault(word, -1.0);
    }
}
