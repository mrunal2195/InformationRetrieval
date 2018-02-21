import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class PerformanceAnalyzer {

	public static void main(String args[]) throws IOException {

		LinkedHashMap<Integer, List<Integer>> expected = new LinkedHashMap<Integer, List<Integer>>();
		LinkedHashMap<Integer, List<Integer>> result = new LinkedHashMap<Integer, List<Integer>>();

		int option = 0;

		Scanner sc = new Scanner(System.in);

		do {
			System.out.println("Please select the output file you want to analyze");
			System.out.println("1. BM25 Output for basic Text transformed data");
			System.out.println("2. BM25 Output for Corpus With Stop Words Removed");
			System.out.println("3. Lucene Output for Corpus With Basic Text Transformation");
			System.out.println("4. Lucene Output for Corpus With Stop Words Removed");
			System.out.println("5. Smoothed Query Liklihood Output for Corpus With Basic Text Transformation");
			System.out.println("6. Smoothed Query Liklihood Output for Corpus With  Stop Words Removed");
			System.out.println("7. Tf-idf Output for Corpus With Basic Text Transformation");
			System.out.println("8. Pseudo relevance Output for Corpus With Basic Text Transformation");
			System.out.println("9. Exit");
			option = sc.nextInt();

			switch (option) {

			case 1:

				result = populateResultList("BM25BasicOutput.txt");
				expected = populateExpectedList("cacm.rel.txt");
				calculateValues(result, expected, "BM25BasicOutput.txt");
				System.out.println("Output Ready");
				break;

			case 2:
				result = populateResultList("BM25StoppedOutput.txt");
				expected = populateExpectedList("cacm.rel.txt");
				calculateValues(result, expected, "BM25StoppedOutput.txt");
				System.out.println("Output Ready");
				break;

			case 3:
				result = populateResultList("LuceneBasic\\LuceneBasicOutput.txt");
				expected = populateExpectedList("cacm.rel.txt");
				calculateValues(result, expected, "LuceneBasicOutput.txt");
				System.out.println("Output Ready");
				break;

			case 4:
				result = populateResultList("LuceneStopped\\LuceneStoppedOutput.txt");
				expected = populateExpectedList("cacm.rel.txt");
				calculateValues(result, expected, "LuceneStoppedOutput.txt");
				System.out.println("Output Ready");
				break;

			case 5:
				result = populateResultList("QuerylikelihoodBasicOutput.txt");
				expected = populateExpectedList("cacm.rel.txt");
				calculateValues(result, expected, "QuerylikelihoodBasicOutput.txt");
				System.out.println("Output Ready");
				break;

			case 6:
				result = populateResultList("QuerylikelihoodStoppedOutput.txt");
				expected = populateExpectedList("cacm.rel.txt");
				calculateValues(result, expected, "QuerylikelihoodStoppedOutput.txt");
				System.out.println("Output Ready");
				break;

			case 7:
				result = populateResultList("tfIdfRanking.txt");
				expected = populateExpectedList("cacm.rel.txt");
				calculateValues(result, expected, "tfIdfRanking.txt");
				System.out.println("Output Ready");
				break;

			case 8:
				result = populateResultList("BM25PseudoOutput.txt");
				expected = populateExpectedList("cacm.rel.txt");
				calculateValues(result, expected, "BM25PseudoOutput.txt");
				System.out.println("Output Ready");
				break;

			case 9:
				System.out.println("Completed");
				sc.close();
				return;
			}
		} while (option < 9);

	}

	public static LinkedHashMap<Integer, List<Integer>> populateResultList(String fileName) {

		LinkedHashMap<Integer, List<Integer>> result = new LinkedHashMap<Integer, List<Integer>>();
		String line = "";

		try {

			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);

			while ((line = br.readLine()) != null) {

				String[] terms = line.split(" ");
				int queryId = Integer.parseInt(terms[0]);
				int docId = Integer.parseInt(terms[2]);

				if (!result.containsKey(queryId)) {

					List<Integer> value = new ArrayList<Integer>();
					value.add(docId);
					result.put(queryId, value);
				} else {

					List<Integer> value = result.get(queryId);
					value.add(docId);
					result.put(queryId, value);
				}
			}

			br.close();
			fr.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;

	}

	public static LinkedHashMap<Integer, List<Integer>> populateExpectedList(String fileName) {

		LinkedHashMap<Integer, List<Integer>> expected = new LinkedHashMap<Integer, List<Integer>>();
		String line = "";

		try {

			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);

			while ((line = br.readLine()) != null) {

				String[] terms = line.split(" ");
				int queryId = Integer.parseInt(terms[0]);
				String document = terms[2].replaceAll("CACM-", "");

				int docId = Integer.parseInt(document);

				if (!expected.containsKey(queryId)) {

					List<Integer> value = new ArrayList<Integer>();
					value.add(docId);
					expected.put(queryId, value);
				} else {

					List<Integer> value = expected.get(queryId);
					value.add(docId);
					expected.put(queryId, value);
				}
			}

			br.close();
			fr.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return expected;

	}

	public static void calculateValues(LinkedHashMap<Integer, List<Integer>> result,
			LinkedHashMap<Integer, List<Integer>> expected, String fileName) throws IOException {

		double meanAveragePrecision = 0;
		double meanReciprocalRank = 0;
		double precisionAt20 = 0;
		double precisionAt5 = 0;
		List<Double> precisions = new ArrayList<Double>();
		List<Double> reciprocalRanks = new ArrayList<Double>();
		List<Double> averagePrecisions = new ArrayList<Double>();

		FileWriter fw = new FileWriter("PerformanceAnalysis.txt", true);
		BufferedWriter bw = new BufferedWriter(fw);

		bw.write("Analysis of " + fileName + "\r\n");

		for (Integer queryId : result.keySet()) {
			List<Integer> retrievedDocumentList = result.get(queryId);

			for (Integer queryId1 : expected.keySet()) {

				if (queryId.equals(queryId1)) {

					int retrievedIntersectionRelevantCount = 0;
					int retrievedDocumentsCount = 0;
					double precision = 0;
					double reciprocalRank = 0;
					double recall = 0;
					double averagePrecision = 0;

					bw.write("#" + queryId + "\r\n");
					List<Integer> expectedRelevantDocumentList = expected.get(queryId1);
					Iterator<Integer> itr = retrievedDocumentList.iterator();

					while (itr.hasNext()) {

						retrievedDocumentsCount++;
						int retrievedDocument = itr.next();

						if (expectedRelevantDocumentList.contains(retrievedDocument)) {
							retrievedIntersectionRelevantCount++;
							precision = (double) retrievedIntersectionRelevantCount / (double) retrievedDocumentsCount;
							recall = (double) retrievedIntersectionRelevantCount
									/ (double) expectedRelevantDocumentList.size();
							precisions.add(precision);
						}

						else {
							precision = (double) retrievedIntersectionRelevantCount / (double) retrievedDocumentsCount;
							recall = (double) retrievedIntersectionRelevantCount
									/ (double) expectedRelevantDocumentList.size();
						}

						if ((retrievedIntersectionRelevantCount == 1) && (reciprocalRank == 0)) {
							reciprocalRank = precision;
							reciprocalRanks.add(reciprocalRank);
						}

						if (retrievedDocumentsCount == 5)
							precisionAt5 = precision;

						if (retrievedDocumentsCount == 20)
							precisionAt20 = precision;

						String line = "precision  : " + precision + " recall  : " + recall + "\r\n";
						bw.write(line);
					}
					
					bw.write("\r\n Precision@5 : " + precisionAt5 + "  Precision@20 : " + precisionAt20 + "\r\n");
					double sum = 0;
					for (Double precision1 : precisions) {

						sum += precision1;
					}
					averagePrecision = sum / precisions.size();
					averagePrecisions.add(averagePrecision);

				}

			}

		}

		double sum = 0;
		for (Double averagePrecision1 : averagePrecisions) {

			sum += averagePrecision1;
		}
		meanAveragePrecision = sum / averagePrecisions.size();

		sum = 0;
		for (Double reciprocalRank1 : reciprocalRanks) {

			sum += reciprocalRank1;
			meanReciprocalRank = sum / reciprocalRanks.size();
		}

		
		bw.write("\r\n meanAveragePrecision : " + meanAveragePrecision + "  meanReciprocalRank : " + meanReciprocalRank
				+ "\r\n\r\n");

		bw.close();
		fw.close();
	}
}