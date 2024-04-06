package labs;
import DocumentClasses.DocumentCollection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class Lab1 {
    public static void main(String[] args) {
        DocumentCollection docs = new DocumentCollection("/Users/zhihe/CSC466/lab1/src/labs/documents.txt");
        try(ObjectOutputStream os = new ObjectOutputStream(new
                FileOutputStream(new File("./files/docvector")))){
            os.writeObject(docs);
        } catch(Exception e){
            System.out.println(e);
        }

        System.out.println(docs);
//        try(ObjectOutputStream os = new ObjectOutputStream(new
//                FileOutputStream(new File("src/labs/files/docvector")))){
//            os.writeObject(docs);
//        } catch(Exception e){
//            System.out.println(e);
//        }
    }
}