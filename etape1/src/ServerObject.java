import java.io.Serializable;
import java.util.List;


public class ServerObject implements Serializable{

	// liste des clients
	private List<Integer> listeDesClient;
	
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
	
	// identifiant de l'objet
	private int id;
	public int getId() {
		return this.id;
	}
	
	// constructeur
	public ServerObject (int id, Object o){
		this.id = id;
		this.ecrivain = 0;
		this.o = o;
	}
	
}
