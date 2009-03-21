import java.io.*;

public class SharedObject implements Serializable, SharedObject_itf {
	
	// defition des etats d'un shared object
	public static final int NL = 0;				// no local read
	public static final int RLC = 1;			// real lock caches (not taken)
	public static final int RTL = 3;			// read lock taken
	public static final int WLT = 4;			// write lock taken
	public static final int RTL_WLC = 5;		// read lock taken and write lock cached
		
	// variable d'etat du shared object
	private int lock;
	public int getLock() {
		return this.lock;
	}
	
	// objet sur lequel pointe le shared object
	private Object o;
	public Object getObject() {
		return this.o;
	}
	
	// idantifiant de l'objet
	private int id;
	public int getId() {
		return this.id;
	}
	
	// nom du sharedObject
	private String name;
	public String getName(){
		return this.name;
	}
	
	// constructeur
	public SharedObject (int id, String name, Object o){
		this.id = id;
		this.name = name;
		this.lock = NL;
		this.o = o;
	}
	
	// invoked by the user program on the client node
	public void lock_read() {
	}

	// invoked by the user program on the client node
	public void lock_write() {
	}

	// invoked by the user program on the client node
	public synchronized void unlock() {
	}


	// callback invoked remotely by the server
	public synchronized Object reduce_lock() {
	}

	// callback invoked remotely by the server
	public synchronized void invalidate_reader() {
	}

	public synchronized Object invalidate_writer() {
	}
}
