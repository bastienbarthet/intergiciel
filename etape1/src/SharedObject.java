import java.io.*;
import java.rmi.RemoteException;

public class SharedObject implements Serializable, SharedObject_itf {
	
	private static final long serialVersionUID = -299256425145734063L;
	
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

	// variable de nom du shared object
	private String name;
	public String getName() {
		return this.name;
	}
	public void setName(String newName) {
		this.name = newName;
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
	public SharedObject (int id, Object _o){
		this.id = id;
		this.lock = NL;
		this.o = _o;
	}
	
	// invoked by the user program on the client node
	public void lock_read() {
		// si RLC, direct RLT
		if ( (this.lock == RLC)||(this.lock == WLC)||(this.lock == RLT_WLC) ) {
			this.lock = RLT;
		}
		else {
			try {
				// ici il faut appeler lock_read du client, pour que celui ci demande le lock_read au serveur
				Object obj = Client.lock_read(this.getId());
				this.o = obj;
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			this.lock = RLT;
		}

	}

	// invoked by the user program on the client node
	public void lock_write() {
		// si WLC, direct WLT
		if ((this.lock == WLC)||(this.lock == RLT_WLC)) {
			this.lock = WLT;
		}
		else {
			try {
				// ici il faut appeler lock_read du client, pour que celui ci demande le lock_read au serveur
				Object obj = Client.lock_write(this.getId());
				this.o = obj;
				this.lock = WLT;
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
		}
	}

	// invoked by the user program on the client node
	public synchronized void unlock() {
		switch (this.lock) {
			case RLT : this.lock = RLC; break;
			case WLT : this.lock = WLC; break;
			case RLT_WLC : this.lock = WLC; break;
			default : break;
		}
	}


	// callback invoked remotely by the server
	public synchronized Object reduce_lock() {
		// permet au serveur de r�clamer le passage d'un verrou de l'�criture a la lecture
		switch (this.lock) {
			
			case NL : break;
			case RLC :  break;
			case WLC : this.lock = RLC; break;
			// probleme sur ces 2 ci dessous!! Il faut waiter que l'appli ai fini pour passer en lecteur...
				// Solution pour WLT : le serveur sait si ce shared object �crit, et va pa lui dire merde
				// Solution pour RLT : il faut waiter...
			case RLT : this.lock = RLC; break;
			case WLT : this.lock = WLC; break;
				// case RLT_WLC : il faut waiter aussi
			case RLT_WLC : this.lock = WLC; break;
			default : break;
		}
	return null; // On est sencer renvoy� qq chose?? moi je crois pas..
	}

	// callback invoked remotely by the server
	public synchronized void invalidate_reader() {
		// 2 cas : RLT ou RLC
		switch (this.lock) {
		
			//case RLT : il faut waiter
			case RLT : break;
			//cas RLC, on invalide
			case RLC :  this.lock = NL; break;
			case RLT_WLC : break; // il faut waiter...
			case WLC :  this.lock = NL; break;
			// case WLT : pas possible, le serveur va pas invalider l'�crivain en cours
			default : break;
		}
		
	}

	public synchronized Object invalidate_writer() {
		switch (this.lock) {
			//Le serveur ne peut invalider que des WLC selon moi..
			case WLC :  this.lock = NL; break;
			default : break;
		}
		return null;
	}
	
}
