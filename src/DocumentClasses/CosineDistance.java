package DocumentClasses;

import java.util.Map;

public class CosineDistance implements DocumentDistance {

    @Override
    public double findDistance(TextVector query, TextVector document, DocumentCollection documents) {

        double dot_product_val = 0;
        double size_of_query_vector = query.getL2Norm();
        double size_of_document_vector = document.getL2Norm();


        // Note: make sure both query and document are normalized
        // Perform dot product on query and document TextVector
        // d dot product q =d1*q1+...+dn*qn
        for (Map.Entry<String, Double> query_entry : query.getNormalizedVectorEntrySet()) {
            String word_to_join = query_entry.getKey();
            double normalized_query_tf_idf = query_entry.getValue();
            double normalized_document_tf_idf = document.getNormalizedFrequency(word_to_join);
            if (normalized_document_tf_idf != -1.0)
                dot_product_val += (normalized_query_tf_idf * normalized_document_tf_idf);
        }


        // calculate similarity scores
        // cosine(d,q) = <d dot product q>/(|d|*|q|)
        // There are documents that contain no text. If a document is empty,
        // then the distance from it to any query should be equal to 0 and the distance function should not be called.
        double similarities = 0;
        if (size_of_query_vector != 0 && size_of_document_vector != 0) {
            similarities = (dot_product_val / (size_of_query_vector*size_of_document_vector));
        }

        return similarities;
    }
}
