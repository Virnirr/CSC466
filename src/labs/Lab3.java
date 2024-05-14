package labs;

import DocumentClasses.CosineDistance;
import DocumentClasses.DocumentCollection;
import DocumentClasses.OkapiDistance;
import DocumentClasses.TextVector;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.*;

public class Lab3 {
    public static DocumentClasses.DocumentCollection documents;
    public static DocumentClasses.DocumentCollection queries;
    public static void main(String[] args) {
        HashMap<Integer, ArrayList<Integer>> okapiResults = new HashMap<>();
        HashMap<Integer, ArrayList<Integer>> cosineResults = new HashMap<>();
        HashMap<Integer, ArrayList<Integer>> humanJudgement = new HashMap<>();

        queries = new DocumentCollection("/Users/zhihe/CSC466/labs/src/labs/queries.txt", "query");
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File("/Users/zhihe/CSC466/labs/src/labs/files/docvector")))) {
            documents = (DocumentCollection) is.readObject();

            // normalize both documents and queries
            documents.normalizeAll(documents);
            queries.normalizeAll(documents);

            File file = new File("/Users/zhihe/CSC466/labs/src/labs/human_judgement.txt");
            Scanner scan_file = new Scanner(file);

            // human_judgement_tokenize[0] = <query num>
            // human_judgement_tokenize[1] = <document num>
            // human_judgement_tokenize[2] = relevance score
            String[] human_judgement_tokenize;

            while (scan_file.hasNextLine()) {
                human_judgement_tokenize = scan_file.nextLine().split(" ");
                int query_num = Integer.parseInt(human_judgement_tokenize[0]);
                int document_num = Integer.parseInt(human_judgement_tokenize[1]);
                int relevance_score = Integer.parseInt(human_judgement_tokenize[2]);

                // For this lab, assume that a number that is equal to 1, 2, or 3
                // means relevant and you can ignore entries with any other degree of relevance.
                if (relevance_score >= 1 && relevance_score <= 3) {
                    humanJudgement.computeIfAbsent(query_num, k -> new ArrayList<>()).add(document_num);
                }
            }

//            System.out.print("THIS IS HUMAN JUDGEMENT: ");
//            System.out.println(humanJudgement);


            CosineDistance CosineDistance = new CosineDistance();
            OkapiDistance OkapiDistance = new OkapiDistance();

            // use the first twenty queries
            // calculate and compare only the first 20 queries
            for (int i = 1; i <= 20; i++) {
                cosineResults.put(i, queries.getDocumentById(i).findClosestDocuments(documents, CosineDistance));
                okapiResults.put(i, queries.getDocumentById(i).findClosestDocuments(documents, OkapiDistance));
            }

            System.out.println(cosineResults);
            System.out.println(okapiResults);

            // Output Result of MAP score
            System.out.println("Cosine MAP = " +
                    computeMAP(humanJudgement, cosineResults));
            System.out.println("Okapi MAP = " +
                    computeMAP(humanJudgement, okapiResults));


        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static double computeMAP(HashMap<Integer, ArrayList<Integer>> humanJudgement,
                                 HashMap<Integer, ArrayList<Integer>> distanceResults) {

        // two are used at the end to calculate for all documents
        double total_map = 0.0;
        double total_doc_crossed = 0.0;

        // iterate through each query results, calculate the MAP score and add it to total_map
        for (Map.Entry <Integer, ArrayList<Integer>> entry : distanceResults.entrySet()) {
            double relevant_doc_num = 0;
            double current_doc_num = 0;
            double precision = 0.0;

            int curr_query_num = entry.getKey();

            ArrayList<Integer> current_relevant_documents_by_query = humanJudgement.get(curr_query_num);
            if (current_relevant_documents_by_query == null) {
                continue;
            }
            total_doc_crossed++;

            // if document is considered relevant by humanJudgement, then increment total relevant documents in
            // current entry
            for (Integer documents : entry.getValue()) {
                current_doc_num++;

                // increment relevant number of documents and calculate precisions thus far
                if (current_relevant_documents_by_query.contains(documents)){
                    relevant_doc_num++;
                    precision += (relevant_doc_num / current_doc_num);
                }
            }

            // precision = sum of precisions / total_returned
            total_map += (precision / current_relevant_documents_by_query.size());
        }

        // return MAP (Mean Average Precision) of multiple documents
        return total_map / total_doc_crossed;
    }
}
