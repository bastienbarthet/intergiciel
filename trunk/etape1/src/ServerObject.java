import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 * @author Bastiens Barthet&Lehmann
 * Classe qui stock l'objet, les lecteurs et l'écrivain, coté serveur
 *
 */
public class ServerObject implements Serializable{

	private static final long serialVersionUID = -7396577889952667013L;

	/**
	 *  liste des clients
	 */
	public ArrayList<Client_itf> listeDesLecteurs;

	/**
	 * 
	 * @return la liste des clients
	 */
	public ArrayList<Client_itf> getListe() {
		return this.listeDesLecteurs;
	}

	private int ID;

	/**
	 * 
	 * @param nouvelID
	 */
	public void setID(int nouvelID) {
		this.ID = nouvelID;
	}

	/**
	 * 
	 * @return L'ID
	 */
	public int getID() {
		return this.ID;
	}

	private Client_itf client_ecrivain;

	private Object o;


	/**
	 * 
	 * @return l'objet
	 */
	public Object getObject() {
		return this.o;
	}

	private String name;

	/**
	 * 
	 * @return le nom de l'objet
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 
	 * @param id l'ID
	 * @param o l'objet
	 */
	public ServerObject (int id, Object o){
		this.ID = id;
		this.listeDesLecteurs = new ArrayList<Client_itf>();
		this.client_ecrivain = null;
		this.o = o;
	}

	/**
	 * accede à la requete d'un client sur les droits de lecture
	 * @param client demandeur
	 * @return l'objet
	 * @throws RemoteException
	 */
	public synchronized Object lock_read(Client_itf client) throws RemoteException {

		this.listeDesLecteurs.add(client);

		if (this.client_ecrivain!=null) {
			this.o = client_ecrivain.reduce_lock(this.ID);
			this.listeDesLecteurs.add(client_ecrivain);
			this.client_ecrivain=null;
		}
		//System.out.println("fin de lock_read : " );
		return this.getObject();

	}

	/**
	 * accede à la requete d'un client sur les droits d'écriture
	 * @param client demandeur
	 * @return l'objet
	 * @throws RemoteException
	 */
	public synchronized Object lock_write(Client_itf client) throws RemoteException {

		this.listeDesLecteurs.remove(client);

		Iterator<Client_itf> it = this.listeDesLecteurs.iterator() ;
		while ( it.hasNext() ) {
			it.next().invalidate_reader(this.ID);
		}

		this.listeDesLecteurs.clear();

		if (this.client_ecrivain!=null) {
			this.o = client_ecrivain.invalidate_writer(this.ID );
		}

		this.client_ecrivain=client;

		//	System.out.println("fin de lock_write : ");
		return this.getObject();
	}

}
