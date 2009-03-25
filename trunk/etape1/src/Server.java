import java.rmi.*;
import java.rmi.registry.*;
import java.util.Hashtable;


public class Server implements Server_itf {

	private int compteurID=1;
	
	private Hashtable<String, Integer> ListeNomsServerObject;
	private Hashtable<Integer, ServerObject> ListeObjetsServerObject;
	
	
	public static void main(String[] args) {
		try{
			// creation du serveur!
			int port = Registry.REGISTRY_PORT;
			LocateRegistry.createRegistry(port);
			Server server = new Server();
			Naming.rebind("//localhost:"+port, server);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	public int create(Object o) throws RemoteException {
		// on cree un serverobject, on genere un id, on le renvoi o client
		ServerObject so = new ServerObject(o);
		// on le rajoute dans la listes des objet
		this.ListeObjetsServerObject.put(compteurID, so);
		return ++compteurID;

	}

	public Object lock_read(int id, Client_itf client) throws RemoteException {
		// on rajoute a l'objet que ya qn en train de lire dessus
		
		// IF ECRIVAIN, VA TE FAIRE FOUTRE
		
		// SINON ON TE DONNE LE DROIT DE LECTURE
		this.ListeObjetsServerObject.get(id).getListe().add(id);
		// va renvoyer un truc de ce genre
		return this.ListeObjetsServerObject.get(id);
	}

	public Object lock_write(int id, Client_itf client) throws RemoteException {
		this.ListeObjetsServerObject.get(id).setEcrivain(id);
		// si yazvai deja un ecrivain, il faut l'invalider
		// il faut aussi invalider tout les lecteurs
		this.ListeObjetsServerObject.get(id).getListe().remove(id);
		return this.ListeObjetsServerObject.get(id);
	}

	public int lookup(String name) throws RemoteException {
		if (!this.ListeNomsServerObject.containsKey(name)) {
			return 0;
		}
		else {
			return (this.ListeNomsServerObject.get(name));
		}	
	}

	public void register(String name, int id) throws RemoteException {
		// on inscrit un nom ds la liste des noms d'objets
		this.ListeNomsServerObject.put(name, id);
	}

}
