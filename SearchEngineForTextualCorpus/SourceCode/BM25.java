import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class BM25 {

	public static void calculateBM25(LinkedHashMap<String, ArrayList<DocInfo>> inverted_list,
			LinkedHashMap<String, Integer> docInfo, String queryFile, String outputFile, int numOfDocs)
			throws IOException {
		BufferedWriter resultFile = writeFile(outputFile);
		BufferedReader query = readFile(queryFile);
		double k1 = 1.2, b = 0.75, k2 = 100, result = 0;
		int query_id = 1;

		String line = null;
		while ((line = query.readLine()) != null) {
			LinkedHashMap<String, Double> docScore = new LinkedHashMap<String, Double>(); // to
																							// store
																							// score
																							// of
																							// each
																							// document
																							// which
																							// contains
																							// the
																							// query
																							// term
			String query_terms[] = line.split(" "); // splitting the query to
													// fetch different terms
			LinkedHashSet<String> unique_list = new LinkedHashSet<String>(Arrays.asList(query_terms));
			LinkedHashMap<String, Double> queryFreq = new LinkedHashMap<String, Double>(); // to
																							// store
																							// query
																							// term
																							// and
																							// its
																							// frequency
			createQueryFrequency(line, queryFreq); // store query term and its
													// frequency in the query

			// calculate for all query terms
			for (String q : unique_list) {
				ArrayList<DocInfo> ilist = inverted_list.get(q);
				if (ilist != null) {
					int ilistSize = ilist.size();
					for (int i = 0; i < ilistSize; i++) {
						String docId = ilist.get(i).getDocId();
						double term1 = (1.0
								/ (((double) ilistSize + 0.5) / ((double) docInfo.size() - (double) ilistSize + 0.5)));
						double k_term = (k1 * ((1.0 - b) + (b * (double) docInfo.get(docId) / get_avdl(docInfo))));
						double term2 = ((k1 + 1.0) * (double) ilist.get(i).getTermFreq())
								/ (k_term + (double) ilist.get(i).getTermFreq());
						double term3 = ((k2 + 1.0) * queryFreq.get(q)) / (k2 + queryFreq.get(q));
						result = Math.log(term1 * term2 * term3);

						// checks if doc id is already added then update the
						// score
						if (docScore.containsKey(docId)) {
							docScore.put(docId, docScore.get(docId) + result);
						} else {
							docScore.put(docId, result);
						}

					}
				} // end of for loop
			} 

			write_query_to_file(line, query_id, docScore, numOfDocs, resultFile);
			query_id++;
		}

		try {
			resultFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static double get_avdl(LinkedHashMap<String, Integer> docInfo) {
		double sum = 0;

		for (Map.Entry<String, Integer> e : docInfo.entrySet()) {
			sum = sum + e.getValue();
		}
		return sum / docInfo.size();
	}

	// linkedHashMap to store query term and its frequency in the query
	private static void createQueryFrequency(String query, LinkedHashMap<String, Double> queryFreq) {
		// TODO Auto-generated method stub
		String query_terms[] = query.split(" ");
		for (String s : query_terms) {
			if (queryFreq.containsKey(s))
				queryFreq.put(s, queryFreq.get(s) + 1.0);
			else
				queryFreq.put(s, 1.0);
		}

	}

	private static void write_query_to_file(String query, int query_id, LinkedHashMap<String, Double> docScore,
			int numOfDocs, BufferedWriter resultFile) {
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

			line += query_id + " Q0 " + e.getKey() + " " + count + " " + e.getValue() + " " + "BM25\r\n";
			count++;
		}
		

		try {
			resultFile.write(line);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

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

}
