

/**
 * Created by Rahul Sampat on 04/21/2016.
 */

package com.twiiter.hashtag.Search;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


// a comparator that compares Strings
class ValueComparator implements Comparator<String>{

    HashMap<String, Double> map = new HashMap<String, Double>();

    public ValueComparator(HashMap<String, Double> map){
        this.map.putAll(map);
    }

    @Override
    public int compare(String s1, String s2) {
        if(map.get(s1) >= map.get(s2)){
            return -1;
        }else{
            return 1;
        }
    }
}

public class LuceneQueryTwitterRankingFunction {

    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    //CSV file header
    private static final String FILE_HEADER = "QUERYSTRING,SCORE";
    private static String TWEETNO = "TweetNo";
    private static String TWEETS = "Tweet";
    private static String HASHTAGS = "Hashtag";

    public static void main(String args[]) throws IOException, ParseException {


        // this has the path where the index is present
        File indexdirectory = new File("C:\\Users\\Niranjan\\Documents\\Spring2016\\BigData\\TweetIndex\\");

        // this is the path from which the documents to be queried
        File datadirectory = new File("C:\\Users\\Niranjan\\Documents\\Spring2016\\BigData\\Query\\");

        // this is the path from which the result needs to be stored
        String resultdirectory = "C:\\Users\\Niranjan\\Documents\\Spring2016\\BigData\\Results\\";

        // filetype that is present in the corpus
        String filetype = "txt";

        // this object will call the index method to generate the indexing
        LuceneQueryTwitterRankingFunction corpusindex = new LuceneQueryTwitterRankingFunction();

        corpusindex.search_query(indexdirectory, datadirectory, resultdirectory, filetype);

    }

    public static TreeMap<String, Double> sortMapByValue(HashMap<String, Double> map){
        Comparator<String> comparator = new ValueComparator(map);

        //TreeMap is a map sorted by its keys.
        //The comparator is used to sort the TreeMap by keys.

        TreeMap<String, Double> result = new TreeMap<String, Double>(comparator);
        result.putAll(map);
        return result;
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

            for (int i = 0; i < files.length; i++) {
                if (!files[i].isDirectory() && !files[i].isHidden() && files[i].canRead() && files[i].exists()) {

                    //Create the file reader
                    List<ArrayList> recommendedTags = new ArrayList();
                    fileReader = new BufferedReader(new FileReader(files[i]));
                    //create File object
                    int count=i+1;
                    File file = new File(resultdirectory + "LDA_Tweet_Dcoument_Scores_for_the_queries_" + count + "." + filetype);
                    String filename = file.getAbsolutePath();
                    //File writer
                    fileWriter = new FileWriter(filename);
                    //Write the CSV file header
                    fileWriter.append(FILE_HEADER.toString());
                    //Add a new line separator after the header
                    fileWriter.append(NEW_LINE_SEPARATOR);

                    System.out.println("Writing Scored Text File for " + file.getAbsolutePath());
                    String line = "";

                    //Read the CSV file header to skip it
                    fileReader.readLine();

                    HashMap<String, Double> scoredQueries = new HashMap<String, Double>();
                    while ((line = fileReader.readLine()) != null) {

                        //Get all tokens available in line
                        String mentionWord = line;


                        //lucene phrase query
                        PhraseQuery queryMentions = new PhraseQuery();

                        queryMentions.add(new Term(TWEETS, mentionWord));
                        BooleanQuery booleanQuery = new BooleanQuery();
                        booleanQuery.add(queryMentions, BooleanClause.Occur.MUST);
                        //do the search
                        TopDocs hits = searcher.search(parser.parse(parser.escape(String.valueOf(booleanQuery))), 100);


                        double score = 0.0;
                        for (ScoreDoc scoreDoc : hits.scoreDocs) {
                            Document d = searcher.doc(scoreDoc.doc);
                            score += scoreDoc.score;
                        }

                        scoredQueries.put(mentionWord,score);
                        //writing the scores to file


                    }
                    TreeMap<String, Double> sortedMap = sortMapByValue(scoredQueries);
                    for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
                        String key = entry.getKey();
                        Double value = entry.getValue();
                        fileWriter.append(key);
                        fileWriter.append(COMMA_DELIMITER);
                        fileWriter.append(Double.toString(value));
                        fileWriter.append(NEW_LINE_SEPARATOR);
                        // use key and value
                    }

                    fileWriter.flush();
                    fileWriter.close();

                    System.out.println("Writing Text File for Successful " + file.getAbsolutePath());
                }

            }
        } catch (Exception e) {
            System.out.println("Error in CSVFileReader/Writer !!!");
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
                /*fileWriter.flush();
                fileWriter.close();*/
            } catch (IOException e) {
                System.out.println("Error while closing fileReader/Writer !!!");
                e.printStackTrace();
            }
        }

        reader.close();
    }

}
