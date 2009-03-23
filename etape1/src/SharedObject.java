import java.io.*;

public class SharedObject implements Serializable, SharedObject_itf {
	
	// defition des etats d'un shared object
	public static final int NL = 0;				// no local read
	public static final int RLC = 1;			// real lock caches (not taken)
	public static final int WLC = 2;			// write lock cached
	public static final int RLT = 3;			// read lock taken
	public static final int WLT = 4;			// write lock taken
	public static final int RLT_WLC = 5;		// read lock taken and write lock cached
		
	// variable d'etat du shared object
	private int lock;
	public int getLock() {
		return this.lock;
	}
	public void setLock(int newLock) {
		this.lock = newLock;
	}
	
	// objet sur lequel pointe le shared object
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
	public SharedObject (int id, Object o){
		this.id = id;
		this.lock = NL;
		this.o = o;
	}
	
	// invoked by the user program on the client node
	public void lock_read() {
		// si RLC, direct RLT
		if (this.getLock()==RLC) {
			this.setLock(RLT);
		}
		else {
			// ici il faut appeler lock_read du client, pour que celui ci demande le lock_read au serveur			
		}

	}

	// invoked by the user program on the client node
	public void lock_write() {
		// si RLC, direct RLT
		if (this.getLock()==WLC) {
			this.setLock(WLT);
		}
		else {
			// ici il faut appeler lock_write du client, pour que celui ci demande le lock_read au serveur et obtienne si necessaire une nouvelle copie			
		}

	}

	// invoked by the user program on the client node
	public synchronized void unlock() {
		switch (this.getLock()) {
		case RLT : this.setLock(RLC); break;
		case WLT : this.setLock(WLC); break;
		case RLT_WLC : this.setLock(WLC); break;
		default : break;
		}
	}


	// callback invoked remotely by the server
	public synchronized Object reduce_lock() {
		// permet au serveur de réclamer le passage d'un verrou de l'écriture a la lecture
		switch (this.getLock()) {
		
		case NL : break;
		case RLC :  break;
		case WLC : this.setLock(RLC); break;
		// probleme sur ces 2 ci dessous!! Il faut waiter que l'appli ai fini pour passer en lecteur...
			// Solution pour WLT : le serveur sait si ce shared object écrit, et va pa lui dire merde
			// Solution pour RLT : il faut waiter...
		case RLT : this.setLock(RLC); break;
		case WLT : this.setLock(WLC); break;
			// case RLT_WLC : il faut waiter aussi
		case RLT_WLC : this.setLock(WLC); break;
		default : break;
		}
	return null; // On est sencer renvoyé qq chose?? moi je crois pas..
	}

	// callback invoked remotely by the server
	public synchronized void invalidate_reader() {
		// 2 cas : RLT ou RLC
		switch (this.getLock()) {
		
		//case RLT : il faut waiter
		case RLT : break;
		//cas RLC, on invalide
		case RLC :  this.setLock(NL); break;
		case RLT_WLC : break; // il faut waiter...
		case WLC :  this.setLock(NL); break;
		// case WLT : pas possible, le serveur va pas invalider l'écrivain en cours
		default : break;
		}
		
	}

	public synchronized Object invalidate_writer() {
	switch (this.getLock()) {
		
		//Le serveur ne peut invalider que des WLC selon moi..
		case WLC :  this.setLock(NL); break;
		default : break;
	}
		return null;
}
	
}
