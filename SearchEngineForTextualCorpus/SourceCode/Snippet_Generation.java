

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * To create Apache Lucene index in a folder and add files into this index based
 * on the input of the user.
 */
public class Snippet_Generation {
    private static Analyzer sAnalyzer=new StandardAnalyzer(Version.LUCENE_47);
  //  private static Analyzer s1Analyzer = new SimpleAnalyzer(Version.LUCENE_47);
    private static String Query_File="queries.txt";

    private IndexWriter writer;
    private ArrayList<File> queue = new ArrayList<File>();

    public static void main(String[] args) throws IOException{
	System.out.println("Enter the FULL path where the index will be created: (e.g. /Usr/index or c:\\temp\\index)");

	String indexLocation = null;
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	String s = br.readLine();

	Snippet_Generation indexer = null;
	try {
	    indexLocation = s;
	    indexer = new Snippet_Generation(s);
	} catch (Exception ex) {
	    System.out.println("Cannot create index..." + ex.getMessage());
	    System.exit(-1);
	}

	// ===================================================
	// read input from user until he enters q for quit
	// ===================================================
	while (!s.equalsIgnoreCase("q")) {
	    try {
		System.out
			.println("Enter the FULL path to add into the index (q=quit): (e.g. /home/mydir/docs or c:\\Users\\mydir\\docs)");
		System.out
			.println("[Acceptable file types: .xml, .html, .html, .txt]");
		s = br.readLine();
		if (s.equalsIgnoreCase("q")) {
		    break;
		}

		// try to add file into the index
		indexer.indexFileOrDirectory(s);
	    } catch (Exception e) {
		System.out.println("Error indexing " + s + " : "+ e.getMessage());
	    }
	}

	// ===================================================
	// after adding, we always have to call the
	// closeIndex, otherwise the index is not created
	// ===================================================
	indexer.closeIndex();

	// =========================================================
	// Now search
	// =========================================================
	

	s = "";
	
	FileWriter fw=new FileWriter("SnippetGenerationOutput.txt");
	FileReader fr=new FileReader(Query_File);
	BufferedReader br1=new BufferedReader(fr);
	int query_id=0;
	
	while((s=br1.readLine()) != null) {
		query_id++;
		
		try
		{
			IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexLocation)));
			IndexSearcher searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(100, true);
			
			System.out.println("\nAverage Document length "+(double)reader.getSumTotalTermFreq("contents")/84679);
			System.out.println("\nVocab size "+(double)reader.getSumTotalTermFreq("contents"));
			
			Query q = new QueryParser(Version.LUCENE_47, "contents",sAnalyzer).parse(s);
			searcher.search(q, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			
			SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter();
	        Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(q));
	        
			//4.display snippets
			
			System.out.println("Found " + hits.length + " hits.");
			fw.write("\n*************Query "+query_id+"*************************\n");
			
			for (int i = 0; i < hits.length; ++i) {
			    
				int docId = hits[i].doc;
			    Document d = searcher.doc(docId);
			    System.out.println((i+1)+" . "+d.get("filename")+" score=" +hits[i].score);
			    fw.write("\n"+query_id+" Q0 "+getDocId(d.get("path"))+" "+(i+1)+" "+hits[i].score+" Lucene_47\r\n");
			    String[] fragment=highlighter.getBestFragments(sAnalyzer, "ncontent",d.get("ncontent"),4);
			    
			    for(int j=0;j<fragment.length;j++) {
			    	fw.write(fragment[j]);
			    }
			    
			    System.out.println("\n");
			}			
	}
	
		catch (Exception e) {
			System.out.println("Error searching " + s + " : "
				+ e.getMessage());
			break;
		    }
	}
		fw.close();
		fr.close();
		br1.close();	
		}



    /**
     * Constructor
     * 
     * @param indexDir
     *            the name of the folder in which the index should be created
     * @throws java.io.IOException
     *             when exception creating index.
     */
    public Snippet_Generation(String indexDir) throws IOException {

	FSDirectory dir = FSDirectory.open(new File(indexDir));

	IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, sAnalyzer);

	writer = new IndexWriter(dir, config);
    }

    /**
     * Indexes a file or directory
     * 
     * @param fileName
     *            the name of a text file or a folder we wish to add to the
     *            index
     * @throws java.io.IOException
     *             when exception
     */
    public void indexFileOrDirectory(String fileName) throws IOException {
	// ===================================================
	// gets the list of files in a folder (if user has submitted
	// the name of a folder) or gets a single file name (is user
	// has submitted only the file name)
	// ===================================================
	addFiles(new File(fileName));

	int originalNumDocs = writer.numDocs();
	for (File f : queue) {
	    FileReader fr = null;
	    try {
		Document doc = new Document();

		// ===================================================
		// add contents of file
		// ===================================================
		fr = new FileReader(f);
		FieldType type=new FieldType();
		type.setIndexed(true);
		type.setIndexOptions(FieldInfo.IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
		type.setStored(true);
		type.setStoreTermVectors(true);
		type.setTokenized(true);
		type.setStoreTermVectorOffsets(true);
		
		Field field=new Field("ncontent",new Scanner(f).useDelimiter("\\A").next(),type);
		
		doc.add(field);
		doc.add(new TextField("contents", fr));
		doc.add(new StringField("path", f.getPath(), Field.Store.YES));
		doc.add(new StringField("filename", f.getName(),Field.Store.YES));

		writer.addDocument(doc);
		System.out.println("Added: " + f);
	    } catch (Exception e) {
		System.out.println("Could not add: " + f);
	    } finally {
		fr.close();
	    }
	}

	int newNumDocs = writer.numDocs();
	System.out.println("");
	System.out.println("************************");
	System.out.println((newNumDocs - originalNumDocs) + " documents added.");
	System.out.println("************************");

	queue.clear();
    }

    private void addFiles(File file) {

	if (!file.exists()) {
	    System.out.println(file + " does not exist.");
	}
	if (file.isDirectory()) {
	    for (File f : file.listFiles()) {
		addFiles(f);
	    }
	} else {
	    String filename = file.getName().toLowerCase();
	    // ===================================================
	    // Only index text files
	    // ===================================================
	    if (filename.endsWith(".htm") || filename.endsWith(".html")
		    || filename.endsWith(".xml") || filename.endsWith(".txt")) {
		queue.add(file);
	    } else {
		System.out.println("Skipped " + filename);
	    }
	}
  }
    
    /**
     * Close the index.
     * 
     * @throws java.io.IOException
     *             when exception closing
     */
    public void closeIndex() throws IOException {
	writer.close();
    }
    
    public static String getDocId(String path)
    {
    	path=path.substring(path.lastIndexOf("\\")+1,path.lastIndexOf("."));
    	return path;
    }
    }

