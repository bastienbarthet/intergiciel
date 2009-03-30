import java.net.InetAddress;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;


public class Server extends UnicastRemoteObject implements Server_itf {

	
	private static final long serialVersionUID = 2910013959992876711L;

	private int compteurID=1;
	
	public Hashtable<String, Integer> ListeNomsServerObject;
	public Hashtable<Integer, ServerObject> ListeObjetsServerObject;
	
	
	protected Server() throws RemoteException {
		super();
		ListeNomsServerObject = new Hashtable<String, Integer>();
		ListeObjetsServerObject = new Hashtable<Integer, ServerObject>();
	}
	
	
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
	
	
	
	public int create(Object o) throws RemoteException {
		// on cree un serverobject, on genere un id, on le renvoi o client
		ServerObject so = new ServerObject(o);
		// on le rajoute dans la listes des objet
		ListeObjetsServerObject.put(compteurID, so);
		return compteurID++;

	}

	public Object lock_read(int id, Client_itf client) throws RemoteException {
		if (ListeObjetsServerObject.contains(id)) {
			return ListeObjetsServerObject.get(id).lock_read(id, client);
		}
		else {
			return null;
		}
				
		}

	public Object lock_write(int id, Client_itf client) throws RemoteException {
		if (ListeObjetsServerObject.containsKey(id)) {
			return ListeObjetsServerObject.get(id).lock_write(id, client);
		}
		else {
			return null;
		}
	}

	public int lookup(String name) throws RemoteException {
		if (ListeNomsServerObject.containsKey(name)) {
			return (ListeNomsServerObject.get(name));
		}
		else {
			return 0;
		}	
	}

	public void register(String name, int id) throws RemoteException {
		// on inscrit un nom ds la liste des noms d'objets
		ListeNomsServerObject.put(name, id);
	}

}
