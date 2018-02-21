Final Project :

Goal : Design and build your information retrieval systems, evaluate and compare their performance levels in terms of retrieval effectiveness 

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;



SYNOPSIS :


This readme file has references and detailed information regarding how to setup, compile and run the programs in the assignment.

The progrms are discussed below in brief:

-- Task1: Building our own search engines with BM25, tf-idf, Smoothed Query Liklihood and Lucene model.
-- Task2: Implementing pseudo-relevance feedback for query expansion on one of the above mentioned models.
-- Task3: Using the same base search engine, implementing stopping and stemming seperately.
-- Phase2: Snippet Generation and query term highlighting.
-- Phase3: Evaluation in form of MAP, MRR, P@K for k=5 and 20, Precision and Recall.

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


GENERAL USAGE NOTES : 


-- This file contains instructions about installing softwares and running the programs in Windows Environment.
-- The instructions in the file might not match the installation procedures in other operating systems like Mac OS, Ubuntu OS etc.
-- However, the programs are independent of any operating systems and will run successfully in all platforms once the initial installation has been done.

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

INSTALLATION GUIDE :

Set up requirements :

Resources Used : 
JDK 1.8.0_144
JRE 1.8.0_144
64-bit system
Windows operating system
IDE Used -> eclipse-jee-mars (64-bit)

External Jars :

lucene-analyzers-common-4.7.2.jar
lucene-core-4.7.2.jar
lucene-queryparser-4.7.2.jar
lucene-highlighter-4.7.2.jar
JSOUP 1.10.3

The code was developed and executed using the above mentioned IDE with JDK and JRE version 1.8.0_144

Make sure to add these jar files to the project before execution

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

Before executing the code please create a project named "IrProject" or any other name would also work. 
Please maintain the folder and file structure inside the "IrProject" folder as it is very important if you change the project name.

Please make sure  the following folders and files are present in your project folder before execution of any code :

Folders :

a. data                   : it has the .html pages from which the corpus has to be generated. Total .html pages present - 3204
b. cleaned_data           : it contains the corpus which has been transformed - case folded and Punctuations have been handled. Initially the folder contains the corpus but if
                            you wish to create a new corpus with basic text transformation then execute the code "Create_Corpus.java" with appropriate option.
c. cleaned_data_stopped   : it contains the corpus with basic text transformation as well as stopped words have been removed. Initially the folder contains the corpus but if
 							you wish to create a new corpus then execute the code "Create_Corpus.java" with appropriate option.
d. cleaned_stemmed_data   : it contains the corpus with stemmed data. Initially the folder contains corpus. This is a way to make all the corpus file format same. The file
							cacm_stem.txt has been read and a uniform corpus format has been maintained. To do this use code "SegregateStemmedCorpus.java"
e. LuceneBasic            : This is an empty folder and should be used as a folder to create index while executing the program "LuceneIndexer.java" on the Basic Text 									transformed data.
f. LuceneStopped  		  : This is an empty folder and should be used as a folder to create index while executing the program "LuceneIndexer.java" on the corpus with Stopped 								words removed.
g. LuceneStemmed 		  : This is an empty folder and should be used as a folder to create index while executing the program "LuceneIndexer.java" on the corpus with Stemmed 								text.
h. term-termfreq          : This folder contains the documents with their tf-idf values in descendin order. It is required to run the code "PseudoRelevance.java".


Files :

a. cacm.query.txt      : It contains the 64 queries in a xml document format which will be converted in different format by excuting the code "CleanQuery.java" to handle all 							 punctuations and to convert it to lower case. After a successful execution of code a text file will be generated "queries.txt" which will be used 								 while execution of other codes.
b. cacm.rel.txt        : It contains the relevance judgement details which will be used to calculate the precision and recall in program "PerformanceAnalyzer.java"
c. cacm_stem.query.txt : It contains the stemmed queries which will be used in BM25.java, LuceneIndexer.java and QueryLikelihood.java
d. cacm_stem.txt       : It contains the steemmed corpus. This file is an input for the code "SegregateStemmedCorpus.java".
e. common_words.txt    : It contains the list of Stop words which will be read in different programs.

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


Program Flow for best results : 

1. Create the corpus containing basic text transormed data and populate the folder "cleaned_data". Exeecute code "Create_Corpus.java" with option 2 and folder "data" as input.
2. Create the corpus containing basic text transormed data along with no stop words and populate the folder "cleaned_data_stopped". Exeecute code "Create_Corpus.java" with 
   option 1  and folder "data" as input.
3. Create the corpus conatining stemmed data and populate the folder "cleaned_stemmed_data". Execute code "SegregateStemmedCorpus.java" with "cacm_stem.txt" as input.
4. Convert the XML format query file to basic text file. Execute "CleanQuery.java" with "cacm.query" as input and "queries.txt" as output.
5. Execute "InvertedIndexMain.java", select the options and follow the instructions. 

Note : While executing "InvertedIndexMain.java" please make sure the folder "term-termfreq" is populated to perform Pseudo relevance correctly.

Preferred order of execution :

1. Run the code "InvertedIndexMain.java"

2. Select Option 1 to execute the "BM25.java" program. 

3. To perform task 1 using BM25, select option "1" to retrieve relevant documents using BM25 on a basic text transformed corpus using the basic text transformed queries.
   The ouput of this run is "BM25BasicOutput.txt"

4. At the end of this task, the system will ask user if they would like to perform Pseudo relevance feedback on this result. Press "1" to perform Pseudo relevance.
   The "Pseudorelevance.java" file will be executed and 2 ouptut file be created - "ExpandedQueries.txt" and "BM25PseudoOutput.txt". The result of Task 2 is "BM25PseudoOutput.txt". Task ends.

 5. Now again the initial options will be displayed. Again select option "1" i.e perform "BM25" but with option "2" i.e on corpus with stopped words removed.
    The output of this run is "BM25StoppedOutput.txt" (Task 3 part a) and the task will end.

 6. Now again the initial options will be displayed. Again select option "1" i.e perform "BM25" but with option "3" i.e on corpus with stemmed words removed and a stemmed 	    query i.e "cacm_stem.query.txt" .The output of this run is "BM25StemmedOutput.txt" (Task 3 part b) and the task will end.

 7. Now again the initial options will be displayed. Select option "2" to retrieve relevant documents using tf-idf values (Task 1). The output for this run is 				    "tfIdfRanking.txt". Task ends

 8. Now again the initial options will be displayed. Select option "3" to retrieve documents using Lucene. If you are performing Lucene on basic text transformed data then
   give the path of index generation as the path till the folder "LuceneBasic" and select option "a". Then enter the path till folder "cleaned_data". After all the 3204 files have been added, enter "q" to execute the search operation on the queries given in file "queries.txt". The output for this will be "LuceneBasicOutput.txt" (Task 1).  The output file is present in the folder "LuceneBasic" and task ends here.

 9. Again the initial options will be displayed. Slect option "3"  to retrieve documents using Lucene. If you are performing Lucene on corpus which has no stop words and has 	basic text transformation  then give the path of index generation as the path till the folder "LuceneStopped" and select option "b". Then enter the path till folder 		  "cleaned_data_stopped". After all the 3204 files have been added, enter "q" to execute the search operation on the queries given in file "queries.txt". The output for this 	 will be "LuceneStoppedOutput.txt" (Task 3). The output file is present in the folder "LuceneStopped" and task ends here. 

10. Again the initial options will be displayed. Slect option "3"  to retrieve documents using Lucene. If you are performing Lucene on corpus which has stemmed data,  then 	give the path of index generation as the path till the folder "LuceneStemmed" and select option "c". Then enter the path till folder "cleaned_stemmed_data". After all the 	   3204 files have been added, enter "q" to execute the search operation on the queries given in file "cacm_stem.query.txt". The output for this will be 						"LuceneStemmedOutput.txt" (Task 3). The output file is present in the folder "LuceneStemmed" and task ends here. 

11. Again the initial options will be displayed. Select option "4" to retrieve documents using Smoothed QueryLiklihood. If you are performing Smoothed QueryLiklihood on basic 	   text transformed data then select option "1". The output will be "QuerylikelihoodBasicOutput.txt" (Task 1). The task ends here.

12. Again the initial options will be displayed. Select option "4" to retrieve documents using Smoothed QueryLiklihood. If you are performing Smoothed QueryLiklihood on 		corpus which has no stop words and has basic text transformation  then select option "2". The output will be "QuerylikelihoodStoppedOutput.txt" (Task 2). The task ends   	  here.

13. The initial options will be displayed. Select option "4" to retrieve documents using Smoothed QueryLiklihood. If you are performing Smoothed QueryLiklihood on corpus 		which has stemmed data, then select option "3". The output will be "QuerylikelihoodStemmedOutput.txt" (Task 2). The task ends here.

14. Now to assess the performance of each algorithm select option "5". Then select the desired option for the output whose output has to be analyzed. A single output file 		will be generated which contains the details of all the selected outputs.  The output file is "PerformanceAnalysis.txt". Please make sure that the output files which are 	  to be analyzed are present in the project folder and Lucene's outputs are present in their respective folder i.e "LuceneBasic", "LuceneStopped" and "LuceneStemmed".  Once 	 completed, select option "9" to exit.

The format of the file "PerformanceAnalysis.txt" is : the file has the output file name which has been analyzed, the it has the query id preceded by symbol "#". Then there is a precision recall table. At the end of each query's precision recall table the P@5 and P@20 values have been mentioned. And at the end of the result of each output file its MRR and MAP values have been mentioned. 

The format is :

Name of the file being assessed
# Query ID
Precision recall Table
P@5 P@20
# Query ID
.
.
.
P@5 P@20

MAP MRR 


15. Now execute the "Snippet_Generation.java" on basic text transformed corpus or the corpus with stop words removed. (Do not perform on stemmed queries and stemmed corpus).
	If you are reusing any folder where an index was already generated by the Lucene, then make sure to remove those files before this execution to get correct results. 
	Then enter the correct path where index has to be generated and the path where the corpus is present. Then press "q" to issue the queries of file "queries.txt". 
	The output of this execution would be "SnippetGeneratioOutput.txt" 




SPECIAL NOTE :  Kindly make sure to create the indexes and output file in the folder "LuceneBasic" "LuceneStopped" and "LuceneStemmed" respectively. These folders must be 					present in the project folder.

				The corpus is already present in the folder "cleaned_data", "cleaned_data_stopped" and "cleaned_stemmed_data". Execution of "Create_Corpus.java" is optional. If it is executed, then please not that the creation of corpus with stopped words removed might take longer time. 

				The transformed queries (Case folded and punctuations handled)  are also present in file "queries.txt" . Hence execution of "CleanQuery.java" is optional.
				If the code is executed then kindly delete the existing "queries.txt" file before execution.


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


CONTACT DETAILS :

The author of the README can be contacted via :
E-Mail : ghanwat.m@husky.neu.edu, suvarna.m@husky.neu.edu, sharma.apa@husky.neu.edu
