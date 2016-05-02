package com.twiiter.hashtag.word2vec;

import org.canova.api.util.ClassPathResource;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.deeplearning4j.ui.UiServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;


/**
 * Created by Niranjan on 4/13/2016.
 */
public class WordTwoVec {


    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String COMMA_SEPARATOR = ",";

    public static void main(String[] args) throws Exception {

        String filePath = new ClassPathResource("File_No_3.csv").getFile().getAbsolutePath();

        //log.info("Load & Vectorize Sentences....");

        System.out.println("Load & Vectorize Sentences....");

        // Strip white space before and after for each line
        SentenceIterator iter = new BasicLineIterator(filePath);

        // Split on white spaces in the line to get words
        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());

        System.out.println("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(5)
                .iterations(1)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();

        System.out.println("Fitting Word2Vec model....");
        vec.fit();

        System.out.println("Writing word vectors to text file....");

        // Write word vectors
        WordVectorSerializer.writeWordVectors(vec, "pathToWriteto.txt");

        System.out.println("Closest Words:");
        Collection<String> lst1 = vec.wordsNearest("day", 100);
        Collection<String>  lst2= vec.wordsNearest("weather", 100);
        Collection<String> lst3 = vec.wordsNearest("movie", 100);
        Collection<String> lst4 = vec.wordsNearest("politics", 100);
        Collection<String> lst5 = vec.wordsNearest("emotion", 100);
        Collection<String> lst6 = vec.wordsNearest("college", 100);
        Collection<String> lst7 = vec.wordsNearest("media", 100);
        Collection<String> lst8 = vec.wordsNearest("travel", 100);
        Collection<String> lst9= vec.wordsNearest("dance", 100);
        Collection<String> lst10 = vec.wordsNearest("computer", 100);

        System.out.println(lst1);
        System.out.println(lst2);
        System.out.println(lst3);
        System.out.println(lst4);
        System.out.println(lst5);
        System.out.println(lst6);
        System.out.println(lst7);
        System.out.println(lst8);
        System.out.println(lst9);
        System.out.println(lst10);

        FileWriter fileWriter = null;


        try {
            //create File object
            File file = new File("Word2Vec_Query_Tags" + ".csv");
            //File writer
            fileWriter = new FileWriter(file);

            fileWriter.append("Topic_one");
            fileWriter.append(NEW_LINE_SEPARATOR);
            for(String word:lst1){
                fileWriter.append(word);
                fileWriter.append(COMMA_SEPARATOR);
            }
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append("Topic_two");
            fileWriter.append(NEW_LINE_SEPARATOR);
            for(String word:lst2){
                fileWriter.append(word);
                fileWriter.append(COMMA_SEPARATOR);
            }
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append("Topic_three");
            fileWriter.append(NEW_LINE_SEPARATOR);
            for(String word:lst3){
                fileWriter.append(word);
                fileWriter.append(COMMA_SEPARATOR);
            }
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append("Topic_four");
            fileWriter.append(NEW_LINE_SEPARATOR);
            for(String word:lst4){
                fileWriter.append(word);
                fileWriter.append(COMMA_SEPARATOR);
            }
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append("Topic_five");
            fileWriter.append(NEW_LINE_SEPARATOR);
            for(String word:lst5){
                fileWriter.append(word);
                fileWriter.append(COMMA_SEPARATOR);
            }
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append("Topic_six");
            fileWriter.append(NEW_LINE_SEPARATOR);
            for(String word:lst6){
                fileWriter.append(word);
                fileWriter.append(COMMA_SEPARATOR);
            }
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append("Topic_seven");
            fileWriter.append(NEW_LINE_SEPARATOR);
            for(String word:lst7){
                fileWriter.append(word);
                fileWriter.append(COMMA_SEPARATOR);
            }
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append("Topic_eight");
            fileWriter.append(NEW_LINE_SEPARATOR);
            for(String word:lst8){
                fileWriter.append(word);
                fileWriter.append(COMMA_SEPARATOR);
            }
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append("Topic_nine");
            fileWriter.append(NEW_LINE_SEPARATOR);
            for(String word:lst9){
                fileWriter.append(word);
                fileWriter.append(COMMA_SEPARATOR);
            }
            fileWriter.append(NEW_LINE_SEPARATOR);
            fileWriter.append("Topic_ten");
            fileWriter.append(NEW_LINE_SEPARATOR);
            for(String word:lst10){
                fileWriter.append(word);
                fileWriter.append(COMMA_SEPARATOR);
            }
            fileWriter.append(NEW_LINE_SEPARATOR);

        }
        catch (Exception e) {
            System.out.println("Error in FileReader/Writer !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while closing fileReader/Writer !!!");
                e.printStackTrace();
            }
        }
    }
}
