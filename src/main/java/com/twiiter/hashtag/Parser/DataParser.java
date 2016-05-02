


/**
 * 
 * 
 * Created by Rohit Patil and Rahul Sampat on 4/14/2016.
 * Parser File which runs through the entire dataset and parses just the tweet text and the hashtags.
 * Output is written in .txt files with an upper limit of 1000 tweets per file
*/



package com.twiiter.hashtag.Parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;



public class DataParser {


	private static final String path = "C:\\Users\\Rohit\\Desktop\\BigData\\TweetIndex\\UDI-TwitterCrawl-Aug2012-Tweets2\\tweets";

	public void readFile() {
		BufferedReader reader = null;
		FileOutputStream fop = null;
		int lineCount = 0;
		String linecontent;
		byte[] linecontentInBytes;
		Map<Integer, String> tweetMap = new TreeMap<Integer, String>();

		try {
			File directory = new File(DataParser.path);
			if(!directory.isDirectory()) {
				System.err.println("This is not a directory!");
				System.exit(1);
			}
			int tweetCount = 0;
			System.out.println("Dir Length: " + directory.list().length);
			for(File file : directory.listFiles()) {
				System.out.println("Processing file: " + file.getName());
				reader = new BufferedReader(new FileReader(file));

				String line = null;
				boolean start = false;


				while((line = reader.readLine()) != null) {
					if(line.equals("***") && !start) {
						start = true;
					}
					else if(line.equals("***") && start) {
						start = false;

					}

					if(start && !line.equals("***")) {

						if(line.startsWith("Text:")) {
							tweetMap.put(++tweetCount, line.substring(5).trim());
						}
						else if(line.startsWith("Hashtags:")) {
							tweetMap.put(tweetCount, tweetMap.get(tweetCount) + "%***%" + line.substring(9).trim());
						}
					}

				}
			}

			System.out.println("Map Size: " + tweetMap.size());
			String fileName = "C:\\Users\\Rohit\\Desktop\\BigData\\TweetIndex\\UDI-TwitterCrawl-Aug2012-Tweets\\Output6\\11\\outfile";

			File	file = new File(fileName + (lineCount++) + ".txt");
			fop = new FileOutputStream(file);

			String content="TweetNo %***% Tweet %***% Hashtag\n";
			byte[] contentInBytes = content.getBytes();
			fop.write(contentInBytes);

			for(Map.Entry<Integer, String> entry : tweetMap.entrySet()) {

				if(entry.getKey()%1000 == 0) {
					file = new File(fileName + (lineCount++) + ".txt");
					fop = new FileOutputStream(file);
					fop.write(contentInBytes);
				}

				linecontent=entry.getKey() + "%***%" + entry.getValue() + "\n";
				linecontentInBytes = linecontent.getBytes();

				fop.write(linecontentInBytes);
				fop.flush();
			}

		}
		catch(Exception ex) {
			System.err.println("Caught Exception: " + ex);
			ex.printStackTrace();
		}
		finally {
			try {
				if(fop != null) {
					fop.flush();
					fop.close();
				}
				if(reader != null){
					reader.close();
				}
			} catch (IOException e) {
				System.out.println("Caught Exception in finally: " + e);
				e.printStackTrace();
			}
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DataParser cls = new DataParser();
		cls.readFile();
	}

}
