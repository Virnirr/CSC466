package DocumentClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.*;

public class DocumentCollection implements Serializable {

    public static final String noiseWordArray[] = {"a", "about", "above", "all", "along",
            "also", "although", "am", "an", "and", "any", "are", "aren't", "as", "at",
            "be", "because", "been", "but", "by", "can", "cannot", "could", "couldn't",
            "did", "didn't", "do", "does", "doesn't", "e.g.", "either", "etc", "etc.",
            "even", "ever", "enough", "for", "from", "further", "get", "gets", "got", "had", "have",
            "hardly", "has", "hasn't", "having", "he", "hence", "her", "here",
            "hereby", "herein", "hereof", "hereon", "hereto", "herewith", "him",
            "his", "how", "however", "i", "i.e.", "if", "in", "into", "it", "it's", "its",
            "me", "more", "most", "mr", "my", "near", "nor", "now", "no", "not", "or", "on", "of", "onto",
            "other", "our", "out", "over", "really", "said", "same", "she",
            "should", "shouldn't", "since", "so", "some", "such",
            "than", "that", "the", "their", "them", "then", "there", "thereby",
            "therefore", "therefrom", "therein", "thereof", "thereon", "thereto",
            "therewith", "these", "they", "this", "those", "through", "thus", "to",
            "too", "under", "until", "unto", "upon", "us", "very", "was", "wasn't",
            "we", "were", "what", "when", "where", "whereby", "wherein", "whether",
            "which", "while", "who", "whom", "whose", "why", "with", "without",
            "would", "you", "your", "yours", "yes"};

    private HashMap<Integer, TextVector> documents;
    public DocumentCollection(String file_name) {
    /*
     a constructor that reads the file that is specified as input and it
     uses the data in the file to populate the documents variable. //about 50 lines of code
     */
     this.documents = new HashMap<Integer, TextVector>();
     try {
         File file = new File(file_name);
         Scanner scan_file = new Scanner(file);
         int curr_doc = 0;

         String[] line = scan_file.nextLine().split(" ");

         // add it to the documents HashMap if it's new document
         if (line[0].equals(".I")) {
             this.documents.put(Integer.valueOf(line[1]), new TextVector());
             curr_doc = Integer.parseInt(line[1]);
         }

         // read each line in the input file
         while (scan_file.hasNextLine()) {
             // parse through unneeded lines
             line = scan_file.nextLine().split(" ");

             // if body (.W), keep reading until reaching new document (.I)
             if (line[0].equals(".W")) {
                 while (scan_file.hasNextLine()) {
                     String line_body = scan_file.nextLine();
                     String[] next_line_body = line_body.split(" ");

                     // break on the next .I (document), end of body
                     if (next_line_body[0].equals(".I")) {
                         this.documents.put(Integer.valueOf(next_line_body[1]), new TextVector());
                         curr_doc = Integer.parseInt(next_line_body[1]);
                         break;
                     }

                     // break words out and extract as TextVector
                     String[] lower_case_words = tokenize(line_body);
                     TextVector curr_vector = this.documents.get(curr_doc);
                     for (String word : lower_case_words) {
                         // only add like a length of 2 and not noise word
                         if (word.length() >= 2 && !isNoiseWord(word))
                            curr_vector.add(word);
                     }
                 }
             }
         }

     } catch (FileNotFoundException e) {
         throw new RuntimeException(e);
     }

    }

    private String[] tokenize(String line) {
        line = line.toLowerCase();
        return line.split("[^a-zA-Z]+");
    }

    public TextVector getDocumentById(int id) {
        /*
           returns the TextVector for the document with the ID that is given.
        */
        return this.documents.get(id);
    }

    public float getAverageDocumentLength() {
        /*
        returns the average length of a document not counting noise words.
        Use the method getTotalWordCount() on each document to calculate the
        number of non-noise words in each document. Add up the numbers and
        divide by the total number of documents.
         */
        return (float) getTotalWordCount() / getSize();
    }

    public int getTotalWordCount() {
        /*
        * Return total frequency count from given document (excluding noise-words)
        * */
        int total_word_count = 0;
        for (Map.Entry<Integer, TextVector> entry : this.getEntrySet()) {
            TextVector document = entry.getValue();
            total_word_count += document.getTotalWordCount();
        }
        return total_word_count;
    }

    public int getSize() {
        /*
         returns number of documents
         */
        return this.documents.size();
    }

    public Set<Map.Entry<Integer, TextVector>> getEntrySet() {
        /*
        returns a mapping of document id to Text Vector, that is an object of type
        Set<Map.Entry<Integer, TextVector>>. Use the method entrySet on the HashMap
        to get the result.
         */
        return this.documents.entrySet();
    }

    public int getDocumentFrequency(String word) {
        /*
            returns the number of documents that contain the input word.
        */
        int total_document_contain_word = 0;

        for (Map.Entry<Integer, TextVector> entry : this.getEntrySet()) {
            TextVector document = entry.getValue();
            if (document.contains(word)) {
                total_document_contain_word += 1;
            }
        }
        return total_document_contain_word;
    }

    private boolean isNoiseWord(String word) {
        /*
        Return true if word is a noiseWord else false
         */
        return Arrays.asList(noiseWordArray).contains(word);
    }

    private int allDistinctWords() {
        int distinct_words = 0;
        for (Map.Entry<Integer, TextVector> entry : this.getEntrySet()) {
            distinct_words += entry.getValue().getDistinctWordCount();
        }
        return distinct_words;
    }

    private TextVector highestSingleDocumentFreq() {
        TextVector highest_word_freq_document = null;
        for (Map.Entry<Integer, TextVector> entry : this.getEntrySet()) {
            TextVector document = entry.getValue();
            if (highest_word_freq_document == null) {
                highest_word_freq_document = document;
            }
            if (highest_word_freq_document.getHighestRawFrequency() < document.getHighestRawFrequency()) {
                highest_word_freq_document = document;
            }
        }
        return highest_word_freq_document;
    }


    public String toString() {
        /* Serialize the object to print out
        *  highest single document frequency and the frequency, the sum of the distinct number of
        * words in each document over all documents, and the sum of the frequencies of all
        * non-noise words that are stored
        * */
        TextVector highest = highestSingleDocumentFreq();
        String word = String.format("Word = %s", highest.getMostFrequentWord());
        String frequency = String.format("Frequency = %d", highest.getHighestRawFrequency());
        String distinct = String.format("Distinct Number of Words = %d", allDistinctWords());
        String total = String.format("Total word count = %d", getTotalWordCount());
        return word + "\n" + frequency + "\n" + distinct + "\n" + total;
    }
}
