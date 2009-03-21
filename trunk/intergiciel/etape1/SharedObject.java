import java.io.*;

public class SharedObject implements Serializable, SharedObject_itf {
	
	// defition des etats d'un shared object
	public static final String NL = 0;			// no local read
	public static final String RLC = 1;			// real lock caches (not taken)
	public static final String WLC = 2;			// write lock cached
	public static final String RTL = 3;			// read lock taken
	public static final String WLT = 4;			// write lock taken
	public static final String RTL_WLC = 5;		// read lock taken and write lock cached
		
	// variable d'etat du shared object
	private int lock;
	
	// objet sur lequel pointe le shared object
	private Object o;
	
	// idantifiant de l'objet
	private int id;
	
	// constructeur
	SharedObject (int id){
		this.id = id;
		this.lock = NL;
		this.o = null;
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
