
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PseudoRelevance {

	// using pseudo relevance on the output of BM25

	private static String queryFilePath = "queries.txt";
	private static String initialResults = "BM25BasicOutput.txt";
	private static String stopWordsFilePath = "common_words.txt";
	private static File tfFrequencyPath = new File("term-termfreq");
	private static File[] listOfFiles = tfFrequencyPath.listFiles();

	private static List<String> stopWords = new ArrayList<String>();
	private static List<String> queries = new ArrayList<String>();

	public static void initialMethod() {

		String initialQuery;
		String finalQuery;
		int queryNumber = 0;

		readQueryFile();
		readStopWords();

		for (int i = 0; i < queries.size(); i++)

		{
			initialQuery = queries.get(queryNumber);
			queryNumber++;

			List<Integer> documentList = new ArrayList<Integer>(getTopRankingDocuments(queryNumber));
			List<String> newWords = new ArrayList<String>(getHighestTfIdfTerms(documentList));

			finalQuery = getExpandedQuery(initialQuery, newWords);
			writeFinalQuery(finalQuery);
		}

	}

	public static void readQueryFile() {

		String query;

		try {

			FileReader fr = new FileReader(queryFilePath);
			BufferedReader br = new BufferedReader(fr);

			while ((query = br.readLine()) != null) {

				queries.add(query);

			}

			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void readStopWords() {

		String stopWord = null;
		try {

			FileReader fr = new FileReader(stopWordsFilePath);
			BufferedReader br = new BufferedReader(fr);

			while ((stopWord = br.readLine()) != null) {

				stopWords.add(stopWord);

			}

			br.close();
			fr.close();
		}

		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<String> getHighestTfIdfTerms(List<Integer> documentList) {

		List<String> newWords = new ArrayList<String>();

		int numberOfTerms = 0;
		int countOfFilesRead = 0;

		try {

			for (int i = 0; i < documentList.size(); i++) {

				FileReader fr = new FileReader(tfFrequencyPath + "\\" + documentList.get(i) + ".txt");
				BufferedReader br = new BufferedReader(fr);

				numberOfTerms = 0;

				if (!listOfFiles[i].isFile())
					continue;

				if (countOfFilesRead <= 10) {

					while (numberOfTerms < 2) {

						String[] line = br.readLine().split(" ");

						if (!stopWords.contains(line[0])) {

							newWords.add(line[0]);
							numberOfTerms++;
						}

					}

					countOfFilesRead++;
				}

				br.close();
				fr.close();
			}

		} catch (IOException e) {

			e.printStackTrace();
		}

		return newWords;

	}

	public static String getExpandedQuery(String initialQuery, List<String> newWords) {

		String finalQuery = initialQuery;

		for (String newWord : newWords) {

			finalQuery += " " + newWord;
		}

		return finalQuery;

	}

	public static List<Integer> getTopRankingDocuments(int queryNumber) {

		String line;
		int topK = 0; 
		
		List<Integer> documentList = new ArrayList<Integer>();
		try {

			FileReader fr = new FileReader(initialResults);
			BufferedReader br = new BufferedReader(fr);

			while (((line = br.readLine()) != null) && (topK <= 10)) {
				
				topK++;
				String[] result = line.split(" ");

				if ((Integer.parseInt(result[0])) == queryNumber) {
					String documentName = result[2];
					documentName = documentName.replaceAll("CACM-", "");
					int documentNumber = Integer.parseInt(documentName);

					documentList.add(documentNumber);
				}

			}

			br.close();
			fr.close();
		}

		catch (IOException e) {

			e.printStackTrace();
		}

		return documentList;
	}

	public static void writeFinalQuery(String finalQuery) {

		try {

			FileWriter fw = new FileWriter("ExpandedQueries.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(finalQuery + "\r\n");
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
