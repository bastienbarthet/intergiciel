public class Sentence implements java.io.Serializable {

	private static final long serialVersionUID = -3727587107783900556L;
	
	String 		data;
	public Sentence() {
		data = new String("phrase");
	}
	
	public void write(String text) {
		data = text;
	}
	public String read() {
		return data;	
	}
	
}