import java.io.Serializable;


public class ServerObject implements Serializable{

	private List<int> listeDesClient;
	
	// variable d'etat du server object
	private int lock;
	public int getLock() {
		return this.lock;
	}
	
	// objet sur lequel pointe le server object
	private Object o;
	public Object getObject() {
		return this.o;
	}
	
	// idantifiant de l'objet
	private int id;
	public int getId() {
		return this.id;
	}
	
	// nom du serverObject
	private String name;
	public String getName(){
		return this.name;
	}
	
	// constructeur
	public ServerObject (int id, String name, Object o){
		this.id = id;
		this.name = name;
		this.lock = NL;
		this.o = o;
	}
	
}
