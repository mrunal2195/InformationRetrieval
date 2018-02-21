
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

public class Create_Corpus {

	public static File File_path = new File("data");
	public static String stopWordsFilePath = "common_words.txt";

	static File[] listOfFiles = File_path.listFiles();

	static List<String> stopWords = new ArrayList<String>();

	static String current_URL;

	static Scanner sc = new Scanner(System.in);

	public static void main(String args[]) {

		while (true) {
			System.out.print("Please select the required options\n");
			System.out.print("1.Stopping\n2.Basic Transformation\n3.Exit\n>");

			switch (sc.nextInt()) {
			case 1:
				clean_data(true, true, true);
				break;

			case 2:
				clean_data(false, true, true);
				break;

			case 3:
				System.out.println("Ending the process");
				return;
			default:
				System.out.println("Please enter either 1, 2, 3 or 4");
				break;
			}

		}

	}

	static void clean_data(boolean stopping, boolean lower_case, boolean punctuation) {

		String filename;

		try {

			/*
			 * the file will be read line by line and each line contains single
			 * url whose web page is to parsed to create the corpus
			 */

			for (int i = 0; i < listOfFiles.length; i++) {

				if (listOfFiles[i].isFile()) {

					filename = listOfFiles[i].getName().replaceAll(".html", "");

					filename = filename.replaceAll("CACM-", "");
					int fileId = Integer.parseInt(filename);

					FileWriter fw;
					BufferedWriter bw;

					if (stopping) {

						fw = new FileWriter("cleaned_data_stopped\\" + fileId + ".txt", false);
						bw = new BufferedWriter(fw);
					} else {

						fw = new FileWriter("cleaned_data\\" + fileId + ".txt", false);
						bw = new BufferedWriter(fw);

					}

					Document doc = Jsoup.parse(listOfFiles[i], "UTF-8", "");
					// remove all urls
					doc.select("a[href]").remove();

					// remove all references to image
					doc.select("img[src$=.png]").remove();

					/*
					 * remove annotation as they are not removed during cleaning
					 * process using Whitelist Hence they are removed so that
					 * their contents do not appear in the corpus
					 */
					doc.select("annotation").remove();

					/*
					 * remove mathematical formulas. All Wikipidea pages have
					 * mathematical formula in the class named mwe-math-element
					 */

					doc.select("span[class$=mwe-math-element]").remove();

					// extracting body of the web page
					String body = doc.body().toString();

					/*
					 * &nbps is handled separately because they are not handled
					 * by jsoup jsoup handles the removal of only html tags not
					 * any other tags
					 */
					body = body.replaceAll("&nbsp;", "");

					// Whitelist.none() removes all html tags from the given
					// string
					String corpus = Jsoup.clean(body, Whitelist.none());

					if (lower_case) {
						corpus = corpus.toLowerCase();
					}

					if (punctuation) {
						final String regex = "[^a-zA-Z0-9 .,-]|(?<!\\d)[.,]|[.,](?!\\d)";
						corpus = corpus.replaceAll(regex, " ");
					}

					if (stopping) {

						getStopWords();

						String[] documentTerms = corpus.split(" ");

						for (int j = 0; j < documentTerms.length; j++) {

							if (stopWords.contains(documentTerms[j])) {

								corpus = corpus.replaceAll("(?<!\\S)" + documentTerms[j] + "(?!\\S)", "");

							}

						}
					}

					bw.write(corpus);
					bw.close();
					fw.close();

				}
			}

			System.out.println("Done");

		}

		catch (

		IOException e) {
			e.printStackTrace();
		}

	}

	public static void getStopWords() {

		BufferedReader br = null;
		FileReader fr = null;

		String stopWord;
		try {

			fr = new FileReader(stopWordsFilePath);
			br = new BufferedReader(fr);

			while ((stopWord = br.readLine()) != null) {

				stopWords.add(stopWord);
			}

			fr.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
