

/**
 * Created by Niranjan Pachaiyappan on 05/02/2016.
 */

package com.twiiter.hashtag.Evaluation;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;


public class GroundTruthTRECEvaluationFileGenerator {

    private static final String TAB_DELIMITER = "\t";
    private static final String NEW_LINE_SEPARATOR = "\n";
    //CSV file header
    private static final String FILE_HEADER = "query-number"+"\t"+"Q0"+"\t"+"document-id"+"\t"+"rank"+"\t"+"score"+"\t"+"Exp";
    private static String TWEETNO = "TweetNo";
    private static String TWEETS = "Tweet";
    private static String HASHTAGS = "Hashtag";

    public static void main(String args[]) throws IOException, ParseException {


        // this has the path where the index is present
        File indexdirectory = new File("C:\\Users\\Niranjan\\Documents\\Spring2016\\BigData\\TweetIndex\\");

        // this is the path from which the documents to be queried
        File datadirectory = new File("C:\\Users\\Niranjan\\Documents\\Spring2016\\BigData\\Evaluation\\query\\word2vec\\");

        // this is the path from which the result needs to be stored
        String resultdirectory = "C:\\Users\\Niranjan\\Documents\\Spring2016\\BigData\\Evaluation\\results\\";

        // filetype that is present in the corpus
        String filetype = "txt";

        // this object will call the index method to generate the indexing
        GroundTruthTRECEvaluationFileGenerator corpusindex = new GroundTruthTRECEvaluationFileGenerator();

        corpusindex.search_query(indexdirectory, datadirectory, resultdirectory, filetype);

    }

    public void search_query(File indexdirectory, File datadirectory, String resultdirectory, String filetype) throws IOException, ParseException {

        //csv files to get the wuery terms
        File[] files = datadirectory.listFiles();

        BufferedReader fileReader = null;
        FileWriter fileWriter = null;

        //lucene queries parser and reader
        Path path = Paths.get(String.valueOf(indexdirectory));

        IndexReader reader = DirectoryReader.open(FSDirectory.open(path));

        IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(path)));

        QueryParser parser = new QueryParser(TWEETS, new StandardAnalyzer());

        DefaultSimilarity similarity = new DefaultSimilarity();
        searcher.setSimilarity(similarity);


        try {
            //create File object
            //File writer
            File file = new File(resultdirectory + "Word2vec_GroundTruth_Evaluation_Tweet_Document_for_the_queries" + "." + filetype);
            String filename = file.getAbsolutePath();
            fileWriter = new FileWriter(filename);
            int count =0;
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isDirectory() && !files[i].isHidden() && files[i].canRead() && files[i].exists()) {

                    //Create the file reader
                    //List<ArrayList> recommendedTags = new ArrayList();
                    fileReader = new BufferedReader(new FileReader(files[i]));

                    //Write the CSV file header
                    fileWriter.append(FILE_HEADER.toString());
                    //Add a new line separator after the header
                    fileWriter.append(NEW_LINE_SEPARATOR);

                    System.out.println("Writing Scored Text File for " + file.getAbsolutePath());
                    String line = "";

                    //Read the CSV file header to skip it
                    fileReader.readLine();

                    while ((line = fileReader.readLine()) != null) {
                        count += 1;
                        //Get all tokens available in line
                        String mentionWord = line;
                        //lucene phrase query
                        PhraseQuery queryMentions = new PhraseQuery();

                        queryMentions.add(new Term(TWEETS, mentionWord));
                        BooleanQuery booleanQueryMust = new BooleanQuery();
                        BooleanQuery booleanQueryMustNot = new BooleanQuery();
                        booleanQueryMust.add(queryMentions, BooleanClause.Occur.MUST);
                        booleanQueryMustNot.add(queryMentions, BooleanClause.Occur.MUST_NOT);
                        //do the search
                        TopDocs hitsMust = searcher.search(parser.parse(parser.escape(String.valueOf(booleanQueryMust))), 10);
                        TopDocs hitsMustNot = searcher.search(parser.parse(parser.escape(String.valueOf(booleanQueryMustNot))), 10);
                        //writing the evaluation ground truth output file for 40 keywords from each method
                        //this will give the gound truth file for trec evaluation
                        //this is for relevancy must results
                        for (ScoreDoc scoreDoc : hitsMust.scoreDocs) {
                            Document d = searcher.doc(scoreDoc.doc);

                            fileWriter.append(Integer.toString(count));
                            fileWriter.append(TAB_DELIMITER);
                            fileWriter.append(Integer.toString(0));
                            fileWriter.append(TAB_DELIMITER);
                            fileWriter.append(d.get(TWEETNO).trim());
                            fileWriter.append(TAB_DELIMITER);
                            fileWriter.append(Integer.toString(1));
                            fileWriter.append(TAB_DELIMITER);
                            fileWriter.append(NEW_LINE_SEPARATOR);

                        }
                        //this is for relevancy must not results
                        for (ScoreDoc scoreDoc : hitsMustNot.scoreDocs) {
                            Document d = searcher.doc(scoreDoc.doc);

                            fileWriter.append(Integer.toString(count));
                            fileWriter.append(TAB_DELIMITER);
                            fileWriter.append(Integer.toString(0));
                            fileWriter.append(TAB_DELIMITER);
                            fileWriter.append(d.get(TWEETNO).trim());
                            fileWriter.append(TAB_DELIMITER);
                            fileWriter.append(Integer.toString(0));
                            fileWriter.append(TAB_DELIMITER);
                            fileWriter.append(NEW_LINE_SEPARATOR);

                        }

                    }

                    System.out.println("Writing Text File for Successful " + file.getAbsolutePath());
                }

            }
        } catch (Exception e) {
            System.out.println("Error in CSVFileReader/Writer !!!");
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader/Writer !!!");
                e.printStackTrace();
            }
        }

        reader.close();
    }

}
