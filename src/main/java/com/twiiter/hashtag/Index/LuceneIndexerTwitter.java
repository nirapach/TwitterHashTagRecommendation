package com.twiiter.hashtag.Index;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.process.DocumentPreprocessor;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Niranjan on 10/3/2016.
*/

@SuppressWarnings("unchecked")
public class LuceneIndexerTwitter {

    /**
     * @param args
     */
    private static String TWEETNUMBER = "TweetNo";
    private static String TWEETS = "Tweet";
    private static String HASHTAGS = "Hashtag";

    private void index(File indexdirectory, File datadirectory, String filetype)
            throws Exception {

        Path path = Paths.get(String.valueOf(indexdirectory));
        Directory inddir;
        inddir = FSDirectory.open(path);
        StandardAnalyzer indexanalyzer = new StandardAnalyzer();

        IndexWriterConfig indwrcon = new IndexWriterConfig(indexanalyzer);

        indwrcon.setOpenMode(OpenMode.CREATE);

        // to write the documents to the index
        IndexWriter indwriter = new IndexWriter(inddir, indwrcon);

        indexing(indwriter, datadirectory, filetype);

        indwriter.forceMerge(1);
        indwriter.commit();

        System.out.println("Index Commited");
        indwriter.close();
        System.out.println("Index Closed");

    }

    private void indexing(IndexWriter indwriter, File datadirectory,
                          String filetype) throws Exception {
        File[] files = datadirectory.listFiles();
        // Document indexDoc = new Document();
        int indexstatus = 0;
        ArrayList<String> tag = new ArrayList<String>();
        tag.add(TWEETS);
        tag.add(HASHTAGS);

        //in future if want to index more fields add them here


        int corpuslen = 0;
        for (int i = 0; i < files.length; i++) {
            corpuslen++;
            int doc_count = 0;
            int extractCount = 0;
            if (!files[i].isDirectory() && !files[i].isHidden()
                    && files[i].canRead() && files[i].exists()) {
                System.out.println("\n Indexing is going on with file"
                        + files[i].getCanonicalPath());

                String fileContent = new String(Files.readAllBytes(Paths
                        .get(files[i].getCanonicalPath())));
                String[] alldocs = fileContent.split("\n");
                int docsize = alldocs.length;
                System.out.println(docsize);
                String[] documents = new String[docsize - 2];
                for (int k = 1; k < docsize-1; k++) {
                    documents[k-1] = alldocs[k];
                }

                for (String docContent : documents) {
                    extractCount += 1;
                    doc_count += 1;
                    //System.out.println(docContent);
                    String[] tweetDocument = docContent.split("\\%\\*\\*\\*\\%");
                    Document document = new Document();

                    if(tweetDocument.length>1){
                    for (int t = 0; t < tweetDocument.length; t++) {


                        if (t == 1 && (tweetDocument[t]!=null || !tweetDocument[t].equals(" "))) {
                            tweetDocument[t] = tweetDocument[t].replaceAll("[-+^:\\/()!'=]", " ");

                            String tagContent = "";

                            StringBuffer contentBuffer = new StringBuffer();

                            contentBuffer.append(tweetDocument[t]);
                            tagContent = contentBuffer.toString();
                            document.add(new TextField(TWEETS, tagContent, Field.Store.YES));


                        }
                        else if (t == 0 &&(tweetDocument[t]!=null || !tweetDocument[t].equals(" "))) {

                            String tagContent = "";
                            StringBuffer contentBuffer = new StringBuffer();

                            contentBuffer.append(tweetDocument[t]);
                            tagContent = contentBuffer.toString();
                            document.add(new TextField(TWEETNUMBER, tagContent, Field.Store.YES));

                        } else {

                            if(tweetDocument[t]!=null || !tweetDocument[t].equals(" ")) {

                                String tagContent = "";
                                StringBuffer contentBuffer = new StringBuffer();

                                contentBuffer.append(tweetDocument[t]);
                                tagContent = contentBuffer.toString();
                                document.add(new TextField(HASHTAGS, tagContent, Field.Store.YES));
                            }
                        }
                    }
                    }
                    indwriter.addDocument(document);
                }


                indexstatus = 1;
            } else {
                indexstatus = 0;
            }
            System.out.println("Number of tweet docs so far:" + extractCount);
        }

        if (indexstatus == 1) {
            System.out.println("Indexing Successful");
            System.out.println("Total Number of  files in the given corpus"
                    + corpuslen);

        }

    }

    // main method where the object for the LuceneIndexerTwitter class is instantiated
    public static void main(String[] args) throws Exception {

        // this has the path where the index needs to be created
        File indexdirectory = new File("C:\\Users\\Niranjan\\Documents\\Spring2016\\BigData\\TweetIndex\\");

        // this is the path from which the documents to be indexed
        File datadirectory = new File("C:\\Users\\Niranjan\\Documents\\Spring2016\\BigData\\Project\\");


        // filetype that is present in the corpus
        String filetype = "txt";


        // this object will call the index method to generate the indexing
        LuceneIndexerTwitter corpusindex = new LuceneIndexerTwitter();

        corpusindex.index(indexdirectory, datadirectory, filetype);

    }

}
