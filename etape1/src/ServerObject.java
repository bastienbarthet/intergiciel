import java.io.Serializable;
import java.util.List;


public class ServerObject implements Serializable{

	// liste des clients
	private List<Integer> listeDesLecteurs;
	
	// mode de verouillage du server object
	private int ecrivain;
	public int getEcrivain() {
		return this.ecrivain;
	}
	public void setEcrivain(int i) {
		this.ecrivain = i;
	}
	
	// objet sur lequel pointe le server object
	private Object o;
	public Object getObject() {
		return this.o;
	}
	
	// nom de l'objet
	private String name;
	public String getName() {
		return this.name;
	}
	
	// constructeur
	public ServerObject (String _name, Object o){
		this.name = _name;
		this.ecrivain = 0;
		this.o = o;
	}
	
}
