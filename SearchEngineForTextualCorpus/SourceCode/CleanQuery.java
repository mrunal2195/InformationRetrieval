
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

public class CleanQuery {

	private static File fileName = new File("cacm.query.txt");

	public static void main(String args[]) {

		String html = "";
		String queries = "";
		String term;

		try {

			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);

			while ((term = br.readLine()) != null) {

				html += term + " ";
			}

			Document doc = Jsoup.parse(html, "", Parser.xmlParser());
			doc.select("docno").remove();

			for (Element e : doc.getElementsByTag("doc")) {

				queries += e;

			}

			queries = queries.replaceAll("<DOC>", "");
			String[] query = queries.split("</DOC>");

			FileWriter fw = new FileWriter("queries.txt", false);
			BufferedWriter bw = new BufferedWriter(fw);

			for (String q : query) {

				q = q.toLowerCase();
				String regex = "[^a-zA-Z0-9 .,-]|(?<!\\d)[.,]|[.,](?!\\d)";
				q = q.replaceAll(regex, " ");
				q = q.replaceAll("(?s).*?(\\p{L}+(?:-\n?\\p{L}+)*)|.+", "$1 ");

				bw.write(q + "\r\n");
			}

			bw.close();

			System.out.println("Done Cleaning");

		}

		catch (IOException e) {

			e.printStackTrace();
		}

	}

}
