
/**
 * Created by Rohit Patil on 04/14/2016.
 * Search the index file based on the top ranked keywords and recommend hashtags (Mentionwords) for the tweets which are found a hit
 * 
 * Output is written in .txt file.
 * Same search query is used for both LDA +BOW and BOW + WORD2VEC
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
import org.apache.lucene.store.FSDirectory;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LuceneQueryTwitter {

    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    
    //CSV file header
    private static final String FILE_HEADER = "TWEET_NO,TWEET,HASHTAGS,RECOMMENDED_HASHTAG";
    private static String TWEETNO = "TweetNo";
    private static String TWEETS = "Tweet";
    private static String HASHTAGS = "Hashtag";

    public static void main(String args[]) throws IOException, ParseException {


        // this has the path where the index is present
        File indexdirectory = new File("C:\\Users\\Rohit\\Desktop\\BigData\\TweetIndex\\");

        // this is the path from which the documents to be queried
        File datadirectory = new File("C:\\Users\\Rohit\\Desktop\\BigData\\Query\\Top25\\Word2VecTopics\\");


        // this is the path from which the result needs to be stored
        String resultdirectory = "C:\\Users\\Rohit\\Desktop\\BigData\\Results\\Top25\\";

        // filetype that is present in the corpus
        String filetype = "csv";

        // this object will call the index method to generate the indexing
        LuceneQueryTwitter corpusindex = new LuceneQueryTwitter();

        corpusindex.search_query(indexdirectory, datadirectory, resultdirectory, filetype);

    }

    public void search_query(File indexdirectory, File datadirectory, String resultdirectory, String filetype) throws IOException, ParseException {

        //csv files to get the query terms
        File[] files = datadirectory.listFiles();

        BufferedReader fileReader = null;
        FileWriter fileWriter = null;

        //lucene queries parser and reader
        Path path = Paths.get(String.valueOf(indexdirectory));

        IndexReader reader = DirectoryReader.open(FSDirectory.open(path));

        IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(path)));

        QueryParser parser = new QueryParser(TWEETS, new StandardAnalyzer());

        try {

            for (int i = 0; i < files.length; i++) {
                if (!files[i].isDirectory() && !files[i].isHidden() && files[i].canRead() && files[i].exists()) {

                    //Create the file reader
                    List<ArrayList> recommendedTags = new ArrayList();
                    fileReader = new BufferedReader(new FileReader(files[i]));

                    //create File object
                    File file = new File(resultdirectory + "Word2vec_Topics_Recommended_Tags_" +i+"." + filetype);
                    String filename = file.getAbsolutePath();

                    //File writer
                    fileWriter = new FileWriter(filename);

                    //Write the CSV file header
                    fileWriter.append(FILE_HEADER.toString());

                    //Add a new line separator after the header
                    fileWriter.append(NEW_LINE_SEPARATOR);

                    System.out.println("Writing Recommended Tags Text File for " + file.getAbsolutePath());
                    String line = "";

                    //Read the CSV file header to skip it
                    fileReader.readLine();

                    HashMap<String, String> eliminatingAllDuplicates = new HashMap<String, String>();
                    while ((line = fileReader.readLine()) != null) {
                        String mentionWord=null;
                        int indexOf=line.indexOf(",");

                        if(indexOf!=-1){
                            String[] inputLine=line.split(",");
                            mentionWord=inputLine[0];
                        }
                        else{
                            mentionWord = line;
                        }

                        //Get all tokens available in line

                        //lucene phrase query
                        PhraseQuery queryMentions = new PhraseQuery();

                        queryMentions.add(new Term(TWEETS, mentionWord));
                        BooleanQuery booleanQuery = new BooleanQuery();
                        booleanQuery.add(queryMentions, BooleanClause.Occur.MUST);

                        //do the actual search
                        TopDocs hits = searcher.search(parser.parse(parser.escape(String.valueOf(booleanQuery))), 100);

                        for (ScoreDoc scoreDoc : hits.scoreDocs) {
                            Document d = searcher.doc(scoreDoc.doc);

                            if (d.get(HASHTAGS) != null && d.get(HASHTAGS) != "") {
                                boolean newTag = d.get(HASHTAGS).toLowerCase().contains(mentionWord.toLowerCase());

                       // Recommended hashtags : mentionWord
                       
                                if (!newTag) {
                                    fileWriter.append(d.get(TWEETNO));
                                    fileWriter.append(COMMA_DELIMITER);
                                    fileWriter.append(d.get(TWEETS));
                                    fileWriter.append(COMMA_DELIMITER);
                                    fileWriter.append(d.get(HASHTAGS));
                                    fileWriter.append(COMMA_DELIMITER);
                                    fileWriter.append("#" + mentionWord);
                                    fileWriter.append(NEW_LINE_SEPARATOR);
                                }
                            }
                        }
                    }


                    System.out.println("Writing Text File for Successful " + file.getAbsolutePath());
                }

            }
        } catch (Exception e) {
            System.out.println("Error in CsvFileReader/Writer !!!");
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
