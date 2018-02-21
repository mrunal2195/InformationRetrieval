
public interface TermFreqInterface {

	void settfidf(double idf);

	void Termfreq(String docId, double term_freq, double totalCount);

	String getDocId();

	void getDocId(String docId);

	double normalizedTermFreq();

	void setTermFreq(int termFreq, int totalCount);

	String toString();

}
