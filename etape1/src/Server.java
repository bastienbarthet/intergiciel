import java.rmi.RemoteException;


public class Server implements Server_itf {

	@Override
	public int create(Object o) throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void register(String name, int id) throws RemoteException {
		// TODO Auto-generated method stub

	}

}
