
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import org.jsoup.select.Evaluator.IndexGreaterThan;

public class QueryLikelihood {

	public static void CalculateQueryLikelihood(LinkedHashMap<String, ArrayList<DocInfo>> uniList,
			LinkedHashMap<String, Integer> docSize, String queryFile, String outputFile, int Doc_Count)
			throws IOException {
		int collectionsize = 0;
		collectionsize = collectionSize(docSize);
		BufferedReader input = openInputFile(queryFile);

		String text = "";

		BufferedWriter output = openOutputFile(outputFile);

		int query_id = 1;
		
		while ((text = input.readLine()) != null) {

			// LinkedHashMap<String, Double> queryFreq=new
			// LinkedHashMap<String,Double>();

			// query_frequency_for_BM25(text,queryFreq);

			LinkedHashMap<String, Double> scores = new LinkedHashMap<String, Double>();
			HashSet<String> unique_list = new LinkedHashSet<String>();
			StringTokenizer st1 = new StringTokenizer(text);
			// String p = st1.toString();
			// String query[] = new String[st1.countTokens()];

			while (st1.hasMoreTokens()) {
				unique_list.add(st1.nextToken());
			}

			// HashSet<String> unique_list=new
			// LinkedHashSet<String>(Arrays.asList(query));

			double result = 0;

			for (String s : unique_list) {
				ArrayList<DocInfo> ilist = uniList.get(s);
				int tfInCollection = termFrequnecyInCollection(uniList, s);
				if (ilist != null) {
					for (int i = 0; i < ilist.size(); i++) {
						double term1 = (double) (1 - 0.35) * ((double) (ilist.get(i).getTermFreq())
								/ ((double) docSize.get(ilist.get(i).getDocId())));
						double term2 = 0.35 * ((double) (tfInCollection) / collectionsize);
						result = (Math.log(term1 + term2))*(-1);

						if (scores.containsKey(ilist.get(i).getDocId()))

							scores.put(ilist.get(i).getDocId(), scores.get(ilist.get(i).getDocId()) + result);
						else

							scores.put(ilist.get(i).getDocId(), result);

					}
				}
			}

			create_query_table(text, query_id, scores, Doc_Count, output);

			query_id++;
		}

		closeInputFile(queryFile, input);
		closeOutputFile(outputFile, output);

	}

	private static void closeInputFile(String queryFile, BufferedReader input) throws IOException {

		if (input != null)
			input.close();
	}

	private static void closeOutputFile(String outputFile, BufferedWriter output) throws IOException {

		if (output != null)
			output.close();
	}

	private static BufferedReader openInputFile(String Filename) throws FileNotFoundException {
		BufferedReader input = null;

		File ipFile = new File(Filename);

		input = new BufferedReader(new FileReader(ipFile));

		return input;

	}

	private static BufferedWriter openOutputFile(String filename) throws IOException {

		BufferedWriter output = null;

		File opfile = new File(filename);

		output = new BufferedWriter(new FileWriter(opfile));

		return output;

	}

	private static void create_query_table(String query, int query_id, LinkedHashMap<String, Double> scores,
			int doc_Count, BufferedWriter output) throws IOException {
		// String text="Document Ranking for query :"+query+" , No. of Hits :
		// "+scores.size()+"\n";
		String text = "";
		List<Map.Entry<String, Double>> entries = new ArrayList<Map.Entry<String, Double>>(scores.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<String, Double>>() {

			public int compare(Map.Entry<String, Double> a, Map.Entry<String, Double> b) {
				if (a.getValue() < b.getValue())

					return 1;

				else if (a.getValue() > b.getValue())

					return -1;

				else
					return 0;
			}

		});

		int count = 1;

		for (Entry<String, Double> e : entries) {
			if (count > doc_Count)
				break;

			text += query_id + " " + "Q0 " + e.getKey() + " " + count + " " + e.getValue() + " "
					+ "QueryLikelyHood\r\n";
			count++;
		}

		

		output.write(text);

	}

	private static void query_frequency_for_BM25(String text, LinkedHashMap<String, Double> queryFreq) {
		String query[] = text.split(" ");

		for (String str : query) {
			if (queryFreq.containsKey(str))
				queryFreq.put(str, queryFreq.get(str) + 1.0);

			else
				queryFreq.put(str, 1.0);
		}
	}

	private static double get_avdl(LinkedHashMap<String, Integer> tokencountlist1) {

		double sum = 0;

		for (Entry<String, Integer> entry : tokencountlist1.entrySet()) {
			sum += entry.getValue();
		}

		return sum / tokencountlist1.size();

	}

	private static int collectionSize(LinkedHashMap<String, Integer> docSize) throws IOException {
		int CollectionSize = 0;
		String text = null;
		for (Entry<String, Integer> doc : docSize.entrySet()) {
			CollectionSize = CollectionSize + doc.getValue();
		}

		return CollectionSize;
	}

	private static int termFrequnecyInCollection(LinkedHashMap<String, ArrayList<DocInfo>> uniList, String key) {
		int termFreq = 0;
	
		ArrayList<DocInfo> docList = uniList.get(key);
		if (docList != null) {
			for (int i = 0; i < docList.size(); i++) {
				termFreq += docList.get(i).getTermFreq();
			}
		} else
			termFreq = 0;
		return termFreq;

	}
}
