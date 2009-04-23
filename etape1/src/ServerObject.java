import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;


public class ServerObject implements Serializable{

	private static final long serialVersionUID = -7396577889952667013L;
	
	// liste des clients
	public ArrayList<Integer> listeDesLecteurs;
	public ArrayList<Integer> getListe() {
		return this.listeDesLecteurs;
	}
	
	private Client_itf client_ecrivain;
	// mode de verouillage du server object
	private int ecrivain;
	public int getEcrivain() {
		return this.ecrivain;
	}
	public void setEcrivain(int i) {
		this.ecrivain = i;
	}
	
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
	public ServerObject (Object o){
		this.listeDesLecteurs = new ArrayList<Integer>();
		this.ecrivain = 0;
		this.o = o;
	}
	
	public synchronized Object lock_read(int id, Client_itf client) throws RemoteException {

		// ON TE DONNE LE DROIT DE LECTURE
		this.getListe().add(id);
		
		if (this.getEcrivain()!=0) {
			// on invalide l'ecrivain
			this.o = client_ecrivain.reduce_lock(this.getEcrivain() );
			this.listeDesLecteurs.add(this.getEcrivain());
			this.setEcrivain(0);
		}

		
		
		// va renvoyer un truc de ce genre
		System.out.println("fin de lock_read : " +this.getEcrivain());
		return this.getObject();
		
	}
	
	public synchronized Object lock_write(int id, Client_itf client) throws RemoteException {
		System.out.println("taille liste lecteur" + this.getListe().size());
		
		Iterator<Integer> it = this.listeDesLecteurs.iterator() ;
		while ( it.hasNext() ) {
			client.invalidate_reader(it.next());
		}
				//for (int i=0 ; i<( this.getListe().size() ) ; i++) {
		//		client.invalidate_reader(this.getListe().get(i));
		//}
		
		
		this.listeDesLecteurs.clear();
		
		if (this.getEcrivain()!=0) {
			// on invalide l'ecrivain
			this.o = client_ecrivain.invalidate_writer(this.getEcrivain() );
		}
		
		//  ON TE DONNE LE DROIT D'ECRITURE
		this.setEcrivain(id);
		// et on invalide tout les lecteurs
			
		// va renvoyer un truc de ce genre
		System.out.println("fin de lock_write : " +this.getEcrivain());
		return this.getObject();
	}
	
}
