import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;


public class ServerObject implements Serializable{

	private static final long serialVersionUID = -7396577889952667013L;
	
	// liste des clients
	public ArrayList<Integer> listeDesLecteurs;
	public ArrayList<Integer> getListe() {
		return this.listeDesLecteurs;
	}
	
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
	
	public Object lock_read(int id, Client_itf client) throws RemoteException {
		// on rajoute a l'objet que ya qn en train de lire dessus
		if (this.getEcrivain()!=0) {
			// IF ECRIVAIN, VA TE FAIRE FOUTRE
			return null;
		}
		else {
		// SINON ON TE DONNE LE DROIT DE LECTURE
		this.getListe().add(id);
		// va renvoyer un truc de ce genre
		return this.getObject();
		}
	}

	public Object lock_write(int id, Client_itf client) throws RemoteException {
		if (this.getEcrivain()!=0) {
			// on invalide l'�crivain
			client.invalidate_writer(this.getEcrivain() );
		}
		//  ON TE DONNE LE DROIT D'ECRITURE
			this.setEcrivain(id);
		// et on invalide tout les lecteurs
			
			for (int i=1 ; i<=( this.getListe().size() ) ; i++) {
				client.invalidate_reader(this.getListe().get(i));
				}
			this.getListe().clear();

		// va renvoyer un truc de ce genre
		return this.getObject();
	}
	
	
	
	
	
}
