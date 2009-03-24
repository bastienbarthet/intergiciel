import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.*;
import java.util.Hashtable;


public class Server implements Server_itf {

	private int compteurID=1;
	
	// <name, serverobject> 
	private Hashtable<String, ServerObject> ListeServerObject;
	
	
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
	
	
	
	@Override
	public int create(Object o) throws RemoteException {
		// on cree un serverobject, on genere un id, on le renvoi o client
			ServerObject so = new ServerObject(o);
			return ++compteurID;

	}

	@Override
	public Object lock_read(int id, Client_itf client) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object lock_write(int id, Client_itf client) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int lookup(String name) throws RemoteException {
		ServerObject so = this.ListeServerObject.get(name);
		if (so==null) {
			return 0;
		}
		else {
		return 	++compteurID;
		}
			
		
	}

	@Override
	public void register(String name, int id) throws RemoteException {
		// TODO Auto-generated method stub

	}

}
