package DocumentClasses;

public interface DocumentDistance {
    //will return the distance between the query and document
    double findDistance(TextVector query, TextVector document, DocumentCollection documents);
}
