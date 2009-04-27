public class Sentence_stub extends SharedObject implements Sentence_itf, java.io.Serializable {
	
	public Sentence_stub(int id, Object _o) {
		super(id, _o);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 3173827029004946005L;
	
	public void write(String text) {
		Sentence s = (Sentence)o;
		s.write(text);
	}
	public String read() {
		Sentence s = (Sentence)o;
		return s.read();	
	}
	
}