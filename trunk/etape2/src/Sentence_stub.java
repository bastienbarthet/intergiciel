public class Sentence_stub extends SharedObject implements Sentence_itf, java.io.Serializable {
	
	public void write(String text) {
		Sentence s = (Sentence)o;
		s.write(text);
	}
	public String read() {
		Sentence s = (Sentence)o;
		return s.read();	
	}
	
}