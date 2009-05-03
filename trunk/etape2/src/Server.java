import java.net.InetAddress;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;

/**
 * Classe du serveur, contenant la liste des serverobject.
 * @author Bastiens Barthet&Lehmann
 *
 */
public class Server extends UnicastRemoteObject implements Server_itf {


	private static final long serialVersionUID = 2910013959992876711L;

	private int compteurID=1;

	/** 
	 * Liste de ServerObject, stocké avec leurs nom
	 */
	public Hashtable<String, ServerObject> ListeNomsServerObject;

	/** 
	 * Liste de ServerObject, stocké avec leurs ID
	 */
	public Hashtable<Integer, ServerObject> ListeIDServerObject;


	/**
	 * 
	 * @throws RemoteException
	 */
	protected Server() throws RemoteException {
		super();
		ListeNomsServerObject = new Hashtable<String, ServerObject>();
		ListeIDServerObject = new Hashtable<Integer, ServerObject>();
	}


	/**
	 * lance le serveur
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			// creation du serveur!
			int port = Registry.REGISTRY_PORT;
			LocateRegistry.createRegistry(port);
			String URL = InetAddress.getLocalHost().getHostName();
			Server server = new Server();
			Naming.rebind("//"+URL+":/toto", server);
			System.out.println("Serveur toto is running ...");	
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}


	/**
	 * creer un ServerObject
	 * @param o : l'object a stocké dans un nouveau ServerObject
	 */
	public int create(Object o) throws RemoteException {
		// on cree un serverobject, on genere un id, on le renvoi o client
		synchronized(this){
			ServerObject so = new ServerObject(compteurID, o);
			// on le rajoute dans la listes des objet
			ListeIDServerObject.put(compteurID, so);

			return compteurID++;
		}
	}

	/**
	 * Transmet la demande du client quant aux droits de lecture
	 * @param id ID du ServerObject
	 * @param client du demandeur
	 */
	public Object lock_read(int id, Client_itf client) throws RemoteException {
		if (ListeIDServerObject.containsKey(id)) {
			return ListeIDServerObject.get(id).lock_read(client);
		}
		else {
			return null;
		}		
	}

	/**
	 * Transmet la demande du client quant aux droits d'écriture
	 * @param id ID du ServerObject
	 * @param client du demandeur
	 */
	public Object lock_write(int id, Client_itf client) throws RemoteException {
		if (ListeIDServerObject.containsKey(id)) {
			return ListeIDServerObject.get(id).lock_write(client);
		}
		else {
			return null;
		}
	}

	/**
	 * 
	 * @param name le nom du ServeurObject à trouver
	 * @return l'ID
	 */
	public int lookup(String name) throws RemoteException {
		if (ListeNomsServerObject.containsKey(name)) {
			return (ListeNomsServerObject.get(name).getID());
		}
		else {
			return 0;
		}	
	}

	/**
	 * enregistre le nouveau ServerObject dans la liste 
	 * @param id
	 * @param name
	 */
	public void register(String name, int id) throws RemoteException {
		// on inscrit un nom ds la liste des noms d'objets
		ListeNomsServerObject.put(name, ListeIDServerObject.get(id));
	}

}
