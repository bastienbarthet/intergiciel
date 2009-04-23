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
//	private String name;
//	public String getName() {
//		return this.name;
//	}
//	public void setName(String newName) {
//		this.name = newName;
//	}
	
	
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
		synchronized (this) {
			switch (this.lock) {
			// si RLC, direct RLT
			case RLC :	this.lock = RLT; break; 
			case WLC : 	this.lock = RLT_WLC; break;
			case NL : try {
						this.o = Client.lock_read(this.getId());
						this.lock=RLT;
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					break;
			default : System.err.println("cas pas possible de verrou ds lock_read"); break;
			}
		}
		System.out.println( "fin du lock_read : " +this.lock);
	}

	// invoked by the user program on the client node
	public void lock_write() {

		synchronized (this) {
			switch (this.lock) {
			case WLC : this.lock = WLT; break;
			default : try {
					this.o = Client.lock_write(this.getId());
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} this.lock = WLT; break;
			}
		}
		System.out.println( "fin du lock_write : " +this.lock);
	}

	// invoked by the user program on the client node
	public synchronized void unlock() {
		switch (this.lock) {
			case RLT : this.lock = RLC; break;
			case WLT : this.lock = WLC; break;
			case RLT_WLC : this.lock = WLC; break;
			default : break;
		}
		notify();
		System.out.println( "fin du unlock : " +this.lock);
	}


	// callback invoked remotely by the server
	public synchronized Object reduce_lock() throws InterruptedException {
		// permet au serveur de réclamer le passage d'un verrou de l'écriture a la lecture
		System.out.println("c : " + this.lock);
		
		while(this.lock!=RLT_WLC && this.lock!=WLC) {
			try {
				wait();
				System.out.println("c2 : " + this.lock);
			} catch (InterruptedException e) {}
		}
		
		System.out.println("c3 : " + this.lock);
		
		switch (this.lock) {
			
			case WLC : this.lock = RLC; break;
			case RLT_WLC : wait(); this.lock = RLT; break;
			default : break;
		}
		System.out.println( "fin du reduce_lock : " +this.lock);
		return this.getObject();
	}

	
	// callback invoked remotely by the server
	public synchronized void invalidate_reader() throws InterruptedException {
		while(this.lock!=RLC && this.lock!=WLT){
			try {
				System.out.println("e");
				wait();
			} catch (InterruptedException e) {}
		}
		
		switch (this.lock) {
			case RLC :  this.lock = NL; break;
			default : break;
		}
		System.out.println( "fin du invalidate_reader : " +this.lock);
	}

	public synchronized Object invalidate_writer() throws InterruptedException {

		while(this.lock!=WLC) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		
		this.lock = NL;
		
		System.out.println( "fin du invalidate_writer : " +this.lock);
		return this.getObject();	
	}
	
}
