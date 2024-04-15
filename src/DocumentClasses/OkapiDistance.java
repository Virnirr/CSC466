package DocumentClasses;

import java.util.Map;

public class OkapiDistance implements DocumentDistance {
    @Override
    public double findDistance(TextVector query, TextVector document, DocumentCollection documents) {
        //Note that the method uses the raw (and not normalized) frequencies

        // constants (e.g. hyper-parameters)
        double k1 = 1.2;
        double b = 0.75;
        double k2 = 100.0;

        // constant for current document
        double N = documents.getSize();
        double avdl = documents.getAverageDocumentLength();
        double dl = document.getTotalWordCount();
        double length_over_average = dl / avdl;
        double okapi_bm25 = 0.0; // will be used to sum okapi-bm25

        // calculate Okapi-BM25 over all the words in the query
        for (Map.Entry<String, Integer> entry : query.getRawVectorEntrySet()) {
            String word = entry.getKey();
            double df_i = documents.getDocumentFrequency(word);
            double f_i_j = document.getRawFrequency(word);
            double f_i_q = entry.getValue();
            double idf = Math.log((N - df_i + 0.5) / (df_i + 0.5));
            double matching_score_part_1 = ((k1 + 1) * (f_i_j)) / (k1 * (1 - b + (b * length_over_average)) + f_i_j);
            double matching_score_part_2 = ((k2 + 1) * f_i_q) / (k2 + f_i_q);
            okapi_bm25 += (idf * matching_score_part_1 * matching_score_part_2);
        }
        return okapi_bm25;
    }
}
