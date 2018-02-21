
public class DocInfo {
	private String docId;
	private int term_freq;
	
	public DocInfo()
	{
		docId = null;
		term_freq = 0;
	}
	
	public DocInfo(String docId)
	{
		this.docId = docId;
		this.term_freq = 0;
	}
	
	public DocInfo(String docId, int term_freq)
	{
		this.docId = docId;
		this.term_freq = term_freq;
	}
	
	public String getDocId()
	{
		return docId;
	}
	
	public void getDocId(String docId)
	{
		this.docId = docId;
	}
	
	public int getTermFreq()
	{
		return term_freq;
	}
	
	public void setTermFreq(int termFreq)
	{
		this.term_freq = termFreq;
	}
	
	public void incrementTermFreq()
	{
		this.term_freq++;
	}
	
	public String toString()
	{
		return "["+ docId + "," + term_freq + "]";
	}
	}
	
	
	


