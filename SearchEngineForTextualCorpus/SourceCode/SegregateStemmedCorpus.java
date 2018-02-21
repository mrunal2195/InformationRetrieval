
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SegregateStemmedCorpus {

	private static String File_path = "cacm_stem.txt";

	public static void main(String args[]) {

		String line = "";
		String corpus = "";
		int docId = 1;

		try {
			FileReader fr = new FileReader(File_path);
			BufferedReader br = new BufferedReader(fr);

			while ((line = br.readLine()) != null) {

				if (line.startsWith("#")) {

					if (!(corpus.equalsIgnoreCase(""))) {

						writeInFile(line, corpus, docId);

						String[] documentId = line.split("#");
						documentId = documentId[1].split(" ");

						System.out.println(documentId[1]);
						docId = Integer.parseInt(documentId[1]);

						line = "";
						corpus = "";
					}

				}

				else {

					corpus += line;
				}
			}

			writeInFile(line, corpus, docId);

			br.close();
			fr.close();

		} catch (

		IOException e) {
			e.printStackTrace();
		}

	}

	public static void writeInFile(String line, String corpus, int docId) {

		try {

			FileWriter fw = new FileWriter("cleaned_stemmed_data\\" + docId + ".txt", false);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(corpus);
			bw.close();
			fw.close();

		}

		catch (IOException e) {

		}

	}

}
