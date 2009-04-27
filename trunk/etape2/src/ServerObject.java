import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;


public class ServerObject implements Serializable{

	private static final long serialVersionUID = -7396577889952667013L;
	
	// liste des clients
	public ArrayList<Client_itf> listeDesLecteurs;
	public ArrayList<Client_itf> getListe() {
		return this.listeDesLecteurs;
	}
	
	private int ID;
	public void setID(int nouvelID) {
		this.ID = nouvelID;
	}
	public int getID() {
		return this.ID;
	}
	
	private Client_itf client_ecrivain;
	
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
	public ServerObject (int id, Object o){
		this.ID = id;
		this.listeDesLecteurs = new ArrayList<Client_itf>();
		this.client_ecrivain = null;
		this.o = o;
	}
	
	public synchronized Object lock_read(Client_itf client) throws RemoteException {

		// ON TE DONNE LE DROIT DE LECTURE
		this.listeDesLecteurs.add(client);
		
		if (this.client_ecrivain!=null) {
			// on invalide l'ecrivain
			this.o = client_ecrivain.reduce_lock(this.ID);
			this.listeDesLecteurs.add(client_ecrivain);
			this.client_ecrivain=null;
		}

		
		
		// va renvoyer un truc de ce genre
		System.out.println("fin de lock_read : " );
		return this.getObject();
		
	}
	
	public synchronized Object lock_write(Client_itf client) throws RemoteException {
		
		System.out.println("taille liste lecteur" + this.listeDesLecteurs.size());
		this.listeDesLecteurs.remove(client);
		
		Iterator<Client_itf> it = this.listeDesLecteurs.iterator() ;
		while ( it.hasNext() ) {
			it.next().invalidate_reader(this.ID);
		}
		//for (int i=0 ; i<( this.listeDesLecteurs.size() ) ; i++) {
		//	this.listeDesLecteurs.get(i).invalidate_reader(this.ID);
		//}
		
		
		this.listeDesLecteurs.clear();
		
		if (this.client_ecrivain!=null) {
			// on invalide l'ecrivain
			this.o = client_ecrivain.invalidate_writer(this.ID );
		}
		
		//  ON TE DONNE LE DROIT D'ECRITURE
		this.client_ecrivain=client;
		
		// va renvoyer un truc de ce genre
		System.out.println("fin de lock_write : ");
		return this.getObject();
	}
	
}