package com.twiiter.hashtag.LDA;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Niranjan on 4/4/2016.
 */
public class ConvertToLDAFormat {
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String COMMA_DELIMITER = ",";

    public void convert(File datadirectory, String resultdirectory, String filetype) throws IOException {
        BufferedReader fileReader = null;
        FileWriter fileWriter = null;
        try {
            int tweetFileCount=0;
            File[] files = datadirectory.listFiles();
            //create File object
            File file = new File(resultdirectory + "LDA" + "." + filetype);
            String filename = file.getAbsolutePath();
            //File writer
            fileWriter = new FileWriter(filename);
            System.out.println("Writing LDA Train File for " + file.getAbsolutePath());
            for (int i = 0; i < files.length; i++) {
                tweetFileCount+=1;
                System.out.println("Files Processed so far"+tweetFileCount);
                if (!files[i].isDirectory() && !files[i].isHidden() && files[i].canRead() && files[i].exists()) {

                    fileReader = new BufferedReader(new FileReader(files[i]));

                    //Read the file header to skip it
                    fileReader.readLine();

                    String fileContent = new String(Files.readAllBytes(Paths
                            .get(files[i].getCanonicalPath())));
                    String[] alldocs = fileContent.split("\n");
                    int docsize = alldocs.length;
                    //System.out.println(docsize);
                    String[] documents = new String[docsize - 2];
                    for (int k = 1; k < docsize - 1; k++) {
                        documents[k - 1] = alldocs[k];
                    }
                    for (String docContent : documents) {

                        String[] tweetDocument = docContent.split("\\%\\*\\*\\*\\%");
                        if (tweetDocument.length > 1 && (tweetDocument[1] != null && !tweetDocument[1].equals(" "))) {

                            fileWriter.append(tweetDocument[1].replaceAll("[-+^:\\/()!'=]", " "));
                            fileWriter.append(COMMA_DELIMITER);
                            fileWriter.append(NEW_LINE_SEPARATOR);
                        }
                    }

                }
            }

        } catch(Exception e){
            System.out.println("Error in CsvFileReader/Writer !!!");
            e.printStackTrace();
        }finally{
            try {
                fileReader.close();
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader/Writer !!!");
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) throws Exception {

        System.out.println("Starting the conversion");

        // this has the path where the index needs to be created
        String resultdirectory = "C:\\Users\\Niranjan\\Documents\\Spring2016\\BigData\\LDA\\";

        // this is the path from which the documents to be indexed
        File datadirectory = new File("C:\\Users\\Niranjan\\Documents\\Spring2016\\BigData\\Project\\");

        // filetype that is present in the corpus
        String filetype = "csv";

        // this object will call the index method to generate the indexing
        ConvertToLDAFormat convertToLDAFormat = new ConvertToLDAFormat();

        convertToLDAFormat.convert(datadirectory, resultdirectory, filetype);

    }
}
