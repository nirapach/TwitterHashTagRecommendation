# TwitterHashTagRecommendation


Twitter Hash-Tag Recommendation based on Textual Data

Parsed Output Files:

	Number of Files: ~61000
	Number of Tweets: ~62 million

Keyword Extraction using Latent Dirichlet Allocation and Word2vec 

	Number of Topics: 10
	Number of Keywords per topic: ~100
	Vocabulary size: ~1429632
	Sub Sample Size : ~10 million



Important modules and the respective Implementation Strategy.


Data Parsing done using : 

	Custom Java Parser

Indexing done using :

	Apache Lucene

Topic Modelling Strategy :

	Bag of Words
	LDA
	Word2Vec
	
Ranking Function: 

	TD - IDF score (Apache Lucene-Similarity Score)


Initial Evaluation Strategy : 

	Manual Evaluation for random sampled data (Range 5-150 Tweets)

New Additions :

	Automated Evaluation: Trec Evaluation



Please find attached project report in the repository


References

	[1] Roman Dovgopol and Matt Nohelty. Twitter Hash Tag Recommendation, University of Minnesota,2015
	
	[2] A. Mazzia, J. Juett Suggesting Hashtags on Twitter. Computer Science and Engineering,University of Michigan, 2009
	
	[3] B. Sriram, D. Fuhry, E. Demir, M. Demirbas. Short Text Classification in Twitter to Improve Information Filtering. Ohio State University
	
	[4] How to intrepret R-Squared Error and P-value :
	http://machinelearningmastery.com/linear-regression-in-r/
	
	[5] Apache Lucene : https://lucene.apache.org/core/
