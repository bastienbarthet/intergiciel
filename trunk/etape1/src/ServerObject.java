import java.io.Serializable;
import java.util.List;


public class ServerObject implements Serializable{

	private static final long serialVersionUID = -7396577889952667013L;
	
	// liste des clients
	private List<Integer> listeDesLecteurs;
	public List<Integer> getListe() {
		return this.listeDesLecteurs;
	}
	
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
	public ServerObject (Object o){
		this.ecrivain = 0;
		this.o = o;
	}
	
}
