import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.net.*;
import java.util.*;

public class Server extends UnicastRemoteObject implements Server_itf {

	// attribut liste de type hasmap pour avoir l'ensemble des Server Objects
	private static Hashtable<String, ServerObject> listeServer;
	
	public Server() throws RemoteException {
		super();
	}

///////////////////////////////////////////////////
//         Interface to be used by applications
///////////////////////////////////////////////////

	// initialization of the Server layer
	public static void init() {
		try {
			// surement le rmi a initialiser avec un serveur.connect() 
			int port = 1418;
			Registry r = LocateRegistry.createRegistry(port);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	// lookup in the name server
	public int lookup(String name) throws java.rmi.RemoteException {
		return listeServer.get(name).getId();
	}		
	
	// binding in the name server
	public void register(String name, int id) throws java.rmi.RemoteException {
		/*
		try {
			Naming.rebind("//localhost:"+name, (Remote)(id));	
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/

	}

	// creation of a server object
	public int create(Object o) throws java.rmi.RemoteException {
		//ServerObject so = new ServerObject(o.getId(), o.toString(), o);
		//listeServer.put(so.getName(), so);
		//return so;
	}
	
/////////////////////////////////////////////////////////////
//    Interface to be used by the consistency protocol
////////////////////////////////////////////////////////////

	
	public Object lock_read(int id, Client_itf client) throws java.rmi.RemoteException {
		//return liste.get(name).getLock();
	}

	
	public Object lock_write(int id, Client_itf client) throws java.rmi.RemoteException {
		// return
	}

}

