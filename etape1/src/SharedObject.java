import java.io.*;
import java.rmi.RemoteException;
	/**
	 * 
	 * @author Bastiens Barthet&Lehmann
	 *	Classe SharedObject, qui stock la donnée transmise.
	 */
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

	/**
	 * 
	 * @return lock, l'etat courant du verrou
	 */
	public int getLock() {
		return this.lock;
	}
	
	
	/**
	 * 
	 * @param newLock le nouveau lock
	 */
	public void setLock(int newLock) {
		this.lock = newLock;
	}


	/**
	 * L'object stocké
	 */
	public Object obj;

	
	/**
	 * L'ID de l'objet partagé
	 */
	private int id;
	
	/**
	 * 
	 * @return l'ID
	 */
	public int getId() {
		return this.id;
	}

	/**Constructeur
	 * 
	 * @param id l'ID de l'objet
	 * @param _o L'object
	 */
	public SharedObject (int id, Object _o){
		this.id = id;
		this.lock = NL;
		this.obj = _o;
	}


	/**
	 * Demande (et obtient) les droits en lecture
	 */
	public void lock_read() {
		synchronized (this) {
			switch (this.lock) {
			case RLC :	this.lock = RLT; break; 
			case WLC : 	this.lock = RLT_WLC; break;
			case NL : try {
				this.obj = Client.lock_read(this.getId());
				this.lock=RLT;
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			break;
			default : System.err.println("cas pas possible de verrou ds lock_read"); break;
			}
		}
		//System.out.println( "fin du lock_read : " +this.lock);
	}

	/**
	 * Demande (et obtient) les droits en ecriture
	 */
	public void lock_write() {
		boolean maj = false;
		synchronized (this) {
			switch (this.lock) {
			case NL : maj = true; this.lock = WLT; break;
			case RLC : maj = true; this.lock = WLT; break;
			case WLC : this.lock = WLT; break;
			default : break;
			}
		}
		if (maj){
			try {
				this.obj = Client.lock_write(this.getId());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		//System.out.println( "fin du lock_write : " +this.lock);
	}

	/**
	 * Methode de relache du verrou
	 */
	public synchronized void unlock() {
		switch (this.lock) {
		case RLT : this.lock = RLC; break;
		case WLT : this.lock = WLC; break;
		case RLT_WLC : this.lock = WLC; break;
		default : break;
		}
		notify();
		//	System.out.println( "fin du unlock : " +this.lock);
	}


	/**
	 * Reduit les droits de l'écrivain en simple lecteur
	 * @return la derniere version de l'object
	 * @throws InterruptedException
	 */
	public synchronized Object reduce_lock() throws InterruptedException {
		// permet au serveur de rÃ©clamer le passage d'un verrou de l'Ã©criture a la lecture
		while(this.lock!=RLT_WLC && this.lock!=WLC) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}

		switch (this.lock) {
		case WLC : this.lock = RLC; break;
		case RLT_WLC : this.lock = RLT; break;
		default : break;
		}
		//System.out.println( "fin du reduce_lock : " +this.lock);
		return this.obj;
	}


	/**	Invalide les droits d'écriture
	 * 
	 * @throws InterruptedException
	 */
	public synchronized void invalidate_reader() throws InterruptedException {
		while(this.lock!=WLT && this.lock!=RLC){
			try {
				wait();
			} catch (InterruptedException e) {}
		}

		switch (this.lock) {
		case RLC :  this.lock = NL; break;
		default : break;
		}
		//System.out.println( "fin du invalidate_reader : " +this.lock);
	}

	/**
	 * Invalide les droits en écriture
	 * @return la derniere version de l'objet
	 * @throws InterruptedException
	 */
	public synchronized Object invalidate_writer() throws InterruptedException {

		while(this.lock!=WLC) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}

		this.lock = NL;

		//System.out.println( "fin du invalidate_writer : " +this.lock);
		return this.obj;	
	}

}
