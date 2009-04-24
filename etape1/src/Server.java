import java.net.InetAddress;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;


public class Server extends UnicastRemoteObject implements Server_itf {

	
	private static final long serialVersionUID = 2910013959992876711L;

	private int compteurID=1;
	
	public Hashtable<String, ServerObject> ListeNomsServerObject;
	public Hashtable<Integer, ServerObject> ListeIDServerObject;
	
	
	protected Server() throws RemoteException {
		super();
		ListeNomsServerObject = new Hashtable<String, ServerObject>();
		ListeIDServerObject = new Hashtable<Integer, ServerObject>();
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
		synchronized(this){
		ServerObject so = new ServerObject(compteurID, o);
		// on le rajoute dans la listes des objet
		ListeIDServerObject.put(compteurID, so);
		//compteurID++;
		return compteurID++;
		}
	}

	public Object lock_read(int id, Client_itf client) throws RemoteException {
		System.out.println(ListeIDServerObject.get(id).getID());
		if (ListeIDServerObject.containsKey(id)) {
			return ListeIDServerObject.get(id).lock_read(client);
		}
		else {
			return null;
		}		
	}

	public Object lock_write(int id, Client_itf client) throws RemoteException {
		if (ListeIDServerObject.containsKey(id)) {
			return ListeIDServerObject.get(id).lock_write(client);
		}
		else {
			return null;
		}
	}

	public int lookup(String name) throws RemoteException {
		if (ListeNomsServerObject.containsKey(name)) {
			return (ListeNomsServerObject.get(name).getID());
		}
		else {
			return 0;
		}	
	}

	public void register(String name, int id) throws RemoteException {
		// on inscrit un nom ds la liste des noms d'objets
		ListeNomsServerObject.put(name, ListeIDServerObject.get(id));
	}

}
