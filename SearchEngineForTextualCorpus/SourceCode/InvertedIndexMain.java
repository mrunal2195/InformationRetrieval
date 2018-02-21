
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.apache.*;
import org.apache.lucene.analysis.ngram.NGramTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;

import java.net.URLConnection;

public class InvertedIndexMain {

	private final int numOfDocs = 100;
	

	public static void main(String[] args) throws IOException {
		LinkedHashMap<String, Integer> tokenCountList = new LinkedHashMap<String, Integer>();
		LinkedHashMap<String, ArrayList<Termfreq>> tfIdf = new LinkedHashMap<String, ArrayList<Termfreq>>();
		LinkedHashMap<String, Double> idfList = new LinkedHashMap<String, Double>();
		LinkedHashMap<String, Double> tfidfRanking = new LinkedHashMap<String, Double>();

		int numOfDocs = 100;
		String basicCorpus = "cleaned_data";
		String StoppedCorpus = "cleaned_data_stopped";
		String StemmedCorpus = "cleaned_stemmed_data";

		int n = 0;
		int pseudo;
		int corpusType = 0;
		do {
			System.out.println("\nChoose 1: BM25 2: Tf-Idf 3: Lucene 4: Query Liklihood 5: Performance Analysis 6: Exit");
			Scanner sc = new Scanner(System.in);

			n = sc.nextInt();
			switch (n) {
			case 1:
				System.out.println("Select the type of corpus for BM25");
				System.out.println("1. Basic Text Transformed Corpus\n2. Stop Words Removed Corpus\n3. Stemmed Corpus");
				corpusType = sc.nextInt();

				if (corpusType == 1) {

					File folder = new File(basicCorpus);
					File[] files = folder.listFiles();

					LinkedHashMap<String, Integer> docInfo = new LinkedHashMap<String, Integer>(
							collectDocInfo(basicCorpus));
					LinkedHashMap<String, ArrayList<DocInfo>> iList = new LinkedHashMap<String, ArrayList<DocInfo>>();
					tfIdf.clear();
					tokenCountList.clear();
					createUnigramFile(files, iList, tokenCountList, tfIdf, basicCorpus);
					BM25.calculateBM25(iList, docInfo, "queries.txt", "BM25BasicOutput.txt", numOfDocs);

					System.out.println("Do you wish to perform Pseudo Relevance feedback");
					System.out.println("Press 1 if yes");
					
					pseudo = sc.nextInt();
					
					if (pseudo == 1) {
						PseudoRelevance.initialMethod();
						
						//tfIdf.clear();
						//tokenCountList.clear();
						////iList.clear();
						//docInfo.clear();
						//createUnigramFile(files, iList, tokenCountList, tfIdf, basicCorpus);
						BM25.calculateBM25(iList, docInfo, "ExpandedQueries.txt", "BM25PseudoOutput.txt", numOfDocs);

					}
					System.out.println("Task Completed");
				}

				if (corpusType == 2) {
					File folder = new File(StoppedCorpus);
					File[] files = folder.listFiles();

					LinkedHashMap<String, Integer> docInfo = new LinkedHashMap<String, Integer>(
							collectDocInfo(StoppedCorpus));
					LinkedHashMap<String, ArrayList<DocInfo>> iList = new LinkedHashMap<String, ArrayList<DocInfo>>();
                    tfIdf.clear();
                    tokenCountList.clear();
					createUnigramFile(files, iList, tokenCountList, tfIdf, StoppedCorpus);
					BM25.calculateBM25(iList, docInfo, "queries.txt", "BM25StoppedOutput.txt", numOfDocs);
					System.out.println("Task Completed");

				}

				if (corpusType == 3) {

					File folder = new File(StemmedCorpus);
					File[] files = folder.listFiles();

					LinkedHashMap<String, Integer> docInfo = new LinkedHashMap<String, Integer>(
							collectDocInfo(StemmedCorpus));
					LinkedHashMap<String, ArrayList<DocInfo>> iList = new LinkedHashMap<String, ArrayList<DocInfo>>();
					tfIdf.clear();
					tokenCountList.clear();
					createUnigramFile(files, iList, tokenCountList, tfIdf, StemmedCorpus);
					BM25.calculateBM25(iList, docInfo, "cacm_stem.query.txt", "BM25StemmedOutput.txt", numOfDocs);
					System.out.println("Task Completed");

				}
				break;
			case 2:
				File folder = new File(basicCorpus);
				File[] files = folder.listFiles();

				LinkedHashMap<String, Integer> docInfo = new LinkedHashMap<String, Integer>(
						collectDocInfo(basicCorpus));
				LinkedHashMap<String, ArrayList<DocInfo>> iList = new LinkedHashMap<String, ArrayList<DocInfo>>();
				LinkedHashMap<String, Integer> termfrequencyList  = new  LinkedHashMap<String, Integer>();
				tfIdf.clear();
				tokenCountList.clear();
				termfrequencyList = createUnigramFile(files, iList, tokenCountList, tfIdf, basicCorpus);
				idfList = calculateTfIdf(tfIdf, termfrequencyList, docInfo.size());
				calculatequeryTfIdf("queries.txt", idfList, files, tfIdf, tfidfRanking, numOfDocs);
				System.out.println("Task Completed");

				break;
				

			case 3:LuceneIndexer.main(args);
			break;
				
				
			case 4: System.out.println("Select the type of corpus for Query Likelihood");
					System.out.println("1. Basic Text Transformed Corpus\n2. Stop Words Removed Corpus\n3. Stemmed Corpus");
					corpusType = sc.nextInt();

				

				if (corpusType == 1) {

					File folder1 = new File(basicCorpus);
					File[] files1 = folder1.listFiles();

					LinkedHashMap<String, Integer> docInfo1 = new LinkedHashMap<String, Integer>(
							collectDocInfo(basicCorpus));
					LinkedHashMap<String, ArrayList<DocInfo>> iList1 = new LinkedHashMap<String, ArrayList<DocInfo>>();
					tfIdf.clear();
					tokenCountList.clear();
					createUnigramFile(files1, iList1, tokenCountList, tfIdf, basicCorpus);
					QueryLikelihood.CalculateQueryLikelihood(iList1, docInfo1, "queries.txt", "QuerylikelihoodBasicOutput.txt", numOfDocs);
					System.out.println("Task Completed");
				}

				if (corpusType == 2) {
					File folder1 = new File(StoppedCorpus);
					File[] files1 = folder1.listFiles();

					LinkedHashMap<String, Integer> docInfo1 = new LinkedHashMap<String, Integer>(
							collectDocInfo(StoppedCorpus));
					LinkedHashMap<String, ArrayList<DocInfo>> iList1 = new LinkedHashMap<String, ArrayList<DocInfo>>();
					tokenCountList.clear();
					tfIdf.clear();
					createUnigramFile(files1, iList1, tokenCountList, tfIdf, StoppedCorpus);
					QueryLikelihood.CalculateQueryLikelihood(iList1, docInfo1, "queries.txt", "QuerylikelihoodStoppedOutput.txt", numOfDocs);
					System.out.println("Task Completed");

				}

				if (corpusType == 3) {

					File folder1 = new File(StemmedCorpus);
					File[] files1 = folder1.listFiles();

					LinkedHashMap<String, Integer> docInfo1 = new LinkedHashMap<String, Integer>(
							collectDocInfo(StemmedCorpus));
					LinkedHashMap<String, ArrayList<DocInfo>> iList1 = new LinkedHashMap<String, ArrayList<DocInfo>>();
					tokenCountList.clear();
					tfIdf.clear();
					createUnigramFile(files1, iList1, tokenCountList, tfIdf, StemmedCorpus);
					QueryLikelihood.CalculateQueryLikelihood(iList1, docInfo1, "cacm_stem.query.txt", "QuerylikelihoodStemmedOutput.txt", numOfDocs);
					System.out.println("Task Completed");

				}
				break;

			case 5:
				PerformanceAnalyzer.main(args);
				
			case 6:
				return;

			}
		} while (n <= 5);

	}

	private static LinkedHashMap<String, Integer> createInvertedIndex(LinkedHashMap<String, ArrayList<DocInfo>> iList,
			LinkedHashMap<String, Integer> tokenCountList, String file,
			LinkedHashMap<String, Integer> totalTokensInEachFile,
			LinkedHashMap<String, ArrayList<Termfreq>> normalizedTF) throws FileNotFoundException {
		
		Scanner in = new Scanner(new File(file));
		LinkedHashMap<String, Double> termAndTermFreq = new LinkedHashMap<String, Double>();
		String docId = null;
		DocInfo dInfo = null;
		String s, id = "";
		double tokensInFile = 0.0;
		while (in.hasNext()) {
			s = in.next();
			String[] terms = file.split("\\\\"); 
			id = terms[1];
			docId = id.replace(".txt", "");
			tokensInFile++;
		
			if (tokenCountList.containsKey(s)) {
				tokenCountList.put(s, (tokenCountList.get(s)) + 1);
			} else {
				tokenCountList.put(s, 1);
			}

			if (!s.matches("\\d+")) {
				if (iList.containsKey(s)) {
					if (iList.get(s).get(iList.get(s).size() - 1).getDocId().equals(docId)) {
						iList.get(s).get(iList.get(s).size() - 1).incrementTermFreq();

					}

					else {
						dInfo = new DocInfo(docId, 1);
						iList.get(s).add(dInfo);

					}
				} else {
					ArrayList<DocInfo> dlist = new ArrayList<DocInfo>();
					dInfo = new DocInfo(docId, 1);
					dlist.add(dInfo);
					iList.put(s, dlist);
				}

			}

			if (!s.matches("\\d+")) {
				if (termAndTermFreq.containsKey(s)) {
					termAndTermFreq.put(s, (termAndTermFreq.get(s)) + 1);
				} else {
					termAndTermFreq.put(s, 1.0);
				}
			} else {
				termAndTermFreq.put(s, 1.0);
			}

		}

		ArrayList<Termfreq> tfList = new ArrayList<Termfreq>();
		for (Map.Entry<String, Double> entry : termAndTermFreq.entrySet()) {
			Termfreq t = new Termfreq(entry.getKey(), entry.getValue(), tokensInFile);
			tfList.add(t);

		}

		normalizedTF.put(docId, tfList);

		return tokenCountList;
	}

	public static List<String> ngrams(int n, String str) {
		List<String> ngrams = new ArrayList<String>();
		String[] words = str.split(" ");
		for (int i = 0; i < words.length - n + 1; i++)
			ngrams.add(concat(words, i, i + n));
		return ngrams;
	}

	public static String concat(String[] words, int start, int end) {
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < end; i++)
			sb.append((i > start ? " " : "") + words[i]);
		return sb.toString();
	}

	private static void createInvertedIndexNgram(LinkedHashMap<String, ArrayList<DocInfo>> iList,
			LinkedHashMap<String, Integer> tokenCountList, String file, int n) throws IOException {
		BufferedReader input = readFile(file);
		// storing file in a string to generate bigrams
		StringBuilder sb = new StringBuilder();
		try {

			String line = input.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(" ");
				line = input.readLine();
			}

		} finally {
			input.close();
		}

		String docId = null;
		DocInfo dInfo = null;

		for (String ngram : ngrams(n, sb.toString()))

		{

			String id = file;
			docId = id.replace(".txt", "");

			if (tokenCountList.containsKey(ngram)) {
				tokenCountList.put(ngram, (tokenCountList.get(ngram)) + 1);
			} else {
				tokenCountList.put(ngram, 1);
			}

			if (!ngram.matches("\\d+")) {
				if (iList.containsKey(ngram)) {
					if (iList.get(ngram).get(iList.get(ngram).size() - 1).getDocId().equals(docId)) {
						iList.get(ngram).get(iList.get(ngram).size() - 1).incrementTermFreq();
					}

					else {
						dInfo = new DocInfo(docId, 1);
						iList.get(ngram).add(dInfo);
					}
				} else {
					ArrayList<DocInfo> dlist = new ArrayList<DocInfo>();
					dInfo = new DocInfo(docId, 1);
					dlist.add(dInfo);
					iList.put(ngram, dlist);
				}

			}

		}

		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static BufferedReader readFile(String file) {
		BufferedReader input = null;
		try {
			File f = new File(file);
			input = new BufferedReader(new FileReader(f));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return input;
	}

	private static BufferedWriter openOutputFile(String file) {
		BufferedWriter output = null;
		try {
			File f = new File(file);
			output = new BufferedWriter(new FileWriter(f));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}

	private static void writeInvertedIndex(LinkedHashMap<String, ArrayList<DocInfo>> iList, BufferedWriter output) {
		for (Map.Entry<String, ArrayList<DocInfo>> entry : iList.entrySet()) {
			String t = "";
			t += entry.getKey() + " ";
			for (DocInfo d : entry.getValue()) {
				t += d + " ";
			}
			t += "\r\n";
			t += "\r\n";
			try {
				output.write(t);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static void writeTable(LinkedHashMap<String, Integer> iList, BufferedWriter output) {
		for (Map.Entry<String, Integer> entry : iList.entrySet()) {
			String t = "";
			t += entry.getKey() + " : ";
			t += entry.getValue() + " ";

			t += "\r\n";
			t += "\r\n";
			try {
				output.write(t);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static void writeDocTokenCount(LinkedHashMap<String, Integer> iList, BufferedWriter output) {
		for (Map.Entry<String, Integer> entry : iList.entrySet()) {
			String t = "";
			t += entry.getKey() + " : ";
			t += entry.getValue();

			t += "\r\n";
			try {
				output.write(t);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static LinkedHashMap<String, Integer> writeTableforDf(LinkedHashMap<String, ArrayList<DocInfo>> iList,
			BufferedWriter output) {
		LinkedHashMap<String, Integer> dfList = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, ArrayList<DocInfo>> entry : iList.entrySet()) {
			String t = "";
			t += entry.getKey() + " :";
			int docCount = 0;
			for (DocInfo d : entry.getValue()) {

				t += d.getDocId() + "    ";
				docCount++;
			}

			t += docCount + " ";
			t += "\r\n";
			t += "\r\n";

			dfList.put(entry.getKey(), docCount);

			try {
				output.write(t);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return dfList;

	}

	private static LinkedHashMap<String, Integer> createUnigramFile(File[] files,
			LinkedHashMap<String, ArrayList<DocInfo>> iList, LinkedHashMap<String, Integer> tokenCountList,
			LinkedHashMap<String, ArrayList<Termfreq>> normalizedTF, String path)
			throws FileNotFoundException {
		LinkedHashMap<String, Integer> tokenCountSortedList = new LinkedHashMap<String, Integer>();
		LinkedHashMap<String, Integer> totalTokensInEachFile = new LinkedHashMap<String, Integer>();
		LinkedHashMap<String, Integer> termfrequencyList = new LinkedHashMap<String, Integer>();
		// LinkedHashMap<String, Double> tfIdfList = new LinkedHashMap<String,
		// Double>();

		LinkedHashMap<String, ArrayList<DocInfo>> dfSortedList = new LinkedHashMap<String, ArrayList<DocInfo>>();
		for (File file : files) {
			// if (!file.isFile()) continue;
			tokenCountList = createInvertedIndex(iList, tokenCountList, path+ "\\" +file.getName(), totalTokensInEachFile,
					normalizedTF);

		}

		// Object[] tokenCounts = tokenCountList.entrySet().toArray();

		BufferedWriter output = openOutputFile("InvertedIndexUnigram.txt");
		BufferedWriter unigramCount = openOutputFile("TermFrequencyUnigram.txt");
		BufferedWriter documentFrequencyUnigram = openOutputFile("DocumentFrequencyUnigram.txt");
		BufferedWriter documenttokenCountList = openOutputFile("documenttokenCount.txt");

		writeInvertedIndex(iList, output);

		tokenCountSortedList = sort(tokenCountList);
		writeTable(tokenCountSortedList, unigramCount);
		writeDocTokenCount(tokenCountList, documenttokenCountList);

		dfSortedList = sortdf(iList);
		termfrequencyList = writeTableforDf(dfSortedList, documentFrequencyUnigram);
		

		try {
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return termfrequencyList;

	}

	private static LinkedHashMap<String, Integer> sort(LinkedHashMap<String, Integer> tf) {
		Object[] a = tf.entrySet().toArray();
		Arrays.sort(a, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				return ((Map.Entry<String, Integer>) o2).getValue()
						.compareTo(((Map.Entry<String, Integer>) o1).getValue());

			}

		});

		tf.clear();
		for (Object e : a) {

			tf.put(((Map.Entry<String, Integer>) e).getKey(), ((Map.Entry<String, Integer>) e).getValue());

		}
		return tf;
	}

	private static LinkedHashMap<String, Double> sortDouble(LinkedHashMap<String, Double> tf) {
		Object[] a = tf.entrySet().toArray();
		Arrays.sort(a, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				return ((Map.Entry<String, Double>) o2).getValue()
						.compareTo(((Map.Entry<String, Double>) o1).getValue());

			}

		});

		tf.clear();
		for (Object e : a) {

			tf.put(((Map.Entry<String, Double>) e).getKey(), ((Map.Entry<String, Double>) e).getValue());

		}
		return tf;
	}

	private static LinkedHashMap<String, ArrayList<DocInfo>> sortdf(LinkedHashMap<String, ArrayList<DocInfo>> df) {
		Object[] a = df.entrySet().toArray();
		Arrays.sort(a, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				return ((Map.Entry<String, ArrayList<DocInfo>>) o1).getKey()
						.compareTo(((Map.Entry<String, ArrayList<DocInfo>>) o2).getKey());

			}

		});

		df.clear();
		for (Object e : a) {

			df.put(((Map.Entry<String, ArrayList<DocInfo>>) e).getKey(),
					((Map.Entry<String, ArrayList<DocInfo>>) e).getValue());

		}

		return df;
	}

	private static void createBigramFile(File[] files, LinkedHashMap<String, ArrayList<DocInfo>> iListBiGram,
			LinkedHashMap<String, Integer> tokenCountListBigram) throws IOException {
		LinkedHashMap<String, Integer> tokenCountSortedList = new LinkedHashMap<String, Integer>();
		LinkedHashMap<String, ArrayList<DocInfo>> dfSortedList = new LinkedHashMap<String, ArrayList<DocInfo>>();
		for (File file : files) {
			if (!file.isFile())
				continue;
			createInvertedIndexNgram(iListBiGram, tokenCountListBigram, file.getName(), 2);
		}
		BufferedWriter outputBigram = openOutputFile("InvertedIndexBigram.txt");
		BufferedWriter bigramCount = openOutputFile("TermFrequencyBigram.txt");
		BufferedWriter documentFrequencyBigram = openOutputFile("DocumentFrequencyBigram.txt");

		writeInvertedIndex(iListBiGram, outputBigram);

		tokenCountSortedList = sort(tokenCountListBigram);
		writeTable(tokenCountSortedList, bigramCount);

		dfSortedList = sortdf(iListBiGram);
		writeTableforDf(dfSortedList, documentFrequencyBigram);

	}

	private static void createTrigramFile(File[] files, LinkedHashMap<String, ArrayList<DocInfo>> iListTriGram,
			LinkedHashMap<String, Integer> tokenCountListTrigram) throws IOException {
		LinkedHashMap<String, Integer> tokenCountSortedList = new LinkedHashMap<String, Integer>();
		LinkedHashMap<String, ArrayList<DocInfo>> dfSortedList = new LinkedHashMap<String, ArrayList<DocInfo>>();
		for (File file : files) {
			if (!file.isFile())
				continue;
			createInvertedIndexNgram(iListTriGram, tokenCountListTrigram, file.getName(), 3);
		}
		BufferedWriter outputTrigram = openOutputFile("InvertedIndexTrigram.txt");
		BufferedWriter trigramCount = openOutputFile("TermFrequencyTrigram.txt");
		BufferedWriter documentFrequencyTrigram = openOutputFile("DocumentFrequencyTrigram.txt");

		writeInvertedIndex(iListTriGram, outputTrigram);

		tokenCountSortedList = sort(tokenCountListTrigram);
		writeTable(tokenCountSortedList, trigramCount);

		dfSortedList = sortdf(iListTriGram);
		writeTableforDf(dfSortedList, documentFrequencyTrigram);

	}

	// create a doc with term and its tfIdf

	/*
	 * private static void writeStopList(LinkedHashMap <String, Double>
	 * tfIdfList, BufferedWriter outFile) { int i = 0; for(Map.Entry<String,
	 * Double> entry:tfIdfList.entrySet()) { if (i == 50) return;
	 * 
	 * String t = ""; t+= entry.getKey(); t+="\r\n";
	 * 
	 * 
	 * 
	 * try { outFile.write(t); } catch (IOException e) { e.printStackTrace(); }
	 * }
	 * 
	 * 
	 * }
	 */

	private static LinkedHashMap<String, Double> calculateTfIdf(LinkedHashMap<String, ArrayList<Termfreq>> tf,
			LinkedHashMap<String, Integer> df, int fileLength) {

		LinkedHashMap<String, Double> idfList = new LinkedHashMap<String, Double>();

		Object[] a = tf.entrySet().toArray();
		
		for (Object e : a) {
			ArrayList<Termfreq> arr = new ArrayList<Termfreq>();
			arr = ((Map.Entry<String, ArrayList<Termfreq>>) e).getValue();
			for (Termfreq t : arr) {
				for (Map.Entry<String, Integer> en : df.entrySet()) {
					if (t.getDocId() == en.getKey()) {
						double idf = 1.0 + Math.log(fileLength / en.getValue());

						t.settfidf(idf);

					}
				}
			}
			

		}

		for (Map.Entry<String, Integer> entry : df.entrySet()) {

			double idf = 1.0 + Math.log(fileLength / entry.getValue());
			idfList.put(entry.getKey(), idf);

		}

		return idfList;
	}

	/*
	 * private static LinkedHashMap<String, ArrayList<DocInfo>>
	 * convertToDataStructue(String file){
	 * 
	 * BufferedReader in = readFile(file); LinkedHashMap<String,
	 * ArrayList<DocInfo>> inverted_list = new LinkedHashMap<String,
	 * ArrayList<DocInfo>>(); String line; try {
	 * 
	 * while((line = in.readLine()) != null) { String index[]= line.split(" ");
	 * ArrayList<DocInfo> documents = new ArrayList<DocInfo>(); for(int i = 0; i
	 * < index.length - 1; i++) { String s = index[i + 1]; String docid =
	 * s.substring( s.lastIndexOf('[') + 1, s.lastIndexOf(',')); // get docId
	 * String dTermFreq = s.substring(s.lastIndexOf(',')+1,
	 * s.lastIndexOf(']')).trim(); // get termfreq
	 * 
	 * 
	 * DocInfo docInfo = new DocInfo(docid,Integer.parseInt(dTermFreq));
	 * documents.add(docInfo); // adds the document id and the term frequency to
	 * the data structure }
	 * 
	 * inverted_list.put(index[0], documents); // add term(key) and docid,term
	 * frequency(value) to the LinkedHashMap }
	 * 
	 * 
	 * } catch (IOException e) { e.printStackTrace(); }
	 * 
	 * try { in.close(); } catch (IOException e) { // TODO Auto-generated catch
	 * block e.printStackTrace(); } return inverted_list; }
	 */

	private static BufferedWriter writeFile(String file) {
		BufferedWriter output = null;
		try {
			File f = new File(file);
			output = new BufferedWriter(new FileWriter(f));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return output;
	}

	// calculate avg document length

	private static void write_query_to_file_for_tfidf(String query, int query_id,
			LinkedHashMap<String, Double> docScore, int numOfDocs, BufferedWriter resultFile) {
		String line = "";

		// sorting based on score
		List<Map.Entry<String, Double>> entries = new ArrayList<Map.Entry<String, Double>>(docScore.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> first, Map.Entry<String, Double> second) {
				if (first.getValue() < second.getValue())
					return 1;
				else if (first.getValue() > second.getValue())
					return -1;
				else
					return 0;
			}
		});

		int count = 1;

		for (Map.Entry<String, Double> e : entries) {
			if (count > numOfDocs)
				break;

			line += query_id + " Q0 " + e.getKey() + " " + count + " " + e.getValue() + " " + "TFIDF\r\n";
			count++;
		}
		

		try {
			resultFile.write(line);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void calculatequeryTfIdf(String queryFile, LinkedHashMap<String, Double> idfList, File[] files,
			LinkedHashMap<String, ArrayList<Termfreq>> tfIdf, LinkedHashMap<String, Double> tfidfRanks, int numOfDocs)
			throws IOException {

		BufferedWriter resultFile = writeFile("tfIdfRanking.txt");
		BufferedReader query = readFile(queryFile);
		String line = null;
		while ((line = query.readLine()) != null) {
			LinkedHashMap<String, Double> queryTfIdf = new LinkedHashMap<String, Double>();
			String query_terms[] = line.split(" "); // splitting the query to
													// fetch different terms
			double total_terms = (double) query_terms.length;
			LinkedHashSet<String> unique_list = new LinkedHashSet<String>(Arrays.asList(query_terms));
			LinkedHashMap<String, Double> queryFreq = new LinkedHashMap<String, Double>(); // to
																							// store
																							// query
																							// term
																							// and
																							// its
																							// frequency
			createQueryFrequency(line, queryFreq);
			int query_id = 1;

			for (String q : unique_list) {
				for (Map.Entry<String, Double> en : idfList.entrySet()) {
					if (en.getKey().equals(q)) {
						double qtf = queryFreq.get(q);
						double idf = 1.0 + Math.log(files.length / en.getValue());
						queryTfIdf.put(q, qtf * idf);

					}
				}
			}

			for (Map.Entry<String, ArrayList<Termfreq>> en : tfIdf.entrySet()) {
				double dotProduct = 0.0;
				double queryMag = 0.0;
				double queryMagnitude = 0.0;
				double documenMagnitude = 0.0;
				double documenMag = 0.0;
				double cosineSimilarity = 0.0;

				for (Termfreq t : en.getValue()) {
					for (Map.Entry<String, Double> e : queryTfIdf.entrySet()) {
						if (t.getDocId().equals(e.getKey())) {

							Double prod = e.getValue() * t.normalizedTermFreq(); // query
																					// tfidf
																					// *
																					// termtfidf
																					// of
																					// document
							dotProduct = dotProduct + prod;
							documenMag = documenMag + Math.pow(t.normalizedTermFreq(), 2);
						}
					}
				}

				for (Map.Entry<String, Double> e : queryTfIdf.entrySet()) {
					queryMag = queryMag + Math.pow(e.getValue(), 2);
				}

				queryMagnitude = Math.sqrt(queryMag);
				documenMagnitude = Math.sqrt(documenMag);

				if (queryMagnitude != 0.0 && documenMagnitude != 0.0) {
					cosineSimilarity = dotProduct / (queryMagnitude * documenMagnitude);
				}

				tfidfRanks.put(en.getKey(), cosineSimilarity);

			}
			write_query_to_file_for_tfidf(line, query_id, tfidfRanks, numOfDocs, resultFile);
			query_id++;

		}

		try {
			resultFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void writefIdfToaFile(LinkedHashMap<String, ArrayList<Termfreq>> tfIdf) throws IOException {

		for (Map.Entry<String, ArrayList<Termfreq>> en : tfIdf.entrySet()) {
			BufferedWriter resultFile = writeFile(en.getKey() + ".txt");
			String line = "";
			ArrayList<Termfreq> arr = new ArrayList<Termfreq>();
			arr = en.getValue();
			Collections.sort(arr, Comparator.comparingDouble(TermFreqInterface::normalizedTermFreq).reversed());
			for (Termfreq t : arr) {
				line = t.getDocId() + " " + t.normalizedTermFreq() + "\r\n";
				resultFile.write(line);
			}

			resultFile.close();

		}
	}

	private static LinkedHashMap<String, Integer> collectDocInfo(String folderName) throws FileNotFoundException {

		File folder = new File(folderName);
		File[] files = folder.listFiles();

		LinkedHashMap<String, Integer> docInfo = new LinkedHashMap<String, Integer>();

		for (File file : files) {
			// if (!file.isFile()) continue;
			Scanner in = new Scanner(new File(folderName + "\\" + file.getName()));
			String docId = null;
			DocInfo dInfo = null;
			String s;
			while (in.hasNext()) {
				s = in.next();
				String id = file.getName();
				docId = id.replace(".txt", "");
				// docId = file;
				if (docInfo.containsKey(docId)) {
					docInfo.put(docId, (docInfo.get(docId)) + 1);
				} else {
					docInfo.put(docId, 1);
				}
			}

		}

		return docInfo;
	}
	
	// linkedHashMap to store query term and its frequency in the query
		private static void createQueryFrequency(String query, LinkedHashMap<String, Double> queryFreq) {
			// TODO Auto-generated method stub
			String query_terms[] = query.split(" ");
			for(String s : query_terms)
			{
				if(queryFreq.containsKey(s))
					queryFreq.put(s,  queryFreq.get(s)+ 1.0);
				else
					queryFreq.put(s, 1.0);
			}
				
		}

}
