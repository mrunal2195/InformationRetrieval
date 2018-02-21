
public class Termfreq implements TermFreqInterface {
	private String docId;
	private double normalizedTF;

	public Termfreq() {
		docId = null;
		normalizedTF = 0.0;
	}

	public Termfreq(String docId) {
		this.docId = docId;
		this.normalizedTF = 0.0;
	}

	public void settfidf(double idf) {

		this.normalizedTF = this.normalizedTF * idf;
	}

	public Termfreq(String docId, double term_freq, double totalCount) {
		this.docId = docId;
		this.normalizedTF = (double) (term_freq / totalCount);
	}

	public String getDocId() {
		return docId;
	}

	public void getDocId(String docId) {
		this.docId = docId;
	}

	public double normalizedTermFreq() {
		return normalizedTF;
	}

	public void setTermFreq(int termFreq, int totalCount) {
		this.normalizedTF = termFreq / totalCount;
	}

	public String toString() {
		return "[" + docId + "," + normalizedTF + "]";
	}

	@Override
	public void Termfreq(String docId, double term_freq, double totalCount) {
		// TODO Auto-generated method stub

	}

}
