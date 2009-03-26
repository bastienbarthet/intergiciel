import java.net.InetAddress;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.util.*;

public class Client extends UnicastRemoteObject implements Client_itf {

	private static final long serialVersionUID = -6547940558906997763L;
	public static final int NL = 0;				// no local read
	public static final int RLC = 1;			// real lock caches (not taken)
	public static final int WLC = 2;			// write lock cached
	public static final int RLT = 3;			// read lock taken
	public static final int WLT = 4;			// write lock taken
	public static final int RLT_WLC = 5;		// read lock taken and write lock cached
	
	
	
	// attribut liste de type hasmap pour avoir l'ensemble des Shared Objects
	// <id, sharedobject>
	private static Hashtable<Integer, SharedObject> listeObjets;
	
	public static Client_itf instance = null; 
	
	private static Server_itf server;
	
	public Client() throws RemoteException {
		super();
	}


///////////////////////////////////////////////////
//         Interface to be used by applications
///////////////////////////////////////////////////

	// initialization of the client layer
	public static void init() {
		try {
			Client.instance = new Client();
			listeObjets = new Hashtable();
			// faire un lookup pr récup la ref du serveur
			String URL = InetAddress.getLocalHost().getHostName();
			server = (Server_itf) Naming.lookup("//"+URL+":/toto");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	// lookup in the name server
	public static SharedObject lookup(String name) throws RemoteException {
		// si on l'a, on le renvoi, sinon on le demande au serveur
		int id =  server.lookup(name);	
		if (id==0) {
			return null;
		}
		else {
			Object o = lock_read(id);
			SharedObject so = new SharedObject(id, o);
			so.unlock();
			return so;
		}
	}		


	// binding in the name server
	public static void register(String name, SharedObject_itf so) {
		// on envoi un sharedobject o serveur, pour l'ajouter au partage
		try {
			server.register(name, ((SharedObject) so).getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// creation of a shared object
	public static SharedObject create(Object o) throws RemoteException {
		// creations du shared object a partir de l'id  renvoy� par le create du server
		int id = server.create(o);
		SharedObject so = new SharedObject(id, o);
		listeObjets.put(id, so);
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
		listeObjets.get(id).setLock(NL);
	}


	// receive a writer invalidation request from the server
	public Object invalidate_writer(int id) throws java.rmi.RemoteException {
		return listeObjets.get(id);
	}
}
