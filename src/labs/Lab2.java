package labs;

import DocumentClasses.CosineDistance;
import DocumentClasses.DocumentCollection;
import DocumentClasses.DocumentDistance;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Lab2 {
    public static DocumentClasses.DocumentCollection documents;
    public static DocumentClasses.DocumentCollection queries;
    public static void main(String[] args) {

        queries = new DocumentCollection("/Users/zhihe/CSC466/labs/src/labs/queries.txt", "query");
        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(new File("/Users/zhihe/CSC466/labs/src/labs/files/docvector")))) {
            documents = (DocumentCollection) is.readObject();

            ArrayList<Integer> first_list = new ArrayList<>(Arrays.asList(13, 184, 12, 51, 486, 1268, 327, 435, 746, 875, 665, 686, 359, 878, 494, 14, 1144, 1186, 332, 154));

            // normalize both documents and queries
            documents.normalizeAll(documents);
            queries.normalizeAll(documents);


            CosineDistance CosineDistance = new CosineDistance();
            System.out.println(queries.getDocumentById(1).findClosestDocuments(documents, CosineDistance));
            System.out.println(queries.getDocumentById(1).findClosestDocuments(documents, CosineDistance).equals(first_list));

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
