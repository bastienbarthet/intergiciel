import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.net.*;
import java.util.*;

public class Client extends UnicastRemoteObject implements Client_itf {

	// attribut liste de type hasmap pour avoir l'ensemble des Shared Objects
	private static Hashtable<Integer, SharedObject> listeObjets;
	
	public Client() throws RemoteException {
		super();
	}


///////////////////////////////////////////////////
//         Interface to be used by applications
///////////////////////////////////////////////////

	// initialization of the client layer
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
	public SharedObject lookup(String name) throws MalformedURLException, RemoteException, NotBoundException {
		return (SharedObject)(Naming.lookup(name));	
	}		
	
	// binding in the name server
	public static void register(String name, SharedObject_itf so) {
		try {
			Naming.rebind("//localhost:"+name, (Remote)(so));	
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// creation of a shared object
	public static SharedObject create(Object o) {
		SharedObject so = new SharedObject(1, o);
		listeObjets.put(so.getId(), so);
		return so;
	}
	
/////////////////////////////////////////////////////////////
//    Interface to be used by the consistency protocol
////////////////////////////////////////////////////////////

	// request a read lock from the server
	public static Object lock_read(int id) {
		// demander au serveur si personne ecrit
		//listeObjets.get(id).setLock(SharedObject.RTL);
		return listeObjets.get(id);
	}

	// request a write lock from the server
	public static Object lock_write (int id) {
		return listeObjets.get(id);
	}

	// receive a lock reduction request from the server
	public Object reduce_lock(int id) throws java.rmi.RemoteException {
		return listeObjets.get(id);
	}


	// receive a reader invalidation request from the server
	public void invalidate_reader(int id) throws java.rmi.RemoteException {
		// il faut retrouver ds la hashtalbe le shared object qui a le num "id"
		// et faire so.setLock(NL);
	}


	// receive a writer invalidation request from the server
	public Object invalidate_writer(int id) throws java.rmi.RemoteException {
		return listeObjets.get(id);
	}
}
