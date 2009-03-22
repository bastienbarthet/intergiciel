import java.io.Serializable;


public class ServerObject implements Serializable{

	// defition des etats d'un shared object
	public static final int NL = 0;				// no local read
	public static final int RLC = 1;			// real lock caches (not taken)
	public static final int RTL = 3;			// read lock taken
	public static final int WLT = 4;			// write lock taken
	public static final int RTL_WLC = 5;		// read lock taken and write lock cached
		
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
